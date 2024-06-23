package primitives;

import geometries.Intersectable;
import geometries.Intersectable.GeoPoint;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3D space, defined by its starting point (head) and direction.
 *
 * @author Hadar Cohen-213953029 and Einat Mazuz -324019553
 */
public class Ray {
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


    public Point getPoint(double t) {
        if(isZero(t))
            return head;
        return head.add(direction.scale(t));
    }
    /**
     * Finds the closest point to the start of the ray (p0) from a list of points.
     *
     * @param points the list of points to search
     * @return the closest point to p0, or null if the list is empty or null
     */
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
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }
    public GeoPoint findClosestGeoPoint(List<GeoPoint> geoPoints) {
        GeoPoint closestGeoPoint = null;
        double minDistance = Double.MAX_VALUE;
        double pointDistance; // the distance between the "this.p0" to each point in the list

        if (!geoPoints.isEmpty()) {
            for (var geoPoint : geoPoints) {
                pointDistance = this.head.distance(geoPoint.point);
                if (pointDistance < minDistance) {
                    minDistance = pointDistance;
                    closestGeoPoint = geoPoint;
                }
            }
        }
        return closestGeoPoint;
    }

}
