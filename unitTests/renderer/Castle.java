package renderer;

import geometries.Geometry;
import primitives.Color;
import primitives.Material;
import primitives.*;

import java.util.List;
import java.util.LinkedList;

public class Castle {
    private final List<Geometry> castleWigs;
    private final HouseWithWindows house;
    private final HouseWithWindows towerFrontLeft;
    private final HouseWithWindows towerFrontRight;
    private final HouseWithWindows towerBackLeft;
    private final HouseWithWindows towerBackRight;

    public Castle(Point frontBottomLeft, double centerLength, double centerWidth, double centerHeight, double centerRoofHeight, double centersWindowsLength, double centerrsWindowsHeight, double towersSideWidthAndLength, double towersHeight, double towersRoofHeight, double towersWindowsLength, double towersWindowsHeight, Vector directionWidth, Vector directionDepth) {
        Vector directionWidthNormalized = directionWidth.normalize();
        Vector directionDepthNormalized = directionDepth.normalize();
        // Creating the house in the center of the castle
        house = new HouseWithWindows(frontBottomLeft, centerLength, centerWidth, centerHeight, centersWindowsLength, centerrsWindowsHeight, centerRoofHeight, directionWidthNormalized, directionDepthNormalized);

// Creating the towers around the house
        Point towerFrontLeftPosition = house.getBase().getFrontBottomLeft().add(directionDepthNormalized.scale(-towersSideWidthAndLength)).add(directionWidthNormalized.scale(-towersSideWidthAndLength));
        towerFrontLeft = new HouseWithWindows(towerFrontLeftPosition, towersSideWidthAndLength, towersSideWidthAndLength, towersHeight, towersWindowsLength, towersWindowsHeight,  towersRoofHeight, directionWidth, directionDepth);

        Point towerFrontRightPosition = house.getBase().getFrontBottomRight().add(directionDepthNormalized.scale(-towersSideWidthAndLength));
        towerFrontRight = new HouseWithWindows(towerFrontRightPosition, towersSideWidthAndLength, towersSideWidthAndLength, towersHeight, towersWindowsLength, towersWindowsHeight, towersRoofHeight, directionWidth, directionDepth);

        Point towerBackLeftPosition = house.getBase().getBackBottomLeft().add(directionWidthNormalized.scale(-towersSideWidthAndLength));
        towerBackLeft = new HouseWithWindows(towerBackLeftPosition, towersSideWidthAndLength, towersSideWidthAndLength, towersHeight, towersWindowsLength, towersWindowsHeight, towersRoofHeight, directionWidth, directionDepth);

        Point towerBackRightPosition = house.getBase().getBackBottomRight();
        towerBackRight = new HouseWithWindows(towerBackRightPosition, towersSideWidthAndLength, towersSideWidthAndLength, towersHeight, towersWindowsLength, towersWindowsHeight, towersRoofHeight, directionWidth, directionDepth);

        castleWigs = new LinkedList<>();
        castleWigs.addAll(house.getHouseWigs());
        castleWigs.addAll(towerFrontLeft.getHouseWigs());
        castleWigs.addAll(towerFrontRight.getHouseWigs());
        castleWigs.addAll(towerBackLeft.getHouseWigs());
        castleWigs.addAll(towerBackRight.getHouseWigs());
    }

    public Castle setWallsMaterial(Material material) {
        house.setWallsMaterial(material);
        towerFrontLeft.setWallsMaterial(material);
        towerFrontRight.setWallsMaterial(material);
        towerBackLeft.setWallsMaterial(material);
        towerBackRight.setWallsMaterial(material);
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
    public Castle setWindowsMaterial(Material material) {
        house.setWindowsMaterial(material);
        towerFrontLeft.setWindowsMaterial(material);
        towerFrontRight.setWindowsMaterial(material);
        towerBackLeft.setWindowsMaterial(material);
        towerBackRight.setWindowsMaterial(material);
        return this;
    }

    public Castle setWallsEmission(Color emission) {
        house.setWallsEmission(emission);
        towerFrontLeft.setWallsEmission(emission);
        towerFrontRight.setWallsEmission(emission);
        towerBackLeft.setWallsEmission(emission);
        towerBackRight.setWallsEmission(emission);
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
    public Castle setWindowsEmission(Color emission) {
        house.setWindowsEmission(emission);
        towerFrontLeft.setWindowsEmission(emission);
        towerFrontRight.setWindowsEmission(emission);
        towerBackLeft.setWindowsEmission(emission);
        towerBackRight.setWindowsEmission(emission);
        return this;
    }
    public List<Geometry> getCastleWigs() {
        return castleWigs;
    }
}
