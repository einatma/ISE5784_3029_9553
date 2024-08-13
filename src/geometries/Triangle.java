package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Vector3;

import java.util.List;
import java.util.stream.Collectors;

import static primitives.Util.alignZero;

/**
 * Represents a triangle in 3D space, defined by its three vertices.
 *
 * @author Hadar Cohen and Einat Mazuz
 */
public class Triangle extends Polygon {
    /**
     * Constructs a new Triangle object with the specified vertices.
     *
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);

    }

    /**
     * Finds all the intersection points between a given ray and the triangle.
     *
     * @param ray the ray to intersect with the triangle.
     * @return a list of points where the ray intersects the triangle, or null if there are no intersections.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        // Check if the ray intersects the plane of the triangle
        List<GeoPoint> result = plane.findGeoIntersections(ray, distance);

        // If there are no intersections with the plane, there can be no intersection with the triangle
        if (result == null) {
            return null;
        }

        // Calculate vectors from the ray's origin to each vertex of the triangle
        Vector v1 = vertices.get(0).subtract(ray.getHead());  // Vector from ray's origin to the first vertex
        Vector v2 = vertices.get(1).subtract(ray.getHead());  // Vector from ray's origin to the second vertex
        Vector v3 = vertices.get(2).subtract(ray.getHead());  // Vector from ray's origin to the third vertex

        // Compute the normal vectors for each side of the triangle
        Vector n1 = v1.crossProduct(v2).normalize();  // Normal vector for the face formed by the first two vertices
        Vector n2 = v2.crossProduct(v3).normalize();  // Normal vector for the face formed by the second and third vertices
        Vector n3 = v3.crossProduct(v1).normalize();  // Normal vector for the face formed by the third and first vertices

        // Get the direction vector of the ray
        Vector v = ray.getDirection();

        // Calculate the dot products between the ray's direction and the normal vectors of the triangle's sides
        double dot1 = alignZero(v.dotProduct(n1));  // Dot product with the normal vector of the first side
        double dot2 = alignZero(v.dotProduct(n2));  // Dot product with the normal vector of the second side
        double dot3 = alignZero(v.dotProduct(n3));  // Dot product with the normal vector of the third side

        // Check if the intersection point is inside the triangle by examining the signs of the dot products
        // If all dot products are positive or all are negative, the point is inside the triangle
        if ((dot1 > 0 && dot2 > 0 && dot3 > 0) || (dot1 < 0 && dot2 < 0 && dot3 < 0)) {
            // If the point is inside the triangle, return a list of GeoPoints with the intersection points
            return result.stream()
                    .map(gp -> new GeoPoint(this, gp.point))
                    .collect(Collectors.toList());
        }

        // If the intersection point is not inside the triangle, return null
        return null;
    }
}
