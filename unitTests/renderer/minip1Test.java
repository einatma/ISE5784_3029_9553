package renderer;

import static java.awt.Color.*;

import lighting.*;
import org.junit.jupiter.api.Test;
import geometries.*;
import lighting.AmbientLight;
import primitives.*;
import scene.Scene;
import java.util.LinkedList;
import java.util.List;

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
            .setLocation(new Point(0 ,-150, 150)).setDirection(new Vector(0, 1, -1), new Vector(-1, 0, 0))
            .setVpDistance(100)
            .setVpSize(500, 500);
//    private final Camera.Builder camera = Camera.getBuilder()
//            .setRayTracer(new SimpleRayTracer(scene))
//            .setLocation(new Point(10, -170, 50))
//            .setDirection(new Vector(0, 1, 0), new Vector(0, 0, 1))
//            .setVpDistance(100)
//            .setVpSize(500, 500);

    /**
     * Produce a scene with a basic 3D model and render it into a png image with a grid
     */
    @Test
    public void renderCastleScene() {
        // Setting the background and ambient light
        scene.setBackground(new Color(135, 206, 235));  // Light blue sky
        scene.setAmbientLight(new AmbientLight(new Color(BLUE), 0.15));
        Material buildingsMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(100);
        // Dimensions and colors for turrets
        double turretLength = 15;
        double turretWidth = 15;
        double turretHeight = 45;
        double roofHeight = 1;
        Color turretColor = new Color(194, 193, 183);


        // Creating the front-left turret
        Point frontLeftTurretPosition = new Point(-100, -100, 0);
        House frontLeftTurret = new House(frontLeftTurretPosition, turretLength, turretWidth, turretHeight, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(turretColor)
                .setRoofEmission(turretColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Creating the front-right turret
        Point frontRightTurretPosition = new Point(70, -100, 0);
        House frontRightTurret = new House(frontRightTurretPosition, turretLength, turretWidth, turretHeight, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(turretColor)
                .setRoofEmission(turretColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Creating the back-left turret
        Point backLeftTurretPosition = new Point(-100, 70, 0);
        House backLeftTurret = new House(backLeftTurretPosition, turretLength, turretWidth, turretHeight, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(turretColor)
                .setRoofEmission(turretColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Creating the back-right turret
        Point backRightTurretPosition = new Point(70, 70, 0);
        House backRightTurret = new House(backRightTurretPosition, turretLength, turretWidth, turretHeight, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(turretColor)
                .setRoofEmission(turretColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Creating walls between the turrets
        double wallHeight = 40;
        double wallWidth = 10;
        double wallLength = 155;
        Point frontLeftWallPosition = frontLeftTurret.getBase().getFrontBottomRight();
        Box frontLeftWall = new Box(frontLeftWallPosition, (wallLength / 2 - 20), wallHeight, wallWidth, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(turretColor)
                .setMaterial(buildingsMaterial);
        // Right lower part of the front wall
        Point frontRightWallPosition = frontLeftWallPosition.add(new Vector((wallLength / 2 + 20), 0, 0));
        Box frontRightWall = new Box(frontRightWallPosition, (wallLength / 2 - 20), wallHeight, wallWidth, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(turretColor)
                .setMaterial(buildingsMaterial);

        // Upper part of the front wall
        Point upperFrontWallPosition = frontLeftWallPosition.add(new Vector(0, 0, wallHeight-10));
        Box upperFrontWall = new Box(upperFrontWallPosition, wallLength , 10, wallWidth, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(turretColor)
                .setMaterial(buildingsMaterial);
        // Adding the doors at a 45-degree angle to the castle wall
        double doorWidth = 10;
        double doorHeight = 30;
        double doorDepth = 2;
        Color doorColor = new Color(73, 50, 17);

        // Door 1
        Point door1Position = frontLeftWall.getFrontBottomRight();
        Vector door1Direction = new Vector(1, 1, 0).normalize();
        Vector door1Normal = door1Direction.crossProduct(new Vector(0, 0, -1)).normalize();
        Box door1 = new Box(door1Position, doorWidth, doorHeight, doorDepth, door1Direction, door1Normal)
                .setEmission(doorColor)
                .setMaterial(buildingsMaterial);

        // Door 2
        Point door2Position = frontRightWall.getFrontBottomLeft();
        Vector door2Direction = new Vector(-1, 1, 0).normalize();
        Vector door2Normal = door2Direction.crossProduct(new Vector(0, 0, -1)).normalize();
        Box door2 = new Box(door2Position, doorWidth, doorHeight, doorDepth, door2Direction, door2Normal)
                .setEmission(doorColor)
                .setMaterial(buildingsMaterial);

        // Back wall
        Point backWallPosition = backRightTurret.getBase().getBackBottomLeft();
        Box backWall = new Box(backWallPosition, 155, wallHeight, wallWidth, new Vector(-1, 0, 0), new Vector(0, -1, 0))
                .setEmission(turretColor)
                .setMaterial(buildingsMaterial);

        // Left wall
        Point leftWallPosition = frontLeftTurret.getBase().getBackBottomLeft();
        Box leftWall = new Box(leftWallPosition, wallWidth, wallHeight, 155, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(turretColor)
                .setMaterial(buildingsMaterial);

        // Right wall
        Point rightWallPosition = frontRightTurret.getBase().getBackBottomRight();
        Box rightWall = new Box(rightWallPosition, wallWidth, wallHeight, 155, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(turretColor)
                .setMaterial(buildingsMaterial);

        Color wallColor = new Color(243, 236, 186);
        Color roofColor = new Color(139, 67, 69);


        // Creating the house in the center of the castle
        Point housePosition = new Point(-50, -90, 0);
        House house = new House(housePosition, 60, 100, 60, 15, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(roofColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);
        // Creating towers in front of the house
        Point towerFront1Position = house.getBase().getFrontBottomRight().add(new Vector(-10, -10, 0));
        House towerFront1 = new House(towerFront1Position, 10, 10, 70, 30, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(roofColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        Point towerFront2Position = house.getBase().getFrontBottomLeft().add(new Vector(0, -10, 0));
        House towerFront2 = new House(towerFront2Position, 10, 10, 70, 30, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(roofColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Creating the towers
        Point tower1Position = new Point(-10, -60, 0);
        House tower1 = new House(tower1Position, 20, 40, 120, 20, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(roofColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        Point tower2Position = new Point(35, 10, 0);
        House tower2 = new House(tower2Position, 20, 30, 150, 30, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(roofColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        Point tower3Position = new Point(-60, -50, 0);
        House tower3 = new House(tower3Position, 30, 30, 100, 20, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(roofColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Bridge between towers
        Point bridgePosition = tower1.getBase().getBackBottomRight().add(new Vector(-10, -10, 120));  // Adjust as needed for placement
        Vector bridgeDirection = tower2.getBase().getBackBottomLeft().subtract(tower1.getBase().getBackBottomLeft());
        House bridge = new House(bridgePosition, 50, 10, 10, 5, bridgeDirection, bridgeDirection.crossProduct(new Vector(0, 0, 1)))
                .setBaseEmission(wallColor)
                .setRoofEmission(wallColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);
        List<Geometry> allGeometries = new LinkedList<>();
        // List of all geometries to add to the scene
        allGeometries.addAll(frontLeftTurret.getHouseWigs());
        allGeometries.addAll(frontRightTurret.getHouseWigs());
        allGeometries.addAll(backLeftTurret.getHouseWigs());
        allGeometries.addAll(backRightTurret.getHouseWigs());
        allGeometries.addAll(house.getHouseWigs());
        allGeometries.addAll(tower1.getHouseWigs());
        allGeometries.addAll(tower2.getHouseWigs());
        allGeometries.addAll(tower3.getHouseWigs());
        allGeometries.addAll(bridge.getHouseWigs());
        allGeometries.addAll(frontLeftWall.getCubeWigs());
        allGeometries.addAll(frontRightWall.getCubeWigs());
        allGeometries.addAll(upperFrontWall.getCubeWigs());
        allGeometries.addAll(backWall.getCubeWigs());
        allGeometries.addAll(leftWall.getCubeWigs());
        allGeometries.addAll(rightWall.getCubeWigs());
        allGeometries.addAll(door1.getCubeWigs());
        allGeometries.addAll(door2.getCubeWigs());
        allGeometries.addAll(towerFront1.getHouseWigs());
        allGeometries.addAll(towerFront2.getHouseWigs());

        // Adding additional planes and sphere
        allGeometries.add(new Plane(new Point(1, 0, -10), new Point(0, 1, -10), new Point(0, 0, -10))
                        .setEmission(new Color(0, 39, 78))
                .setMaterial(new Material().setShininess(50).setKt(0.9).setKs(0.7).setKd(0.2).setKr(0.1)));
        allGeometries.add(new Plane(new Point(0, 0, -60), new Point(0, 1, -60), new Point(1, 1, -60))
                .setEmission(new Color(52, 66, 39)).setMaterial(new Material().setKd(0.5).setKs(0.8).setShininess(0)));
        allGeometries.add(new Sphere(new Point(5, 120, -878), 900)
                .setEmission(new Color(86, 125, 50)));
        allGeometries.add(new Sphere(new Point(-70, 130, -878), 900)
                .setEmission(new Color(86, 125, 50)));
allGeometries.add(new Cylinder(100, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 50)
                .setEmission(new Color(0, 0, 0))
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));
        // Adding all geometries to the scene
        for (Geometry geometry : allGeometries) {
            scene.geometries.add(geometry);
        }

        // Setting ambient light and adding a spotlight
        scene.setAmbientLight(new AmbientLight(new Color(50, 60, 60), Double3.ONE));
        scene.lights.add(new SpotLight(new Color(750, 650, 700), new Point(0, 0, 200), new Vector(0, 0, -1))
                .setKl(4E-4).setKq(2E-5));

        // Rendering the image
        camera.setImageWriter(new ImageWriter("minip1Test", 1000, 1000))
                .build()
                .renderImage();

        // Write rendered image to file
        camera.build().writeToImage();
    }
}
