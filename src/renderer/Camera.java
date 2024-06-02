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
        // Methods to set camera attributes
        public Builder setLocation(Point location) {
            if (location == null) {
                throw new IllegalArgumentException("Location cannot be null");
            }
            this.camera.location = location;
            return this;
        }

        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo == null || vUp == null) {
                throw new IllegalArgumentException("Direction vectors cannot be null");
            }
            // Normalize and set direction vectors
            this.camera.vTo = vTo.normalize();
            this.camera.vUp = vUp.normalize();
            this.camera.vRight = vTo.crossProduct(vUp).normalize();
            return this;
        }

        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            this.camera.width = width;
            this.camera.height = height;
            return this;
        }

        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }
            this.camera.distance = distance;
            return this;
        }

        // Build method
        public Camera build() {
            if (camera.width == 0 || camera.height == 0 || camera.distance == 0) {
                throw new MissingResourceException("Missing render data", "Camera", "width/height/distance");
            }
            // Additional calculations if needed
            // Clone and return the camera
            return camera.clone();
        }
    }

    // Clone method
    @Override
    protected Camera clone() {
        try {
            return (Camera) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen
        }

    }

}
