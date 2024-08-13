package geometries;

import primitives.*;

import java.util.*;

/**
 * The Intersectable interface represents geometric objects that can be intersected by rays.
 * It provides methods to find intersections of rays with these geometric objects.
 */
public abstract class Intersectable {

    protected BoundingBox boundingBox= new BoundingBox(Point.NEGATIVE_INFINITY, Point.POSITIVE_INFINITY);
    boolean BVH = true;

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

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
         * @param point    the point of intersection
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GeoPoint geoPoint)) return false;

            return Objects.equals(geometry, geoPoint.geometry) && point.equals(geoPoint.point);
        }

        @Override
        public int hashCode() {
            int result = Objects.hashCode(geometry);
            result = 31 * result + point.hashCode();
            return result;
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
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        if (BVH && !hasIntersection(ray)) {
            return null;
        }
        return findGeoIntersectionsHelper(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Finds the geometric intersections of a ray with the intersectable object within a specified distance.
     *
     * @param ray      the ray to intersect with
     * @param distance the maximum distance from the ray's origin to consider intersections
     * @return a list of GeoPoint representing the intersections within the specified distance, or null if no intersections are found
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray, double distance) {
        if (BVH && !hasIntersection(ray)) {
            return null;
        }
        return findGeoIntersectionsHelper(ray, distance);
    }

    /**
     * Helper method to find the geometric intersections of a ray with the intersectable object.
     * This method must be implemented by subclasses.
     *
     * @param ray the ray to intersect with
     * @return a list of GeoPoint representing the intersections, or null if no intersections are found
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance);

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
    /**
     * Checks if a ray intersects with this bounding box.
     *
     * @param ray The ray to test for intersection
     * @return true if the ray intersects the box, false otherwise
     */
    public boolean hasIntersection(Ray ray) {
        if (!BVH || boundingBox == null)
            return true;

        double xMin = (boundingBox.min.getX() - ray.getHead().getX()) / ray.getDirection().getX();
        double xMax = (boundingBox.max.getX() - ray.getHead().getX()) / ray.getDirection().getX();

        if (xMin > xMax) {
            double temp = xMin;
            xMin = xMax;
            xMax = temp;
        }

        double yMin = (boundingBox.min.getY() - ray.getHead().getY()) / ray.getDirection().getY();
        double yMax = (boundingBox.max.getY() - ray.getHead().getY()) / ray.getDirection().getY();

        if (yMin > yMax) {
            double temp = yMin;
            yMin = yMax;
            yMax = temp;
        }

        if ((xMin > yMax) || (yMin > xMax)) {
            return false;
        }

        if (yMin > xMin) {
            xMin = yMin;
        }

        if (yMax < xMax) {
            xMax = yMax;
        }

        double zMin = (boundingBox.min.getZ() - ray.getHead().getZ()) / ray.getDirection().getZ();
        double zMax = (boundingBox.max.getZ() - ray.getHead().getZ()) / ray.getDirection().getZ();

        if (zMin > zMax) {
            double temp = zMin;
            zMin = zMax;
            zMax = temp;
        }

        if ((xMin > zMax) || (zMin > xMax)) {
            return false;
        }

        if (zMin > xMin) {
            xMin = zMin;
        }

        if (zMax < xMax) {
            xMax = zMax;
        }

        return xMax > 0;

    }

    public class BoundingBox {
        private Point min = Point.NEGATIVE_INFINITY;
        private Point max = Point.POSITIVE_INFINITY;

        public BoundingBox(Point min, Point max) {
            this.min = min;
            this.max = max;
        }




        public Point getCenter() {
            return new Point(
                    (min.getX() + max.getX()) / 2,
                    (min.getY() + max.getY()) / 2,
                    (min.getZ() + max.getZ()) / 2
            );
        }

        /**
         * Union of two bounding boxes
         *
         * @param box the other bounding box
         * @return the union of the two bounding boxes
         */
        private BoundingBox union(BoundingBox box) {
            return new BoundingBox(
                    new Point(Math.min(min.getX(), box.min.getX()), Math.min(min.getY(),
                            box.min.getY()), Math.min(min.getZ(), box.min.getZ())),
                    new Point(Math.max(max.getX(), box.max.getX()), Math.max(max.getY(),
                            box.max.getY()), Math.max(max.getZ(), box.max.getZ()))
            );
        }


        static public List<Intersectable> buildBVH(List<Intersectable> intersectableList) {
            if (intersectableList.size() <= 1) {
                return intersectableList;
            }

            // extract infinite geometries into a separate list
            List<Intersectable> infiniteGeometries = new LinkedList<>();
            for (int i = 0; i < intersectableList.size(); i++) {
                var g = intersectableList.get(i);
                if (g.getBoundingBox() == null) {
                    infiniteGeometries.add(g);
                    intersectableList.remove(i);
                    i--;
                }
            }

            if (intersectableList.isEmpty()) {
                return infiniteGeometries;
            }

            // sort geometries based on their bounding box centroids along an axis (e.g.,x - axis)
            intersectableList.sort(Comparator.comparingDouble(g ->
                    g.getBoundingBox().getCenter().getX()));

            // split the list into two halves
            int mid = intersectableList.size() / 2;
            List<Intersectable> leftGeometries = buildBVH(intersectableList.subList(0, mid));
            List<Intersectable> rightGeometries = buildBVH(intersectableList.subList(mid, intersectableList.size()));

            // create a bounding box for the left and right geometries
            BoundingBox leftBox = getBoundingBox(leftGeometries);
            BoundingBox rightBox = getBoundingBox(rightGeometries);

            // create a combined bounding box
            Geometries combined = new Geometries();
            for (var g : leftGeometries) {
                if (g != null)
                    combined.add(g);
            }
            for (var g : rightGeometries) {
                if (g != null)
                    combined.add(g);
            }

            if (leftBox != null && rightBox != null) {
                combined.boundingBox = leftBox.union(rightBox);
            } else if (leftBox != null) {
                combined.boundingBox = leftBox;
            } else if (rightBox != null) {
                combined.boundingBox = rightBox;
            }

            // return the list of geometries
            List<Intersectable> result = new LinkedList<>(infiniteGeometries);
            result.add(combined);
            return result;
        }

        private static BoundingBox getBoundingBox(List<Intersectable> intersectableList) {
            if (intersectableList.isEmpty()) {
                return null;
            }
            // get the bounding box of the first geometry and union it with the rest
            BoundingBox boundingBox = intersectableList.getFirst().getBoundingBox();
            for (var g: intersectableList) {
                boundingBox = boundingBox.union(g.getBoundingBox());
            }
            return boundingBox;


    }
    }

}
