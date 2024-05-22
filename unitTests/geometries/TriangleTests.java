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

}