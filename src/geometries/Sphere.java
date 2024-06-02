package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

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

    /**
     * Finds all the intersection points between a given ray and the geometric object.
     *
     * @param ray the ray to intersect with the geometric object
     * @return a list of points where the ray intersects the object, or an empty list if there are no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point P0 = ray.getHead();
        Vector v = ray.getDirection();
        Vector u;

        // if p0 on center, calculate with line parametric representation
        // the direction vector normalized.
        if (center.equals(P0)) {
            Point newPoint = ray.addToHead(getRadius());
            return List.of(newPoint);
        }

        try {
            u = center.subtract(P0);    // vector from P0 to the sphere center
        } catch (IllegalArgumentException e) {
            // P0 is exactly at the sphere's center
            u = new Vector(0, 0, 0);
        }

        double tm = alignZero(v.dotProduct(u));
        double dSquared = alignZero(u.lengthSquared() - tm * tm);
        double rSquared = getRadiusSquared();

        // No intersections: the ray's line is outside the sphere
        if (dSquared >= rSquared) {
            return null;
        }

        double th = sqrt(rSquared - dSquared);
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        List<Point> intersections = new ArrayList<>();

        // Check if t1 and t2 are valid
        if (t1 > 0 && t2 > 0) {
            intersections = List.of(ray.addToHead(t1), ray.addToHead(t2));
        } else {
            // Check if t1 is valid
            if (t1 > 0) {
                intersections = List.of(ray.addToHead(t1));
            }
            // Check if t2 is valid
            if (t2 > 0) {
                intersections = List.of(ray.addToHead(t2));
            }
        }

        return intersections.isEmpty() ? null : intersections;
    }




    // Helper method to deal with numerical precision issues
    private double alignZero(double num) {
        return Math.abs(num) < 1e-10 ? 0.0 : num;
    }

}
