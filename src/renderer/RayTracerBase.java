package renderer;

import primitives.Color;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Abstract class for ray tracing.
 * Contains the scene to be traced.
 */
public abstract class RayTracerBase {
    protected Scene scene;
    private static final int MAX_RECURSION_DEPTH = 5;
    private static final double VARIANCE_THRESHOLD = 0.01;
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
     * Traces a list of rays and returns the color at the intersection point.
     * @param rays the ray to be traced.
     * @return the color at the intersection point.
     */
    public abstract Color traceRay(List<Ray> rays);


    /**
     * Computes the final color by tracing a list of rays and averaging their
     * resulting colors.
     *
     * @param rays The list of rays to be traced.
     * @return The averaged color resulting from tracing all the rays in the list.
     */
    public abstract Color computeFinalColor(List<Ray> rays);


}
