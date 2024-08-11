package renderer;

import geometries.Geometry;
import org.junit.jupiter.api.Test;
import primitives.*;
import primitives.Point;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;
//hadar Cohen

/**
 * Test rendering the project image
 */
public class minip1Test2 {
    /**
     * Scene of the tests
     */
    private final Scene scene = new Scene("minip1test2 scene");
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
            .setVpSize(500, 500);

    /**
     * Produce a scene with 3D model and render it into a png image with a grid
     */
    @Test
    public void renderRoomScene() {
        // Setting the background
        scene.setBackground(new Color(135, 206, 235));  // Light blue sky
        // Creating the list that will contain all the geometries in the scene
        List<Geometry> allGeometries = new LinkedList<>();
        //sense building :
        //creating the walls
        double wallHeight = 200;
        double wallLength = 400;
        double wallDepth = 20;
        Vector directionRoomDepth = new Vector(1,0,0);
        Vector directionRoomWidth = new Vector(0,1,0);
        Vector directionRoomHeight = directionRoomWidth.crossProduct(directionRoomWidth).normalize();
        Color bricksColor = new Color(100, 100, 100);
        Color slotsColor = new Color(java.awt.Color.BLACK);
        Material bricksMaterial = new Material();
        BrickWall backWall = new BrickWall(Point.ZERO, wallDepth, wallHeight, wallLength, 10, 10, 10,directionRoomWidth,directionRoomDepth).setBricksEmission(bricksColor).setSlotsEmission(slotsColor).setBricksMaterial(bricksMaterial).setSlotsMaterial(bricksMaterial);
        allGeometries.addAll(backWall.getBrickWallWigs());
        BrickWall leftWall = new BrickWall(Point.ZERO.add(directionRoomWidth.scale(wallLength)), wallDepth, wallHeight, wallLength, 10, 10, 10,directionRoomDepth,directionRoomWidth).setBricksEmission(bricksColor).setSlotsEmission(slotsColor).setBricksMaterial(bricksMaterial).setSlotsMaterial(bricksMaterial);
        allGeometries.addAll(leftWall.getBrickWallWigs());
        BrickWall rightWall = new BrickWall(Point.ZERO, wallDepth, wallHeight, wallLength, 10, 10, 10,directionRoomDepth,directionRoomWidth).setBricksEmission(bricksColor).setSlotsEmission(slotsColor).setBricksMaterial(bricksMaterial).setSlotsMaterial(bricksMaterial);

    }
    }