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

    public HouseBase(Point frontBottomLeft, double width, double height, double depth, double windowsLength, double windowsHeight, double doorLength, double doorHeight, Vector directionWidth, Vector directionDepth) {
        if (width <= 0 || height <= 0 || depth <= 0)
            throw new IllegalArgumentException("Width, height and depth must be positive");
        if (directionWidth == null || directionDepth == null || !isZero(directionWidth.dotProduct(directionDepth)))
            throw new IllegalArgumentException("Direction vectors must be orthogonal");
        if (windowsLength <= 0 || windowsHeight <= 0 || doorLength <= 0 || doorHeight <= 0)
            throw new IllegalArgumentException("Windows and door dimensions must be positive");
        if (windowsLength >= depth*2/3 || windowsLength >= width*2/3 || doorLength >= width || doorHeight+windowsHeight >= height*3/4)
            throw new IllegalArgumentException("Windows and door dimensions must be smaller than the house dimensions");

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
        addWallWithWindows(frontBottomLeft, depth, height, windowsHeight, windowsLength, directionWidthNormalized, directionHeight);
        addWallWithWindows(frontBottomRight, depth, height, windowsHeight, windowsLength, directionWidthNormalized.scale(-1), directionHeight);
        addWallWithWindows(frontBottomLeft.add(directionDepthNormalized.scale(depth)), width, height, windowsHeight, windowsLength, directionDepthNormalized, directionHeight);
        addWallWithWindows(frontBottomLeft, width, height, windowsHeight, windowsLength, directionDepthNormalized.scale(-1), directionHeight);

        // Add bottom and top walls
        wallWigs.add(new Polygon(frontBottomLeft, frontBottomRight, backBottomRight, backBottomLeft));
        wallWigs.add(new Polygon(frontTopLeft, frontTopRight, backTopRight, backTopLeft));

        // Add front wall parts
        addFrontWallParts(frontBottomLeft, directionWidthNormalized, directionHeight, width, height, doorLength, doorHeight, windowsHeight, windowsLength, depth);
    }

    private void addWallWithWindows(Point start, double length, double height, double windowHeight, double windowLength, Vector directionLength, Vector directionHeight) {
        List<Geometry> wall = new LinkedList<>();
        List<Geometry> windows = new LinkedList<>();
        Point bottomRight = start.add(directionLength.scale(length));

        // Add wall parts
        wall.add(new Polygon(start, bottomRight, bottomRight.add(directionHeight.scale(height*3/4 - windowHeight)), start.add(directionHeight.scale(height*3/4 - windowHeight))));
        Point topLeft = start.add(directionHeight.scale(height));
        wall.add(new Polygon(topLeft, topLeft.add(directionLength.scale(length/6)), topLeft.add(directionLength.scale(length/6)).add(directionHeight.scale(-height/4-windowHeight)), topLeft.add(directionHeight.scale(-height/4-windowHeight))));
        Point topRight = bottomRight.add(directionHeight.scale(height));
        wall.add(new Polygon(topRight, topRight.add(directionLength.scale(-length/6)), topRight.add(directionLength.scale(-length/6)).add(directionHeight.scale(-height/4-windowHeight)), topRight.add(directionHeight.scale(-height/4-windowHeight))));
        Point upCenterPartOfWall = start.add(directionHeight.scale(height*3/4-windowHeight)).add(directionLength.scale(length/6+windowLength));
        wall.add(new Polygon(upCenterPartOfWall, upCenterPartOfWall.add(directionLength.scale(length*2/3-windowLength*2)), upCenterPartOfWall.add(directionLength.scale(length*2/3-windowLength*2)).add(directionHeight.scale(windowHeight)), upCenterPartOfWall.add(directionHeight.scale(windowHeight))));
        Point upPartOfWall = topLeft.add(directionHeight.scale(-height/4)).add(directionLength.scale(length/6));
        wall.add(new Polygon(upPartOfWall, upPartOfWall.add(directionLength.scale(length*2/3)), upPartOfWall.add(directionLength.scale(length*2/3)).add(directionHeight.scale(height/4)), upPartOfWall.add(directionHeight.scale(height/4))));

        // Add windows
        Point leftWindowPosition = start.add(directionHeight.scale(height*3/4-windowHeight)).add(directionLength.scale(length/6));
        windows.add(new Polygon(leftWindowPosition, leftWindowPosition.add(directionLength.scale(windowLength)), leftWindowPosition.add(directionLength.scale(windowLength)).add(directionHeight.scale(windowHeight)), leftWindowPosition.add(directionHeight.scale(windowHeight))));
        Point rightWindowPosition = start.add(directionHeight.scale(height*3/4-windowHeight)).add(directionLength.scale(length*5/6 - windowLength));
        windows.add(new Polygon(rightWindowPosition, rightWindowPosition.add(directionLength.scale(windowLength)), rightWindowPosition.add(directionLength.scale(windowLength)).add(directionHeight.scale(windowHeight)), rightWindowPosition.add(directionHeight.scale(windowHeight))));

        wallWigs.addAll(wall);
        windowsWigs.addAll(windows);
    }

    private void addFrontWallParts(Point frontBottomLeft, Vector directionWidthNormalized, Vector directionHeight, double width, double height, double doorLength, double doorHeight, double windowsHeight, double windowsLength, double depth) {
        // Add the front wall with door and windows
        wallWigs.add(new Polygon(frontBottomLeft, frontBottomLeft.add(directionWidthNormalized.scale((width-doorLength)/2)), frontBottomLeft.add(directionWidthNormalized.scale((width-doorLength)/2).add(directionHeight.scale(height*3/4-doorHeight))),frontBottomLeft.add(directionHeight.scale(height*3/4-doorHeight))));
        wallWigs.add(new Polygon(frontBottomRight, frontBottomRight.add(directionWidthNormalized.scale(-(width-doorLength)/2)), frontBottomRight.add(directionWidthNormalized.scale(-(width-doorLength)/2)).add(directionHeight.scale(height*3/4 - windowsHeight)), frontBottomRight.add(directionHeight.scale(height*3/4 - windowsHeight))));

        Point frontWallCenterPosition = frontBottomLeft.add(directionWidthNormalized.scale((width-doorLength)/2)).add(directionHeight.scale(doorHeight));
        wallWigs.add(new Polygon(frontWallCenterPosition, frontWallCenterPosition.add(directionWidthNormalized.scale(doorLength)), frontWallCenterPosition.add(directionWidthNormalized.scale(doorLength)).add(directionHeight.scale(height*3/4-windowsHeight-doorHeight)), frontWallCenterPosition.add(directionHeight.scale(height*3/4-windowsHeight-doorHeight))));

        Point frontWallopLeftPosition = frontBottomLeft.add(directionHeight.scale(height));
        wallWigs.add(new Polygon(frontWallopLeftPosition, frontWallopLeftPosition.add(directionWidthNormalized.scale(width/6)), frontWallopLeftPosition.add(directionWidthNormalized.scale(width/6)).add(directionHeight.scale(-height/4-windowsHeight)), frontWallopLeftPosition.add(directionHeight.scale(-height/4-windowsHeight))));

        Point frontWallTopRightPosition = frontBottomRight.add(directionHeight.scale(height));
        wallWigs.add(new Polygon(frontWallTopRightPosition, frontWallTopRightPosition.add(directionWidthNormalized.scale(-width/6)), frontWallTopRightPosition.add(directionWidthNormalized.scale(-width/6)).add(directionHeight.scale(-height/4-windowsHeight)), frontWallTopRightPosition.add(directionHeight.scale(-height/4-windowsHeight))));

        Point upCenterPartOfWall = frontBottomLeft.add(directionHeight.scale(height*3/4-windowsHeight)).add(directionWidthNormalized.scale(width/6+windowsLength));
        wallWigs.add(new Polygon(upCenterPartOfWall, upCenterPartOfWall.add(directionWidthNormalized.scale(width*2/3-windowsLength*2)), upCenterPartOfWall.add(directionWidthNormalized.scale(width*2/3-windowsLength*2)).add(directionHeight.scale(windowsHeight)), upCenterPartOfWall.add(directionHeight.scale(windowsHeight))));

        Point upPartOfWall = frontWallopLeftPosition.add(directionHeight.scale(-height/4)).add(directionWidthNormalized.scale(width/6));
        wallWigs.add(new Polygon(upPartOfWall, upPartOfWall.add(directionWidthNormalized.scale(width*2/3)), upPartOfWall.add(directionWidthNormalized.scale(width*2/3)).add(directionHeight.scale(height/4)), upPartOfWall.add(directionHeight.scale(height/4))));

        Point leftWindowPosition = frontBottomLeft.add(directionHeight.scale(height*3/4-windowsHeight)).add(directionWidthNormalized.scale(width/6));
        windowsWigs.add(new Polygon(leftWindowPosition, leftWindowPosition.add(directionWidthNormalized.scale(windowsLength)), leftWindowPosition.add(directionWidthNormalized.scale(windowsLength)).add(directionHeight.scale(windowsHeight)), leftWindowPosition.add(directionHeight.scale(windowsHeight))));

        Point rightWindowPosition = frontBottomLeft.add(directionHeight.scale(height*3/4-windowsHeight)).add(directionWidthNormalized.scale(width*2/3 - windowsLength));
        windowsWigs.add(new Polygon(rightWindowPosition, rightWindowPosition.add(directionWidthNormalized.scale(windowsLength)), rightWindowPosition.add(directionWidthNormalized.scale(windowsLength)).add(directionHeight.scale(windowsHeight)), rightWindowPosition.add(directionHeight.scale(windowsHeight))));
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

    public HouseBase setMaterial(Material material) {
        this.material = material;
        for (Geometry face : wallWigs) {
            face.setMaterial(material);
        }
        return this;
    }

    public HouseBase setEmission(Color color) {
        this.emission = color;
        for (Geometry face : wallWigs) {
            face.setEmission(color);
        }
        return this;
    }

    private class WallWithWindows {
        private final List<Geometry> wall;
        private final List<Geometry> windows;

        public WallWithWindows(Point bottomLeft, double length, double height, double windowHeight, double windowLength,  Vector directionLength, Vector directionHeight) {
            wall = new LinkedList<>();
            windows = new LinkedList<>();

            // Add wall parts
            Point bottomRight = bottomLeft.add(directionLength.scale(length));
            wall.add(new Polygon(bottomLeft, bottomRight, bottomRight.add(directionHeight.scale(height*3/4 - windowHeight)), bottomLeft.add(directionHeight.scale(height*3/4 - windowHeight))));
            Point topLeft = bottomLeft.add(directionHeight.scale(height));
            wall.add(new Polygon(topLeft, topLeft.add(directionLength.scale(length/6)), topLeft.add(directionLength.scale(length/6)).add(directionHeight.scale(-height/4-windowHeight)), topLeft.add(directionHeight.scale(-height/4-windowHeight))));
            Point topRight = bottomRight.add(directionHeight.scale(height));
            wall.add(new Polygon(topRight, topRight.add(directionLength.scale(-length/6)), topRight.add(directionLength.scale(-length/6)).add(directionHeight.scale(-height/4-windowHeight)), topRight.add(directionHeight.scale(-height/4-windowHeight))));
            Point upCenterPartOfWall = bottomLeft.add(directionHeight.scale(height*3/4-windowHeight)).add(directionLength.scale(length/6+windowLength));
            wall.add(new Polygon(upCenterPartOfWall, upCenterPartOfWall.add(directionLength.scale(length*2/3-windowLength*2)), upCenterPartOfWall.add(directionLength.scale(length*2/3-windowLength*2)).add(directionHeight.scale(windowHeight)), upCenterPartOfWall.add(directionHeight.scale(windowHeight))));
            Point upPartOfWall = topLeft.add(directionHeight.scale(-height/4)).add(directionLength.scale(length/6));
            wall.add(new Polygon(upPartOfWall, upPartOfWall.add(directionLength.scale(length*2/3)), upPartOfWall.add(directionLength.scale(length*2/3)).add(directionHeight.scale(height/4)), upPartOfWall.add(directionHeight.scale(height/4))));

            // Add windows
            Point leftWindowPosition = bottomLeft.add(directionHeight.scale(height*3/4-windowHeight)).add(directionLength.scale(length/6));
            windows.add(new Polygon(leftWindowPosition, leftWindowPosition.add(directionLength.scale(windowLength)), leftWindowPosition.add(directionLength.scale(windowLength)).add(directionHeight.scale(windowHeight)), leftWindowPosition.add(directionHeight.scale(windowHeight))));
            Point rightWindowPosition = bottomLeft.add(directionHeight.scale(height*3/4-windowHeight)).add(directionLength.scale(length*5/6 - windowLength));
            windows.add(new Polygon(rightWindowPosition, rightWindowPosition.add(directionLength.scale(windowLength)), rightWindowPosition.add(directionLength.scale(windowLength)).add(directionHeight.scale(windowHeight)), rightWindowPosition.add(directionHeight.scale(windowHeight))));

            wallWigs.addAll(wall);
            windowsWigs.addAll(windows);
        }
    }
}
