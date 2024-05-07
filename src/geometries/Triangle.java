package geometries;
import primitives.Point;
/**
 * Represents a triangle in 3D space, defined by its three vertices.
 * @author Hadar Cohen and Einat Mazuz
 */
public class Triangle extends Polygon{
    /**
     * Constructs a new Triangle object with the specified vertices.
     *
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3)
    {
        super(p1,p2,p3);
    }
}
