package renderer;

import geometries.*;
import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static java.awt.Color.*;
import static primitives.Util.isZero;

public class Sailboat {
    private final Point boatFrontTopLeft;
    private final Point boatFrontTopRight;
    private final Point boatBackTopLeft;
    private final Point boatBackTopRight;
    private final Point boatRidgeFront;
    private final Point boatRidgeBack;
    private final List<Geometry> sailWigs;
    private final Geometry sail;

    public Sailboat(Point frontTopLeft, double boatWidth, double boatHeight, double boatDepth, double mastLength, double mastHeight, double sailLength, double sailHeight, Vector directionWidth, Vector directionDepth) {
        if (boatWidth <= 0 || boatHeight <= 0 || boatDepth <= 0 || mastHeight <= 0 || sailLength <= 0 || sailHeight <= 0)
            throw new IllegalArgumentException("Width, height and depth must be positive");
        if (directionWidth == null || directionDepth == null || !isZero(directionWidth.dotProduct(directionDepth)))
            throw new IllegalArgumentException("Direction vectors must be orthogonal");

        this.boatFrontTopLeft = frontTopLeft;
        Vector directionHeight = directionWidth.crossProduct(directionDepth).normalize();
        Vector directionWidthNormalized = directionWidth.normalize();
        Vector directionDepthNormalized = directionDepth.normalize();
        sailWigs = new LinkedList<>();
        this.boatFrontTopRight = boatFrontTopLeft.add(directionWidthNormalized.scale(boatWidth));
        this.boatBackTopLeft = boatFrontTopLeft.add(directionDepthNormalized.scale(boatDepth));
        this.boatBackTopRight = boatBackTopLeft.add(directionWidthNormalized.scale(boatWidth));
        this.boatRidgeFront = boatFrontTopLeft.add(directionHeight.scale(-boatHeight)).add(directionWidthNormalized.scale(boatWidth / 2)).add(directionDepthNormalized.scale(boatDepth / 3));
        this.boatRidgeBack = boatBackTopLeft.add(directionHeight.scale(-boatHeight)).add(directionWidthNormalized.scale(boatWidth / 2)).add(directionDepthNormalized.scale(-boatDepth / 3));
        sailWigs.add(new Polygon(boatFrontTopLeft, boatRidgeFront, boatRidgeBack, boatBackTopLeft));
        // Right slope
        sailWigs.add(new Polygon(boatFrontTopRight, boatRidgeFront, boatRidgeBack, boatBackTopRight));
        // Front triangle
        sailWigs.add(new Triangle(boatFrontTopLeft, boatFrontTopRight, boatRidgeFront));
        // Back triangle
        sailWigs.add(new Triangle(boatBackTopLeft, boatBackTopRight, boatRidgeBack));

        Point mastBaseLocation = boatBackTopLeft.add(directionDepthNormalized.scale(-boatDepth / 2)).add(directionHeight.scale(-boatHeight/3)).add(directionWidthNormalized.scale(boatWidth / 4));
        Box mastBase = new Box(mastBaseLocation, mastLength, mastHeight, mastLength, directionWidthNormalized, directionDepthNormalized);
        sailWigs.addAll(mastBase.getCubeWigs());
        // Create the horizontal boom (horan)
        Box horan = new Box(mastBase.getFrontTopLeft().add(directionHeight.scale(-sailHeight)), sailLength/2, mastLength, mastLength, directionDepthNormalized, directionHeight);
        sailWigs.addAll(horan.getCubeWigs());

        // Create the sail triangle
        Point sailBottomLeft = horan.getFrontBottomRight();
        Point sailBottomRight = sailBottomLeft.add(directionDepthNormalized.scale(-sailLength));
        Point sailTop = mastBase.getBackTopLeft();
        sail = new Triangle(
                sailTop,
                sailBottomLeft,
                sailBottomRight
        );

    }

    public List<Geometry> getSailWigs() {
        List<Geometry> sailWigs = new LinkedList<>();
        sailWigs.addAll(this.sailWigs);
        sailWigs.add(sail);
        return sailWigs;
    }

    public Sailboat setBoatEmission(Color boatEmission) {
        for (Geometry face : sailWigs) {
            face.setEmission(boatEmission);
        }
        return this;
    }

    public Sailboat setSailEmission(Color sailEmission) {
        sail.setEmission(sailEmission);
        return this;
    }

    public Sailboat setBoatMaterial(Material boatMaterial) {
        for (Geometry face : sailWigs) {
            face.setMaterial(boatMaterial);
        }
        return this;
    }

    public Sailboat setSailMaterial(Material sailMaterial) {
        sail.setMaterial(sailMaterial);
        return this;
    }
}
