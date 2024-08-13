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
     * Checks if a ray intersects with this bounding box, used for BVH optimization.
     *
     * This method calculates the intersection of the ray with the bounding box using
     * the slab method, which involves checking intersection with the planes defining
     * the bounding box's sides. The method returns true if the ray intersects the
     * bounding box and false otherwise.
     *
     * @param ray The ray to test for intersection
     * @return true if the ray intersects the bounding box, false otherwise
     */
    public boolean hasIntersection(Ray ray) {
        // If BVH is not enabled or the bounding box is null, assume intersection is possible
        if (!BVH || boundingBox == null) {
            return true;
        }

        // Calculate intersections with the slabs on the x-axis
        double xMin = (boundingBox.min.getX() - ray.getHead().getX()) / ray.getDirection().getX();
        double xMax = (boundingBox.max.getX() - ray.getHead().getX()) / ray.getDirection().getX();

        // Ensure xMin is the smaller value, xMax is the larger value
        if (xMin > xMax) {
            double temp = xMin;
            xMin = xMax;
            xMax = temp;
        }

        // Calculate intersections with the slabs on the y-axis
        double yMin = (boundingBox.min.getY() - ray.getHead().getY()) / ray.getDirection().getY();
        double yMax = (boundingBox.max.getY() - ray.getHead().getY()) / ray.getDirection().getY();

        // Ensure yMin is the smaller value, yMax is the larger value
        if (yMin > yMax) {
            double temp = yMin;
            yMin = yMax;
            yMax = temp;
        }

        // Check for any potential separation on the x and y planes, indicating no intersection
        if ((xMin > yMax) || (yMin > xMax)) {
            return false;
        }

        // Update xMin to the maximum of xMin and yMin (overlap region)
        if (yMin > xMin) {
            xMin = yMin;
        }

        // Update xMax to the minimum of xMax and yMax (overlap region)
        if (yMax < xMax) {
            xMax = yMax;
        }

        // Calculate intersections with the slabs on the z-axis
        double zMin = (boundingBox.min.getZ() - ray.getHead().getZ()) / ray.getDirection().getZ();
        double zMax = (boundingBox.max.getZ() - ray.getHead().getZ()) / ray.getDirection().getZ();

        // Ensure zMin is the smaller value, zMax is the larger value
        if (zMin > zMax) {
            double temp = zMin;
            zMin = zMax;
            zMax = temp;
        }

        // Check for any potential separation between x and z, or y and z planes, indicating no intersection
        if ((xMin > zMax) || (zMin > xMax)) {
            return false;
        }

        // Update xMin to the maximum of xMin and zMin (overlap region)
        if (zMin > xMin) {
            xMin = zMin;
        }

        // Update xMax to the minimum of xMax and zMax (overlap region)
        if (zMax < xMax) {
            xMax = zMax;
        }

        // Ensure the intersection occurs in front of the ray origin
        return xMax > 0;
    }

    /**
     * The BoundingBox class represents an axis-aligned bounding box (AABB) used for
     * spatial partitioning in a 3D space. It is used for optimizing ray intersection tests
     * by enclosing geometric objects within these boxes.
     */
    public class BoundingBox {
        private Point min = Point.NEGATIVE_INFINITY;  // The minimum corner of the bounding box
        private Point max = Point.POSITIVE_INFINITY;  // The maximum corner of the bounding box

        /**
         * Constructor for creating a bounding box with specified minimum and maximum points.
         *
         * @param min The minimum corner point of the bounding box
         * @param max The maximum corner point of the bounding box
         */
        public BoundingBox(Point min, Point max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Calculates and returns the center point of the bounding box.
         *
         * @return The center point of the bounding box
         */
        public Point getCenter() {
            return new Point(
                    (min.getX() + max.getX()) / 2,
                    (min.getY() + max.getY()) / 2,
                    (min.getZ() + max.getZ()) / 2
            );
        }

        /**
         * Merges this bounding box with another bounding box to create a new bounding box
         * that encloses both.
         *
         * @param box The other bounding box to merge with
         * @return A new bounding box that is the union of the two bounding boxes
         */
        private BoundingBox union(BoundingBox box) {
            return new BoundingBox(
                    new Point(Math.min(min.getX(), box.min.getX()),
                            Math.min(min.getY(), box.min.getY()),
                            Math.min(min.getZ(), box.min.getZ())),
                    new Point(Math.max(max.getX(), box.max.getX()),
                            Math.max(max.getY(), box.max.getY()),
                            Math.max(max.getZ(), box.max.getZ()))
            );
        }

        /**
         * Constructs a Bounding Volume Hierarchy (BVH) from a list of intersectable geometries.
         * The BVH is a binary tree structure used to efficiently test for ray intersections
         * with complex scenes by dividing the scene into smaller bounding volumes.
         *
         * @param intersectableList The list of geometries to build the BVH from
         * @return A list of intersectable geometries organized into a BVH
         */
        public static List<Intersectable> buildBVH(List<Intersectable> intersectableList) {
            // Base case: if the list contains one or no geometries, return the list as is
            if (intersectableList.size() <= 1) {
                return intersectableList;
            }

            // Extract infinite geometries (geometries without bounding boxes) into a separate list
            List<Intersectable> infiniteGeometries = new LinkedList<>();
            for (int i = 0; i < intersectableList.size(); i++) {
                var g = intersectableList.get(i);
                if (g.getBoundingBox() == null) {
                    infiniteGeometries.add(g);
                    intersectableList.remove(i);
                    i--;
                }
            }

            // If all geometries are infinite, return the list of infinite geometries
            if (intersectableList.isEmpty()) {
                return infiniteGeometries;
            }

            // Sort the remaining geometries based on the x-axis of their bounding box centers
            intersectableList.sort(Comparator.comparingDouble(g ->
                    g.getBoundingBox().getCenter().getX()));

            // Split the list into two halves for recursive BVH construction
            int mid = intersectableList.size() / 2;
            List<Intersectable> leftGeometries = buildBVH(intersectableList.subList(0, mid));
            List<Intersectable> rightGeometries = buildBVH(intersectableList.subList(mid, intersectableList.size()));

            // Create bounding boxes for the left and right sets of geometries
            BoundingBox leftBox = getBoundingBox(leftGeometries);
            BoundingBox rightBox = getBoundingBox(rightGeometries);

            // Combine the left and right geometries into a single Geometries object
            Geometries combined = new Geometries();
            for (var g : leftGeometries) {
                if (g != null)
                    combined.add(g);
            }
            for (var g : rightGeometries) {
                if (g != null)
                    combined.add(g);
            }

            // Create a combined bounding box that encloses both the left and right boxes
            if (leftBox != null && rightBox != null) {
                combined.boundingBox = leftBox.union(rightBox);
            } else if (leftBox != null) {
                combined.boundingBox = leftBox;
            } else if (rightBox != null) {
                combined.boundingBox = rightBox;
            }

            // Add the combined bounding box to the list of geometries and return the result
            List<Intersectable> result = new LinkedList<>(infiniteGeometries);
            result.add(combined);
            return result;
        }

        /**
         * Calculates the bounding box that encloses a list of intersectable geometries.
         *
         * @param intersectableList The list of geometries to calculate the bounding box for
         * @return A bounding box that encloses all the geometries in the list, or null if the list is empty
         */
        private static BoundingBox getBoundingBox(List<Intersectable> intersectableList) {
            if (intersectableList.isEmpty()) {
                return null;
            }

            // Initialize the bounding box with the first geometry's bounding box
            BoundingBox boundingBox = intersectableList.get(0).getBoundingBox();

            // Union the bounding box with each subsequent geometry's bounding box
            for (var g: intersectableList) {
                boundingBox = boundingBox.union(g.getBoundingBox());
            }

            return boundingBox;
        }
    }
}