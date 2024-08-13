package renderer;
import static java.awt.Color.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import geometries.*;
import primitives.*;
import scene.Scene;

class SailboatTests {
    /**
     * Scene of the tests
     */
    private final Scene scene = new Scene("Sailboat Test Scene");

    /**
     * Camera builder of the tests
     */
    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(new Point(-10, -70, 0))
            .setDirection(new Vector(1, 3, 0), new Vector(0,0,1))  // Ensuring the up vector is perpendicular
            .setVpDistance(500)
            .setVpSize(500, 500);

    /**
     * Produce a scene with a Box and render it into a png image with a grid
     */
    @Test
    public void renderSailboatTests() {
        // Create a Box (rectangular prism)
        Sailboat sailboat = new Sailboat(new Point(0, 0, 0), 5, 10, 25,1,20, 30,15, new Vector(0, -1, 0), new Vector(1, 0, 0))
                .setBoatEmission(new Color(57, 50, 0)).setSailEmission(new Color(WHITE)).setBoatMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(90)).setSailMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(30));
        // Add Box polygons to the scene
        int i = 2;
        for (Geometry face : sailboat.getSailWigs()) {
            i++;
            scene.geometries.add(face);
        }

        // Set ambient light and background color
        scene.setAmbientLight(new AmbientLight(new Color(110, 110, 110), Double3.ONE))
                .setBackground(new Color(34, 121, 154));
        scene.lights.add(
                new SpotLight(new Color(65, 255, 210), new Point(40, 40, 115), new Vector(-1, -1, -4))
                        .setKl(4E-4).setKq(2E-5));

        // Configure the camera and render the image
        camera.setImageWriter(new ImageWriter("sailboat_render_test", 1000, 1000))
                .build()
                .renderImage();
        camera.build()
                .writeToImage();
    }
}
