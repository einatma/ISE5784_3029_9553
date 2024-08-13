package renderer;
//H

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import geometries.Intersectable.GeoPoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.*;

import geometries.Geometries;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.stream.*;

/**
 * The Camera class represents a camera in a 3D space.
 * It constructs rays through a view plane to capture an image.
 * <p>
 * This class supports building a camera using a fluent Builder pattern,
 * setting various parameters such as location, direction vectors, distance to view plane,
 * and view plane size.
 * <p>
 * The camera constructs rays through each pixel on the image plane and
 * traces them using a specified ray tracer to render an image.
 * It also provides methods to print a grid on the image and write the image to an output file.
 *
 * @author Hadar Cohen-213953029 and Einat Mazuz -324019553
 */
public class Camera implements Cloneable {
    private RayTracerBase rayTracer;
    private ImageWriter imageWriter;
    private Point location;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double distance = 0;
    private double width = 0;
    private double height = 0;
    /** Aperture radius */
    double apertureRadius = 0;

    /** Focal length */
    double focalLength = 0;

    /** DoF active */
    boolean DoFActive = false;

    /**
     * Aperture area grid density
     */
    int gridDensity = 1;


    /**
     * DoF points on the aperture plane
     */
    public List<Point> DoFPoints = null;
    private static final int MAX_RAYS = 100;  // ערך התחלתי מומלץ, אפשר לשנות לפי הצורך




    /**
     * Number of threads to use for rendering.
     */
    private int threadsCount = 0; // -2 auto, -1 range/stream, 0 no threads, 1+ number of threads

    /**
     * Number of spare threads to keep available.
     */
    private final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores

    /**
     * Interval for printing progress percentage.
     */
    private double printInterval = 0; // printing progress percentage interval


    /**
     * PixelManager instance for managing pixels in multi-threading.
     */
    private PixelManager pixelManager; // PixelManager instance

    /**
     * Constructs a Camera object using a Builder.
     * Private constructor to enforce Builder usage.
     */
    private Camera() {
    }

    /**
     * Gets the Builder instance for Camera.
     *
     * @return a new Builder instance.
     */
    public static Builder getBuilder() {
        return new Builder();
    }


