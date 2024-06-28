package lighting;

import primitives.*;

/**
 * Interface representing a light source in a scene.
 *
 * @author Hadar Cohen and Einat Mazuz
 */
public interface LightSource {
    /**
     * Retrieves the intensity of the light at a given point.
     *
     * @param p the point in space
     * @return the intensity/color of the light at the given point
     */
    public Color getIntensity(Point p);

    /**
     * Retrieves the direction vector from the light source to a given point.
     *
     * @param p the point in space
     * @return the direction vector from the light source to the given point
     */
    public Vector getL(Point p);

    /**
     * Retrieves the distance from the light source to a given point.
     *
     * @param p the point in space
     * @return the distance from the light source to the given point
     */
    public double getDistance(Point p);

}
