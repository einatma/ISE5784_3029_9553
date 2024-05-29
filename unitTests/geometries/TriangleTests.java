package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTests {

    @Test
    void testGetNormal() {
        Triangle t = new Triangle(new Point(0, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that getNormal() returns proper value
        assertEquals(new Vector(0, 0, 1), t.getNormal(new Point(0, 0, 0)), "ERROR: normal to triangle isn't correct");
    }

    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct 3 points
        assertDoesNotThrow(() -> new Triangle(new Point(0, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0)), "Failed constructing a correct triangle");
        // ================== Boundary Values Tests ==================

        // TC11: 2 points that are the same
        assertThrows(IllegalArgumentException.class, () -> new Triangle(new Point(0, 0, 0), new Point(0, 0, 0), new Point(0, 1, 0)), "Constructed a triangle with 2 identical points");
        // TC12: 3 points on the same line
        assertThrows(IllegalArgumentException.class, () -> new Triangle(new Point(0, 0, 0), new Point(1, 0, 0), new Point(2, 0, 0)), "Constructed a triangle with 3 points on the same line");
    }

    @Test
    public void testfindIntersections() {
        Triangle triangle = new Triangle(new Point(1, 1, 1), new Point(3, 1, 1), new Point(2, 3, 1));

        // ============ Equivalence Partitions Tests ==============
        // TC01: The intersection point is in the triangle
        assertEquals(List.of(new Point(2, 2, 1)),
                triangle.findIntersections(new Ray(new Point(2, 2, 2), new Vector(0, 0, -1))),
                "The point supposed to be in the triangle");

        // TC02: The intersection point is outside the triangle, against edge
        assertNull(triangle.findIntersections(new Ray(new Point(0, 0, 2), new Vector(1, 0, -1))),
                "The point supposed to be outside the triangle, against edge");

        // TC03: The intersection point is outside the triangle, against vertex
        assertNull(triangle.findIntersections(new Ray(new Point(4, 0, 2), new Vector(0, 1, -1))),
                "The point supposed to be outside the triangle, against vertex");

        // =============== Boundary Values Tests ==================
        // TC10: The point is on edge
        assertNull(triangle.findIntersections(new Ray(new Point(1, 1, 2), new Vector(1, 0, -1))),
                "The point supposed to be on edge");

        // TC11: The point is in vertex
        assertNull(triangle.findIntersections(new Ray(new Point(3, 1, 2), new Vector(0, 0, -1))),
                "The point supposed to be in vertex");

        // TC12: The point is on edge's continuation
        assertNull(triangle.findIntersections(new Ray(new Point(0, 0, 2), new Vector(3, 0, -1))),
                "The point supposed to be on edge's continuation");
    }
}