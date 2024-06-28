package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a directional light source in a scene.
 *
 * @author Hadar Cohen and Einat Mazuz
 */
public class DirectionalLight extends Light implements LightSource {
    private final Vector direction;

    /**
     * Constructs a directional light with the given intensity and direction.
     *
     * @param intensity the intensity/color of the light
     * @param direction the direction of the light as a normalized vector
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    /**
     * Retrieves the intensity of the light at a given point (not applicable for directional lights).
     *
     * @param p the point in the scene (ignored for directional lights)
     * @return the intensity/color of the light
     */
    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    /**
     * Retrieves the direction vector of the light at a given point.
     *
     * @param p the point in the scene (ignored for directional lights)
     * @return the direction vector of the light
     */
    @Override
    public Vector getL(Point p) {
        return this.direction;
    }

    /**
     * Retrieves the distance from the light to a given point in the scene.
     *
     * @param p the point in the scene (ignored for directional lights)
     * @return positive infinity, indicating the light is infinitely far away
     */
    @Override
    public double getDistance(Point p) {
        return Double.POSITIVE_INFINITY;
    }

}
