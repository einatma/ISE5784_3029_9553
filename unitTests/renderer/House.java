package renderer;

import geometries.*;
import primitives.*;

import java.util.LinkedList;
import java.util.List;

public class House {
    private final Box base;
    private final Roof roof;
    private final List<Geometry> houseWigs;

    public House(Point frontBottomLeft, double length, double width, double height, double roofHeight, Vector directionWidth, Vector directionDepth)  {
        if (length <= 0 || width <= 0 || height <= 0 || roofHeight <= 0)
            throw new IllegalArgumentException("Length, width, height and roof height must be positive");
        if (directionWidth == null || directionDepth == null || !Util.isZero(directionWidth.dotProduct(directionDepth)))
            throw new IllegalArgumentException("Direction vectors must be orthogonal");
        houseWigs = new LinkedList<>();
        // Create the base
        this.base = new Box(frontBottomLeft, length, height, width, directionWidth, directionDepth);
        houseWigs.addAll(base.getCubeWigs());

        // Calculate the directionHeight vector
        Vector directionHeight = directionWidth.crossProduct(directionDepth).normalize();

        // Calculate the position of the roof's front bottom left point
        Point roofFrontBottomLeft = frontBottomLeft.add(directionHeight.scale(height));

        // Create the roof
        this.roof = new Roof(roofFrontBottomLeft, length, roofHeight, width, directionWidth, directionDepth);
        houseWigs.addAll(roof.getRoofGeometry());
}
    public House setBaseMaterial(Material material) {
        base.setMaterial(material);
        return this;
    }

    public House setRoofMaterial(Material material) {
        roof.setMaterial(material);
        return this;
    }

    public House setBaseEmission(Color emission) {
        base.setEmission(emission);
        return this;
    }

    public House setRoofEmission(Color emission) {
        roof.setEmission(emission);
        return this;
    }
    public List<Geometry> getHouseWigs() {
        return houseWigs;
    }
}

