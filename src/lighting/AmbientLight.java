package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight extends Light {
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);



    /**
     * Constructs an AmbientLight object with the given color and intensity coefficient.
     *
     * @param Ia the color of the ambient light
     * @param Ka the intensity coefficient as a double
     */
    public AmbientLight(Color Ia, Double3 Ka) {
       super(Ia.scale(Ka));
    }

    public AmbientLight(Color Ia, double Ka) {
        super(Ia.scale(Ka));
    }

}
