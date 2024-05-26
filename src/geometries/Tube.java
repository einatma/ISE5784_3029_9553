package geometries;
import primitives.*;
/**
 * Represents a tube in 3D space, defined by its central axis and radius.
 */
public class Tube extends RadialGeometry{
    /** The central axis of the tube. */
    final Ray axis;
    /**
     * Constructs a new Tube object with the specified central axis and radius.
     *
     * @param a The central axis of the tube.
     * @param r The radius of the tube.
     */
    public Tube(Ray a, double r)
    {
        super(r);
        axis=a;
    }

//    @Override
//    public Vector getNormal(Point p) {
//        return null;
//    }
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
        // Calculate the projection of point p on the tube's axis
        // projection is the distance of p from head along the direction of the axis
        double projection = tubeCenterVector.dotProduct(p.subtract(head));
        // If the projection is 0, it indicates an error as the point should not be at the exact base center
        if (projection == 0) {
            throw new IllegalArgumentException("ERROR: the projection cant be 0");
        }
        // Calculate the center of the circle that intersects with point p on the tube's side
        Point tubeCenterPoint = head.add(tubeCenterVector.scale(projection));
        // Return the normalized vector from the center of the intersection circle to point p
        return p.subtract(tubeCenterPoint).normalize();
    }
}
