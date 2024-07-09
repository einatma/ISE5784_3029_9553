package renderer;

import geometries.*;
import primitives.*;

public class House {
    private final Box base;
    private final Roof roof;
    Material baseMaterial = new Material();
    Material roofMaterial = new Material();
    Color baseEmission = new Color(0, 0, 0);
    Color roofEmission = new Color(0, 0, 0);

    public House(Point frontBottomLeft, double length, double width, double height, double roofHeight, Vector directionWidth, Vector directionDepth) {
        // Create the base
        this.base = new Box(frontBottomLeft, length, width, height, new Vector(1, 0, 0), new Vector(0, 1, 0))
        // Create the roof
        this.roof = new Roof(frontBottomLeft.add(new Vector(0, 0, height)), length, roofHeight, width, new Vector(1, 0, 0), new Vector(0, 1, 0))
    }

    public Box getBase() {
        return base;
    }

    public Roof getRoof() {
        return roof;
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
}
