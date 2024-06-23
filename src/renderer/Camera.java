package renderer;
//H

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import geometries.Intersectable.GeoPoint;
import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Camera class represents a camera in a 3D space.
 * It constructs rays through a view plane to capture an image.
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

    /**
     * Constructs a Camera object using a Builder.
     *
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
    public void renderImage() {
        if (this.imageWriter == null)
            throw new UnsupportedOperationException("Missing imageWriter");
        if (this.rayTracer == null)
            throw new UnsupportedOperationException("Missing rayTracerBase");
        // Loop through each pixel in the image
        for (int i = 0; i < this.imageWriter.getNx(); i++) {
            for (int j = 0; j < this.imageWriter.getNy(); j++) {
                // Construct a ray through the current pixel and trace it and get the color at the intersection point
                Color color = rayTracer.traceRay(constructRay(imageWriter.getNx(), imageWriter.getNy(), j, i));
                // Write the color to the pixel in the image
                this.imageWriter.writePixel(j, i, color);
            }
        }

    }
    /**
     * Prints a grid on the image by setting the color of pixels at regular intervals.
     *
     * @param interval the interval between grid lines
     * @param color    the color of the grid lines
     */
    public void printGrid(int interval, Color color) {
        // Loop through each pixel in the image
        for (int i = 0; i < imageWriter.getNx(); i++) {
            for (int j = 0; j < imageWriter.getNy(); j++) {
                // Write the grid line color to the pixel
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(i, j, color);
                }
            }
        }

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
    private Color castRay(int j, int i) {
        //writes a pixel to an image using a ray tracing algorithm.
        //It calls the `writePixel` method of the `imageWriter` object to set the color of the pixel at position (j, i) in the image.
        // The color is determined by tracing a ray through the scene using the `traceRay` method of the `rayTracer` object.
        // The `traceRay` method constructs a ray originating from the camera's position and passing through the (j, i) pixel on the image plane.
        // It then calculates the color of the pixel based on the intersections of the ray with objects in the scene.
        // The resulting color is used to set the pixel's color in the image.
        return rayTracer.traceRay(constructRay(imageWriter.getNx(), imageWriter.getNy(), j, i));
    }


    /**
     * Builder class for constructing Camera objects.
     */
    public static class Builder {

        private Camera camera = new Camera();


        public Builder setRayTracer(RayTracerBase rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        //
//        /**
//         * Sets the ImageWriter for the Camera.
//         *
//         * @param imageWriter the ImageWriter to set.
//         * @return the Builder instance.
//         */
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
            camera.vRight= camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }
    }
}

