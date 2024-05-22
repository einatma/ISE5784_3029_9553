package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that getNormal() returns proper value
        Tube t = new Tube( new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)),1);
        assertEquals(new Vector(0, 1, 0), t.getNormal(new Point(0, 0, 0)), "ERROR: normal to tube isn't correct");
        // ================== Boundary Values Tests ==================
        // TC11: Test that the beam head is perpendicular to the axis of the cylinder
        Tube t1 = new Tube(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0)), 1);
        assertEquals(new Vector(1, 0, 0), t.getNormal(new Point(1, 0, 0)), "ERROR: normal to tube isn't correct");

    }
}