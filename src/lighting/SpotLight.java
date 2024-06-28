package lighting;

import primitives.*;

import static primitives.Util.alignZero;

/**
 * Represents a spotlight in a 3D scene, extending PointLight.
 *
 * @author Hadar Cohen and Einat Mazuz
 */
public class SpotLight extends PointLight {
    // Direction in which the spotlight is pointing
    private Vector direction;

    /**
     * Constructs a SpotLight object with the specified intensity, position, and direction.
     *
     * @param I         the intensity/color of the light
     * @param position  the position of the spotlight
     * @param direction the direction in which the spotlight is pointing
     */
    public SpotLight(Color I, Point position, Vector direction) {
        super(I, position);
        this.direction = direction.normalize();
    }

    /**
     * Retrieves the intensity of the light at a given point, considering distance attenuation
     * and angular attenuation based on the cosine of the angle between the spotlight's direction
     * and the vector to the point.
     *
     * @param p the point in space
     * @return the intensity/color of the light at the given point
     */
    public Color getIntensity(Point p) {
        Vector l = getL(p);
        if (l == null) {
            l = direction;
        }
        double cos = alignZero(direction.dotProduct(l));
        if (cos <= 0) {
            return Color.BLACK;
        }
        return super.getIntensity(p).scale(cos);
    }

    /**
     * Retrieves the direction vector from the light source to a given point,
     * considering the spotlight's direction.
     *
     * @param p the point in space
     * @return the direction vector from the light source to the given point
     */
    public Vector getL(Point p) {
        return super.getL(p);
    }
}