    /**
     * Constructs a ray through a specific pixel on the view plane.
     *
     * @param nX the number of pixels in the x direction.
     * @param nY the number of pixels in the y direction.
     * @param j  the x index of the pixel.
     * @param i  the y index of the pixel.
     * @return the constructed Ray through the specified pixel.
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        double Ry = (double) height / nY;
        double Rx = (double) width / nX;

        // Image center
        Point Pc = location.add(vTo.scale(distance));
        Point Pij = Pc;
        double Yi = -(i - ((nY - 1) / 2d)) * Ry;
        double Xj = (j - ((nX - 1) / 2d)) * Rx;

        // Move to middle of pixel i, j
        if (!isZero(Xj)) { // vRight needs to be scaled with Xj, so it cannot be zero
            Pij = Pij.add(vRight.scale(Xj));
        }
        if (!isZero(Yi)) { // vUp needs to be scaled with Yi, so it cannot be zero
            Pij = Pij.add(vUp.scale(Yi));
        }
        return new Ray(location, Pij.subtract(location));
    }

    /**
     * Renders the image by tracing rays through each pixel and writing the corresponding color to the image.
     * If the imageWriter or rayTracer is not set, throws an UnsupportedOperationException.
     *
     * @throws UnsupportedOperationException if imageWriter or rayTracer is not set
     */
    public Camera renderImage() {
        if (this.imageWriter == null)
            throw new UnsupportedOperationException("Missing imageWriter");
        if (this.rayTracer == null)
            throw new UnsupportedOperationException("Missing rayTracerBase");
        final int nX = imageWriter.getNx();
        final int nY = imageWriter.getNy();

        pixelManager = new PixelManager(nY, nX, printInterval);

        if (DoFActive) {
            this.DoFPoints = Camera.generatePoints(gridDensity, apertureRadius, location, vUp, vRight);
            if (this.DoFPoints == null || this.DoFPoints.isEmpty()) {
                // טיפול במקרה שבו הנקודות לא נוצרו
                this.DoFPoints = List.of(location);  // או כל ערך ברירת מחדל אחר
            }
        }


        if (threadsCount == 0) { // Single-threaded rendering
            for (int i = 0; i < nY; ++i) {
                for (int j = 0; j < nX; ++j) {
                    if (this.gridDensity != 1 && DoFActive) {
                        var focalPoint = constructRay(nX, nY, j, i).getPoint(focalLength);
                        imageWriter.writePixel(j, i, rayTracer.computeFinalColor(Ray.RayBundle(focalPoint, DoFPoints)));
                    } else {
                        castRay(nX, nY, j, i);
                    }
                    pixelManager.pixelDone(); // Update progress after processing each pixel
                }
            }
        } else { // Multi-threaded rendering
            var threads = new LinkedList<Thread>(); // list of threads
            while (threadsCount-- > 0) // add appropriate number of threads
                threads.add(new Thread(() -> { // add a thread with its code
                    PixelManager.Pixel pixel; // current pixel(row,col)
                    // allocate pixel(row,col) in loop until there are no more pixels
                    while ((pixel = pixelManager.nextPixel()) != null) {
                        if (this.gridDensity != 1 && DoFActive) {
                            var focalPoint = constructRay(nX, nY, pixel.col(), pixel.row()).getPoint(focalLength);
                            imageWriter.writePixel(pixel.col(), pixel.row(), rayTracer.computeFinalColor(Ray.RayBundle(focalPoint, DoFPoints)));
                        } else {
                            castRay(nX, nY, pixel.col(), pixel.row());
                        }
                        pixelManager.pixelDone(); // Update progress after processing each pixel
                    }
                }));
            // start all the threads
            for (var thread : threads)
                thread.start();
            // wait until all the threads have finished
            try {
                for (var thread : threads)
                    thread.join();
            } catch (InterruptedException ignore) {
            }
        }
        return this;
    }




    /**
     * Prints a grid over the image at an interval of pixels and colors it
     * accordingly.
     *
     * @param interval the space in pixels
     * @param color    color of the grid
     * @return the Camera instance
     */
    public Camera printGrid(int interval, Color color) {
        int nY = imageWriter.getNy();
        int nX = imageWriter.getNx();
        for (int i = 0; i < nY; i += interval)
            for (int j = 0; j < nX; j += 1)
                imageWriter.writePixel(i, j, color);// Writes grid color to current pixel.
        for (int i = 0; i < nY; i += 1)
            for (int j = 0; j < nX; j += interval)// Loop over all columns at equal intervals
                imageWriter.writePixel(i, j, color);// Writes grid color to current pixel.י
        return this;
    }

    /**
     * Writes the image to the output file using the ImageWriter.
     */
    public void writeToImage() {
        // Save the image to a file
        this.imageWriter.writeToImage();
    }

    /**
     * Casts a ray through the specified pixel and returns the color at the intersection point.
     *
     * @param j the x index of the pixel
     * @param i the y index of the pixel
     * @return the color at the intersection point
     */
    /**
     * Cast ray from camera and color a pixel
     *
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row row pixel's row number (pixel index in column)
     */
    private void castRay(int nX, int nY, int col, int row) {
        imageWriter.writePixel(col, row, rayTracer.traceRay(constructRay(nX, nY, col, row)));
        pixelManager.pixelDone();
    }
        // Uses rayTracer to calculate the color of the intersection point and returns
        // it.
        //this.imageWriter.writePixel(j, i, rayTracer.traceRay(ray));
        //return rayTracer.traceRay(constructRay(imageWriter.getNx(), imageWriter.getNy(), j, i));



