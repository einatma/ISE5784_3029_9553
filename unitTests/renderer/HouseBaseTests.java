package renderer;
import static java.awt.Color.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import geometries.*;
import primitives.*;
import scene.Scene;

class HouseBaseTests {
    /**
     * Scene of the tests
     */
    private final Scene scene = new Scene("Box Test Scene");

    /**
     * Camera builder of the tests
     */
    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(new Point(-20, -20, 10))
            .setDirection(new Vector(1, 1, 0), new Vector(0,0,1))  // Ensuring the up vector is perpendicular
            .setVpDistance(500)
            .setVpSize(500, 500);

    /**
     * Produce a scene with a Box and render it into a png image with a grid
     */
    @Test
    public void renderBoxTest() {
        // Create a Box (rectangular prism)
        HouseBase houseBase = new HouseBase(new Point(0, 0, 0), 30, 20, 20,10,7, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setWallMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(90)).setWallEmission(new Color(blue)).setWindowEmission(new Color(100, 100, 255)).setWindowMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(30).setKt(0.8));
        // Add Box polygons to the scene
        int i = 2;
        for (Geometry face : houseBase.getHouseBaseGeometries()) {
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
        camera.setImageWriter(new ImageWriter("houseBase_render_test", 1000, 1000))
                .build()
                .renderImage();
        camera.build()
                .writeToImage();
    }
}
