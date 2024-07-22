package renderer;

import geometries.*;
import primitives.*;

import java.util.LinkedList;
import java.util.List;

public class HouseWithWindows {
    private final HouseBase base;
    private final Roof roof;
    private final List<Geometry> houseWigs;

    public HouseWithWindows(Point frontBottomLeft, double length, double width, double height, double windowsLength, double windowsHeight, double roofHeight, Vector directionWidth, Vector directionDepth)  {
        if (length <= 0 || width <= 0 || height <= 0 || roofHeight <= 0)
            throw new IllegalArgumentException("Length, width, height and roof height must be positive");
        if (directionWidth == null || directionDepth == null || !Util.isZero(directionWidth.dotProduct(directionDepth)))
            throw new IllegalArgumentException("Direction vectors must be orthogonal");
        if (windowsLength <= 0 || windowsHeight <= 0)
            throw new IllegalArgumentException("Windows dimensions must be positive");
        if (windowsLength >= length || windowsLength >= width || windowsHeight >= height)
            throw new IllegalArgumentException("Windows dimensions must be smaller than the house dimensions");
        houseWigs = new LinkedList<>();
        // Create the base
        this.base = new HouseBase(frontBottomLeft, length, height, width ,windowsLength,windowsHeight, directionWidth, directionDepth);
        houseWigs.addAll(base.getHouseBaseGeometries());

        // Calculate the directionHeight vector
        Vector directionHeight = directionWidth.crossProduct(directionDepth).normalize();

        // Calculate the position of the roof's front bottom left point
        Point roofFrontBottomLeft = frontBottomLeft.add(directionHeight.scale(height));

        // Create the roof
        this.roof = new Roof(roofFrontBottomLeft, length, roofHeight, width, directionWidth, directionDepth);
        houseWigs.addAll(roof.getRoofGeometry());
    }
    public HouseWithWindows setWallsMaterial(Material material) {
        base.setWallMaterial(material);
        return this;
    }
    public HouseWithWindows setWindowsMaterial(Material material) {
        base.setWindowMaterial(material);
        return this;
    }

    public HouseWithWindows setRoofMaterial(Material material) {
        roof.setMaterial(material);
        return this;
    }

    public HouseWithWindows setWallsEmission(Color emission) {
        base.setWallEmission(emission);
        return this;
    }
    public HouseWithWindows setWindowsEmission(Color emission) {
        base.setWindowEmission(emission);
        return this;
    }

    public HouseWithWindows setRoofEmission(Color emission) {
        roof.setEmission(emission);
        return this;
    }
    public List<Geometry> getHouseWigs() {
        return houseWigs;
    }
    public HouseBase getBase() {
        return base;
    }

    protected Roof getRoof() {
        return roof;
    }
}
