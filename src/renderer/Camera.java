package renderer;
//H
import primitives.*;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;

public class Camera implements Cloneable {

    private Point location;
    private Vector vUp;
    private Vector vTo;
    private Vector vRight;
    private double width = 0.0;
    private double height = 0.0;
    private double distance = 0.0;

    private Camera() {
        location = new Point(0, 0, 0);
        vUp = new Vector(0, 1, 0);
        vTo = new Vector(0, 0, -1);
        vRight = new Vector(1, 0, 0);
    }

    public Vector getvRight() {
        return vRight;
    }

    public Vector getvUp() {
        return vUp;
    }

    public Vector getvTo() {
        return vTo;
    }

    public Point getLocation() {
        return location;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i) {
        return null;
    }

    public static class Builder {
        private Camera camera;
        public Builder() {
            this.camera = new Camera();
        }
        //???
        public Builder(Camera cam) {
            camera.location = cam.location;
            camera.vUp = cam.vUp;
            camera.vTo = cam.vTo;
            camera.vRight = cam.vRight;
            camera.width = cam.width;
            camera.height = cam.height;
            camera.distance = cam.distance;
        }

        public Builder setLocation(Point location) {
            camera.location = location;
            return this;
        }

        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo.dotProduct(vUp) != 0) {
                throw new IllegalArgumentException("vTo and vUp are not orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        public Builder setVpSize(double width, double height) {
            if (alignZero(width) <= 0 || alignZero(height) <= 0) {
                throw new IllegalArgumentException("width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Builder setDistance(double distance) {
            if (alignZero(distance) <= 0)
                throw new IllegalArgumentException("distance must be positive");
            camera.distance = distance;
            return this;
        }

        /**
         * Builds the Camera object after validating all necessary fields.
         *
         * @return a copy of the Camera object with computed fields.
         * @throws MissingResourceException if any required field is missing.
         * @throws IllegalArgumentException if any field value is invalid.
         */
        public Camera build() throws CloneNotSupportedException {
            Camera camera = new Camera();
            final String MISSING_RENDERING_DATA = "Missing rendering data in";
            final String CAMERA_CLASS_NAME = "Camera";
            if (camera.location == null) {
                throw new IllegalArgumentException(MISSING_RENDERING_DATA + CAMERA_CLASS_NAME + " location");
            }
            if (camera.vUp == null) {
                throw new IllegalArgumentException(MISSING_RENDERING_DATA + CAMERA_CLASS_NAME + " vUp");
            }
            if (camera.vTo == null) {
                throw new IllegalArgumentException(MISSING_RENDERING_DATA + CAMERA_CLASS_NAME + " vTo");
            }
            if ((camera.vUp).dotProduct(camera.vTo) != 0) {
                throw new IllegalArgumentException("vUp and vTo must be orthogonal");
            }
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();


            return (Camera) camera.clone();
        }

    }

}
