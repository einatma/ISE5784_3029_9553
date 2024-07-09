package renderer;

import lighting.AmbientLight;
import lighting.PointLight;
import org.junit.jupiter.api.Test;
import geometries.*;
import lighting.SpotLight;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;

class HouseTests {
    private final Scene scene = new Scene("House Test Scene");

    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(new Point(50, 50, 50))
            .setDirection(new Vector(-1, -1, -1), new Vector(0, 1, -1))  // Ensuring the up vector is perpendicular
            .setVpDistance(500)
            .setVpSize(500, 500);

    @Test
    public void renderHouseTest() {
        // Create a house with a base and a roof
        House house = new House(
                new Point(0, 0, 0),
                30, 20, 15, 10,
                new Material().setKd(0.2).setKs(0.4).setShininess(30), // base material
                new Material().setKd(0.2).setKs(0.4).setShininess(30), // roof material
                new Color(blue), // base emission color
                new Color(red) // roof emission color
        );

        // Add house base and roof geometries to the scene
        for (Geometry part : house.getBase().getCubeWigs()) {
            scene.geometries.add(part);
        }
        for (Geometry part : house.getRoof().getRoofGeometry()) {
            scene.geometries.add(part);
        }

        // Set ambient light and background color
        scene.setAmbientLight(new AmbientLight(new Color(100, 120, 120), Double3.ONE))
                .setBackground(new Color(75, 127, 90));
        scene.lights.add(
                new SpotLight(new Color(500, 400, 400), new Point(40, 40, 115), new Vector(-1, -1, -4))
                        .setKl(4E-4).setKq(2E-5));

        // Configure the camera and render the image
        camera.setImageWriter(new ImageWriter("house_render_test", 1000, 1000))
                .build()
                .renderImage();
        camera.build()
                .writeToImage();
    }
}
