package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a plane in 3D space.
 * A plane is defined by a point on the plane and a normal vector to the plane.
 *
 * @author Hadar Cohen and Einat Mazuz
 */
public class Plane extends Geometry {
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
     * Finds all the intersection points between a given ray and the plane.
     *
     * @param ray the ray to intersect with the plane.
     *            //* @param distance
     * @return a list of points where the ray intersects the plane, or null if there are no intersections.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        Vector direction = ray.getDirection();
        Point head = ray.getHead();

        // Calculate the denominator N * V
        double denominator = alignZero(normal.dotProduct(direction));

        // If denominator is zero, the ray is parallel to the plane and there's no intersection
        if (isZero(denominator)) {
            return null;
        }

        // Calculate the numerator N * (Q - P0)
        if (q.equals(head))
            return null;

        double numerator = alignZero(normal.dotProduct(q.subtract(head)));

        // Calculate the parameter t
        double t = alignZero(numerator / denominator);

        // If t is less than or equal to zero, there is no intersection (the intersection is behind the ray's origin)
        if (t <= 0 || alignZero(t - distance) > 0){
            return null;
        }

        // Calculate and return the intersection point as a list
        return List.of(new GeoPoint(this, ray.getPoint(t)));
    }




}
