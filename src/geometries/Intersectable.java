package geometries;

import primitives.*;

import java.util.List;
import java.util.Objects;

/**
 * The Intersectable interface represents geometric objects that can be intersected by rays.
 * It provides methods to find intersections of rays with these geometric objects.
 */
public abstract class Intersectable {

    /**
     * GeoPoint class represents a point of intersection between a ray and a geometry.
     */
    public static class GeoPoint {
        public Geometry geometry; // The geometry that the ray intersects with
        public Point point;       // The point of intersection

        /**
         * Constructor for GeoPoint.
         *
         * @param geometry the geometry intersected by the ray
         * @param point the point of intersection
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GeoPoint geoPoint)) return false;
            return geometry.equals(geoPoint.geometry) && point.equals(geoPoint.point);
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }

    /**
     * Finds the geometric intersections of a ray with the intersectable object.
     *
     * @param ray the ray to intersect with
     * @return a list of GeoPoint representing the intersections, or null if no intersections are found
     */
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersectionsHelper(ray);
    }

    /**
     * Finds the geometric intersections of a ray with the intersectable object within a specified distance.
     *
     * @param ray the ray to intersect with
     * @param distance the maximum distance from the ray's origin to consider intersections
     * @return a list of GeoPoint representing the intersections within the specified distance, or null if no intersections are found
     */
    public List<GeoPoint> findGeoIntersections(Ray ray, double distance) {
        return findGeoIntersectionsHelper(ray);
    }

    /**
     * Helper method to find the geometric intersections of a ray with the intersectable object.
     * This method must be implemented by subclasses.
     *
     * @param ray the ray to intersect with
     * @return a list of GeoPoint representing the intersections, or null if no intersections are found
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

    /**
     * Finds the intersections of a ray with the intersectable object and returns the points of intersection.
     *
     * @param ray the ray to intersect with
     * @return a list of Points representing the intersections, or null if no intersections are found
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }





}
