package lighting;

import primitives.*;

/**
 * Represents a point light source in a 3D scene.
 *
 * @author Hadar Cohen and Einat Mazuz
 */
public class PointLight extends Light implements LightSource {
    // Position of the point light source
    protected Point position;
    // Attenuation coefficients
    private double kC = 1d;   // Constant attenuation
    private double kL = 0d;   // Linear attenuation
    private double kQ = 0d;   // Quadratic attenuation

    /**
     * Constructs a PointLight object with the specified intensity and position.
     *
     * @param I        the intensity/color of the light
     * @param position the position of the point light source
     */
    public PointLight(Color I, Point position) {
        super(I);
        this.position = position;
    }

    /**
     * Sets the constant attenuation coefficient.
     *
     * @param kC the constant attenuation coefficient to set
     * @return this PointLight instance for chaining method calls
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation coefficient.
     *
     * @param kL the linear attenuation coefficient to set
     * @return this PointLight instance for chaining method calls
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation coefficient.
     *
     * @param kQ the quadratic attenuation coefficient to set
     * @return this PointLight instance for chaining method calls
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Retrieves the intensity of the light at a given point, considering distance attenuation.
     *
     * @param p the point in space
     * @return the intensity/color of the light at the given point
     */
    public Color getIntensity(Point p) {
        double disSquared = position.distanceSquared(p);
        double dis = position.distance(p);
        return getIntensity().scale(1 / (kC + kL * dis + kQ * disSquared));
    }

    /**
     * Retrieves the direction vector from the light source to a given point.
     *
     * @param p the point in space
     * @return the direction vector from the light source to the given point
     */
    public Vector getL(Point p) {
        if (p.equals(position)) {
            return null;
        }
        return p.subtract(position).normalize();
    }

    /**
     * Retrieves the distance from the light source to a given point.
     *
     * @param p the point in space
     * @return the distance from the light source to the given point
     */
    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }


}
