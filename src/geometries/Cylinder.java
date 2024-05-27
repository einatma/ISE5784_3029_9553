package geometries;
import primitives.*;

import static primitives.Util.isZero;

/**
 * Represents a vector in 3D space, defined by its components (x, y, z).
 * @author Hadar Cohen-213953029 and Einat Mazuz -324019553
 */
public class Cylinder extends  Tube  {
    /** The height of the cylinder. */
    private double height;
    /**
     * Constructs a new Cylinder object with the specified height, axis, and radius.
     *
     * @param h The height of the cylinder.
     * @param a The axis (ray) of the cylinder.
     * @param r The radius of the cylinder.
     */
    public Cylinder (double h, Ray a, double r)
    {
        super(a,r);
        height = h;
    }

//    @Override
//    public Vector getNormal(Point p) {
//        return super.getNormal(p);
//    }
//    * Calculates the normal vector to the surface of the cylinder at a given point.
//            *
//            * @param p The point on the surface of the cylinder.
//     * @return The normal vector to the surface at point p.
//     */
    @Override
    public Vector getNormal(Point p) {
        // The direction vector of the cylinder's central axis
        Vector cylinderCenterVector = axis.getDirection();
        // The center point of the bottom base of the cylinder
        Point centerOfOneSide = axis.getHead();
        // The center point of the top base of the cylinder
        Point centerOfSecondSide = axis.getHead().add(axis.getDirection().scale(height));
        // Check if the point is at the center of the bottom base
        if (p.equals(centerOfOneSide)) {
            return cylinderCenterVector.scale(-1);
        }
        // Check if the point is at the center of the top base
        else if (p.equals(centerOfSecondSide)){
            return cylinderCenterVector;
        }
        // Calculate the t of point p on the cylinder's axis
        double t = cylinderCenterVector.dotProduct(p.subtract(centerOfOneSide));
        // If the t is 0, the point is on the bottom base but not at the center
        if (isZero(t))
            return p.subtract(centerOfOneSide).normalize();
        // Calculate the center of the circle that intersects with point p on the cylinder's side
        Point center = centerOfOneSide.add(cylinderCenterVector.scale(t));
        // Return the normalized vector from the center of the intersection circle to point p
        return p.subtract(center).normalize();
    }
}
