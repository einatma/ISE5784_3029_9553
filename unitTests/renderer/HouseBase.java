
package renderer;

import geometries.*;
import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

public class HouseBase {
    private final Point frontBottomLeft;
    private final Point frontBottomRight;
    private final Point backBottomLeft;
    private final Point backBottomRight;
    private final Point frontTopLeft;
    private final Point frontTopRight;
    private final Point backTopLeft;
    private final Point backTopRight;
    private Color windowEmission = new Color(100, 100, 255);  // Example window color
    private Material windowMaterial = new Material().setKd(0.1).setKs(0.1).setShininess(30);  // Example window material
    private Color emission = new Color(0, 0, 0);
    private Material material = new Material();
    private final List<Geometry> wallWigs;
    private final List<Geometry> windowsWigs;

    public HouseBase(Point frontBottomLeft, double width, double height, double depth, double windowsLength, double windowsHeight, Vector directionWidth, Vector directionDepth) {
        if (width <= 0 || height <= 0 || depth <= 0)
            throw new IllegalArgumentException("Width, height and depth must be positive");
        if (directionWidth == null || directionDepth == null || !isZero(directionWidth.dotProduct(directionDepth)))
            throw new IllegalArgumentException("Direction vectors must be orthogonal");
        if (windowsLength <= 0 || windowsHeight <= 0)
            throw new IllegalArgumentException("Windows and door dimensions must be positive");
//        if (windowsLength >= depth*2/3 || windowsLength >= width*2/3 || windowsHeight >= height*3/4)
//            throw new IllegalArgumentException("Windows dimensions must be smaller than the house dimensions");

        Vector directionHeight = directionWidth.crossProduct(directionDepth).normalize();
        Vector directionWidthNormalized = directionWidth.normalize();
        Vector directionDepthNormalized = directionDepth.normalize();
        wallWigs = new LinkedList<>();
        windowsWigs = new LinkedList<>();

        // Defining corners
        this.frontBottomLeft = frontBottomLeft;
        this.frontBottomRight = frontBottomLeft.add(directionWidthNormalized.scale(width));
        this.backBottomLeft = frontBottomLeft.add(directionDepthNormalized.scale(depth));
        this.backBottomRight = backBottomLeft.add(directionWidthNormalized.scale(width));
        this.frontTopLeft = frontBottomLeft.add(directionHeight.scale(height));
        this.frontTopRight = frontBottomRight.add(directionHeight.scale(height));
        this.backTopLeft = backBottomLeft.add(directionHeight.scale(height));
        this.backTopRight = backBottomRight.add(directionHeight.scale(height));

        // Add the walls with windows
        WallWithWindow frontWall = new WallWithWindow(frontBottomLeft, width, height, windowsHeight, windowsLength, directionWidthNormalized, directionHeight);

        WallWithWindow rightWall = new WallWithWindow(frontBottomRight, depth, height, windowsHeight, windowsLength, directionDepthNormalized, directionHeight);
        WallWithWindow backWall = new WallWithWindow(backBottomRight, width, height, windowsHeight, windowsLength, directionWidthNormalized.scale(-1), directionHeight);
        WallWithWindow leftWall = new WallWithWindow(backBottomLeft, depth, height, windowsHeight, windowsLength, directionDepthNormalized.scale(-1), directionHeight);
wallWigs.addAll(frontWall.wall);
        wallWigs.addAll(rightWall.wall);
        wallWigs.addAll(backWall.wall);
        wallWigs.addAll(leftWall.wall);
        windowsWigs.addAll(frontWall.windows);
        windowsWigs.addAll(rightWall.windows);
        windowsWigs.addAll(backWall.windows);
        windowsWigs.addAll(leftWall.windows);
        wallWigs.add(new Polygon(frontBottomLeft, frontBottomRight, backBottomRight, backBottomLeft));
        wallWigs.add(new Polygon(frontTopLeft, frontTopRight, backTopRight, backTopLeft));
    }

