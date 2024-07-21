package renderer;
import static java.awt.Color.*;

import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import geometries.*;
import lighting.AmbientLight;
import primitives.*;
import scene.Scene;

class BoxTests {
        /**
         * Scene of the tests
         */
        private final Scene scene = new Scene("Box Test Scene");

        /**
         * Camera builder of the tests
         */
        private final Camera.Builder camera = Camera.getBuilder()
                .setRayTracer(new SimpleRayTracer(scene))
                .setLocation(new Point(30, 35, 40))
                .setDirection(new Vector(-1, -1, -1), new Vector(0, 1, -1))  // Ensuring the up vector is perpendicular
                .setVpDistance(500)
                .setVpSize(500, 500);

        /**
         * Produce a scene with a Box and render it into a png image with a grid
         */
        @Test
        public void renderBoxTest() {
            // Create a Box (rectangular prism)
            Box box = new Box(new Point(0, 0, 0), 10, 15, 20, new Vector(1, 0, 0), new Vector(0, 1, 0)).setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(90)).setEmission(new Color(blue));
//scene.geometries.add( new Cylinder(10, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 20).setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(90)).setEmission(new Color(blue)));
            // Add Box polygons to the scene
            int i = 2;
            for (Geometry face : box.getCubeWigs()) {
                i++;
                scene.geometries.add(face);
            }

            // Set ambient light and background color
            scene.setAmbientLight(new AmbientLight(new Color(100, 120, 120), Double3.ONE))
                    .setBackground(new Color(50, 100, 50));
            scene.lights.add(
                    new SpotLight(new Color(500, 400, 400), new Point(40, 40, 115), new Vector(-1, -1, -4))
                            .setKl(4E-4).setKq(2E-5));

            // Configure the camera and render the image
            camera.setImageWriter(new ImageWriter("box_render_test", 1000, 1000))
                    .build()
                    .renderImage();
            camera.build()
                    .writeToImage();
        }
}
