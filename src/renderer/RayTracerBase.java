package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

/**
 * Abstract class for ray tracing.
 * Contains the scene to be traced.
 */
public abstract class RayTracerBase {
    protected Scene scene;

    /**
     * Constructor for RayTracerBase.
     *
     * @param scene the scene to be traced
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Abstract method to trace a ray and return the color.
     *
     * @param ray the ray to trace
     * @return the color resulting from tracing the ray
     */
    public abstract Color traceRay(Ray ray);
}
