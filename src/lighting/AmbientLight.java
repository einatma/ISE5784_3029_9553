package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

    private final Color intensity;

    /**
     * Constructs an AmbientLight object with the given color and intensity coefficient.
     *
     * @param Ia the color of the ambient light
     * @param Ka the intensity coefficient as a double
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        intensity = Ia.scale(Ka);
    }
    /**
     * Returns the intensity of the ambient light.
     *
     * @return the intensity of the ambient light
     */
    public AmbientLight(Color Ia, double Ka) {
        intensity = Ia.scale(Ka);
    }

    public Color getIntensity() {
        return intensity;
    }
}
