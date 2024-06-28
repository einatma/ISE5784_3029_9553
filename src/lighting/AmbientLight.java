package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Represents ambient light in a scene. It is a general light source that affects all objects equally.
 *
 * @author Hadar Cohen and Einat Mazuz
 */
public class AmbientLight extends Light {
    /**
     * A constant representing no ambient light.
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

    /**
     * Constructs an AmbientLight object with the given color and intensity coefficient.
     *
     * @param Ia the color of the ambient light.
     * @param Ka the intensity coefficient as a Double3 object.
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        super(Ia.scale(Ka));
    }

    /**
     * Constructs an AmbientLight object with the given color and intensity coefficient.
     *
     * @param Ia the color of the ambient light.
     * @param Ka the intensity coefficient as a double.
     */
    public AmbientLight(Color Ia, double Ka) {
        super(Ia.scale(Ka));
    }

}
