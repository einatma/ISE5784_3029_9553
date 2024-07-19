package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

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

    /**
     * Computes the final color by averaging the colors obtained from tracing multiple rays.
     *
     * @return The averaged color obtained from tracing the rays.
     */
    public Color computeFinalColor(List<Ray> rays) {
        Color finalColor = Color.BLACK;
        for (Ray ray : rays) {
            finalColor = finalColor.add(traceRay(ray));
        }
        return finalColor.scale(1.0/(rays.size()));
    }
}
