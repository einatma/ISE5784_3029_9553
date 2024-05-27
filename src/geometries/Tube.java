package geometries;

import primitives.*;

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
            throw new IllegalArgumentException("ERROR: the point cant be at the base center");
        }
        // Calculate the t of point p on the tube's axis
        // t is the distance of p from head along the direction of the axis
        double t = tubeCenterVector.dotProduct(p.subtract(head));
        if(isZero(t)){
            throw new IllegalArgumentException("ERROR: can't be zero vector");        }

        // Calculate the center of the circle that intersects with point p on the tube's side
        Point tubeCenterPoint = head.add(tubeCenterVector.scale(t));
        // Return the normalized vector from the center of the intersection circle to point p
        return p.subtract(tubeCenterPoint).normalize();
    }
}
