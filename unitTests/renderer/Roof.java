package renderer;

import geometries.*;
import primitives.*;

import java.util.LinkedList;
import java.util.List;

public class Roof {
    private final Point frontBottomLeft;
    private final Point frontBottomRight;
    private final Point backBottomLeft;
    private final Point backBottomRight;
    private final Point ridgeFront;
    private final Point ridgeBack;
    private Material material = new Material();
    private Color emission = new Color(0, 0, 0);
    private final List<Geometry> roofGeometry;

    public Roof(Point frontBottomLeft, double width, double height, double depth, Vector directionWidth, Vector directionDepth) {
        if (width <= 0 || height <= 0 || depth <= 0)
            throw new IllegalArgumentException("Width, height and depth must be positive");
        if (directionWidth == null || directionDepth == null || !Util.isZero(directionWidth.dotProduct(directionDepth)))
            throw new IllegalArgumentException("Direction vectors must be orthogonal");
        Vector directionHeight = directionWidth.crossProduct(directionDepth).normalize();
        Vector directionWidthNormalized = directionWidth.normalize();
        Vector directionDepthNormalized = directionDepth.normalize();

        this.frontBottomLeft = frontBottomLeft;
        this.frontBottomRight = frontBottomLeft.add(directionWidthNormalized.scale(width));
        this.backBottomLeft = frontBottomLeft.add(directionDepthNormalized.scale(depth));
        this.backBottomRight = backBottomLeft.add(directionWidthNormalized.scale(width));
        this.ridgeFront = frontBottomLeft.add(directionHeight.scale(height)).add(directionWidthNormalized.scale(width / 2));
        this.ridgeBack = backBottomLeft.add(directionHeight.scale(height)).add(directionWidthNormalized.scale(width / 2));

        // Create the roof
        roofGeometry = new LinkedList<>();
        // Left slope
        roofGeometry.add(new Polygon(frontBottomLeft, ridgeFront, ridgeBack, backBottomLeft));
        // Right slope
        roofGeometry.add(new Polygon(frontBottomRight, ridgeFront, ridgeBack, backBottomRight));
        // Front triangle
        roofGeometry.add(new Triangle(frontBottomLeft, frontBottomRight, ridgeFront));
        // Back triangle
        roofGeometry.add(new Triangle(backBottomLeft, backBottomRight, ridgeBack));
    }

    public List<Geometry> getRoofGeometry() {
        return roofGeometry;
    }

    public Roof setMaterial(Material material) {
        this.material = material;
        for (Geometry face : roofGeometry) {
            face.setMaterial(material);
        }
        return this;
    }

    public Roof setEmission(Color emission) {
        this.emission = emission;
        for (Geometry face : roofGeometry) {
            face.setEmission(emission);
        }
        return this;
    }
}
