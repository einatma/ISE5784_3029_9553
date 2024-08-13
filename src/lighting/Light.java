package lighting;

import primitives.Color;

/**
 * Abstract class representing a light source in a scene.
 *
 * @author Hadar Cohen and Einat Mazuz
 */
abstract class Light {
    protected Color intensity;

    /**
     * Constructor for Light.
     *
     * @param intensity the intensity of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for the intensity of the light.
     *
     * @return the intensity of the light
     */
    public Color getIntensity() {
        return this.intensity;
    }
}
