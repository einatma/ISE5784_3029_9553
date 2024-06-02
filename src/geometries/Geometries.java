package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Composite class for aggregating multiple Intersectable objects.
 */
public class Geometries implements Intersectable {
    // List of intersectable geometries initialized with an empty LinkedList
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor.
     * Initializes an empty collection of geometries.
     */
    public Geometries() {
    }

    /**
     * Constructor with a variable number of Intersectable objects.
     * Adds the provided geometries to the collection.
     *
     * @param geometries the geometries to be added to the collection.
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds multiple Intersectable objects to the geometries list.
     *
     * @param geometries the geometries to be added.
     */
    public void add(Intersectable... geometries) {
        for (Intersectable geometry : geometries) {
            this.geometries.add(geometry);
        }
    }

    /**
     * Finds all intersection points between the given ray and the geometries in the collection.
     *
     * @param ray the ray for which intersections are to be found.
     * @return a list of all intersection points. If no intersections are found, returns null.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = null; // List to store the intersection points
        // Iterate over each geometry in the list
        for (Intersectable geometry : geometries) {
            // Find intersection points with the current geometry
            List<Point> intersections = geometry.findIntersections(ray);

            // If intersections are found, add them to the result list
            if (intersections != null) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(intersections);
            }
        }
        // Return the list of intersection points, or null if no intersections were found
        return result;
    }
}
