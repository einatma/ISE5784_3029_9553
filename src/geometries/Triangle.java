package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Represents a triangle in 3D space, defined by its three vertices.
 *
 * @author Hadar Cohen and Einat Mazuz
 */
public class Triangle extends Polygon {
    /**
     * Constructs a new Triangle object with the specified vertices.
     *
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        //Check if the ray intersects the plane.
        if (plane.findIntersections(ray) == null) {
            return null;
        }

        // the three vectors from the same starting point
        var v1 = vertices.get(0).subtract(ray.getHead());
        var v2 = vertices.get(1).subtract(ray.getHead());
        var v3 = vertices.get(2).subtract(ray.getHead());

        // we want to get a normal for each pyramid's face so we do the crossProduct
        var n1 = v1.crossProduct(v2).normalize();
        var n2 = v2.crossProduct(v3).normalize();
        var n3 = v3.crossProduct(v1).normalize();

        // the ray's vector  - it has the same starting point as the three vectors from above
        var v = ray.getDirection();

        // check if the vector's direction (from Subtraction between the ray's vector to each vector from above) are equal
        // if not - there is no intersection point between the ray and the triangle
        if ((alignZero(v.dotProduct(n1)) > 0 && alignZero(v.dotProduct(n2)) > 0 && alignZero(v.dotProduct(n3)) > 0) ||
                (alignZero(v.dotProduct(n1)) < 0 && alignZero(v.dotProduct(n2)) < 0 && alignZero(v.dotProduct(n3)) < 0)){

            return plane.findIntersections(ray);
        }

        return null;
    }

}
