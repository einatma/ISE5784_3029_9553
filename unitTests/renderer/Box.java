package renderer;
import geometries.*;
import primitives.*;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.*;

public class Box {
    private final Point frontBottomLeft;
    private final Point frontBottomRight;
    private final Point backBottomLeft;
    private final Point backBottomRight;
    private final Point frontTopLeft;
    private final Point frontTopRight;
    private final Point backTopLeft;
    private final Point backTopRight;
    private final double width;
    private final double height;
    private final double depth;
    private final List<Polygon> cubeWigs;

    public Box(Point frontBottomLeft, double width, double height, double depth, Vector directionWidth, Vector directionDepth) {
        if (width <= 0 || height <= 0 || depth <= 0)
            throw new IllegalArgumentException("Width, height and depth must be positive");
        if (directionWidth == null || directionDepth == null || !isZero(directionWidth.dotProduct(directionDepth)))
            throw new IllegalArgumentException("Direction vectors must be orthogonal");
        this.width = width;
        this.height = height;
        this.depth = depth;
        Vector directionHightNormalized = directionWidth.crossProduct(directionDepth).normalize();
        Vector directionWidthNormalized = directionWidth.normalize();
        Vector directionDepthNormalized = directionDepth.normalize();
        this.frontBottomLeft = frontBottomLeft;
        this.frontBottomRight = frontBottomLeft.add(directionWidthNormalized.scale(width));
        this.backBottomLeft = frontBottomLeft.add(directionDepthNormalized.scale(depth));
        this.backBottomRight = backBottomLeft.add(directionWidthNormalized.scale(width));
        this.frontTopLeft = frontBottomLeft.add(directionHightNormalized.scale(height));
        this.frontTopRight = frontBottomRight.add(directionHightNormalized.scale(height));
        this.backTopLeft = backBottomLeft.add(directionHightNormalized.scale(height));
        this.backTopRight = backBottomRight.add(directionHightNormalized.scale(height));
        // Create the cube
        cubeWigs = new LinkedList<>();
        //cube's front wig
        cubeWigs.add(new Polygon(frontBottomLeft, frontBottomRight, frontTopRight, frontTopLeft));
        //cube's back wig
        cubeWigs.add(new Polygon(backBottomLeft, backBottomRight, backTopRight, backTopLeft));
        //cube's left wigs
        cubeWigs.add(new Polygon(frontBottomLeft, backBottomLeft, backTopLeft, frontTopLeft));
        //cube's right wigs
        cubeWigs.add(new Polygon(frontBottomRight, backBottomRight, backTopRight, frontTopRight));
        //cube's top wigs
        cubeWigs.add(new Polygon(frontTopLeft, frontTopRight, backTopRight, backTopLeft));
        //cube's bottom wigs
        cubeWigs.add(new Polygon(frontBottomLeft, frontBottomRight, backBottomRight, backBottomLeft));
    }

    public List<Polygon> getCubeWigs() {
        return cubeWigs;
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

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDepth() {
        return depth;
    }
}
