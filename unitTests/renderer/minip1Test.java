package renderer;

import static java.awt.Color.*;

import lighting.*;
import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.AmbientLight;
import primitives.*;
import scene.Scene;

/**
 * Test rendering a basic image
 */
public class minip1Test {
    /**
     * Scene of the tests
     */
    private final Scene scene = new Scene("minip1 scene");
    /**
     * Camera builder of the tests
     */
    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(new Point(200, 200, 200)).setDirection(new Vector(-1, -1, -1), new Vector(0, 1, -1))
            .setVpDistance(1000)
            .setVpSize(500, 500);

    /**
     * Produce a scene with basic 3D model and render it into a png image with a grid
     */
    @Test
    public void renderCastleScene() {
        scene.setBackground(new Color(135, 206, 235));  // Light blue sky
        scene.setAmbientLight(new AmbientLight(new Color(BLUE), 0.15));

        // Create walls
        Polygon wall1 = new Polygon(
                new Point(0, 0, 0),
                new Point(0, 50, 0),
                new Point(50, 50, 0),
                new Point(50, 0, 0)
        );
        wall1.setEmission(new Color(169, 169, 169));  // Gray color for walls

        Polygon wall2 = new Polygon(
                new Point(50, 0, 0),
                new Point(50, 50, 0),
                new Point(100, 50, 0),
                new Point(100, 0, 0)
        );
        wall2.setEmission(new Color(169, 169, 169));  // Gray color for walls

        // Add roofs to towers (pyramids)
        Polygon roof1 = new Polygon(
                new Point(15, 15, 60),
                new Point(35, 15, 60),
                new Point(25, 25, 80)
        );
        roof1.setEmission(new Color(178, 34, 34));  // Red color for roofs

        Polygon roof2 = new Polygon(
                new Point(65, 15, 60),
                new Point(85, 15, 60),
                new Point(75, 25, 80)
        );
        roof2.setEmission(new Color(178, 34, 34));  // Red color for roofs

        Polygon roof3 = new Polygon(
                new Point(15, 65, 60),
                new Point(35, 65, 60),
                new Point(25, 75, 80)
        );
        roof3.setEmission(new Color(178, 34, 34));  // Red color for roofs

        Polygon roof4 = new Polygon(
                new Point(65, 65, 60),
                new Point(85, 65, 60),
                new Point(75, 75, 80)
        );
        roof4.setEmission(new Color(178, 34, 34));  // Red color for roofs

        // Create a reflective plane for the lake
        Plane lake = new Plane(
                new Point(0, 0, -1),
                new Vector(0, 0, 1)
        );
        lake.setEmission(new Color(70, 130, 180))  // Blue color for the lake
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300).setKr(0.8)); // High reflectivity

        // Add geometries to the scene
        scene.geometries.add(wall1, wall2, roof1, roof2, roof3, roof4, lake);

        // Add a spot light to the scene
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(40, 40, 115), new Vector(-1, -1, -4))
                        .setKl(4E-4).setKq(2E-5)
        );

        camera
                .setImageWriter(new ImageWriter("minip1Test", 600, 600))
                .build()
                .renderImage();
        camera.build()
                .writeToImage();
    }
}



