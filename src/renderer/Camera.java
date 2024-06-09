package renderer;
//H

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import javax.swing.plaf.basic.BasicArrowButton;
import java.security.cert.CertPathBuilder;

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
    private double hight = 0;

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
        this.hight = builder.hight;
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
        double Ry = (double) hight / nY;
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
        private double hight;

        /**
         * Sets the RayTracerBase for the Camera.
         *
         * @param rayTracer the RayTracerBase to set.
         * @return the Builder instance.
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            this.rayTracer = rayTracer;
            return this;
        }

        /**
         * Sets the ImageWriter for the Camera.
         *
         * @param imageWriter the ImageWriter to set.
         * @return the Builder instance.
         */
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
            this.vTo = vTo.normalize();
            this.vUp = vUp.normalize();
            this.vRight = vTo.crossProduct(vUp).normalize();
            return this;
        }

        /**
         * Sets the view plane distance for the Camera.
         * @param distance the distance to set.
         * @throws IllegalArgumentException If the distance is zero or negative.
         * @return the Builder instance.
         */
        public Builder setVpDistance(double distance) {
            if (distance<=0)
                throw new IllegalArgumentException("Distance cannot be zero or negative");
            this.distance = distance;
            return this;
        }

        /**
         * Sets the view plane size for the Camera.
         *
         * @param width  the width to set.
         * @param hight  the height to set.
         * @throws IllegalArgumentException If the width or the height are zero or negative.
         * @return the Builder instance.
         */
        public Builder setVpSize(double width, double hight) {
            if (width<=0 || hight<=0)
                throw new IllegalArgumentException("Width and length cannot be zero or negative");
            this.width = width;
            this.hight = hight;
            return this;
        }

        /**
         * Builds and returns a Camera instance.
         *
         * @return the constructed Camera instance.
         */
        public Camera build() {
            Camera camera = new Camera(this);
            return camera;
        }
    }

    /**
     * Creates and returns a copy of this Camera object.
     *
     * @return a clone of this Camera.
     */
    @Override
    public Camera clone() {
        try {
            Camera cloned = (Camera) super.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen, since we are Cloneable
        }
    }

}
