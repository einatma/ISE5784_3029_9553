package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray/*, double distance*/) {
        //Check if the ray intersects the plane.
        List<GeoPoint> result = plane.findGeoIntersections(ray/*, distance*/);
        if (result == null) {
            return null;
        }

        // the three vectors from the same starting point
        Vector v1 = vertices.get(0).subtract(ray.getHead());
        Vector v2 = vertices.get(1).subtract(ray.getHead());
        Vector v3 = vertices.get(2).subtract(ray.getHead());

        // we want to get a normal for each pyramid's face so we do the crossProduct
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        // the ray's vector  - it has the same starting point as the three vectors from above
        Vector v = ray.getDirection();

        // check if the vector's direction (from Subtraction between the ray's vector to each vector from above) are equal
        // if not - there is no intersection point between the ray and the triangle
        // Check if the intersection point is inside the triangle
        double dot1 = alignZero(v.dotProduct(n1));
        double dot2 = alignZero(v.dotProduct(n2));
        double dot3 = alignZero(v.dotProduct(n3));

        if ((dot1 > 0 && dot2 > 0 && dot3 > 0) || (dot1 < 0 && dot2 < 0 && dot3 < 0)) {
            return result.stream()
                    .map(gp -> new GeoPoint(this, gp.point))
                    .collect(Collectors.toList());
        }
        return null;
    }

}
