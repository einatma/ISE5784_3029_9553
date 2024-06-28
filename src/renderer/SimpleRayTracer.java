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

    private static final double DELTA = 0.1;

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

        // If there are no intersections, return the background color
        if (intersections == null || intersections.isEmpty()) {
            return this.scene.background;
        }

        // Find the closest intersection point
        GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);

        // Calculate the color at the closest intersection point
        return calcColor(closestPoint, ray);
    }

    /**
     * Calculates the color at a given point.
     *
     * @param intersection the point at which to calculate the color
     * @param ray the ray that intersects with the point
     * @return the calculated color
     */
    private Color calcColor(GeoPoint intersection, Ray ray) {
        // Start with the ambient light intensity as the base color
        Color result = this.scene.ambientLight.getIntensity();

        // Add local effects (diffuse and specular reflections) from all light sources
        result = result.add(calcLocalEffects(intersection, ray));

        // Return the final computed color at the intersection point
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
            if (nl * nv > 0 && unshaded(gp, lightSource, l, n, nl)) {
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
     * Determines if a point is unshaded by checking if any geometry obstructs the light.
     * This method creates a shadow ray from the point towards the light source and checks for intersections.
     * If an intersection is found closer than the light source, the point is considered shaded.
     *
     * @param gp the geometry point
     * @param light the light source
     * @param l the light direction vector
     * @param n the normal vector at the point
     * @param nl the dot product of the normal and light vectors
     * @return true if the point is unshaded, false otherwise
     */
    private boolean unshaded(GeoPoint gp, LightSource light, Vector l, Vector n, double nl) {
        // Calculate the direction vector from the point to the light source (opposite of light direction)
        Vector lightDirection = l.scale(-1);

        // Apply a small offset to the point in the direction of the normal to avoid self-intersection
        Vector epsVector = n.scale(nl < 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);

        // Create a ray from the point towards the light source
        Ray lightRay = new Ray(point, lightDirection);

        // Find intersections of this ray with the geometries in the scene
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        // If no intersections are found, the point is unshaded
        if (intersections == null) return true;

        // Get the distance from the point to the light source
        double lightDistance = light.getDistance(gp.point);

        // Check each intersection point
        for (GeoPoint geo : intersections) {
            // If any intersection point is closer than the light source, the point is shaded
            if (alignZero(geo.point.distance(gp.point) - lightDistance) <= 0)
                return false;
        }

        // If no intersection points are closer than the light source, the point is unshaded
        return true;
    }
}
