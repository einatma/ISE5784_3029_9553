package lighting;

import primitives.Color;

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
