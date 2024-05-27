package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTests {


    @Test
    void testGetNormal() {
        Vector normal = new Vector(0, 0, 1);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that getNormal() returns proper value
        Plane p = new Plane(new Point(0, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0));
        assertEquals(normal, p.getNormal(new Point(0, 0, 0)), "ERROR: normal to plane isn't correct");
    }

    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct 3 points
        assertDoesNotThrow(() -> new Plane(new Point(0, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0)),
                "Failed constructing a correct plane");
        // ================== Boundary Values Tests ==================

        // TC11: 2 points that are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(0, 0, 0), new Point(0, 0, 0), new Point(0, 1, 0)),
                "Constructed a plane with 2 identical points");
        // TC12: 3 points on the same line
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(0, 0, 0), new Point(1, 0, 0), new Point(2, 0, 0)),
                "Constructed a plane with 3 points on the same line");


    }

}
