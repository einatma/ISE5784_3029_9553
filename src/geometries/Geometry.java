package geometries;

import primitives.*;

/**
 * An abstract class representing a geometric shape in 3D space.
 * Extends {@link Intersectable}.
 * Provides emission color and material properties.
 *
 * @author Hadar Cohen-213953029 and Einat Mazuz -324019553</p>
 */
public abstract class Geometry extends Intersectable {
    protected Color emission = Color.BLACK;

    private Material material = new Material();

    /**
     * Calculates the normal vector at a given point on the surface of the geometry.
     *
     * @param p The point on the surface of the geometry.
     * @return The normal vector at the given point.
     */
    public abstract Vector getNormal(Point p);

    /**
     * Gets the emission color of the geometry.
     *
     * @return the emission color.
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the emission color to set.
     * @return the current instance of {@code Geometry}.
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Gets the material of the geometry.
     *
     * @return the material.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the geometry.
     *
     * @param material the material to set.
     * @return the current instance of {@code Geometry}.
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
