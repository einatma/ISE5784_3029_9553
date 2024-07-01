package geometries;

import java.util.List;
import java.util.stream.Collectors;

import static primitives.Util.*;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */

public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected final List<Point> vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected final Plane plane;
    /**
     * The size of the polygon - the amount of the vertices in the polygon
     */
    private final int size;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by
     *                 edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3) throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size = vertices.length;

        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return; // no need for more tests for a Triangle

        Vector n = plane.getNormal();
        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with the normal. If all the rest consequent edges will generate the same sign
        // - the polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    /**
     * Gets the normal vector to the polygon at a given point.
     *
     * @param point The point on the surface of the polygon.
     * @return The normal vector at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal();
    }

    /**
     * Finds all the intersection points between a given ray and the polygon.
     *
     * @param ray the ray to intersect with the polygon.
     *            //* @param distance
     * @return a list of points where the ray intersects the polygon, or null if there are no intersections.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        List<GeoPoint> result = plane.findGeoIntersections(ray, distance);
        if (result == null) {
            return null;
        }
        // Iterate through the vertices of the polygon to check if the intersection point is inside the polygon
        for (int i = 1; i < size - 1; i++) {
            for (int j = i + 1; j < size - 1; j++) {
                Vector v1 = vertices.get(0).subtract(ray.getHead());
                Vector v2 = vertices.get(i).subtract(ray.getHead());
                Vector v3 = vertices.get(j).subtract(ray.getHead());
                Vector direction = ray.getDirection();

                // Calculate the dot products for the cross products of the vectors formed by the vertices and the ray direction
                double dot1 = alignZero(direction.dotProduct(v1.crossProduct(v2).normalize()));
                double dot2 = alignZero(direction.dotProduct(v2.crossProduct(v3).normalize()));
                double dot3 = alignZero(direction.dotProduct(v3.crossProduct(v1).normalize()));

                // If all the dot products have the same sign, the intersection point is inside the polygon
                if ((dot1 > 0 && dot2 > 0 && dot3 > 0) || (dot1 < 0 && dot2 < 0 && dot3 < 0)) {
                    return result.stream()
                            .map(gp -> new GeoPoint(this, gp.point))
                            .collect(Collectors.toList());
                }
            }
        }

        // If no intersection point is found inside the polygon, return null
        return null;
    }
}
