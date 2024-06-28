package geometries;

/**
 * Represents a radial geometry in 3D space, defined by a radius.
 * This is an abstract class that serves as a base for all radial geometries.
 *
 * @author Hadar Cohen and Einat Mazuz
 */
public abstract class RadialGeometry extends Geometry {
    /**
     * The radius of the radial geometry.
     */
    final double radius;
    /**
     * The squared radius of the radial geometry.
     */
    final double RadiusSquared;

    /**
     * Constructs a new RadialGeometry object with the specified radius.
     *
     * @param r The radius of the radial geometry.
     */
    public RadialGeometry(double r) {
        radius = r;
        RadiusSquared = r * r;

    }

    /**
     * Gets the radius of the radial geometry.
     *
     * @return The radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Gets the squared radius of the radial geometry.
     *
     * @return The squared radius.
     */
    public double getRadiusSquared() {
        return RadiusSquared;
    }
}
