package geometries;
import primitives.*;
/**
 * An interface representing a geometric shape in 3D space.
 * @author Hadar Cohen and Einat Mazuz
*/
public interface Geometry {
    /**
     * Calculates the normal vector at a given point on the surface of the geometry.
     *
     * @param p The point on the surface of the geometry.
     * @return The normal vector at the given point.
     */
    public Vector getNormal(Point p);
}
