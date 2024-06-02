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

    /* Computes the normal vector to the tube at a given point.
     *
     * @param p The point on the tube surface where the normal is to be computed.
     * @return The normal vector to the tube at the given point.
     */
    @Override
    public Vector getNormal(Point p) {
        // The direction vector of the tube's central axis
        Vector tubeCenterVector = axis.getDirection();
        // The starting point of the tube's central axis (the base of the tube)
        Point head = axis.getHead();
        // Check if the point is at the base center
        if (p.equals(head)) {
            throw new IllegalArgumentException("ERROR: the point can't be at the base center");
        }
        // Calculate the t of point p on the tube's axis
        // t is the distance of p from head along the direction of the axis
        double t = tubeCenterVector.dotProduct(p.subtract(head));
        if (isZero(t)) {
            throw new IllegalArgumentException("ERROR: can't be zero vector");
        }

        // Calculate the center of the circle that intersects with point p on the tube's side
        Point tubeCenterPoint = axis.addToHead(t);
        // Return the normalized vector from the center of the intersection circle to point p
        return p.subtract(tubeCenterPoint).normalize();
    }

    /**
     * Finds all the intersection points between a given ray and the geometric object.
     *
     * @param ray the ray to intersect with the geometric object
     * @return a list of points where the ray intersects the object, or an empty list if there are no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        // Extract direction and head of the ray
        Vector rayDirection = ray.getDirection();
        Point rayHead = ray.getHead();

        // Extract direction and head of the tube's axis
        Vector tubeAxisDirection = axis.getDirection();
        Point tubeHead = axis.getHead();

        // Calculate the vector from the tube's head to the ray's head
        Vector deltaP = rayHead.subtract(tubeHead);

        // Calculate the projections of rayDirection and deltaP on tubeAxisDirection
        Vector vProjection = tubeAxisDirection.scale(rayDirection.dotProduct(tubeAxisDirection));
        Vector deltaPProjection = tubeAxisDirection.scale(deltaP.dotProduct(tubeAxisDirection));

        // Calculate the components perpendicular to the tube axis
        Vector vMinusVa = rayDirection.subtract(vProjection);
        Vector deltaPMinusVa = deltaP.subtract(deltaPProjection);

        // Ensure that vMinusVa and deltaPMinusVa are not zero vectors
        if (vMinusVa.length() == 0) {
            throw new IllegalArgumentException("ERROR: Ray direction projection results in a zero vector");
        }
        if (deltaPMinusVa.length() == 0 && deltaP.length() != 0) {
            throw new IllegalArgumentException("ERROR: Delta P projection results in a zero vector");
        }

        // Calculate the coefficients for the quadratic equation
        double a = vMinusVa.dotProduct(vMinusVa);
        double b = 2 * vMinusVa.dotProduct(deltaPMinusVa);
        double c = deltaPMinusVa.dotProduct(deltaPMinusVa) - radius * radius;

        // Calculate the discriminant
        double discriminant = b * b - 4 * a * c;

        // If the discriminant is negative, there are no real intersections
        if (discriminant < 0) {
            return null;
        }

        // Calculate the two solutions of the quadratic equation
        double sqrtDiscriminant = Math.sqrt(discriminant);
        double t1 = (-b + sqrtDiscriminant) / (2 * a);
        double t2 = (-b - sqrtDiscriminant) / (2 * a);

        List<Point> intersections = new ArrayList<>();

        // Check if the solutions are valid (t > 0) and add them to the intersections list
        if (t1 > 0) {
            intersections.add(ray.addToHead(t1));
        }
        if (t2 > 0) {
            intersections.add(ray.addToHead(t2));
        }

        return intersections.isEmpty() ? null : intersections;
    }
}
