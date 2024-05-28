package geometries;

import primitives.*;

import java.util.List;

/**
 * Intersectable interface represents geometric objects that can be intersected by rays.
 */
public interface Intersectable {

    /**
     * Finds all the intersection points between a given ray and the geometric object.
     *
     * @param ray the ray to intersect with the geometric object
     * @return a list of points where the ray intersects the object, or an empty list if there are no intersections
     */
    List<Point> findIntersections(Ray ray);
}
