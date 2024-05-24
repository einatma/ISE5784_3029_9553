package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

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

}