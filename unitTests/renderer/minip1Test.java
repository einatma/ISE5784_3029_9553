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

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
//hadar Cohen
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
//        private final Camera.Builder camera = Camera.getBuilder()
//            .setRayTracer(new SimpleRayTracer(scene))
//            .setLocation(new Point(0 ,-150, 150)).setDirection(new Vector(0, 1, -1), new Vector(-1, 0, 0))
//            .setVpDistance(100)
//            .setVpSize(500, 500);
    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(new Point(10, -170, 50))
            .setDirection(new Vector(0, 1, 0), new Vector(0, 0, 1))
            .setVpDistance(100)
            .setVpSize(500, 500);

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

        // הזזת הבניינים לאחור
        double moveBackDistance = 30;

        // Creating the front-left turret
        Point frontLeftTurretPosition = new Point(-100, -100 + moveBackDistance, 0);
        House frontLeftTurret = new House(frontLeftTurretPosition, turretLength, turretWidth, turretHeight, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(turretColor)
                .setRoofEmission(turretColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Creating the front-right turret
        Point frontRightTurretPosition = new Point(70, -100 + moveBackDistance, 0);
        House frontRightTurret = new House(frontRightTurretPosition, turretLength, turretWidth, turretHeight, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(turretColor)
                .setRoofEmission(turretColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Creating the back-left turret
        Point backLeftTurretPosition = new Point(-100, 70 + moveBackDistance, 0);
        House backLeftTurret = new House(backLeftTurretPosition, turretLength, turretWidth, turretHeight, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(turretColor)
                .setRoofEmission(turretColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        // Creating the back-right turret
        Point backRightTurretPosition = new Point(70, 70 + moveBackDistance, 0);
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
        Point upperFrontWallPosition = frontLeftWallPosition.add(new Vector(0, 0, wallHeight - 10));
        Box upperFrontWall = new Box(upperFrontWallPosition, wallLength, 10, wallWidth, new Vector(1, 0, 0), new Vector(0, 1, 0))
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
        Point housePosition = new Point(-50, -90 + moveBackDistance, 0);
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
        Point tower1Position = new Point(-10, -60 + moveBackDistance, 0);
        House tower1 = new House(tower1Position, 20, 40, 120, 20, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(roofColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        Point tower2Position = new Point(35, 10 + moveBackDistance, 0);
        House tower2 = new House(tower2Position, 20, 30, 150, 30, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setBaseEmission(wallColor)
                .setRoofEmission(roofColor)
                .setBaseMaterial(buildingsMaterial)
                .setRoofMaterial(buildingsMaterial);

        Point tower3Position = new Point(-60, -50 + moveBackDistance, 0);
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



        //sun

        // Setting ambient light and adding a spotlight
        Geometry sun  =  new Sphere(new Point(0, -50, 240), 30)
                .setEmission(new Color(255, 223, 0))  // צבע צהוב עז
                .setMaterial(new Material().setKt(0).setKd(0.9).setKs(0.1).setKr(0).setShininess(30));
        scene.geometries.add(sun);

        // Setting ambient light and adding a directional light to simulate the sun
        // הגדרת מקור אור חזק יותר לשמש
        scene.setAmbientLight(new AmbientLight(new Color(50, 60, 60), Double3.ONE));
        DirectionalLight sunLight = new DirectionalLight(new Color(WHITE).scale(1.5), new Vector(0, -1, -1));
        scene.lights.add(sunLight);

        //Grass
        // הוספת צל לדשא
        Material grassMaterial = new Material().setKd(0.5).setKs(0.3).setShininess(50).setKr(0.1); // הגדרת חומר הדשא עם החזרות חלקיות ליצירת צל

// הוספת גבעולי דשא בתלת-ממד באמצעות משולשים מצטלבים עם צל
        // הוספת גבעולי דשא בצורה מפוזרת על המשטח הירוק במקומות החדשים
        addGrassPatch(new Point(-60, -90, 1), 10, 20, grassMaterial);
        addGrassPatch(new Point(20, -100, 1), 10, 20, grassMaterial);
        addGrassPatch(new Point(-30, -60, 1), 8, 15, grassMaterial);
        addGrassPatch(new Point(60, -80, 1), 8, 15, grassMaterial);   // מיקום חדש כדי להרחיק מהמים
        addGrassPatch(new Point(0, 0, 1), 12, 25, grassMaterial);
        addGrassPatch(new Point(-50, 40, 1), 10, 20, grassMaterial);
        addGrassPatch(new Point(50, 40, 1), 10, 20, grassMaterial);
        addGrassPatch(new Point(-80, -90, 1), 12, 25, grassMaterial);
        addGrassPatch(new Point(80, -90, 1), 12, 25, grassMaterial);
        addGrassPatch(new Point(-60, -50, 1), 8, 15, grassMaterial);
        addGrassPatch(new Point(60, -50, 1), 8, 15, grassMaterial);
        addGrassPatch(new Point(-40, -30, 1), 10, 20, grassMaterial);
        addGrassPatch(new Point(40, -30, 1), 10, 20, grassMaterial);
        addGrassPatch(new Point(70, -100, 1), 10, 20, grassMaterial);

        // Parameters for the boat
        Material boatMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(30);
        Color boatColor = new Color(73, 50, 17);
        Color roofColorBoat = new Color(150, 75, 0);

        Point boatBase = new Point(100, -100, 0); // Adjusted base position
        double boatLength = 40; // Reduced length
        double boatWidth = 15; // Reduced width
        double boatHeight = 10; // Reduced height

// Lower part of the boat
        Box boatBottom = new Box(boatBase, boatLength, boatWidth, boatHeight / 2, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(boatColor)
                .setMaterial(boatMaterial);
        allGeometries.addAll(boatBottom.getCubeWigs());

// Upper part of the boat
        Box boatTop = new Box(boatBase.add(new Vector(0, 0, boatHeight / 2)), boatLength, boatWidth / 2, boatHeight / 2, new Vector(1, 0, 0), new Vector(0, 1, 0))
                .setEmission(boatColor)
                .setMaterial(boatMaterial);
        allGeometries.addAll(boatTop.getCubeWigs());

// Creating the arch for the front of the boat
        Point archFront = boatBase.add(new Vector(boatLength / 2, 0, boatHeight / 2));
        Point archBackLeft = boatBase.add(new Vector(-boatLength / 2, -boatWidth / 2, boatHeight / 2));
        Point archBackRight = boatBase.add(new Vector(-boatLength / 2, boatWidth / 2, boatHeight / 2));

        Geometry archLeft = new Triangle(archFront, archBackLeft, archBackRight)
                .setEmission(boatColor)
                .setMaterial(boatMaterial);
        scene.geometries.add(archLeft);

        Geometry archRight = new Triangle(archFront, archBackRight, archBackLeft)
                .setEmission(boatColor)
                .setMaterial(boatMaterial);
        scene.geometries.add(archRight);

// Creating the roof of the boat
        Point roofBase = boatBase.add(new Vector(0, 0, boatHeight));
        Point roofFront = roofBase.add(new Vector(boatLength / 2, 0, 0));
        Point roofBackLeft = roofBase.add(new Vector(-boatLength / 2, -boatWidth / 4, 0));
        Point roofBackRight = roofBase.add(new Vector(-boatLength / 2, boatWidth / 4, 0));

        Geometry roofLeft = new Triangle(roofFront, roofBackLeft, roofBackRight)
                .setEmission(roofColorBoat)
                .setMaterial(boatMaterial);
        scene.geometries.add(roofLeft);

        Geometry roofRight = new Triangle(roofFront, roofBackRight, roofBackLeft)
                .setEmission(roofColorBoat)
                .setMaterial(boatMaterial);
        scene.geometries.add(roofRight);
        // Adding all geometries to the scene
        for (Geometry geometry : allGeometries) {
            scene.geometries.add(geometry);
        }
        // Center point for the boat's hull

        // Rendering the image
        camera.setImageWriter(new ImageWriter("minip1Test", 1000, 1000))
                .build()
                .renderImage();

        // Write rendered image to file
        camera.build().writeToImage();
    }

    // פונקציה מעודכנת להוספת גבעולי דשא עם חומר מוגדר
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
