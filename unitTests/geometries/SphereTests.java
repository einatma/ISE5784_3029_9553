package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;

import static java.util.stream.Collectors.toList;
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

    private final Point p001 = new Point(0, 0, 1);
    private final Point p100 = new Point(1, 0, 0);
    private final Vector v001 = new Vector(0, 0, 1);

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(p100, 1d);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final var exp = List.of(gp1, gp2);
        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310))
                .stream().sorted(Comparator.comparingDouble(p -> p.distance(p01)))
                .toList();
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        final Point pInside = new Point(0.5, 0, 0);
        final var result2 = sphere.findIntersections(new Ray(pInside, v310))
                .stream().sorted(Comparator.comparingDouble(p -> p.distance(pInside)))
                .toList();
        assertEquals(1, result2.size(), "Wrong number of points");
        assertEquals(List.of(gp2), result2, "Ray starts inside sphere");

        // TC04: Ray starts after the sphere (0 points)
        final Point pOut = new Point(2, 0, 0);
        assertNull(sphere.findIntersections(new Ray(pOut, v110)), "Ray starts after the sphere");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        final Point pOnSurfaceIn = new Point(0, 0, 0);
        final Point gp4 = new Point(1.6651530771650466, 0.355051025721682, 0);
        final var result3 = sphere.findIntersections(new Ray(pOnSurfaceIn, v310))
                .stream().sorted(Comparator.comparingDouble(p -> p.distance(pOnSurfaceIn)))
                .toList();
        assertEquals(1, result3.size(), "Wrong number of points");
        assertEquals(List.of(gp4), result3, "Ray starts at sphere and goes inside");

        // TC12: Ray starts at sphere and goes outside (0 points)
        final Point pOnSurfaceOut = new Point(1, 0, 0);
        assertNull(sphere.findIntersections(new Ray(pOnSurfaceOut, v310)), "Ray starts at sphere and goes outside");

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        final Point pBefore = new Point(2, 0, 0);
        final Vector vCenter = new Vector(-1, 0, 0);
        final Point gp5 = new Point(0, 0, 0);
        final Point gp6 = new Point(2, 0, 0);
        final var exp3 = List.of(gp5, gp6);
        final var result4 = sphere.findIntersections(new Ray(pBefore, vCenter))
                .stream().sorted(Comparator.comparingDouble(p -> p.distance(pBefore)))
                .toList();
        assertEquals(2, result4.size(), "Wrong number of points");
        assertEquals(exp3, result4, "Ray goes through the center");

        // TC14: Ray starts at sphere and goes inside (1 point)
        final Point gp7 = new Point(0, 0, 0);
        final var result5 = sphere.findIntersections(new Ray(p100, vCenter));
        assertEquals(1, result5.size(), "Wrong number of points");
        assertEquals(List.of(gp7), result5, "Ray starts at sphere and goes inside through center");

        // TC15: Ray starts inside (1 point)
        final var result6 = sphere.findIntersections(new Ray(pInside, vCenter));
        assertEquals(1, result6.size(), "Wrong number of points");
        assertEquals(List.of(gp7), result6, "Ray starts inside and goes through center");

        // TC16: Ray starts at the center (1 point)
        final Point pCenter = new Point(1, 0, 0);
        final var result7 = sphere.findIntersections(new Ray(pCenter, vCenter));
        assertEquals(1, result7.size(), "Wrong number of points");
        assertEquals(List.of(gp7), result7, "Ray starts at the center and goes through sphere");

        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(p100, v310)), "Ray starts at sphere and goes outside through center");

        // TC18: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(pBefore, vCenter)), "Ray starts after the sphere through center");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        final Point pBeforeTangent = new Point(1, -1, 0);
        final Vector vTangent = new Vector(1, 0, 0);
        assertNull(sphere.findIntersections(new Ray(pBeforeTangent, vTangent)), "Ray starts before the tangent point");

        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(p100, vTangent)), "Ray starts at the tangent point");

        // TC21: Ray starts after the tangent point
        final Point pAfterTangent = new Point(1, 1, 0);
        assertNull(sphere.findIntersections(new Ray(pAfterTangent, vTangent)), "Ray starts after the tangent point");

        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        final Point pOrthogonal = new Point(0, 2, 0);
        final Vector vOrthogonal = new Vector(0, -1, 0);
        assertNull(sphere.findIntersections(new Ray(pOrthogonal, vOrthogonal)), "Ray is orthogonal to ray start to sphere's center line");
    }
}