    public List<Geometry> getHouseBaseGeometries() {
        List<Geometry> houseBaseGeometries = new LinkedList<>();
        houseBaseGeometries.addAll(wallWigs);
        houseBaseGeometries.addAll(windowsWigs);
        return houseBaseGeometries;
    }

    public Point getFrontBottomLeft() {
        return frontBottomLeft;
    }

    public Point getFrontBottomRight() {
        return frontBottomRight;
    }

    public Point getBackBottomLeft() {
        return backBottomLeft;
    }

    public Point getBackBottomRight() {
        return backBottomRight;
    }

    public Point getFrontTopLeft() {
        return frontTopLeft;
    }

    public Point getFrontTopRight() {
        return frontTopRight;
    }

    public Point getBackTopLeft() {
        return backTopLeft;
    }

    public Point getBackTopRight() {
        return backTopRight;
    }

    public HouseBase setWallMaterial(Material material) {
        this.material = material;
        for (Geometry face : wallWigs) {
            face.setMaterial(material);
        }
        return this;
    }
    public HouseBase setWindowMaterial(Material material) {
        this.windowMaterial = material;
        for (Geometry face : windowsWigs) {
            face.setMaterial(material);
        }
        return this;
    }

    public HouseBase setWallEmission(Color color) {
        this.emission = color;
        for (Geometry face : wallWigs) {
            face.setEmission(color);
        }
        return this;
    }
    public HouseBase setWindowEmission(Color color) {
        this.windowEmission = color;
        for (Geometry face : windowsWigs) {
            face.setEmission(color);
        }
        return this;
    }

    private class WallWithWindow {
        private final List<Geometry> wall;
        private final List<Geometry> windows;

        public WallWithWindow(Point bottomLeft, double length, double height, double windowHeight, double windowLength, Vector directionLength, Vector directionHeight) {
            wall = new LinkedList<>();
            windows = new LinkedList<>();

            // Add wall parts
            Point bottomRight = bottomLeft.add(directionLength.scale(length));
            wall.add(new Polygon(bottomLeft, bottomRight, bottomRight.add(directionHeight.scale(height*3/4 - windowHeight)), bottomLeft.add(directionHeight.scale(height*3/4 - windowHeight))));
            Point topLeft = bottomLeft.add(directionHeight.scale(height));
            wall.add(new Polygon(topLeft, topLeft.add(directionLength.scale((length-windowLength)/2)), topLeft.add(directionLength.scale((length-windowLength)/2)).add(directionHeight.scale(-height/4-windowHeight)), topLeft.add(directionHeight.scale(-height/4-windowHeight))));
            Point topRight = bottomRight.add(directionHeight.scale(height));
            wall.add(new Polygon(topRight, topRight.add(directionLength.scale(-(length-windowLength)/2)), topRight.add(directionLength.scale(-(length-windowLength)/2)).add(directionHeight.scale(-height/4-windowHeight)), topRight.add(directionHeight.scale(-height/4-windowHeight))));
            Point upPartOfWall = topLeft.add(directionHeight.scale(-height/4)).add(directionLength.scale((length-windowLength)/2));
            wall.add(new Polygon(upPartOfWall, upPartOfWall.add(directionLength.scale(windowLength)), upPartOfWall.add(directionLength.scale(windowLength)).add(directionHeight.scale(height/4)), upPartOfWall.add(directionHeight.scale(height/4))));

            // Add the window
            Point leftWindowPosition = bottomLeft.add(directionHeight.scale(height*3/4-windowHeight)).add(directionLength.scale((length-windowLength)/2));
            windows.add(new Polygon(leftWindowPosition, leftWindowPosition.add(directionLength.scale(windowLength)), leftWindowPosition.add(directionLength.scale(windowLength)).add(directionHeight.scale(windowHeight)), leftWindowPosition.add(directionHeight.scale(windowHeight))));
        }
    }
}