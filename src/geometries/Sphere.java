package geometries;

import primitives.*;

/**
 * Sphere class represents a three-dimensional sphere in 3D Cartesian coordinate system.
 * Extends RadialGeometry.
 */
public class Sphere extends RadialGeometry {
    private final Point center;

    /**
     * Constructor to initialize Sphere with its center point and radius.
     *
     * @param c the center point of the sphere.
     * @param r the radius of the sphere.
     */
    public Sphere(Point c, double r) {
        super(r);
        center = c;
    }

    /**
     * Returns the normal vector to the sphere at a given point.
     *
     * @param p the point on the surface of the sphere.
     * @return the normal vector at the given point.
     */
    @Override
    public Vector getNormal(Point p) {
        return p.subtract(center).normalize();
    }

}
