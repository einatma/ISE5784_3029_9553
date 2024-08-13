package geometries;

import primitives.*;

import java.util.*;

/**
 * Composite class for aggregating multiple Intersectable objects.
 * This class allows for grouping various geometric shapes and treating them as a single entity
 * when performing intersection tests with rays.
 * It supports adding geometries dynamically and finding all intersections between a given ray and
 * the aggregated geometries.
 */
public class Geometries extends Intersectable {
    /**
     * List of intersectable geometries initialized with an empty LinkedList.
     */
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor.
     * Initializes an empty collection of geometries.
     */
    public Geometries() {
        // Empty constructor, initializing an empty geometries list
    }

    /**
     * Constructor with a variable number of Intersectable objects.
     * Adds the provided geometries to the collection.
     *
     * @param geometries the geometries to be added to the collection.
     */
    public Geometries(Intersectable... geometries) {
        if (geometries != null) {
            add(geometries); // Add the provided geometries to the collection
        }
    }

    /**
     * Adds multiple Intersectable objects to the geometries list.
     * This method allows for adding multiple geometric shapes to the collection at once.
     *
     * @param geometries the geometries to be added.
     */
    public void add(Intersectable... geometries) {
        // Add all provided geometries to the internal list
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Finds all intersection points between the given ray and the geometries in the collection.
     * The method iterates over all geometries in the collection and checks for intersections
     * with the provided ray. If intersections are found, they are aggregated into a single list.
     *
     * @param ray      the ray for which intersections are to be found.
     * @param distance the maximum distance to consider for intersections.
     * @return a list of all intersection points. If no intersections are found, returns null.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        List<GeoPoint> result = null; // Initialize the result list as null

        // Iterate over each geometry in the collection
        for (Intersectable geometry : geometries) {
            // Find intersections with the current geometry
            List<GeoPoint> intersections = geometry.findGeoIntersections(ray, distance);

            // If intersections are found, add them to the result list
            if (intersections != null) {
                if (result == null) {
                    result = new LinkedList<>(); // Initialize the result list if it is null
                }
                result.addAll(intersections); // Add the found intersections to the result list
            }
        }
        return result; // Return the list of intersections or null if no intersections were found
    }

    /**
     * Converts the list of geometries into a Bounding Volume Hierarchy (BVH) tree.
     * This method optimizes the performance of intersection tests by organizing the geometries
     * into a tree structure that allows for efficient spatial queries.
     */
    public void makeBVH() {
        // Build the BVH tree from the current list of geometries
        List<Intersectable> intersectables = BoundingBox.buildBVH(geometries);

        // Clear the current list of geometries
        geometries.clear();

        // Add the BVH tree nodes back into the geometries list
        geometries.addAll(intersectables);
    }
}
