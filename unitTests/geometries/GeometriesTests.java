package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Geometries class.
 */
class GeometriesTests {

    @Test
    void testFindIntersections() {
        Plane testPlane = new Plane(new Point(1, 0, 0), new Point(2, 0, 0), new Point(1.5, 0, 1));
        Sphere testSphere = new Sphere(new Point(1, 0, 1), 1);
        Triangle testTriangle = new Triangle(new Point(0, 2, 0), new Point(2, 2, 0), new Point(1.5, 2, 2));
        Geometries testGeometries = new Geometries(testPlane, testSphere, testTriangle);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Multiple objects intersect (but not all objects)
        Ray rayMultipleIntersections = new Ray(new Point(1, 1.5, 1), new Vector(0, -1, 0));
        assertEquals(3, testGeometries.findIntersections(rayMultipleIntersections, ).size(),
                "Multiple objects should intersect");

        // =============== Boundary Values Tests ==================
        // TC10: Empty list
        Geometries emptyGeometries = new Geometries();
        Ray rayForEmptyList = new Ray(new Point(1, 1, 1), new Vector(0, -1, 0));
        assertNull(emptyGeometries.findIntersections(rayForEmptyList, ), "No intersections for empty geometries");

        // TC11: No intersection with the objects
        Ray rayNoIntersections = new Ray(new Point(1, -1, 1), new Vector(0, -1, 0));
        assertNull(testGeometries.findIntersections(rayNoIntersections, ), "No intersections with any objects");

        // TC12: One object intersects
        Ray raySingleIntersection = new Ray(new Point(1.5, 1.5, 0.5), new Vector(0, 1, 0));
        assertEquals(1, testGeometries.findIntersections(raySingleIntersection, ).size(),
                "One object should intersect");

        // TC13: All the objects intersect
        Ray rayAllIntersections = new Ray(new Point(1, 2.5, 1), new Vector(0, -1, 0));
        assertEquals(4, testGeometries.findIntersections(rayAllIntersections, ).size(),
                "All objects should intersect");
    }




}
