package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * Simple ray tracer class that extends RayTracerBase.
 * This class is responsible for tracing rays through the scene and determining the color at each point.
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
    /**
     * Traces a ray through the scene to determine the color at the intersection point.
     * If the ray does not intersect with any geometry, it returns the background color of the scene.
     * Otherwise, it finds the closest intersection point and calculates the color at that point.
     *
     * @param ray the ray to be traced through the scene
     * @return the color at the intersection point, or the background color if no intersection occurs
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = this.scene.geometries.findIntersections(ray);

        if (intersections == null || intersections.isEmpty()) {
            return this.scene.background;
        }

        Point closestPoint = ray.findClosestPoint(intersections);

        return calcColor(closestPoint);
    }
    /**
     * Calculates the color at a given point.
     *
     * @param point the point at which to calculate the color
     * @return the calculated color
     */
    private Color calcColor(Point point) {
        return this.scene.ambientLight.getIntensity();
    }
}
