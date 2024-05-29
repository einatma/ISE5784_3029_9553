package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

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
    @Test
    public void testfindIntersections() {
        Plane plane = new Plane(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1));

        // ============ Equivalence Partitions Tests ==============
        // TC01: The intersection point is in the plane - the ray is outside the plane not parallel to the plane and not orthogonal to the plane
        assertEquals(List.of(new Point(0, 0, 1)),
                plane.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, -1))),
                "The point supposed to be in the plane");

        // TC02: The intersection point isn't in the plane - the ray is outside the plane not parallel to the plane and not orthogonal to the plane
        assertNull(plane.findIntersections(new Ray(new Point(2, 2, 2), new Vector(1, 0, 0))),
                "The point supposed to be outside the plane");

        // ================== Boundary Values Tests ==================
        // *********Group: Ray is parallel to the plane
        // TC11: The ray is parallel to the plane and not included in it
        assertNull(plane.findIntersections(new Ray(new Point(1, 1, 2), new Vector(1, 1, 0))),
                "The ray is parallel to the plane and not included in it");

        // TC12: The ray is parallel to the plane and included in it
        assertNull(plane.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 1, 0))),
                "The ray is parallel to the plane and included in it");

        // *********Group: Ray is orthogonal to the plane
        // TC21: The ray is orthogonal to the plane and starts after it
        assertNull(plane.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, 1))),
                "The ray is orthogonal to the plane and starts after it");

        // TC22: The ray is orthogonal to the plane and starts before it
        assertEquals(List.of(new Point(0, 0, 1)),
                plane.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1))),
                "The ray is orthogonal to the plane and starts before it");

        // TC23: The ray is orthogonal to the plane and starts in it
        assertNull(plane.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0, 0, 1))),
                "The ray is orthogonal to the plane and starts in it");

        // *********Group: Ray is neither orthogonal nor parallel to the plane
        // TC31: The ray is neither orthogonal nor parallel to the plane and starts in the plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 1, 1))),
                "The ray is neither orthogonal nor parallel to the plane and starts in the plane");

        Plane plane1 = new Plane(new Point(1, 1, 1), new Vector(1, 1, 1));
        // TC32: The ray is neither orthogonal nor parallel to the plane and starts in the point that the plane passes through (1,1,1)
        assertNull(plane1.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 1, 1))),
                "The ray is neither orthogonal nor parallel to the plane and starts in the point that the plane passes through (1,1,1)");




    }

}
