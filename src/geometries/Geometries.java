package geometries;

import primitives.*;

import java.util.*;
//
/**
 * Composite class for aggregating multiple Intersectable objects.
 */
public class Geometries extends Intersectable {
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
        if (geometries != null)
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
     *            //* @param distance
     * @return a list of all intersection points. If no intersections are found, returns null.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        List<GeoPoint> result = null;
        for (Intersectable geometry : geometries) {
            List<GeoPoint> intersections = geometry.findGeoIntersections(ray, distance);
            if (intersections != null) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(intersections);
            }
        }
        return result;
    }
    public void makeBVH() {
        List<Intersectable> intersectables = BoundingBox.buildBVH(geometries);
        geometries.clear();
        geometries.addAll(intersectables);

    }


}