    /**
     * Generates a list of points randomly distributed within a circular area.
     *
     * @param gridDensity The number of points to generate.
     * @param radius      The radius of the circular area.
     * @param center      The center point of the circular area.
     * @param up          A vector representing the up direction for the circular
     *                    area.
     * @param right       A vector representing the right direction for the circular
     *                    area.
     * @return A list of points randomly distributed within the circular area.
     */
    public static List<Point> generatePoints(int gridDensity, double radius, Point center, Vector up, Vector right) {
        List<Point> points = new ArrayList<>();

        for (int i = 0; i < gridDensity; i++) {
            double angle = 2 * Math.PI * Math.random();
            double r = radius * Math.sqrt(Math.random());
            double offsetX = r * Math.cos(angle);
            double offsetY = r * Math.sin(angle);

            Point point = center.add(right.scale(offsetX)).add(up.scale(offsetY));
            points.add(point);
        }
        return points;
    }
    /**
     * Constructs a ray through a specific point in a pixel on the view plane.
     *
     * @param Ry the height of the pixel
     * @param Rx the width of the pixel
     * @param yi the y-offset from the center of the pixel
     * @param xj the x-offset from the center of the pixel
     * @param j  the pixel index on the x-axis
     * @param i  the pixel index on the y-axis
     * @return a Ray object that goes through the specified point in the pixel
     */
    private Ray constructRaysThroughPixel(double Ry, double Rx, double yi, double xj, int j, int i) {
        Point Pc = location.add(vTo.scale(distance));
        // מחשבים את הנקודה ההתחלתית של הפיקסל בציר ה-y
        double y_sample_i = (i * Ry + Ry / 2.0);
        // מחשבים את הנקודה ההתחלתית של הפיקסל בציר ה-x
        double x_sample_j = (j * Rx + Rx / 2.0);
        // קובעים את הנקודה דרכה נורה הקרן
        Point Pij = Pc;
        // אם הנקודה בציר ה-x אינה אפס, מוסיפים לה את ההיסט (xj)
        if (!isZero(x_sample_j + xj)) {
            Pij = Pij.add(vRight.scale(-x_sample_j - xj));
        }

        // אם הנקודה בציר ה-y אינה אפס, מוסיפים לה את ההיסט (yi)
        if (!isZero(y_sample_i + yi)) {
            Pij = Pij.add(vUp.scale(-y_sample_i - yi));
        }

        // יוצרים וקטור (Vij) מהמצלמה לנקודה המחושבת
        Vector Vij = Pij.subtract(location);

        // יוצרים קרן (Ray) מהמצלמה לנקודה המחושבת ומחזירים את הקרן
        return new Ray(location, Vij);
    }

    /**
     * Constructs a beam of rays through a specific pixel on the view plane.
     *
     * @param nX         the number of pixels in the x-axis on the view plane
     * @param nY         the number of pixels in the y-axis on the view plane
     * @param j          the pixel index on the x-axis
     * @param i          the pixel index on the y-axis
     * @param raysAmount the total number of rays to construct through the pixel
     * @return a list of Ray objects that represent the beam through the specified
     *         pixel
     */
    public List<Ray> constructBeamThroughPixel(int nX, int nY, int j, int i, int raysAmount) {
        // The distance between the screen and the camera cannot be 0
        if (isZero(distance)) {
            throw new IllegalArgumentException("distance cannot be 0");
        }

        int numOfRays = (int) Math.floor(Math.sqrt(raysAmount)); // number of rays in each row or column

        if (numOfRays == 1)
            return List.of(constructRay(nX, nY, j, i));

        double Ry = this.height / nY;
        double Rx = this.width / nX;
        double Yi = (i - (nY - 1) / 2d) * Ry;
        double Xj = (j - (nX - 1) / 2d) * Rx;

        double PRy = Ry / numOfRays; // height distance between each ray
        double PRx = Rx / numOfRays; // width distance between each ray

        List<Ray> sample_rays = new ArrayList<>();

        for (int row = 0; row < numOfRays; ++row) {
            for (int column = 0; column < numOfRays; ++column) {
                sample_rays.add(constructRaysThroughPixel(PRy, PRx, Yi, Xj, row, column)); // add the ray

            }
        }
        sample_rays.add(constructRay(nX, nY, j, i)); // add the center screen ray
        return sample_rays;
    }
    /**
     * Builder class for constructing Camera objects.
     */
    public static class Builder {

        private Camera camera = new Camera();

