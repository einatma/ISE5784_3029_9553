package renderer;

import geometries.Geometry;
import primitives.Color;
import primitives.Material;
import primitives.*;

import java.util.List;
import java.util.LinkedList;

public class Castle {
    private final List<Geometry> castleWigs;
    private final House house;
    private final House towerFrontLeft;
    private final House towerFrontRight;
    private final House towerBackLeft;
    private final House towerBackRight;

    public Castle(Point frontBottomLeft, double centerLength, double centerWidth, double centerHeight, double centerRoofHeight, double towersSideWidthAndLength, double towersHeight, double towersRoofHeight, Vector directionWidth, Vector directionDepth) {
        Vector directionWidthNormalized = directionWidth.normalize();
        Vector directionDepthNormalized = directionDepth.normalize();
        // Creating the house in the center of the castle
        house = new House(frontBottomLeft, centerLength, centerWidth, centerHeight, centerRoofHeight, directionWidthNormalized, directionDepthNormalized);

// Creating the towers around the house
        Point towerFrontLeftPosition = house.getBase().getFrontBottomLeft().add(directionWidthNormalized.scale(-towersSideWidthAndLength));
        towerFrontLeft = new House(towerFrontLeftPosition, towersSideWidthAndLength, towersSideWidthAndLength, towersHeight, towersRoofHeight, directionWidth, directionDepth);

        Point towerFrontRightPosition = house.getBase().getFrontBottomRight();
        towerFrontRight = new House(towerFrontRightPosition, towersSideWidthAndLength, towersSideWidthAndLength, towersHeight, towersRoofHeight, directionWidth, directionDepth);

        Point towerBackLeftPosition = house.getBase().getBackBottomLeft().add(directionDepthNormalized.scale(-towersSideWidthAndLength)).add(directionWidthNormalized.scale(-towersSideWidthAndLength));
        towerBackLeft = new House(towerBackLeftPosition, towersSideWidthAndLength, towersSideWidthAndLength, towersHeight, towersRoofHeight, directionWidth, directionDepth);

        Point towerBackRightPosition = house.getBase().getBackBottomRight();//.add(directionWidthNormalized.scale(-towersSideWidthAndLength));
        towerBackRight = new House(towerBackRightPosition, towersSideWidthAndLength, towersSideWidthAndLength, towersHeight, towersRoofHeight, directionWidth, directionDepth);

        castleWigs = new LinkedList<>();
        castleWigs.addAll(house.getHouseWigs());
        castleWigs.addAll(towerFrontLeft.getHouseWigs());
        castleWigs.addAll(towerFrontRight.getHouseWigs());
        castleWigs.addAll(towerBackLeft.getHouseWigs());
        castleWigs.addAll(towerBackRight.getHouseWigs());
    }

    public Castle setBuilingsMaterial(Material material) {
        house.setBaseMaterial(material);
        towerFrontLeft.setBaseMaterial(material);
        towerFrontRight.setBaseMaterial(material);
        towerBackLeft.setBaseMaterial(material);
        towerBackRight.setBaseMaterial(material);
        return this;
    }

    public Castle setRoofsMaterial(Material material) {
        house.setRoofMaterial(material);
        towerFrontLeft.setRoofMaterial(material);
        towerFrontRight.setRoofMaterial(material);
        towerBackLeft.setRoofMaterial(material);
        towerBackRight.setRoofMaterial(material);
        return this;
    }

    public Castle setBuilingsEmission(Color emission) {
        house.setBaseEmission(emission);
        towerFrontLeft.setBaseEmission(emission);
        towerFrontRight.setBaseEmission(emission);
        towerBackLeft.setBaseEmission(emission);
        towerBackRight.setBaseEmission(emission);
        return this;
    }

    public Castle setRoofsEmission(Color emission) {
        house.setRoofEmission(emission);
        towerFrontLeft.setRoofEmission(emission);
        towerFrontRight.setRoofEmission(emission);
        towerBackLeft.setRoofEmission(emission);
        towerBackRight.setRoofEmission(emission);
        return this;
    }
    public List<Geometry> getCastleWigs() {
        return castleWigs;
    }
}
