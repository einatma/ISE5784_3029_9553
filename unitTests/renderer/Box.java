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
    private Color emission = new Color(0, 0, 0);
    private Material material = new Material();
    private final List<Geometry> cubeWigs;

    public Box(Point frontBottomLeft, double width, double height, double depth, Vector directionWidth, Vector directionDepth) {
        if (width <= 0 || height <= 0 || depth <= 0)
            throw new IllegalArgumentException("Width, height and depth must be positive");
        if (directionWidth == null || directionDepth == null || !isZero(directionWidth.dotProduct(directionDepth)))
            throw new IllegalArgumentException("Direction vectors must be orthogonal");
        Vector directionHeight = directionWidth.crossProduct(directionDepth).normalize();
        Vector directionWidthNormalized = directionWidth.normalize();
        Vector directionDepthNormalized = directionDepth.normalize();

        this.frontBottomLeft = frontBottomLeft;
        this.frontBottomRight = frontBottomLeft.add(directionWidthNormalized.scale(width));
        this.backBottomLeft = frontBottomLeft.add(directionDepthNormalized.scale(depth));
        this.backBottomRight = this.backBottomLeft.add(directionWidthNormalized.scale(width));

        this.frontTopLeft = this.frontBottomLeft.add(directionHeight.scale(height));
        this.frontTopRight = this.frontBottomRight.add(directionHeight.scale(height));
        this.backTopLeft = this.backBottomLeft.add(directionHeight.scale(height));
        this.backTopRight = this.backBottomRight.add(directionHeight.scale(height));

        // Create the cube
        cubeWigs = new LinkedList<>();
        // Cube's front face
        cubeWigs.add(new Polygon(frontBottomLeft, frontBottomRight, frontTopRight, frontTopLeft));
        // Cube's back face
        cubeWigs.add(new Polygon(backBottomLeft, backBottomRight, backTopRight, backTopLeft));
        // Cube's left face
        cubeWigs.add(new Polygon(frontBottomLeft, backBottomLeft, backTopLeft, frontTopLeft));
        // Cube's right face
        cubeWigs.add(new Polygon(frontBottomRight, backBottomRight, backTopRight, frontTopRight));
        // Cube's top face
        cubeWigs.add(new Polygon(frontTopLeft, frontTopRight, backTopRight, backTopLeft));
        // Cube's bottom face
        cubeWigs.add(new Polygon(frontBottomLeft, frontBottomRight, backBottomRight, backBottomLeft));
    }



    public List<Geometry> getCubeWigs() {
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
    public Box setMaterial(Material material) {
        this.material = material;
        for (Geometry face : cubeWigs) {
            face.setMaterial(material);
        }
        return this;
    }
    public Box setEmission(Color color) {
        this.emission = color;
        for (Geometry face : cubeWigs) {
            face.setEmission(color);
        }
        return this;
    }
}
