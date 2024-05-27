package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class PointTests {
    private static final double DELTA = 0.0000001;

    @Test
    void testAdd() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 6);
        Vector v1 = new Vector(1, 2, 3);
        Vector v1Opposite = new Vector(-1, -2, -3);
        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that the connection of a vector and a point in the same direction gets a correct value
        assertEquals(p2, p1.add(v1), "ERROR: (point + vector) = other point does not work correctly");
        // TC02: Test that the connection of a vector and a point in the opposite directions gets a correct value
        assertEquals(Point.ZERO, p1.add(v1Opposite), "ERROR: (point + vector) = center of coordinates does not work correctly");
    }

    @Test
    void testSubtract() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 6);
        Vector v1 = new Vector(1, 2, 3);
        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that the subtraction of a vector and a point in the same direction gets a correct value
        assertEquals(v1, p2.subtract(p1), "ERROR: (point + vector) = other point does not work correctly");
        // =============== Boundary Values Tests ==================
        // TC11: Test that subtracting a point from itself throws an exception
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "ERROR: (point - itself) does not throws an exception");
    }

    @Test
    void testDistanceSquared() {
        Point p1 = new Point(1, 2, 3);
        Point p3 = new Point(2, 4, 5);
        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that the squared distance calculation between 2 points is correct
        assertEquals(9, p1.distanceSquared(p3), "ERROR: squared distance between points is wrong");
        assertEquals(9, p3.distanceSquared(p1), "ERROR: squared distance between points is wrong");
        // =============== Boundary Values Tests ==================
        // TC11: Test that the squared distance between a point and itself is 0
        assertEquals(0, p1.distanceSquared(p1), "ERROR: point squared distance to itself is not zero");
    }

    @Test
    void testDistance() {
        Point p1 = new Point(1, 2, 3);
        Point p3 = new Point(2, 4, 5);
        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that the distance calculation between 2 points is correct
        assertEquals(3, p1.distance(p3), "ERROR: distance between points is wrong");
        assertEquals(3, p3.distance(p1), "ERROR: distance between points is wrong");
        // =============== Boundary Values Tests ==================
        // TC11: Test that the distance between a point and itself is 0
        assertEquals(0, p1.distance(p1), "ERROR: point distance to itself is not zero");
    }
}