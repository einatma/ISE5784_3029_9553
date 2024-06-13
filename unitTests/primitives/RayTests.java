package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTests {

    @Test
    void findClosestPoint() {
// ============ Equivalence Partitions Tests ==============
       //TC01: Test that a point in the middle of the list is returned
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Point p1 = new Point(1, 1, 1);
        Point p2 = new Point(2, 2, 2);
        Point p3 = new Point(3, 3, 3);

        assertEquals(p1, ray.findClosestPoint(List.of(p2, p1, p3)), "ERROR: findClosestPoint() does not return the middle point");

        // =============== Boundary Values Tests ==================
        //TC11: Test that the first point in the list is returned
        assertEquals(p1, ray.findClosestPoint(List.of(p1, p2, p3)), "ERROR: findClosestPoint() does not return the first point");
        //TC12: Test that the last point in the list is returned
        assertEquals(p1, ray.findClosestPoint(List.of( p3, p2, p1)), "ERROR: findClosestPoint() does not return the last point");
        //TC13: Test that the list is empty
        assertNull(ray.findClosestPoint(List.of()), "ERROR: findClosestPoint() does not return null for an empty list");

    }
}