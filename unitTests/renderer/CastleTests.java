package renderer;

import geometries.Geometry;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.red;

class CastleTests {
    private final Scene scene = new Scene("House Test Scene");

    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(new Point(-100, -100, 80))  // Moving the camera to show two walls
            .setDirection(new Vector(1, 1, 0), new Vector(0, 0, 1))  // Ensuring the up vector is perpendicular and direction to (1, 1, 0)
            .setLocation(new Point(200, 200, 80))  // Moving the camera to show the back side
            .setDirection(new Vector(-1, -1, 0), new Vector(0, 0, 1))
            .setVpDistance(300)
            .setVpSize(500, 500);

    @Test
    public void renderCastleTest() {
    // Create a house with a base and a roof
    Castle castle = new Castle(
            new Point(0, 0, 0),
            60, 60, 50, 10, 20, 60, 20,
            new Vector(1, 0, 0),  // Width direction
            new Vector(0, 1, 0)   // Depth direction
    );

        castle.setBuilingsMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(30)) // base material
                .setRoofsMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(30)) // roof material
                .setBuilingsEmission(new Color(100,70,50)) // base emission color
                .setRoofsEmission(new Color(red)); // roof emission color

    // Add house geometries to the scene
        for(
    Geometry part :castle.getCastleWigs())

    {
        scene.geometries.add(part);
    }

    // Set ambient light and background color
        scene.setAmbientLight(new AmbientLight(new Color(100, 120,120), Double3.ONE))
            .

    setBackground(new Color(75, 127,90));

    // Add lights to the scene
        scene.lights.add(new SpotLight(new Color(1000, 800,800), new Point(100,-200,200), new Vector(-1,1,-0.5))
            .setKl(4E-4).setKq(2E-5));
        scene.lights.add(new SpotLight(new Color(800, 600,600), new Point(-100,200,200), new Vector(1,-1,-0.5))
                .setKl(4E-4).setKq(2E-5));
        scene.lights.add(new PointLight(new Color(500, 500,500), new Point(-200,-200,100))
            .setKl(4E-4).setKq(2E-5));
        scene.lights.add(new PointLight(new Color(300, 300,300), new Point(200,200,100))
            .setKl(4E-4).setKq(2E-5));

    // Configure the camera and render the image
        camera.setImageWriter(new ImageWriter("house_render_test",1000,1000)).build().renderImage();
        camera.build().writeToImage();
}

}