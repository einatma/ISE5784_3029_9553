package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Simple ray tracer class that extends RayTracerBase.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor for SimpleRayTracer.
     *
     * @param scene the scene to be traced
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        // Implementation will be added in the future
        return null;
    }
}
