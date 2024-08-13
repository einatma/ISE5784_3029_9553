package renderer;

import java.util.List;

import static geometries.Intersectable.GeoPoint;
import static primitives.Util.*;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

/**
 * The SimpleRayTracer class is a simple implementation of the RayTracerBase
 * class.
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * Small offset to avoid self-intersection.
     */
    private static final double DELTA = 0.1;
    /**
     * Maximum level of recursion for calculating the color of a point.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /**
     * Minimum value for the calculation of the color coefficient.
     */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * Initial value for the calculation of the color coefficient.
     */
    private static final Double3 INITIAL_K = Double3.ONE;


    /**
     * Constructs a SimpleRayTracer with the specified scene.
     *
     * @param scene the scene to be traced.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a ray through the scene and calculates the color at the point where
     * the ray intersects with an object.
     *
     * @param ray the ray to trace through the scene
     * @return the color at the point where the ray intersects with an object, or
     * the background color if no intersection is found
     */
    @Override
    public Color traceRay(Ray ray) {
        var intersection = findClosestIntersection(ray);
        return intersection == null ? this.scene.background : calcColor(intersection, ray);
    }

    /**
     * Calculates the color at an intersection point considering global and local
     * lighting effects.
     *
     * @param gp  the intersection point
     * @param ray the ray that intersects the point
     * @return the color at the intersection point
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return scene.ambientLight.getIntensity().add(calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }

    /**
     * Recursive method to calculate the color at an intersection point, including
     * global effects like reflection and refraction.
     *
     * @param geoPoint the intersection point
     * @param ray      the ray that intersects the point
     * @param level    current recursion level
     * @param k        coefficient for color calculation
     * @return the color at the intersection point considering global effects
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(geoPoint, ray, k);
        return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }

    /**
     * Calculates the global effects of reflection and refraction.
     *
     * @param gp    the intersection point
     * @param ray   the ray from the camera
     * @param level the current recursion level
     * @param k     the accumulated attenuation factor
     * @return the color including global effects
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();
        return calcGlobalEffect(constructReflectedRay(gp, v, n), material.kR, level, k)
                .add(calcGlobalEffect(constructRefractedRay(gp, v, n), material.kT, level, k));

    }

    /**
     * Calculates the global effect (reflection or refraction) for a given ray.
     *
     * @param ray   the ray to calculate the effect for
     * @param kx    the attenuation coefficient
     * @param level the current recursion level
     * @param k     the accumulated attenuation factor
     * @return the color including the global effect
     */

    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        return gp == null ? scene.background : calcColor(gp, ray, level - 1, kkx).scale(kx);
    }

    /**
     * Calculates the local effects (diffuse and specular) at a given point.
     *
     * @param k   Coefficients for transparency calculations.
     * @param gp  the geometry point at which to calculate the local effects
     * @param ray the ray that intersects with the point
     * @return the color including local effects
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
        Vector n = gp.geometry.getNormal(gp.point);
        Vector v = ray.getDirection();
        Color color = gp.geometry.getEmission();
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return color;
        Material material = gp.geometry.getMaterial();
        for (LightSource lightSource : this.scene.lights) {
            Vector light = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(light));
            // Check if the light is in the same direction as the view (both positive or
            // both negative)
            // and if the point is unshaded
            if (nl * nv > 0) {
                Double3 ktr = transparency(gp, lightSource, light, n, nv);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    // Get the intensity of the light at the point
                    Color iL = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color
                            .add(iL.scale(calcDiffusive(material, nl).add(calcSpecular(material, n, light, nl, v))));
                }
            }
        }
        return color;
    }

    /**
     * Calculates the transparency of an intersection point in relation to a light
     * source.
     *
     * @param nv       dot product between the direction vector to the normal (n).
     * @param geopoint the intersection point
     * @param ls       the light source
     * @param l        the direction vector to the light source
     * @param n        the normal vector at the intersection point
     * @return the transparency coefficient for the point and light source
     */
    private Double3 transparency(GeoPoint geopoint, LightSource ls, Vector l, Vector n, double nv) {
        Double3 result = Double3.ONE;
        Vector lightDirection = l.scale(-1);
        Ray lightRay = new Ray(geopoint.point, lightDirection, n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
        if (intersections == null)
            return result; // no intersections

        double lightDistance = ls.getDistance(geopoint.point);
        for (GeoPoint gp : intersections) {
            if (alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0) {
                result = result.product(gp.geometry.getMaterial().kT); // the more transparency the less shadow
                if (result.lowerThan(MIN_CALC_COLOR_K))
                    return Double3.ZERO;
            }
        }
        return result;
    }

    /**
     * Constructs the reflected ray from a given point.
     *
     * @param gp the point at which the ray is reflected
     * @param v  the view direction
     * @param n  the normal at the point
     * @return the reflected ray
     */
    private Ray constructReflectedRay(GeoPoint gp, Vector v, Vector n) {
        Vector r = v.subtract(n.scale(2 * v.dotProduct(n)));
        return new Ray(gp.point, r, n);
    }

    /**
     * Constructs the refracted ray from a given point.
     *
     * @param gp the point at which the ray is refracted
     * @param v  the view direction
     * @param n  the normal at the point
     * @return the refracted ray
     */
    private Ray constructRefractedRay(GeoPoint gp, Vector v, Vector n) {
        return new Ray(gp.point, v, n);
    }

    /**
     * Calculates the diffuse reflection component.
     *
     * @param material the material properties
     * @param nl       the dot product of the normal and light vectors
     * @return the diffuse reflection component
     */
    private Double3 calcDiffusive(Material material, double nl) {
        return material.kD.scale(nl < 0 ? -nl : nl);
    }

    /**
     * Calculates the specular reflection component.
     *
     * @param material the material properties
     * @param n        the normal vector at the point
     * @param l        the light direction vector
     * @param nl       the dot product of the normal and light vectors
     * @param v        the view direction vector
     * @return the specular reflection component
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        // Calculate the reflection vector using the reflection formula:
        // R = L - 2 * (L · N) * N
        Vector r = l.subtract(n.scale(2 * nl)).normalize();
        double vr = alignZero(-v.dotProduct(r));
        return vr <= 0 ? Double3.ZERO : material.kS.scale(Math.pow(vr, material.nShininess));
    }

    /**
     * the function checks if this place is not shaded
     *
     * @param light    the light source
     * @param geopoint the point we are checking
     * @param l        the vector to the point (from the camera)
     * @param n        the normal
     * @return if this place is not shaded
     */
    @SuppressWarnings("unused")
    @Deprecated(forRemoval = true)
    private boolean unshaded(LightSource light, GeoPoint geopoint, Vector l, Vector n) {
        // Calculate the direction vector from the point to the light source (opposite of light direction)
        Vector lightDirection = l.scale(-1);

        // Apply a small offset to the point in the direction of the normal to avoid self-intersection
        Vector epsVector = n.scale(n.dotProduct(l) < 0 ? DELTA : -DELTA);
        Point point = geopoint.point.add(epsVector);
        double lightDistance = light.getDistance(geopoint.point);
        // Create a ray from the point towards the light source
        Ray lightRay = new Ray(point, lightDirection);

        // Find intersections of this ray with the geometries in the scene
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        // If no intersections are found, the point is unshaded
        if (intersections == null) {
            return true; //no intersections
        }

        for (GeoPoint gp : intersections) {
            if (alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * find the closest intersection to the starting point of the ray
     *
     * @param ray the ray that intersect with the geometries of the scene
     * @return the geoPoint that is point is the closest point to the starting point
     * of the ray
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = this.scene.geometries.findGeoIntersections(ray);
        return intersections == null ? null : ray.findClosestGeoPoint(intersections);
    }

    @Override
    public Color traceRay(List<Ray> rays) {
        if (rays == null)
            return scene.background;
        Color color = scene.background;
        for (Ray ray : rays) {
            color = color.add(traceRay(ray));
        }
        color = color.add(scene.ambientLight.getIntensity());
        int size = rays.size();
        return color.reduce(size);
    }


    @Override
    public Color computeFinalColor(List<Ray> rays) {
        Color finalColor = Color.BLACK;
        for (Ray ray : rays) {
            finalColor = finalColor.add(traceRay(ray));
        }
        return finalColor.reduce(rays.size());
    }

}