//package renderer;
//
//import static java.awt.Color.*;
//
//import lighting.*;
//import org.junit.jupiter.api.Test;
//
//import geometries.*;
//import lighting.AmbientLight;
//import primitives.*;
//import scene.Scene;
//
///**
// * Test rendering a basic image
// *
// * @author Dan
// */
//public class minip1Test {
//    /**
//     * Scene of the tests
//     */
//    private final Scene scene = new Scene("minip1 scene");
//    /**
//     * Camera builder of the tests
//     */
//    private final Camera.Builder camera = Camera.getBuilder()
//            .setRayTracer(new SimpleRayTracer(scene))
//            .setLocation(new Point(200, 200, 200)).setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
//            .setVpDistance(500)
//            .setVpSize(500, 500);
//
//    /**
//     * Produce a scene with basic 3D model and render it into a png image with a
//     * grid
//     */
//    @Test
//    public void renderCastleScene() {
//        scene.setBackground(new Color(135, 206, 235));  // Light blue sky
//        scene.setAmbientLight(new AmbientLight(new Color(BLUE), 0.15));
//
//        // Create walls
//        Polygon wall1 = new Polygon(
//                new Point(0, 0, 0),
//                new Point(0, 50, 0),
//                new Point(50, 50, 0),
//                new Point(50, 0, 0)
//        );
//        wall1.setEmission(new Color(169, 169, 169));  // Gray color for walls
//
//        Polygon wall2 = new Polygon(
//                new Point(50, 0, 0),
//                new Point(50, 50, 0),
//                new Point(100, 50, 0),
//                new Point(100, 0, 0)
//        );
//        wall2.setEmission(new Color(169, 169, 169));  // Gray color for walls
//
//        // Create towers (cylinders)
//        //Cylinder tower1 = new Cylinder(
//         //       60, new Ray(new Point(25, 25, 0), new Vector(0, 0, 1)), 10
//        //);
//        //tower1.setEmission(new Color(169, 169, 169));  // Gray color for towers
//
//        //Cylinder tower2 = new Cylinder(
//        //        60, new Ray(new Point(75, 25, 0), new Vector(0, 0, 1)), 10
//        //);
//        //tower2.setEmission(new Color(169, 169, 169));  // Gray color for towers
//
//        //Cylinder tower3 = new Cylinder(
//        //        60, new Ray(new Point(25, 75, 0), new Vector(0, 0, 1)), 10
//        //);
//        //tower3.setEmission(new Color(169, 169, 169));  // Gray color for towers
//
//        //Cylinder tower4 = new Cylinder(
//        //        60, new Ray(new Point(75, 75, 0), new Vector(0, 0, 1)), 10
//        //);
//        //tower4.setEmission(new Color(169, 169, 169));  // Gray color for towers
//
//        // Add roofs to towers (pyramids)
//        Polygon roof1 = new Polygon(
//                new Point(15, 15, 60),
//                new Point(35, 15, 60),
//                new Point(25, 25, 80)
//        );
//        roof1.setEmission(new Color(178, 34, 34));  // Red color for roofs
//
//        Polygon roof2 = new Polygon(
//                new Point(65, 15, 60),
//                new Point(85, 15, 60),
//                new Point(75, 25, 80)
//        );
//        roof2.setEmission(new Color(178, 34, 34));  // Red color for roofs
//
//        Polygon roof3 = new Polygon(
//                new Point(15, 65, 60),
//                new Point(35, 65, 60),
//                new Point(25, 75, 80)
//        );
//        roof3.setEmission(new Color(178, 34, 34));  // Red color for roofs
//
//        Polygon roof4 = new Polygon(
//                new Point(65, 65, 60),
//                new Point(85, 65, 60),
//                new Point(75, 75, 80)
//        );
//        roof4.setEmission(new Color(178, 34, 34));  // Red color for roofs
//
//        // Create a reflective plane for the lake
//        Plane lake = new Plane(
//                new Point(0, 0, -1),
//                new Vector(0, 0, 1)
//        );
//        lake.setEmission(new Color(70, 130, 180))  // Blue color for the lake
//                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300).setKr(0.8)); // High reflectivity
//
//        // Add geometries to the scene
//        scene.geometries.add(wall1, wall2, /*tower1, tower2, tower3, tower4,*/ roof1, roof2, roof3, roof4, lake);
//
//        // Add a spot light to the scene
//        scene.lights.add(
//                new SpotLight(new Color(700, 400, 400), new Point(40, 40, 115), new Vector(-1, -1, -4))
//                        .setKl(4E-4).setKq(2E-5)
//        );
//
//        // right
//        camera
//                .setImageWriter(new ImageWriter("minip1Test", 600, 600))
//                .build()
//                .renderImage();
//        camera.build()
//                .writeToImage();
//    }
//
//}
//