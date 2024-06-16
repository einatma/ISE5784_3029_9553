package geometries;

import primitives.*;

/**
 * An interface representing a geometric shape in 3D space.
 *
 * @author Hadar Cohen-213953029 and Einat Mazuz -324019553
 */
public abstract class Geometry extends Intersectable {
    private Color emission = Color.BLACK;
    /**
     * Calculates the normal vector at a given point on the surface of the geometry.
     *
     * @param p The point on the surface of the geometry.
     * @return The normal vector at the given point.
     */
    abstract Vector getNormal(Point p);

    public Color getEmission() {
        return emission;
    }

    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
}
