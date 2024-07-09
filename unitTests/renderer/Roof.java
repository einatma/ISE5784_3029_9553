package renderer;

import geometries.*;
import primitives.*;

public class Roof {
    private final double width;
    private final double length;
    private final double roofHeight;
    //a house represented by polygons.
    public Roof(Point frontBottomLeft ,double width, double length, double roofHeight) {
        this.width = width;
        this.length = length;
        this.roofHeight = roofHeight;
        //create the roof

        // Create the points for the roof
        Point frontTopCenter = new Point(frontBottomLeft.getX() + width / 2, frontBottomLeft.getY(), frontBottomLeft.getZ() + roofHeight);
        Point backTopCenter = new Point(frontBottomLeft.getX() + width / 2, frontBottomLeft.getY() + length, frontBottomLeft.getZ() + roofHeight);

        // Create the front triangular part of the roof
        Polygon frontTriangle = new Polygon(
                frontBottomLeft,
                new Point(frontBottomLeft.getX() + width, frontBottomLeft.getY(), frontBottomLeft.getZ()),
                frontTopCenter
        );

        // Create the back triangular part of the roof
        Polygon backTriangle = new Polygon(
                new Point(frontBottomLeft.getX(), frontBottomLeft.getY() + length, frontBottomLeft.getZ()),
                new Point(frontBottomLeft.getX() + width, frontBottomLeft.getY() + length, frontBottomLeft.getZ()),
                backTopCenter
        );

        // Create the left rectangular part of the roof
        Polygon leftRectangle = new Polygon(
                frontBottomLeft,
                new Point(frontBottomLeft.getX(), frontBottomLeft.getY() + length, frontBottomLeft.getZ()),
                backTopCenter,
                frontTopCenter
        );

        // Create the right rectangular part of the roof
        Polygon rightRectangle = new Polygon(
                new Point(frontBottomLeft.getX() + width, frontBottomLeft.getY(), frontBottomLeft.getZ()),
                new Point(frontBottomLeft.getX() + width, frontBottomLeft.getY() + length, frontBottomLeft.getZ()),
                backTopCenter,
                frontTopCenter
        );

    }
    public double getRoofHeight() {
        return roofHeight;
    }
    public double getWidth() {
        return width;
    }
    public double getLength() {
        return length;
    }
}
