package renderer;
import static java.awt.Color.*;

import lighting.DirectionalLight;
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
                .setLocation(new Point(100, 100, 100))
                .setDirection(new Vector(-1, -1, -1), new Vector(0, 1, -1))  // Ensuring the up vector is perpendicular
                .setVpDistance(500)
                .setVpSize(500, 500);

        /**
         * Produce a scene with a Box and render it into a png image with a grid
         */
        @Test
        public void renderBoxTest() {
            // Create a Box (rectangular prism)
            Box box = new Box(new Point(0, 0, 0), 10, 15, 20, new Vector(1, 0, 0), new Vector(0, 1, 0));

            // Print points for debugging
            System.out.println("Front Bottom Left: " + box.getFrontBottomLeft());
            System.out.println("Front Bottom Right: " + box.getFrontBottomRight());
            System.out.println("Back Bottom Left: " + box.getBackBottomLeft());
            System.out.println("Back Bottom Right: " + box.getBackBottomRight());
            System.out.println("Front Top Left: " + box.getFrontTopLeft());
            System.out.println("Front Top Right: " + box.getFrontTopRight());
            System.out.println("Back Top Left: " + box.getBackTopLeft());
            System.out.println("Back Top Right: " + box.getBackTopRight());

            // Add Box polygons to the scene
            int i = 2;
            for (Polygon face : box.getCubeWigs()) {
                i++;
                scene.geometries.add(face.setMaterial(new Material().setKd(0.8).setKs(0.2).setShininess(30)).setEmission(new Color(i, i * 2, i * i)));
            }

            // Set ambient light and background color
            scene.setAmbientLight(new AmbientLight(new Color(100, 120, 120), Double3.ONE))
                    .setBackground(new Color(75, 127, 90));

            // Add a directional light to illuminate the box
            scene.lights.add(new DirectionalLight(new Color(255, 255, 255), new Vector(-1, -1, -1)), new SpotLight(new Color(255, 255, 255), new Point(0, 0, 0), new Vector(-1, -1, -1), 0.1, 0.00001, 0.000005));

            // Configure the camera and render the image
            camera.setImageWriter(new ImageWriter("box_render_test", 1000, 1000))
                    .build()
                    .renderImage();
            camera.build()
                    .writeToImage();
        }
}
