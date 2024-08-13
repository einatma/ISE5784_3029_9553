package geometries;

import primitives.*;

/**
 * An abstract class representing a geometric shape in 3D space.
 * Extends {@link Intersectable} and provides properties related to emission color and material.
 * This class serves as a base for all geometric shapes, encapsulating common properties and behaviors
 * such as emission color and material characteristics.
 *
 * @author Hadar Cohen
 * @version 213953029 and Einat Mazuz - 324019553
 */
public abstract class Geometry extends Intersectable {
    /**
     * The emission color of the geometry.
     * This represents the inherent color of the object, independent of lighting.
     * Initialized to black by default.
     */
    protected Color emission = Color.BLACK;

    /**
     * The material of the geometry.
     * This defines the physical properties of the object, such as shininess, transparency, and reflection.
     * Initialized with default values.
     */
    private Material material = new Material();

    /**
     * Calculates the normal vector at a given point on the surface of the geometry.
     * The normal vector is perpendicular to the surface at the specified point and is
     * typically used in lighting calculations to determine how light interacts with the surface.
     *
     * @param p The point on the surface of the geometry.
     * @return The normal vector at the given point.
     */
    public abstract Vector getNormal(Point p);

    /**
     * Gets the emission color of the geometry.
     * The emission color is the base color emitted by the geometry, not affected by external light sources.
     *
     * @return the emission color.
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     * Allows the emission color to be modified, which changes the base color of the geometry.
     *
     * @param emission the emission color to set.
     * @return the current instance of {@code Geometry}, allowing for method chaining.
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Gets the material of the geometry.
     * The material defines how the geometry interacts with light, including properties like
     * diffuseness, specularity, and transparency.
     *
     * @return the material.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the geometry.
     * Allows the material properties of the geometry to be modified, affecting how the geometry
     * responds to light.
     *
     * @param material the material to set.
     * @return the current instance of {@code Geometry}, allowing for method chaining.
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
