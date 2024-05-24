package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTests {

    @Test
    void getNormal() {
        Cylinder c = new Cylinder(1, new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)), 1);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that getNormal() is in the side of the cylinder
        assertEquals(new Vector(0, 1, 0), c.getNormal(new Point(0, 0, 0.5)), "ERROR: normal to cylinder isn't correct");
        // TC02: Test that getNormal() is in the bottom of the cylinder
        assertEquals(new Vector(0, 0, -1), c.getNormal(new Point(0, 0, 0)), "ERROR: normal to cylinder isn't correct");
        // TC03: Test that getNormal() is in the top of the cylinder
        assertEquals(new Vector(0, 0, 1), c.getNormal(new Point(0, 0, 1)), "ERROR: normal to cylinder isn't correct");
        // ================== Boundary Values Tests ==================
        // TC11: Test that the beam head is perpendicular to the axis of the cylinder
        Cylinder c1 = new Cylinder(1, new Ray(new Point(0, 0, 0), new Vector(1, 0, 0)), 1);
        assertEquals(new Vector(1, 0, 0), c1.getNormal(new Point(1, 0, 0)), "ERROR: normal to cylinder isn't correct");

    }
}