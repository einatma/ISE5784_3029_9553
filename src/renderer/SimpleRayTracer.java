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
        GeoPoint closestPoint = findClosestIntersection(ray);
if (closestPoint==null)
    return this.scene.background;
        return calcColor(closestPoint, ray);
    }

    /**
     * Calculates the color at an intersection point considering global and local lighting effects.
     *
     * @param point the intersection point
     * @param ray the ray that intersects the point
     * @return the color at the intersection point
     */
    private Color calcColor(GeoPoint point, Ray ray) {
        return calcColor(point, ray, MAX_CALC_COLOR_LEVEL, Double3.ONE)
                .add(this.scene.ambientLight.getIntensity());
    }
    /**
     * Recursive method to calculate the color at an intersection point, including global effects like reflection and refraction.
     *
     * @param geoPoint the intersection point
     * @param ray the ray that intersects the point
     * @param level current recursion level
     * @param k coefficient for color calculation
     * @return the color at the intersection point considering global effects
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = geoPoint.geometry.getEmission()
                .add(calcLocalEffects(geoPoint, ray, k));

        return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }
    /**
     * Calculates global lighting effects like reflection and refraction.
     *
     * @param gp the intersection point
     * @param ray the ray that intersects the point
     * @param level current recursion level
     * @param k coefficient for color calculation
     * @return the color considering global lighting effects
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;
        Material material = gp.geometry.getMaterial();
        Double3 kr = material.kR;
        Double3 kkr = k.product(kr);

        Vector n = gp.geometry.getNormal(gp.point);

        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) {
            Ray reflectedRay = constructReflected(gp, ray);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);

            if (reflectedPoint == null){
                return color.add(this.scene.background);
            }
            color = color.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
        }

        Double3 kt = material.kT;
        Double3 kkt = k.product(kt);

        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) {
            Ray refractedRay = constructRefracted(gp, ray);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);

            if (refractedPoint == null) {
                return color.add(this.scene.background);
            }
            color = color.add(calcColor(refractedPoint, refractedRay, level - 1, kkt).scale(kt));
        }
        return color;
    }

    private Ray constructReflected(GeoPoint gp, Ray ray) {
        Vector v = ray.getDirection();

        Vector n = gp.geometry.getNormal(gp.point);
        double nv = alignZero(v.dotProduct(n));

        // r = v - 2 * (v * n) * n
        Vector r = v.subtract(n.scale(2d * nv)).normalize();

        return new Ray(gp.point, r, n); //use the constructor with the normal for moving the head
    }
    /**
     * Constructs a refracted ray based on the intersection point and incident ray.
     *
     * @param gp the intersection point
     * @param ray the incident ray
     * @return the refracted ray
     */
    private Ray constructRefracted(GeoPoint gp, Ray ray) {
        return new Ray(gp.point, ray.getDirection(), gp.geometry.getNormal(gp.point));
    }
    /**
     * Calculates the local lighting effects (diffuse and specular) at an intersection point.
     *
     * @param gp the intersection point
     * @param ray the ray that intersects with the point
     * @param k coefficient for color calculation
     * @return the color with local lighting effects
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
        Vector v = ray.getDirection();

        Vector n = gp.geometry.getNormal(gp.point);
        double nv = alignZero(n.dotProduct(v));

        if (nv == 0) {
            return Color.BLACK;
        }

        Material material = gp.geometry.getMaterial();

        Color color = Color.BLACK;
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));

            if (nl * nv > 0) { // sign(nl) == sing(nv)
                Double3 ktr = transparency(gp, lightSource, l, n); //intensity of shadow
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color iL = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(iL.scale(calcDiffusive(material, nl)),
                            iL.scale(calcSpecular(material, n, l, n.dotProduct(l), v)));
                }
            }
        }
        return color;
    }
    /**
     * Calculates the transparency of an intersection point in relation to a light source.
     *
     * @param geopoint the intersection point
     * @param light the light source
     * @param l the direction vector to the light source
     * @param n the normal vector at the intersection point
     * @return the transparency coefficient for the point and light source
     */
    private Double3 transparency(GeoPoint geopoint, LightSource light, Vector l, Vector n) {
        Double3 result = Double3.ONE;
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
        if (intersections == null){
            return result; //no intersections
        }

        for (GeoPoint gp : intersections) {
            if (alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0) {
                result = result.product(gp.geometry.getMaterial().kT); //the more transparency the less shadow
                if (result.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
            }
        }
        return result;
    }

    /**
     * Calculates the local effects (diffuse and specular) at a given point.
     *
     * @param gp the geometry point at which to calculate the local effects
     * @param ray the ray that intersects with the point
     * @return the color including local effects
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        // Get the normal vector at the geometry point
        Vector n = gp.geometry.getNormal(gp.point);

        // Get the direction of the incoming ray
        Vector v = ray.getDirection();

        // Calculate the dot product between the normal and the view direction
        double nv = alignZero(n.dotProduct(v));

        // If the dot product is zero, the view direction is perpendicular to the normal,
        // so there is no local effect (return black color)
        if (nv == 0) return Color.BLACK;

        // Get the material properties of the geometry
        Material mat = gp.geometry.getMaterial();

        // Start with the emission color of the geometry
        Color color = gp.geometry.getEmission();

        // Iterate over each light source in the scene
        for (LightSource lightSource : this.scene.lights) {
            // Get the direction vector from the point to the light source
            Vector l = lightSource.getL(gp.point);

            // Calculate the dot product between the normal and the light direction
            double nl = alignZero(n.dotProduct(l));

            // Check if the light is in the same direction as the view (both positive or both negative)
            // and if the point is unshaded
            if (nl * nv > 0 && transparency(gp, lightSource, l, n).equals(Double3.ZERO)) {
                // Get the intensity of the light at the point
                Color iL = lightSource.getIntensity(gp.point);

                // Add the diffuse and specular components to the color
                color = color.add(
                        iL.scale(calcDiffusive(mat, nl)
                                .add(calcSpecular(mat, n, l, nl, v)))
                );
            }
        }

        // Return the calculated color with local effects
        return color;
    }

    /**
     * Calculates the specular reflection component.
     *
     * @param material the material properties
     * @param n the normal vector at the point
     * @param l the light direction vector
     * @param nl the dot product of the normal and light vectors
     * @param v the view direction vector
     * @return the specular reflection component
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        // Specular reflection coefficient of the material
        Double3 ks = material.kS;

        // Shininess level of the material
        int nShininess = material.nShininess;

        // Calculate the reflection vector using the reflection formula: R = L - 2 * (L · N) * N
        Vector r = l.subtract(n.scale(2 * nl)).normalize();

        // Calculate the dot product between the reflection vector and the view vector,
        // and align it to zero if the value is very small
        double vr = alignZero(-v.dotProduct(r));

        // If the dot product is less than or equal to zero, there is no specular reflection
        if (vr <= 0) return Double3.ZERO;

        // Return the specular reflection component, scaled by the shininess factor
        return ks.scale(Math.pow(vr, nShininess));
    }

    /**
     * Calculates the diffuse reflection component.
     *
     * @param material the material properties
     * @param nl the dot product of the normal and light vectors
     * @return the diffuse reflection component
     */
    private Double3 calcDiffusive(Material material, double nl) {
        // Diffuse reflection coefficient of the material
        Double3 kd = material.kD;

        // If the dot product between the normal and the light direction is negative,
        // take its absolute value to ensure a positive contribution to the diffuse reflection
        if (nl < 0) nl = -nl;

        // Return the diffuse reflection component, scaled by the dot product
        return kd.scale(nl);
    }

    /**
     * find the closest intersection to the starting point of the ray
     *
     * @param ray the ray that intersect with the geometries of the scene
     * @return the geoPoint that is point is the closest point to the starting point of the ray
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = this.scene.geometries.findGeoIntersections(ray);
        if (intersections == null) {
            return null;
        }
        return ray.findClosestGeoPoint(intersections);
    }
}
