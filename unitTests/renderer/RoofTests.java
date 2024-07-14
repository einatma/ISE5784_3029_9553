package renderer;

import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import geometries.*;
import lighting.AmbientLight;
import primitives.*;
import scene.Scene;

import static java.awt.Color.blue;

class RoofTests {
    private final Scene scene = new Scene("Roof Test Scene");

    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(new Point(50, 50, 50))
            .setDirection(new Vector(-1, -1, -1), new Vector(0, 1, -1))  // Ensuring the up vector is perpendicular
            .setVpDistance(500)
            .setVpSize(500, 500);

    @Test
    public void renderRoofTest() {
        Roof roof = new Roof(new Point(0, 0, 0), 10, 5, 20, new Vector(1, 0, 0), new Vector(0, 1, 0));

        for (Geometry face : roof.getRoofGeometry()) {
            scene.geometries.add(face.setMaterial(new Material().setKd(0.2).setKs(0.4).setShininess(30)).setEmission(new Color(255, 0, 0)));
        }

        scene.setAmbientLight(new AmbientLight(new Color(100, 120, 120), Double3.ONE))
                .setBackground(new Color(75, 127, 90));
        scene.lights.add(
                new SpotLight(new Color(500, 400, 400), new Point(40, 40, 115), new Vector(-1, -1, -4))
                        .setKl(4E-4).setKq(2E-5));

        camera.setImageWriter(new ImageWriter("roof_render_test", 1000, 1000))
                .build()
                .renderImage();
        camera.build()
                .writeToImage();
    }
}
