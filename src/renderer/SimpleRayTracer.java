package renderer;

import lighting.*;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import java.util.List;

import static primitives.Util.alignZero;

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
        List<GeoPoint> intersections = this.scene.geometries.findGeoIntersections(ray);

        // todo comment
        if (intersections == null || intersections.isEmpty()) {
            return this.scene.background;
        }

        GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);

        return calcColor(closestPoint,ray);
    }
    /**
     * Calculates the color at a given point.
     *
     * @param intersection the point at which to calculate the color
     * @return the calculated color
     */
    private Color calcColor(GeoPoint intersection,Ray ray) {
        Color result = this.scene.ambientLight.getIntensity();
        result = result.add(calcLocalEffects(intersection, ray));
        return result;
    }

    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Vector n = gp.geometry.getNormal(gp.point);
        Vector v=ray.getDirection();
        double nv = alignZero(n.dotProduct(v));
        if(nv==0)return Color.BLACK;
        Material mat = gp.geometry.getMaterial();
        Color color=gp.geometry.getEmission();
        for (LightSource lightSource : this.scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {
                Color iL=lightSource.getIntensity(gp.point);
                color = color.add(
                        iL.scale(calcDiffusive(mat, nl)
                                .add(calcSpecular(mat, n, l, nl, v))));


            }
        }
        return color;

    }

    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        Double3 ks = material.kS;
        int nShininess = material.nShininess;

        Vector r = l.subtract(n.scale(2 * nl)).normalize();
        double vr = alignZero(-v.dotProduct(r));

        if (vr <= 0) return Double3.ZERO;

        return ks.scale(Math.pow(vr, nShininess));
    }
    private Double3 calcDiffusive(Material material, double nl) {
        Double3 kd = material.kD;
        if (nl < 0) nl = -nl;
        return kd.scale(nl);
    }
}
