package renderer;
//H

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
     * @param builder the builder object containing all the necessary parameters.
     */
    public Camera(Builder builder) {
        this.rayTracer = builder.rayTracer;
        this.imageWriter = builder.imageWriter;
        this.location = builder.location;
        this.vTo = builder.vTo;
        this.vUp = builder.vUp;
        this.vRight = builder.vRight;
        this.distance = builder.distance;
        this.width = builder.width;
        this.height = builder.height;
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

    public void renderImage() {
        if (this.imageWriter == null)
            throw new UnsupportedOperationException("Missing imageWriter");
        if (this.rayTracer == null)
            throw new UnsupportedOperationException("Missing rayTracerBase");
        for (int i = 0; i < this.imageWriter.getNx(); i++) {
            for (int j = 0; j < this.imageWriter.getNy(); j++) {
                Color color = rayTracer.traceRay(constructRay(imageWriter.getNx(), imageWriter.getNy(), j, i));
                this.imageWriter.writePixel(j, i, color);
            }
        }
    }
    public void printGrid(int interval, Color color) {
        for (int i = 0; i < imageWriter.getNx(); i++) {
            for (int j = 0; j < imageWriter.getNy(); j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(i, j, color);
                }
            }
        }
    }
    public void writeToImage() {
        this.imageWriter.writeToImage();
    }

    private Color castRay(int j, int i) {
        return rayTracer.traceRay(constructRay(imageWriter.getNx(), imageWriter.getNy(), j, i));
    }


    /**
     * Builder class for constructing Camera objects.
     */
    public static class Builder {
        private RayTracerBase rayTracer;
        private ImageWriter imageWriter;
        private Point location;
        private Vector vTo;
        private Vector vUp;
        private Vector vRight;
        private double distance;
        private double width;
        private double height;

        //        /**
//         * Sets the RayTracerBase for the Camera.
//         *
//         * @param rayTracer the RayTracerBase to set.
//         * @return the Builder instance.
//         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            this.rayTracer = rayTracer;
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
            this.imageWriter = imageWriter;
            return this;
        }

        /**
         * Sets the location for the Camera.
         *
         * @param location the location to set.
         * @return the Builder instance.
         */
        public Builder setLocation(Point location) {
            this.location = location;
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
            this.vTo = vTo.normalize();
            this.vUp = vUp.normalize();
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
            this.distance = distance;
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
            this.width = width;
            this.height = height;
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
            if (vTo == null) throw new MissingResourceException(Exception, NameClass, "vTo");
            if (vUp == null) throw new MissingResourceException(Exception, NameClass, "vUp");
            if (location == null) throw new MissingResourceException(Exception, NameClass, "location");
            if (distance == 0) throw new MissingResourceException(Exception, NameClass, "distance");
            if (width == 0) throw new MissingResourceException(Exception, NameClass, "width");
            if (height == 0) throw new MissingResourceException(Exception, NameClass, "height");
            if (rayTracer == null) throw new MissingResourceException(Exception, NameClass, "rayTracer");
            if (imageWriter == null) throw new MissingResourceException(Exception, NameClass, "imageWriter");
            vRight = vTo.crossProduct(vUp).normalize();

            Camera camera = new Camera(this);
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException ignore) {
                return null;
            }
        }

    }
}

