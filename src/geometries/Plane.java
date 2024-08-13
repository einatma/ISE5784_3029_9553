package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a plane in 3D space.
 * A plane is defined by a point on the plane and a normal vector to the plane.
 * The plane can be constructed using either three points on the plane or a point
 * and a normal vector.
 * <p>
 * The Plane class also provides methods for calculating the normal vector and finding
 * intersections between a ray and the plane.
 *
 * @author Hadar Cohen
 * @author Einat Mazuz
 */
public class Plane extends Geometry {
    /**
     * A point on the plane.
     */
    private final Point q;

    /**
     * The normal vector to the plane.
     */
    private final Vector normal;

    /**
     * Constructs a new Plane object from three points lying on the plane.
     * The normal vector is calculated as the cross product of two vectors
     * lying on the plane, formed by the three points.
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
     * Constructs a new Plane object with a specified point on the plane and its normal vector.
     *
     * @param p The point on the plane.
     * @param n The normal vector to the plane.
     */
    public Plane(Point p, Vector n) {
        q = p;
        normal = n;
    }

    /**
     * Gets the normal vector to the plane at a given point.
     * Since the plane is flat, the normal vector is the same at every point on the plane.
     *
     * @param point A point on the plane (unused in this method).
     * @return The normal vector to the plane.
     */
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
     * Finds all intersection points between a given ray and the plane.
     * If the ray intersects the plane within the specified distance, the intersection point is returned.
     * If the ray is parallel to the plane or the intersection is behind the ray's origin, no intersections are found.
     *
     * @param ray      The ray to intersect with the plane.
     * @param distance The maximum distance within which to find intersections.
     * @return A list containing the intersection points, or null if no intersections are found.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        Vector direction = ray.getDirection();
        Point head = ray.getHead();

        // Calculate the denominator N * V (dot product of the plane's normal and the ray's direction)
        double denominator = alignZero(normal.dotProduct(direction));

        // If denominator is zero, the ray is parallel to the plane and no intersection occurs
        if (isZero(denominator)) {
            return null;
        }

        // Calculate the numerator N * (Q - P0) (dot product of the plane's normal and the vector from ray origin to plane point)
        if (q.equals(head)) {
            return null;
        }

        double numerator = alignZero(normal.dotProduct(q.subtract(head)));

        // Calculate the parameter t, which represents the intersection point along the ray's direction
        double t = alignZero(numerator / denominator);

        // If t is less than or equal to zero, or if t exceeds the specified distance, there's no valid intersection
        if (t <= 0 || alignZero(t - distance) > 0) {
            return null;
        }

        // Calculate and return the intersection point as a list containing a single GeoPoint
        return List.of(new GeoPoint(this, ray.getPoint(t)));
    }
}
