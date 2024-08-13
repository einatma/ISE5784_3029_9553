package renderer;

import geometries.Geometry;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

class BrickWallTests {
    private final Scene scene = new Scene("House Test Scene");

    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(new Point(-60, -60, 50))  // Moving the camera to show two walls
            .setDirection(new Vector(1, 1, 0), new Vector(0, 0, 1))  // Ensuring the up vector is perpendicular and direction to (1, 1, 0)
//            .setLocation(new Point(150, 150, 0))  // Moving the camera to show the back side
//            .setDirection(new Vector(-1, -1, 0), new Vector(0, 0, -1))
            .setVpDistance(300)
            .setVpSize(500, 500);

    @Test
    public void renderBrickWallTest() {
        // Create a house with a base and a roof
        BrickWall brickWall = new BrickWall(
                new Point(0, 0, 0),
                60, 60, 10, 1, 10, 5,
                new Vector(1, 0, 0),  // Width direction
                new Vector(0, 1, 0)   // Depth direction
        ).setBricksEmission(new Color(java.awt.Color.BLUE)).setSlotsEmission(new Color(java.awt.Color.RED));
        // Add house geometries to the scene
        for(Geometry part :brickWall.getBrickWallWigs())
            scene.geometries.add(part);

        // Set ambient light and background color
        scene.setAmbientLight(new AmbientLight(new Color(100, 120,120), Double3.ONE))
                .setBackground(new Color(75, 127,90));

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
        camera.setImageWriter(new ImageWriter("breakWall_render_test",1000,1000)).build().renderImage();
        camera.build().writeToImage();
    }

}
