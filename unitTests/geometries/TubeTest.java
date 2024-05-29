package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    @Test
    void testgetNormal() {
        Tube tube = new Tube(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0)), 1);

        // ============ Equivalence Partitions Tests ==============

        assertEquals(new Vector(0, 0, 1),
                tube.getNormal(new Point(1, 0, 1)),
                "ERROR: The calculation of normal to the tube is not calculated correctly");

        // =============== Boundary Values Tests ==================
        //Test when the point is orthogonal to the ray's head goes to the ZERO vector
        assertThrows(IllegalArgumentException.class, () -> {
                    tube.getNormal(new Point(0, 0, 1));
                },
                "ZERO vector is not allowed");
    }
    @Test
    void testfindIntersections() {
        Tube tube = new Tube(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0)), 1);

        // =============== Equivalence Partitions Tests ================//

        // TC01: Ray's line is outside the tube (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 1, 0))),
                "Ray's line out of tube");

        // TC02: Ray starts before and crosses the tube (2 points)
        Point p1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        Point p2 = new Point(1.53484692283495, 0.844948974278318, 0);

        List<Point> result = tube.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(3, 1, 0)));

        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX()) {
            result = List.of(result.get(1), result.get(0));
        }
        assertEquals(List.of(p1, p2), result, "Ray crosses tube");

        // TC03: Ray starts inside the tube (1 point)
        assertEquals(List.of(p2),
                tube.findIntersections(new Ray(new Point(0.8, 0.6, 0), new Vector(3, 1, 0))),
                "Ray crosses tube on one point");

        // TC04: Ray starts after the tube (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(3, 0, 0), new Vector(1, 1, 0))),
                "Ray's line after the tube");

        // TC05: Ray's line is tangent to the tube (1 point)
        assertEquals(List.of(new Point(1, 1, 0)),
                tube.findIntersections(new Ray(new Point(1, 2, 0), new Vector(0, -1, 0))),
                "Ray is tangent to the tube");

        // TC06: Ray's line intersects the tube's surface at two points
        Point p3 = new Point(-0.6, 0.8, 0);
        Point p4 = new Point(0.6, -0.8, 0);

        result = tube.findIntersections(new Ray(new Point(-1, 1, 0), new Vector(2, -2, 0)));

        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX()) {
            result = List.of(result.get(1), result.get(0));
        }
        assertEquals(List.of(p3, p4), result, "Ray intersects the tube's surface at two points");

        // TC07: Ray's line is inside the tube and intersects the surface at one point
        assertEquals(List.of(new Point(1, 1, 0)),
                tube.findIntersections(new Ray(new Point(0, 0.5, 0), new Vector(1, 0.5, 0))),
                "Ray's line is inside the tube and intersects the surface at one point");

        // TC08: Ray's line is inside the tube and does not intersect the surface
        assertNull(tube.findIntersections(new Ray(new Point(0.5, 0.5, 0), new Vector(0.5, 0.5, 0))),
                "Ray's line is inside the tube and does not intersect the surface");

        // TC09: Ray's line starts at the surface and goes outward
        assertNull(tube.findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 0, 0))),
                "Ray's line starts at the surface and goes outward");

        // TC10: Ray's line starts at the surface and goes inward
        assertEquals(List.of(new Point(2, 0, 0)),
                tube.findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 0, 0))),
                "Ray's line starts at the surface and goes inward");

        // =============== Boundary Values Tests ==================

        // TC11: Ray is parallel to the tube and outside (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, 2, 0), new Vector(1, 0, 0))),
                "Ray is parallel to the tube and outside");

        // TC12: Ray is parallel to the tube and inside (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, 0.5, 0), new Vector(1, 0, 0))),
                "Ray is parallel to the tube and inside");

        // TC13: Ray is orthogonal to the tube's axis and intersects the surface at one point
        assertEquals(List.of(new Point(1, 1, 0)),
                tube.findIntersections(new Ray(new Point(1, 2, 0), new Vector(0, -1, 0))),
                "Ray is orthogonal to the tube's axis and intersects the surface at one point");

        // TC14: Ray is orthogonal to the tube's axis and passes through the center (2 points)
        p3 = new Point(0, 1, 0);
        p4 = new Point(2, 1, 0);

        result = tube.findIntersections(new Ray(new Point(-1, 1, 0), new Vector(3, 0, 0)));

        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX()) {
            result = List.of(result.get(1), result.get(0));
        }
        assertEquals(List.of(p3, p4), result, "Ray is orthogonal to the tube's axis and passes through the center");

        // TC15: Ray starts on the surface of the tube and goes outward (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(1, 1, 0), new Vector(1, 1, 0))),
                "Ray starts on the surface of the tube and goes outward");

        // TC16: Ray starts on the surface of the tube and goes inward (1 point)
        assertEquals(List.of(p4),
                tube.findIntersections(new Ray(new Point(1, 1, 0), new Vector(1, 0, 0))),
                "Ray starts on the surface of the tube and goes inward");

        // TC17: Ray starts at the center of the tube and goes outward (1 point)
        assertEquals(List.of(new Point(1, 1, 0)),
                tube.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 1, 0))),
                "Ray starts at the center of the tube and goes outward");

        // TC18: Ray starts at the center of the tube and is parallel to the tube's axis (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))),
                "Ray starts at the center of the tube and is parallel to the tube's axis");

        // TC19: Ray starts outside and is tangent to the tube (1 point)
        assertEquals(List.of(new Point(1, 1, 0)),
                tube.findIntersections(new Ray(new Point(1, 2, 0), new Vector(0, -1, 0))),
                "Ray is tangent to the tube");

        // TC20: Ray starts outside and misses the tube (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-2, 0, 0), new Vector(1, 1, 0))),
                "Ray starts outside and misses the tube");

        // TC21: Ray starts inside the tube and moves parallel to the axis (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0.5, 0.5, 0), new Vector(1, 0, 0))),
                "Ray starts inside the tube and moves parallel to the axis");

        // TC22: Ray starts inside the tube and moves diagonally outwards (1 point)
        assertEquals(List.of(new Point(2, 0.5, 0.5)),
                tube.findIntersections(new Ray(new Point(0.5, 0.5, 0), new Vector(1, 0, 0))),
                "Ray starts inside the tube and moves diagonally outwards");

        // TC23: Ray starts on the axis and moves outwards (1 point)
        assertEquals(List.of(new Point(1, 0, 0)),
                tube.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))),
                "Ray starts on the axis and moves outwards");

        // TC24: Ray starts outside and crosses the tube at a shallow angle (2 points)
        p3 = new Point(0.2928932188134524, 0.7071067811865476, 0);
        p4 = new Point(2.2928932188134525, 0.7071067811865475, 0);

        result = tube.findIntersections(new Ray(new Point(-1, 1, 0), new Vector(3, 0, 0)));

        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX()) {
            result = List.of(result.get(1), result.get(0));
        }
        assertEquals(List.of(p3, p4), result, "Ray crosses the tube at a shallow angle");

        // TC25: Ray starts at the edge and moves tangentially (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(1, 1, 0), new Vector(1, 1, 0))),
                "Ray starts at the edge and moves tangentially");

        // TC26: Ray starts inside the tube and moves outwards at an angle (1 point)
        assertEquals(List.of(new Point(1.4142135623730951, 1.414213562373095, 0)),
                tube.findIntersections(new Ray(new Point(0.5, 0.5, 0), new Vector(1, 1, 0))),
                "Ray starts inside the tube and moves outwards at an angle");

        // TC27: Ray starts inside and is orthogonal to the axis (1 point)
        assertEquals(List.of(new Point(1, 1, 0)),
                tube.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 1, 0))),
                "Ray starts inside and is orthogonal to the axis");

        // TC28: Ray starts at the axis and is orthogonal to the axis (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 1, 0))),
                "Ray starts at the axis and is orthogonal to the axis");

        // TC29: Ray starts at the edge and moves along the surface (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 1, 0))),
                "Ray starts at the edge and moves along the surface");

        // TC30: Ray starts on the surface and moves inside (1 point)
        assertEquals(List.of(new Point(2, 0, 0)),
                tube.findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 0, 0))),
                "Ray starts on the surface and moves inside");

        // TC31: Ray starts outside and is orthogonal to the axis and tangent (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(1, 2, 0), new Vector(0, -1, 0))),
                "Ray starts outside and is orthogonal to the axis and tangent");

        // TC32: Ray starts inside and moves parallel to the axis (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0.5, 0.5, 0), new Vector(1, 0, 0))),
                "Ray starts inside and moves parallel to the axis");

        // TC33: Ray starts on the surface and moves along the surface (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(1, 1, 0), new Vector(1, 0, 0))),
                "Ray starts on the surface and moves along the surface");

        // TC34: Ray starts on the axis and moves at an angle (1 point)
        assertEquals(List.of(new Point(1, 1, 0)),
                tube.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 1, 0))),
                "Ray starts on the axis and moves at an angle");

        // TC35: Ray starts at the center and moves parallel to the axis (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))),
                "Ray starts at the center and moves parallel to the axis");

        // TC36: Ray starts outside and crosses the axis at an angle (2 points)
        p3 = new Point(-1, 0, 0);
        p4 = new Point(1, 0, 0);

        result = tube.findIntersections(new Ray(new Point(-2, -1, 0), new Vector(2, 2, 0)));

        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX()) {
            result = List.of(result.get(1), result.get(0));
        }
        assertEquals(List.of(p3, p4), result, "Ray crosses the axis at an angle");

        // TC37: Ray starts outside and is tangent to the tube's surface (1 point)
        assertEquals(List.of(new Point(1, 1, 0)),
                tube.findIntersections(new Ray(new Point(2, 1, 0), new Vector(-1, 0, 0))),
                "Ray starts outside and is tangent to the tube's surface");

        // TC38: Ray starts at the surface and moves diagonally outwards (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(1, 1, 0), new Vector(1, 1, 0))),
                "Ray starts at the surface and moves diagonally outwards");

        // TC39: Ray starts at the surface and moves diagonally inwards (1 point)
        assertEquals(List.of(new Point(2, 1, 0)),
                tube.findIntersections(new Ray(new Point(1, 1, 0), new Vector(1, 0, 0))),
                "Ray starts at the surface and moves diagonally inwards");

        // TC40: Ray starts inside and moves orthogonally to the axis (1 point)
        assertEquals(List.of(new Point(1, 1, 0)),
                tube.findIntersections(new Ray(new Point(0.5, 0.5, 0), new Vector(1, 1, 0))),
                "Ray starts inside and moves orthogonally to the axis");
    }
}