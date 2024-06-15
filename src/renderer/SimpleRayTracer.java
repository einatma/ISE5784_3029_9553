package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

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
        List<Point> intersections = this.scene.geometries.findIntersections(ray);

        if (intersections == null || intersections.isEmpty()) {
            return this.scene.background;
        }

        Point closestPoint = ray.findClosestPoint(intersections);

        return calcColor(closestPoint);
    }
    private Color calcColor(Point point) {
        return this.scene.ambientLight.getIntensity();
    }
}
