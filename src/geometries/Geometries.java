package geometries;

import primitives.*;

import java.util.*;

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
        Collections.addAll(this.geometries, geometries);
        }


    /**
     * Finds all intersection points between the given ray and the geometries in the collection.
     *
     * @param ray the ray for which intersections are to be found.
     * @return a list of all intersection points. If no intersections are found, returns null.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = null;
        for (Intersectable geometry : geometries) {
            List<Point> intersections = geometry.findIntersections(ray);
            if (intersections != null) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(intersections);
            }
        }
        return result
                .stream()
                .sorted(Comparator.comparingDouble(p -> ray.getHead().distance(p)))
                .toList();
    }
}
