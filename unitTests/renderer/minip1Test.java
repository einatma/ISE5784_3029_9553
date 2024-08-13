package renderer;

import static java.awt.Color.*;

import lighting.*;
import org.junit.jupiter.api.Test;
import geometries.*;
import lighting.AmbientLight;
import primitives.*;
import primitives.Color;
import primitives.Point;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;
//hadar Cohen

/**
 * Test rendering the project image
 */
public class minip1Test {
    /**
     * Scene of the tests
     */
    private final Scene scene = new Scene("minip1 scene");
    // another camera positions
//    private final Camera.Builder camera = Camera.getBuilder()
//            .setRayTracer(new SimpleRayTracer(scene))
//            .setLocation(new Point(0, -150, 150)).setDirection(new Vector(0, 1, -1), new Vector(-1, 0, 0))
//            .setVpDistance(100)
//            .setVpSize(500, 500);
//    private final Camera.Builder camera = Camera.getBuilder()
//            .setRayTracer(new SimpleRayTracer(scene))
//            .setLocation(new Point(10, -170, 50))
//            .setDirection(new Vector(0, 1, 0), new Vector(0, 0, 1))
//            .setVpDistance(100)
//            .setVpSize(500, 500);
    /**
     * Camera builder of the tests
     */
    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(new Point(-100, -155, 70))
            .setDirection(new Vector(1, 1.5, 0).normalize(), new Vector(0, 0, 1))
            .setVpDistance(100)
            .setVpSize(500, 500)
            .setDoFActive(false)
            .setFocalSize(10,100,20)
            .setMultiThreading(3)
            .setDebugPrint(0.1);