        /**
         * Sets the aperture radius for the camera.
         *
         * @param apertureRadius the aperture radius to set
         * @param focalLength    the focal length to set
         * @param gridDensity    the grid density to set
         * @return the Builder instance
         */
        public Builder setFocalSize(double apertureRadius, double focalLength, int gridDensity) {
            this.camera.apertureRadius = apertureRadius;
            this.camera.focalLength = focalLength;
            this.camera.gridDensity = gridDensity;
            return this;
        }

        public Builder setDoFActive(boolean doFActive) {
            this.camera.DoFActive = doFActive;
            return this;
        }
        /**
         * amount of threads setter for multi-threading
         *
         * @param threads number of threads to run at the same time
         * @return camera (builder)
         */
        public Builder setMultiThreading(int threads) {
            if (threads < -2)
                throw new IllegalArgumentException("Multithreading must be -2 or higher");
            if (threads >= -1)
                this.camera.threadsCount = threads;
            else { // == -2
                int cores = Runtime.getRuntime().availableProcessors() - this.camera.SPARE_THREADS;
                this.camera.threadsCount = cores <= 2 ? 1 : cores;
            }
            return this;
        }

        /**
         * interval setter for debug print
         *
         * @param interval the print interval
         * @return camera (builder)
         */
        public Builder setDebugPrint(double interval) {
            this.camera.printInterval = interval;
            return this;
        }



        /**
         * Sets the RayTracerBase for the Camera.
         *
         * @param rayTracer the RayTracerBase to set.
         * @return the Builder instance.
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * Sets the ImageWriter for the Camera.
         *
         * @param imageWriter the ImageWriter to set.
         * @return the Builder instance.
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Sets the location for the Camera.
         *
         * @param location the location to set.
         * @return the Builder instance.
         */
        public Builder setLocation(Point location) {
            camera.location = location;
            return this;
        }

        /**
         * Sets the direction vectors for the Camera.
         *
         * @param vTo the direction vector to set.
         * @param vUp the up vector to set.
         * @return the Builder instance.
         * @throws IllegalArgumentException if vTo and vUp are not orthogonal.
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!isZero(vTo.dotProduct(vUp)))
                throw new IllegalArgumentException("vTo and vUp are not orthogonal");
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Sets the view plane distance for the Camera.
         *
         * @param distance the distance to set.
         * @return the Builder instance.
         * @throws IllegalArgumentException if distance is not positive.
         */
        public Builder setVpDistance(double distance) {
            if (alignZero(distance) <= 0) throw new IllegalArgumentException("distance must be positive");
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the view plane size for the Camera.
         *
         * @param width  the width to set.
         * @param height the height to set.
         * @return the Builder instance.
         * @throws IllegalArgumentException if width or height is not positive.
         */
        public Builder setVpSize(double width, double height) {
            if (alignZero(width) <= 0 || alignZero(height) <= 0)
                throw new IllegalArgumentException("width and height must be positive");
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Builds and returns a Camera instance.
         *
         * @return the constructed Camera instance.
         * @throws MissingResourceException if any required field is not set.
         */
        final String Exception = "Missing Resource";
        final String NameClass = "Camera";

        public Camera build() {
            if (camera.vTo == null) throw new MissingResourceException(Exception, NameClass, "vTo");
            if (camera.vUp == null) throw new MissingResourceException(Exception, NameClass, "vUp");
            if (camera.location == null) throw new MissingResourceException(Exception, NameClass, "location");
            if (camera.distance == 0) throw new MissingResourceException(Exception, NameClass, "distance");
            if (camera.width == 0) throw new MissingResourceException(Exception, NameClass, "width");
            if (camera.height == 0) throw new MissingResourceException(Exception, NameClass, "height");
            if (camera.rayTracer == null) throw new MissingResourceException(Exception, NameClass, "rayTracer");
            if (camera.imageWriter == null) throw new MissingResourceException(Exception, NameClass, "imageWriter");
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException ignore) {
                return null;
            }
        }

        public Builder setDirection(Point inFront, Vector up) {
            camera.vTo = inFront.subtract(camera.location).normalize();
            camera.vUp = up.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }

    }
}

