package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a tube in 3D space, defined by its central axis and radius.
 */
public class Tube extends RadialGeometry {
    /**
     * The central axis of the tube.
     */
    final Ray axis;

    /**
     * Constructs a new Tube object with the specified central axis and radius.
     *
     * @param a The central axis of the tube.
     * @param r The radius of the tube.
     */
    public Tube(Ray a, double r) {
        super(r);
        axis = a;
    }

    /**
     * Computes the normal vector to the tube at a given point.
     * <p>
     * This method calculates the normal vector to the surface of the tube at a specified point. The normal is perpendicular
     * to the tube surface and points outward from the central axis.
     *
     * @param p The point on the tube surface where the normal is to be computed.
     * @return The normal vector to the tube at the given point.
     * @throws IllegalArgumentException If the point is at the base center of the tube or if there is a zero vector calculation.
     */
    @Override
    public Vector getNormal(Point p) {
        // The starting point of the tube's central axis (the base of the tube)
        Point head = axis.getHead();

        // Check if the point is exactly at the base center of the tube, which is invalid for normal calculation
        if (p.equals(head))
            throw new IllegalArgumentException("ERROR: the point can't be at the base center");

        // Calculate the distance from the point to the axis along the tube's direction
        double t = p.subtract(head).dotProduct(axis.getDirection());

        // If t is zero, the point is on the axis itself, which is invalid for normal calculation
        if (isZero(t))
            throw new IllegalArgumentException("ERROR: can't be zero vector");

        // Find the point on the tube's axis that is closest to the given point
        Point tubeCenterPoint = axis.getPoint(t);

        // Compute and return the normal vector, which is the direction from the tube's axis point to the point on the surface
        return p.subtract(tubeCenterPoint).normalize();
    }

    /**
     * Finds all the intersection points between a given ray and the tube.
     * <p>
     * This method determines if and where a given ray intersects with the tube. It handles cases where the ray is parallel
     * to the tube's axis, and checks if the intersection points are within the valid distance range.
     *
     * @param ray The ray to intersect with the tube.
     * @param distance The maximum distance within which to find intersections.
     * @return A list of {@link GeoPoint} objects representing the intersection points where the ray intersects the tube,
     *         or null if there are no intersections within the specified distance.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        // Get the direction and starting point of the ray
        Vector rayDirection = ray.getDirection();
        Point rayHead = ray.getHead();

        // Get the direction and starting point of the tube's axis
        Vector tubeAxisDirection = axis.getDirection();
        Point tubeHead = axis.getHead();

        // Check if the ray starts at the same point as the tube's axis, which is an invalid case
        if (rayHead.equals(tubeHead)) {
            return null;
        }

        // Calculate the vector from the tube's axis base to the ray's starting point
        Vector deltaP = rayHead.subtract(tubeHead);

        // Check if the ray is parallel to the tube's axis
        if (isZero(rayDirection.dotProduct(tubeAxisDirection))) {
            // Calculate the distance from the ray to the tube's axis
            double distanceFromAxis = deltaP.crossProduct(tubeAxisDirection).length() / tubeAxisDirection.length();
            if (distanceFromAxis >= radius) {
                return null; // Ray is parallel and either outside the tube or on the surface
            }
        } else {
            // Check if the ray intersects the tube's axis within the bounds
            double projection = deltaP.dotProduct(tubeAxisDirection);
            if (projection < 0 || projection > axis.getDirection().length()) {
                return null; // Ray is not parallel and outside the tube
            }
        }

        // Ensure that the ray does not start at the tube's head or is aligned with the tube's axis
        if (rayHead.equals(tubeHead) || isZero(deltaP.dotProduct(tubeAxisDirection))) {
            return null;
        }

        // Project rayDirection and deltaP onto tubeAxisDirection
        Vector vProjection = tubeAxisDirection.scale(rayDirection.dotProduct(tubeAxisDirection));
        Vector deltaPProjection = tubeAxisDirection.scale(deltaP.dotProduct(tubeAxisDirection));

        // Calculate the components of the vectors that are perpendicular to the tube's axis
        if (vProjection.equals(rayDirection) || deltaPProjection.equals(deltaP)) {
            return null;
        }

        Vector vMinusVa = rayDirection.subtract(vProjection);
        Vector deltaPMinusVa = deltaP.subtract(deltaPProjection);

        // Ensure that the perpendicular vectors are not zero
        if (isZero(vMinusVa.length()) || isZero(deltaPMinusVa.length())) {
            return null;
        }

        // Compute coefficients for the quadratic equation to find intersection points
        double a = vMinusVa.dotProduct(vMinusVa);
        double b = 2 * vMinusVa.dotProduct(deltaPMinusVa);
        double c = deltaPMinusVa.dotProduct(deltaPMinusVa) - radius * radius;

        // Calculate the discriminant of the quadratic equation
        double discriminant = b * b - 4 * a * c;

        // If the discriminant is negative, there are no real intersections
        if (discriminant < 0) {
            return null;
        }

        // Compute the solutions for the quadratic equation
        double sqrtDiscriminant = Math.sqrt(discriminant);
        double t1 = (-b + sqrtDiscriminant) / (2 * a);
        double t2 = (-b - sqrtDiscriminant) / (2 * a);

        List<GeoPoint> intersections = new ArrayList<>();

        // Add valid intersection points (where t > 0 and t < distance) to the list
        if (t1 > 0 && t1 < distance) {
            intersections.add(new GeoPoint(this, ray.getPoint(t1)));
        }
        if (t2 > 0 && t2 < distance) {
            intersections.add(new GeoPoint(this, ray.getPoint(t2)));
        }

        // Return the list of intersections, or null if empty
        return intersections.isEmpty() ? null : intersections;
    }
}
