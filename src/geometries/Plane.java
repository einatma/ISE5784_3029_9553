package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;

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
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        normal = v1.crossProduct(v2).normalize();

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


    /**
     * Finds all the intersection points between a given ray and the geometric object.
     *
     * @param ray the ray to intersect with the geometric object
     * @return a list of points where the ray intersects the object, or an empty list if there are no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        // Calculate the parameter t using the intersection formula t= (N * (Q - P0)) / (N * V)
        double t = alignZero(normal.dotProduct(q.subtract(ray.getHead()))
                / normal.dotProduct(ray.getDirection()));
        // If t is less than or equal to zero, there is no intersection
        if (t <= 0) {
            return null;
        }
        // Calculate and return the intersection point as a list
        return List.of(ray.getHead().add(ray.getDirection().scale(t)));
    }
}
