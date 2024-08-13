package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a vector in 3D space, defined by its components (x, y, z).
 *
 * @author Hadar Cohen-213953029 and Einat Mazuz -324019553
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    private double height;

    /**
     * Constructs a new Cylinder object with the specified height, axis, and radius.
     *
     * @param h The height of the cylinder.
     * @param a The axis (ray) of the cylinder.
     * @param r The radius of the cylinder.
     */
    public Cylinder(double h, Ray a, double r) {
        super(a, r);
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
//        // The direction vector of the cylinder's central axis
//        Vector cylinderCenterVector = axis.getDirection();
//        // The center point of the bottom base of the cylinder
//        Point centerOfOneSide = axis.getHead();
//        // The center point of the top base of the cylinder
//        Point centerOfSecondSide = axis.getHead().add(axis.getDirection().scale(height));
//        // Check if the point is at the center of the bottom base
//        if (p.equals(centerOfOneSide)) {
//            return cylinderCenterVector.scale(-1);
//        }
//        // Check if the point is at the center of the top base
//        else if (p.equals(centerOfSecondSide)) {
//            return cylinderCenterVector;
//        }
//        // Calculate the t of point p on the cylinder's axis
//        double t = cylinderCenterVector.dotProduct(p.subtract(centerOfOneSide));
//        // If the t is 0, the point is on the bottom base but not at the center
//        if (isZero(t))
//            return p.subtract(centerOfOneSide).normalize();
//        // Calculate the center of the circle that intersects with point p on the cylinder's side
//        Point center = centerOfOneSide.add(cylinderCenterVector.scale(t));
//        // Return the normalized vector from the center of the intersection circle to point p
//        return p.subtract(center).normalize();
        Vector axisDirection = axis.getDirection();
        Point axisHead = axis.getHead();
        if (p.equals(axisHead))
            return axisDirection.normalize();
        double t = p.subtract(axisHead).dotProduct(axisDirection);
        if (isZero(t) || isZero(t - height)) {
            // Point is on one of the bases
            return axisDirection.normalize();
        }
        // Point is on the side surface
        return super.getNormal(p);
    }

    /**
     * Finds all the intersection points between a given ray and the cylinder.
     * @param ray the ray to intersect with the tube.
     * @param distance
     * @return
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        List<GeoPoint> result = new LinkedList<>();
        Vector va = this.axis.getDirection();
        Point p1 = this.axis.getHead();
        Point p2 = p1.add(this.axis.getDirection().scale(this.height));

        Plane plane1 = new Plane(p1, this.axis.getDirection()); //get plane of bottom base
        List<GeoPoint> result2 = plane1.findGeoIntersections(ray); //intersections with bottom's plane

        if (result2 != null) {
            //Add all intersections of bottom's plane that are in the base's bounders
            for (GeoPoint point : result2) {
                if (point.point.equals(p1)) { //to avoid vector ZERO
                    result.add(point);
                }
                //checks that point is inside the base
                else if ((point.point.subtract(p1).dotProduct(point.point.subtract(p1)) < this.radius * this.radius)) {
                    result.add(point);
                }
            }
        }

        List<GeoPoint> result1 = super.findGeoIntersections(ray); //get intersections for tube

        if (result1 != null) {
            //Add all intersections of tube that are in the cylinder's bounders
            for (GeoPoint point : result1) {
                if (va.dotProduct(point.point.subtract(p1)) > 0 && va.dotProduct(point.point.subtract(p2)) < 0) {
                    result.add(point);
                }
            }
        }

        Plane plane2 = new Plane(p2, this.axis.getDirection()); //get plane of top base
        List<GeoPoint> result3 = plane2.findGeoIntersections(ray); //intersections with top's plane

        if (result3 != null) {
            for (GeoPoint point : result3) {
                if (point.point.equals(p2)) { //to avoid vector ZERO
                    result.add(point);
                }
                //Formula that checks that point is inside the base
                else if ((point.point.subtract(p2).dotProduct(point.point.subtract(p2)) < this.radius * this.radius)) {
                    result.add(point);
                }
            }
        }

        if (result.size() > 0) {
            return result;
        }

        return null;
    }
}

