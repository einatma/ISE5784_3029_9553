package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Polygons
 *
 * @author Dan
 */
public class PolygonTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertix on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "Constructed a polygon with vertice on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a polygon with vertice on a side");

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)};
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon's normal is not orthogonal to one of the edges");
    }

    @Test
    public void testFindIntersections() {
        // Create a triangle polygon in the XY plane
        Polygon triangle = new Polygon(
                (Point) List.of(
                        new Point(0, 0, 0),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0)
                )
        );

        // =============== Equivalence Partitions Tests ================//

        // TC01: Ray intersects the polygon (1 point)
        Ray ray1 = new Ray(new Point(0.5, 0.5, 1), new Vector(0, 0, -1));
        List<Point> result = triangle.findIntersections(ray1);
        assertNotNull(result, "Ray should intersect the polygon");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(new Point(0.5, 0.5, 0), result.get(0), "Intersection point is incorrect");

        // TC02: Ray misses the polygon (0 points)
        Ray ray2 = new Ray(new Point(2, 2, 1), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray2), "Ray should not intersect the polygon");

        // TC03: Ray is parallel to the polygon plane (0 points)
        Ray ray3 = new Ray(new Point(0.5, 0.5, 0), new Vector(1, 1, 0));
        assertNull(triangle.findIntersections(ray3), "Ray should be parallel to the plane and not intersect the polygon");

        // TC04: Ray starts inside the polygon (0 points)
        Ray ray4 = new Ray(new Point(0.25, 0.25, 0), new Vector(0, 0, 1));
        assertNull(triangle.findIntersections(ray4), "Ray starts inside the polygon and should not have a valid intersection");

        // TC05: Ray intersects exactly on an edge of the polygon (1 point)
        Ray ray5 = new Ray(new Point(0.5, -0.5, 1), new Vector(0, 0, -1));
        result = triangle.findIntersections(ray5);
        assertNotNull(result, "Ray should intersect the polygon");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(new Point(0.5, -0.5, 0), result.get(0), "Intersection point is incorrect");

        // ================= Boundary Values Tests ====================//

        // TC11: Ray starts at polygon and goes inside (1 point)
        Ray ray6 = new Ray(new Point(0, 0, 0), new Vector(0.5, 0.5, -1));
        result = triangle.findIntersections(ray6);
        assertNotNull(result, "Ray should intersect the polygon");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(new Point(0.5, 0.5, -1), result.get(0), "Intersection point is incorrect");

        // TC12: Ray starts at polygon and goes outside (0 points)
        Ray ray7 = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0));
        assertNull(triangle.findIntersections(ray7), "Ray starts at polygon and goes outside");

        // TC13: Ray starts before the polygon and goes through an edge (1 point)
        Ray ray8 = new Ray(new Point(0.5, -1, 0), new Vector(0, 1, 0));
        result = triangle.findIntersections(ray8);
        assertNotNull(result, "Ray should intersect the polygon");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(new Point(0.5, 0, 0), result.get(0), "Intersection point is incorrect");

        // TC14: Ray starts before the polygon and goes through a vertex (1 point)
        Ray ray9 = new Ray(new Point(-1, -1, 0), new Vector(1, 1, 0));
        result = triangle.findIntersections(ray9);
        assertNotNull(result, "Ray should intersect the polygon");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(new Point(0, 0, 0), result.get(0), "Intersection point is incorrect");

        // TC15: Ray is tangent to the polygon (0 points)
        Ray ray10 = new Ray(new Point(0.5, 1, 0), new Vector(0, -1, 0));
        assertNull(triangle.findIntersections(ray10), "Ray is tangent to the polygon");
    }
}

