package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

class SphereTests {

    @Test
    void testGetNormal() {

        // ============ Equivalence Partitions Tests ==============
        Sphere s = new Sphere(Point.ZERO, 1);
        Point pt = new Point(0, 0, 10);
        // TC01: Test that getNormal() returns proper value
        assertEquals(new Vector(0, 0, 1), s.getNormal(pt));
    }

}