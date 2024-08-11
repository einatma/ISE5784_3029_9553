package primitives;

import geometries.Intersectable;
import geometries.Intersectable.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3D space, defined by its starting point (head) and direction.
 *
 * @author Hadar Cohen-213953029 and Einat Mazuz -324019553
 */
public class Ray {
    private static final double DELTA = 0.1;
    /**
     * The starting point (head) of the ray.
     */
    private final Point head;
    /**
     * The direction vector of the ray, normalized.
     */
    private final Vector direction;

    /**
     * Constructs a new Ray object with the specified starting point and direction.
     *
     * @param p   The starting point (head) of the ray.
     * @param vec The direction vector of the ray.
     */
    public Ray(Point p, Vector vec) {
        head = p;
        direction = vec.normalize();
    }

    /**
     * Constructor to initialize ray
     *
     * @param p0  point of the ray
     * @param n   normal vector
     * @param dir direction vector of the ray
     */
    public Ray(Point p0, Vector dir, Vector n) {
        double delta = dir.dotProduct(n) >= 0 ? DELTA : -DELTA;
        this.head = p0.add(n.scale(delta));
        this.direction = dir;
    }




    /**
     * Returns the starting point (head) of the ray.
     *
     * @return The starting point (head) of the ray.
     */
    public Point getHead() {
        return head;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ray other = (Ray) obj;
        return head.equals(other.head) && direction.equals(other.direction);
    }

    /**
     * Returns the direction vector of the ray.
     *
     * @return The direction vector of the ray.
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Returns the point at a given parameter t along the ray.
     *
     * @param t The parameter value along the ray.
     * @return The point at parameter t along the ray.
     */
    public Point getPoint(double t) {
        try {
            return head.add(direction.scale(t));
        }
        catch (IllegalArgumentException ignore ) {
            return head;
        }
    }
//    /**
//     * Finds the closest point to the start of the ray (p0) from a list of points.
//     *
//     * @param points the list of points to search
//     * @return the closest point to p0, or null if the list is empty or null
//     */
//    public Point findClosestPoint(List<Point> points) {
//        Point closestPoint = null;
//        double minDistance = Double.MAX_VALUE;
//        double pointDistance; // the distance between the "this.p0" to each point in the list
//
//        if (!points.isEmpty()) {
//            for (var pointInList : points) {
//                pointDistance = this.head.distance(pointInList);
//                if (pointDistance < minDistance) {
//                    minDistance = pointDistance;
//                    closestPoint = pointInList;
//                }
//            }
//        }
//        return closestPoint;
//    }

    /**
     * Finds the closest point to the start of the ray (head) from a list of points.
     *
     * @param points The list of points to search.
     * @return The closest point to head, or null if the list is empty or null.
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream()
                .map(p -> new GeoPoint(null, p))
                .toList())
                .point;
    }

    /**
     * Finds the closest GeoPoint to the start of the ray (head) from a list of GeoPoints.
     *
     * @param geoPoints The list of GeoPoints to search.
     * @return The closest GeoPoint to head, or null if the list is empty or null.
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> geoPoints) {
        GeoPoint closestGeoPoint = null;
        double minDistance = Double.MAX_VALUE;
        double pointDistance; // the distance between the "this.p0" to each point in the list


        for (var geoPoint : geoPoints) {
            pointDistance = this.head.distance(geoPoint.point);
            if (pointDistance < minDistance) {
                minDistance = pointDistance;
                closestGeoPoint = geoPoint;
            }
        }

        return closestGeoPoint;
    }

    /**
     * Generates a bundle of rays originating from points in a list and directed
     * towards a focus point.
     *
     * @param focusPoint The point towards which all the rays will be directed.
     * @param points     The list of points from which the rays will originate.
     * @return A list of rays originating from the given points and directed towards
     *         the focus point.
     */
    public static List<Ray> RayBundle(Point focusPoint, List<Point> points) {
        List<Ray> rays = new ArrayList<>();
        for (Point point : points) {
            rays.add(new Ray(point, focusPoint.subtract(point)));
        }
        return rays;
    }


}
