package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a cylinder in 3D space, defined by its height, axis, and radius.
 * Inherits from the Tube class, which provides the cylindrical side surface.
 * This class also provides methods to calculate the normal vector at a given point
 * on the cylinder and to find intersection points between a ray and the cylinder.
 *
 * @author Hadar Cohen-213953029
 * @author Einat Mazuz -324019553
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    private final double height;

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

    /**
     * Calculates the normal vector to the surface of the cylinder at a given point.
     * The normal is determined based on the location of the point relative to the cylinder:
     * - If the point is on one of the bases, the normal is parallel to the axis.
     * - If the point is on the side surface, the normal is perpendicular to the axis.
     *
     * @param p The point on the surface of the cylinder.
     * @return The normal vector to the surface at point p.
     */
    @Override
    public Vector getNormal(Point p) {
        // Get the direction vector of the cylinder's central axis
        Vector axisDirection = axis.getDirection();
        // Get the base point of the cylinder's axis (the head of the axis ray)
        Point axisHead = axis.getHead();

        // Check if the point p is at the base of the cylinder's axis
        if (p.equals(axisHead))
            return axisDirection.normalize(); // The normal is in the direction of the axis

        // Calculate the projection of point p onto the cylinder's axis
        double t = p.subtract(axisHead).dotProduct(axisDirection);

        // If the point is on one of the bases (t is 0 or equals the height)
        if (isZero(t) || isZero(t - height)) {
            return axisDirection.normalize(); // The normal is in the direction of the axis
        }

        // If the point is on the side surface of the cylinder, delegate to the superclass method
        return super.getNormal(p);
    }

    /**
     * Finds all the intersection points between a given ray and the cylinder.
     * The method first checks for intersections with the top and bottom bases of the cylinder,
     * then checks for intersections with the side surface.
     *
     * @param ray      The ray to intersect with the cylinder.
     * @param distance The maximum distance to consider for intersections.
     * @return A list of GeoPoints representing the intersections, or null if there are none.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        // Initialize a list to store the intersection points
        List<GeoPoint> result = new LinkedList<>();

        // Get the direction vector of the cylinder's central axis
        Vector va = this.axis.getDirection();
        // Get the base point of the cylinder's axis (the head of the axis ray)
        Point p1 = this.axis.getHead();
        // Calculate the top point of the cylinder's axis (p1 + height * direction)
        Point p2 = p1.add(this.axis.getDirection().scale(this.height));

        // Find intersections with the bottom base of the cylinder
        Plane plane1 = new Plane(p1, this.axis.getDirection()); // Define the plane for the bottom base
        List<GeoPoint> result2 = plane1.findGeoIntersections(ray); // Get intersections with the bottom plane

        if (result2 != null) {
            // Add all valid intersections that are within the bounds of the bottom base
            for (GeoPoint point : result2) {
                // Check if the point is exactly at the base point to avoid zero vector
                if (point.point.equals(p1)) {
                    result.add(point);
                } else if (point.point.subtract(p1).dotProduct(point.point.subtract(p1)) < this.radius * this.radius) {
                    // Check if the point is within the radius of the base
                    result.add(point);
                }
            }
        }

        // Find intersections with the cylindrical side surface
        List<GeoPoint> result1 = super.findGeoIntersections(ray); // Get intersections from the Tube class

        if (result1 != null) {
            // Add all valid intersections that are within the height of the cylinder
            for (GeoPoint point : result1) {
                // Check if the intersection point is between the top and bottom planes
                if (va.dotProduct(point.point.subtract(p1)) > 0 && va.dotProduct(point.point.subtract(p2)) < 0) {
                    result.add(point);
                }
            }
        }

        // Find intersections with the top base of the cylinder
        Plane plane2 = new Plane(p2, this.axis.getDirection()); // Define the plane for the top base
        List<GeoPoint> result3 = plane2.findGeoIntersections(ray); // Get intersections with the top plane

        if (result3 != null) {
            // Add all valid intersections that are within the bounds of the top base
            for (GeoPoint point : result3) {
                // Check if the point is exactly at the top point to avoid zero vector
                if (point.point.equals(p2)) {
                    result.add(point);
                } else if (point.point.subtract(p2).dotProduct(point.point.subtract(p2)) < this.radius * this.radius) {
                    // Check if the point is within the radius of the top base
                    result.add(point);
                }
            }
        }

        // If there are valid intersection points, return them; otherwise, return null
        if (result.size() > 0) {
            return result;
        }

        return null;
    }
}
