package geometries;

import primitives.*;

/**
 * Represents a plane in 3D space.
 *
 * @author Hadar Cohen and Einat Mazuz
 */
public class Plane implements Geometry {
    /**
     * A point on the plane.
     */
    final private Point q;
    /**
     * The normal vector to the plane.
     */
    final private Vector normal;

    /**
     * Constructs a new Plane object from three points lying on the plane.
     *
     * @param p1 The first point on the plane.
     * @param p2 The second point on the plane.
     * @param p3 The third point on the plane.
     */
    public Plane(Point p1, Point p2, Point p3) {
        q = p1;
        normal=null;
    }

    /**
     * Constructs a new Plane object with a point on the plane and its normal vector.
     *
     * @param p The point on the plane.
     * @param n The normal vector to the plane.
     */
    public Plane(Point p, Vector n) {
        q = p;
        normal = n;
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    /**
     * Gets the normal vector to the plane.
     *
     * @return The normal vector to the plane.
     */
    public Vector getNormal() {
        return normal;
    }


}