    /**
     * Produce a scene with 3D model and render it into a png image with a grid
     */
    @Test
    public void renderCastleScene() {
        // Setting the background
        scene.setBackground(new Color(135, 206, 235));  // Light blue sky
        // Creating the list that will contain all the geometries in the scene
        List<Geometry> allGeometries = new LinkedList<>();

        //sense building :

        //defining the material type of all structures in the scene
        Material buildingsMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(100);

        //the color of the walls and turrets
        Color wallColor = new Color(134, 116, 104);

        // Dimensions for turrets
        double turretLength = 15;
        double turretWidth = 15;
        double turretHeight = 45;
        double roofHeight = 1;


        // creating the turrets around the walls :

        // Creating the front-left turret
        Point frontLeftTurretPosition = new Point(-100, -100, 0);
        House frontLeftTurret = new House(frontLeftTurretPosition, turretLength, turretWidth, turretHeight, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(wallColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Creating the front-right turret
        Point frontRightTurretPosition = new Point(70, -100, 0);
        House frontRightTurret = new House(frontRightTurretPosition, turretLength, turretWidth, turretHeight, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(wallColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Creating the back-left turret
        Point backLeftTurretPosition = new Point(-100, 50, 0);
        House backLeftTurret = new House(backLeftTurretPosition, turretLength, turretWidth, turretHeight, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(wallColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Creating the back-right turret
        Point backRightTurretPosition = new Point(70, 50, 0);
        House backRightTurret = new House(backRightTurretPosition, turretLength, turretWidth, turretHeight, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(wallColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);


        // Creating walls between the turrets

        // Dimensions for the walls
        double wallHeight = 40;
        double wallWidth = 10;
        double wallLength = 155;

        //Front wall:
        //Left lower part of the front wall
        Point frontLeftWallPosition = frontLeftTurret.getBase().getFrontBottomRight();
        Box frontLeftWall = new Box(frontLeftWallPosition, (wallLength / 2 - 20), wallHeight, wallWidth, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(wallColor)
                .setMaterial(buildingsMaterial);
        allGeometries.addAll(frontLeftTurret.getHouseWigs());
        // Right lower part of the front wall
        Point frontRightWallPosition = frontLeftWallPosition.add(new Vector((wallLength / 2 + 20), 0, 0));
        Box frontRightWall = new Box(frontRightWallPosition, (wallLength / 2 - 20), wallHeight, wallWidth, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(wallColor)
                .setMaterial(buildingsMaterial);

        // Upper part of the front wall
        Point upperFrontWallPosition = frontLeftWallPosition.add(new Vector(0, 0, wallHeight - 10));
        Box upperFrontWall = new Box(upperFrontWallPosition, wallLength, 10, wallWidth, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(wallColor)
                .setMaterial(buildingsMaterial);


        // Back wall
        Point backWallPosition = backRightTurret.getBase().getBackBottomLeft();
        Box backWall = new Box(backWallPosition, 155, wallHeight, wallWidth, new Vector(-1, 0, 0), new Vector(0, -1, 0))
                .setEmission(wallColor)
                .setMaterial(buildingsMaterial);

        // Left wall
        Point leftWallPosition = frontLeftTurret.getBase().getBackBottomLeft();
        Box leftWall = new Box(leftWallPosition, wallWidth, wallHeight, 155, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(wallColor)
                .setMaterial(buildingsMaterial);

        // Right wall
        Point rightWallPosition = frontRightTurret.getBase().getFrontBottomLeft();
        Box rightWall = new Box(rightWallPosition, wallWidth, wallHeight, 155, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(wallColor)
                .setMaterial(buildingsMaterial);


        // Dimensions for the castle
        Color castleColor = new Color(155, 133, 103);
        Color roofColor = new Color(153, 39, 41);
        Color windowsColor = new Color(5, 50, 89);
        Material windowsMaterial = new Material().setKd(0.1).setKs(0.1).setShininess(0).setKt(0.8);
        //creating the castle
        Castle castle = new Castle(new Point(-50, -60, 0), 60, 100, 60, 15, 30, 10, 20, 70, 30, 5, 10, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setRoofsMaterial(buildingsMaterial)
                .setRoofsMaterial(buildingsMaterial)
                .setWindowsMaterial(windowsMaterial)
                .setWallsEmission(castleColor)
                .setRoofsEmission(roofColor)
                .setWindowsEmission(windowsColor);


        allGeometries.addAll(frontRightTurret.getHouseWigs());
        allGeometries.addAll(backLeftTurret.getHouseWigs());
        allGeometries.addAll(backRightTurret.getHouseWigs());
        allGeometries.addAll(castle.getCastleWigs());
        allGeometries.addAll(frontLeftWall.getCubeWigs());
        allGeometries.addAll(frontRightWall.getCubeWigs());
        allGeometries.addAll(upperFrontWall.getCubeWigs());
        allGeometries.addAll(backWall.getCubeWigs());
        allGeometries.addAll(leftWall.getCubeWigs());
        allGeometries.addAll(rightWall.getCubeWigs());


        // Adding additional planes and sphere
        allGeometries.add(new Plane(new Point(1, 0, -10), new Point(0, 1, -10), new Point(0, 0, -10))
                .setEmission(new Color(0, 39, 78))
                .setMaterial(new Material().setShininess(50).setKt(0.9).setKs(0.7).setKd(0.2).setKr(0.1)));
        allGeometries.add(new Plane(new Point(1, 0, -1500), new Point(0, 1, -1500), new Point(0, 0, -1500))
                .setEmission(new Color(101, 61, 0)));
        allGeometries.add(new Sphere(new Point(60, 60, -1000), 1007)
                .setEmission(new Color(86, 125, 50)));
        allGeometries.add(new Sphere(new Point(-3, 55, -1000), 1006)
                .setEmission(new Color(86, 125, 50)));


        //adding mountains in the background
        Color mountainsColor = new Color(84, 114, 120);
        int mountainLocationX = -800;
        for (int i = 0; mountainLocationX < 6000; mountainLocationX += i * 800, i += 1) {
            allGeometries.add(new Sphere(new Point(mountainLocationX, 1900, -500 - 10 * i), 1000)
                    .setEmission(mountainsColor));
        }
        //adding boats
        Color boatColor = new Color(81, 61, 34);
        Color sailColor = new Color(WHITE);
        Material boatMaterial = new Material().setKd(0.1).setKs(0.1).setShininess(0);
        Material sailMaterial = new Material().setKd(0.1).setKs(0.1).setShininess(100);
        Sailboat sailboat1 = new Sailboat(new Point(70, -130, -5), 7, 10, 30, 1, 40, 30, 30, new Vector(-1, -1, 0), new Vector(1, 1, 0).crossProduct(new Vector(0, 0, 1)))
                .setBoatEmission(boatColor).setSailEmission(sailColor).setBoatMaterial(boatMaterial).setSailMaterial(sailMaterial);
        for (Geometry face : sailboat1.getSailWigs()) {
            scene.geometries.add(face);
        }
        Sailboat sailboat2 = new Sailboat(new Point(100, -110, -5), 7, 10, 30, 1, 40, 30, 30, new Vector(-1, -1, 0), new Vector(1, 1, 0).crossProduct(new Vector(0, 0, 1)))
                .setBoatEmission(boatColor).setSailEmission(sailColor).setBoatMaterial(boatMaterial).setSailMaterial(sailMaterial);
        for (Geometry face : sailboat2.getSailWigs()) {
            scene.geometries.add(face);
        }
        double shipLocationX = 400;
        for (int i = 0; i < 9; i++) {
            Sailboat sailboat = new Sailboat(new Point(shipLocationX, Math.random() * 2800 - 1000, 0), 20, 30, 40, 1, 70, 40, 50, new Vector(-1, -1, 0), new Vector(1, 1, 0).crossProduct(new Vector(0, 0, 1)))
                    .setBoatEmission(boatColor).setSailEmission(sailColor).setBoatMaterial(boatMaterial).setSailMaterial(sailMaterial);
            for (Geometry face : sailboat.getSailWigs()) {
                scene.geometries.add(face);
            }
            shipLocationX *= 1.3;
        }

        // Setting ambient light and adding a directional light to simulate the sun
        scene.setAmbientLight(new AmbientLight(new Color(77, 83, 114), 0.001));
        scene.lights.add(new DirectionalLight(new Color(103, 90, 90), new Vector(0, 1, 0)));
        scene.lights.add(new PointLight(new Color(110, 110, 0),new Point(-20, -50, 35)).setKc(0.0002).setKl(0.002).setKq(0.0002));
        scene.lights.add(new SpotLight(new Color(255, 255, 77),new Point(-110, -80, 10), new Vector(0.0001,1,0.01)).setKc(0.1).setKl(0.1).setKq(0.1));
        scene.lights.add(new PointLight(new Color(255, 0  , 0), new Point(70000, -3000, 1500)).setKc(0.000000001).setKl(0.00000000001).setKq(0.00000001));

        //Add spheres for Eliezer
        scene.geometries.add(new Sphere(new Point(-120, -30, 5), 10)
                .setEmission(new Color(188,190,82)).setMaterial(new Material().setShininess(50).setKt(0.1).setKs(0.1).setKd(0.2).setKr(0.1)));
        scene.geometries.add(new Sphere(new Point(-120, 10, 7), 10)
                .setEmission(new Color(64,190,84)).setMaterial(new Material().setKd(0.5).setKs(0.06).setKt(0.1).setKr(0.02).setShininess(0)));
        scene.geometries.add(new Sphere(new Point(-130, -50, 0), 9)
                .setEmission(new Color(59,116,190)).setMaterial(new Material().setKd(0.05).setKs(0).setKt(0.2).setKr(0.002).setShininess(0)));
        scene.geometries.add(new Sphere(new Point(-140, 40, 10), 10)
                .setEmission(new Color(190,87,97)).setMaterial(new Material().setKd(0).setKs(0).setKt(0.5).setKr(0.002).setShininess(0)));

        //Grass
        Material grassMaterial = new Material().setKd(0.5).setKs(0.3).setShininess(50).setKr(0.1);

        double grossWidth = 3;
        double grossHeight = 6;
//adding grass patches
        addGrassPatch(new Point(-60, -100, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(20, -115, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(-30, -90, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(60, -80, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(0, -120, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(50, -100, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(50, -110, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(-80, -85, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(80, -90, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(0, -85, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(60, -80, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(-40, -90, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(40, -100, -0.75), grossWidth, grossHeight, grassMaterial);
        addGrassPatch(new Point(-100, -90, -0.75), grossWidth, grossHeight, grassMaterial);

        // Adding all geometries to the scene
        for (Geometry geometry : allGeometries) {
            scene.geometries.add(geometry);
        }
        scene.geometries.makeBVH();
        // Rendering the image
        camera.setImageWriter(new ImageWriter("castleSenseBetterResolution", 2000, 2000))
                .build()
                .renderImage();

        // Write rendered image to file
        camera.build().writeToImage();
    }

    private void addGrassPatch(Point base, double width, double height, Material material) {
        Color grassColor = new Color(34, 139, 34); // Green color for grass
        Point basePoint1 = base.add(new Vector(-width / 2, 0, 0));
        Point basePoint2 = base.add(new Vector(width / 2, 0, 0));
        Point tipPoint1 = base.add(new Vector(0, 0, height));
        Point tipPoint2 = base.add(new Vector(0, -width / 2, height));

        Geometry grass1a = new Triangle(basePoint1, basePoint2, tipPoint1)
                .setEmission(grassColor)
                .setMaterial(material);
        Geometry grass1b = new Triangle(basePoint1, basePoint2, tipPoint2)
                .setEmission(grassColor)
                .setMaterial(material);

        scene.geometries.add(grass1a);
        scene.geometries.add(grass1b);
    }
}