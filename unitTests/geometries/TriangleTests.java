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
        assertDoesNotThrow(() -> new Triangle(new Point(0, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0)),
                "Failed constructing a correct triangle");
        // ================== Boundary Values Tests ==================

        // TC11: 2 points that are the same
        assertThrows(IllegalArgumentException.class, () -> new Triangle(new Point(0, 0, 0), new Point(0, 0, 0), new Point(0, 1, 0)),
                "Constructed a triangle with 2 identical points");
        // TC12: 3 points on the same line
        assertThrows(IllegalArgumentException.class, () -> new Triangle(new Point(0, 0, 0), new Point(1, 0, 0), new Point(2, 0, 0)),
                "Constructed a triangle with 3 points on the same line");
    }

    @Test
    void testfindIntersections() {
        Triangle t = new Triangle(new Point(0, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0));
        // =============== Equivalence Partitions Tests ================//
        // TC01: Ray intersects in the triangle
        Ray ray = new Ray(new Point(0.5, 0.5, -1), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(0.5, 0.5, 0)), t.findIntersections(ray), "Ray intersects in the triangle");

        // TC02: The intersection point with the "contained" plane is outside the triangle - "opposite" one of the sides
        ray = new Ray(new Point(0.5, 0.5, -1), new Vector(0, 0, -1));
        assertNull(t.findIntersections(ray), "The intersection point with the \"contained\" plane is outside the polygon/triangle - \"opposite\" one of the sides");
        // TC03: The intersection point with the "contained" plane is outside the triangle - "opposite" one of the vertices.
        ray = new Ray(new Point(0.5, 0.5, -1), new Vector(0, 0, -1));
        assertNull(t.findIntersections(ray), "The intersection point with the \"contained\" plane is outside the polygon/triangle - \"opposite\" one of the vertices.");

        //=========Boundary Values Tests================
        // TC05: Ray intersects the triangle on the edge
        ray = new Ray(new Point(0.5, 0.5, -1), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(0.5, 0.5, 0)), t.findIntersections(ray), "Ray intersects the triangle on the edge");

        // TC06: Ray intersects the triangle on the vertex
        ray = new Ray(new Point(1, 0, -1), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(1, 0, 0)), t.findIntersections(ray), "Ray intersects the triangle on the vertex");

        // TC07: Ray intersects the triangle on the edge
        ray = new Ray(new Point(0, 1, -1), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(0, 1, 0)), t.findIntersections(ray), "Ray intersects the triangle on the edge");

    }


}