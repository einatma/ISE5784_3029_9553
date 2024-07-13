package renderer;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;
public class TurretTests {
    private final Scene scene = new Scene("Turret Test Scene");

    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(new Point(0, 0, 100))  // Closer to the turret
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))  // Downward view
            .setVpDistance(100)  // Closer viewpoint distance
            .setVpSize(500, 500);

    @Test
    public void renderTurretTest() {
        Turret turret = new Turret(
                new Point(0, 0, 0),
                new Vector(1, 0, 0),  // Width direction
                new Vector(0, 1, 0),  // Length direction
                50,  // Base width
                50,  // Base height
                50,  // Base length
                20,  // Base depth
                30,  // Roof height
                4    // Number of crenellations each side
        );

        for (Geometry part : turret.getTurretGeometries()) {
            scene.geometries.add(part);
        }

        scene.setAmbientLight(new AmbientLight(new Color(100, 120, 120), Double3.ONE))
                .setBackground(new Color(75, 127, 90));

        scene.lights.add(
                new SpotLight(new Color(2000, 1600, 1600), new Point(100, -200, 200), new Vector(-1, 1, -0.5))
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add(
                new SpotLight(new Color(1600, 1200, 1200), new Point(-100, 200, 200), new Vector(1, -1, -0.5))
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add(
                new PointLight(new Color(1000, 1000, 1000), new Point(-200, -200, 100))
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add(
                new PointLight(new Color(600, 600, 600), new Point(200, 200, 100))
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add(
                new SpotLight(new Color(2400, 2000, 2000), new Point(0, 0, 300), new Vector(0, 0, -1))
                        .setKl(4E-4).setKq(2E-5));  // Light source above the turret

        camera.setImageWriter(new ImageWriter("turret_render_test", 1000, 1000))
                .build()
                .renderImage();
        camera.build()
                .writeToImage();
    }
}