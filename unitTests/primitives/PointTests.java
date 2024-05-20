package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class PointTests {

    Point  p1         = new Point(1, 2, 3);
    Point  p2         = new Point(2, 4, 6);
    Point  p3         = new Point(2, 4, 5);

    Vector v1         = new Vector(1, 2, 3);
    Vector v1Opposite = new Vector(-1, -2, -3);
    Vector v2         = new Vector(-2, -4, -6);
    Vector v3         = new Vector(0, 3, -2);
    Vector v4         = new Vector(1, 2, 2);

    @Test
    void testAdd() {
        // Add vector to point
        if (!(p1.add(v1).equals(p2)))
            out.println("ERROR: (point + vector) = other point does not work correctly");
        if (!(p1.add(v1Opposite).equals(Point.ZERO)))
            out.println("ERROR: (point + vector) = center of coordinates does not work correctly");

    }

    @Test
    void testSubtract() {
        // Subtract points
        if (!p2.subtract(p1).equals(v1))
            out.println("ERROR: (point2 - point1) does not work correctly");
        try {
            p1.subtract(p1);
            out.println("ERROR: (point - itself) does not throw an exception");
        } catch (IllegalArgumentException ignore) {} catch (Exception ignore) {
            out.println("ERROR: (point - itself) throws wrong exception");
        }


    }

    @Test
    void testDistanceSquared() {
        if (!isZero(p1.distanceSquared(p1)))
            out.println("ERROR: point squared distance to itself is not zero");
        if (!isZero(p1.distanceSquared(p3) - 9))
            out.println("ERROR: squared distance between points is wrong");
        if (!isZero(p3.distanceSquared(p1) - 9))
            out.println("ERROR: squared distance between points is wrong");
    }

    @Test
    void testDistance() {

        if (!isZero(p1.distance(p1)))
            out.println("ERROR: point distance to itself is not zero");
        if (!isZero(p1.distance(p3) - 3))
            out.println("ERROR: distance between points to itself is wrong");
        if (!isZero(p3.distance(p1) - 3))
            out.println("ERROR: distance between points to itself is wrong");

    }
}