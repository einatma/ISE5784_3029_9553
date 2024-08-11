package renderer;

import geometries.*;
import primitives.*;

import java.util.LinkedList;
import java.util.List;

public class BrickWall {
    private final List<Geometry> bricks;
    private final List<Geometry> slots;
    private final Point frontBottomLeft;
    private final Point frontBottomRight;
    private final Point backBottomLeft;
    private final Point backBottomRight;
    private final Point frontTopLeft;
    private final Point frontTopRight;
    private final Point backTopLeft;
    private final Point backTopRight;

    public BrickWall(Point frontBottomLeft, double depth, double width, double height, double slotsWidthAndHeight, int numOfBricksWidth, int numOfBricksHeight, Vector directionWidth, Vector directionDepth) {
        if (depth <= 0 || width <= 0 || height <= 0 || slotsWidthAndHeight <= 0 ||  numOfBricksWidth <= 0 || numOfBricksHeight <= 0)

            throw new IllegalArgumentException("Length, width and height must be positive");
        if (directionWidth == null || directionDepth == null || !Util.isZero(directionWidth.dotProduct(directionDepth)))
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
        double bricksWidth = (width - (numOfBricksWidth-1)*slotsWidthAndHeight) / numOfBricksWidth;
        double bricksHeight = (height - (numOfBricksHeight-1)*slotsWidthAndHeight) / numOfBricksHeight;
        slots = new LinkedList<>();
        for (int i=0; i< numOfBricksWidth-1; i++) {
            Point slotFrontBottomLeft =  frontBottomLeft.add(directionWidthNormalized.scale((i+1)*bricksWidth + i*slotsWidthAndHeight));
            Box slot = new Box(slotFrontBottomLeft, slotsWidthAndHeight, height, depth, directionWidthNormalized, directionDepthNormalized);
            slots.addAll(slot.getCubeWigs());
        }
        for (int i=0; i<numOfBricksHeight-1; i++) {
            Point slotFrontBottomLeft =  frontBottomLeft.add(directionHeight.scale((i+1)*bricksHeight + slotsWidthAndHeight*i));
            Box slot = new Box(slotFrontBottomLeft, width, slotsWidthAndHeight, depth, directionWidthNormalized, directionDepthNormalized);
            slots.addAll(slot.getCubeWigs());
        }
        bricks = new LinkedList<>();
        for (int i = 0; i < numOfBricksWidth; i++)
            for (int j = 0; j < numOfBricksHeight; j++) {
                Point brickFrontBottomLeft = this.frontBottomLeft;
                if (i != 0)
                    brickFrontBottomLeft = brickFrontBottomLeft.add(directionWidthNormalized.scale(i * (bricksWidth+slotsWidthAndHeight)));
                if (j != 0)
                    brickFrontBottomLeft = brickFrontBottomLeft.add(directionHeight.scale(j * (bricksHeight+slotsWidthAndHeight)));
                Box brick = new Box(brickFrontBottomLeft, bricksWidth, bricksHeight, depth, directionWidthNormalized, directionDepthNormalized);
                bricks.addAll(brick.getCubeWigs());
            }
    }

    public List<Geometry> getBrickWallWigs() {
        List<Geometry> brickWallWigs = new LinkedList<>();
        brickWallWigs.addAll(bricks);
        brickWallWigs.addAll(slots);
        return brickWallWigs;
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

    public BrickWall setBricksMaterial(Material material) {
        for (Geometry brickWig : bricks) {
            brickWig.setMaterial(material);
        }
        return this;
    }

    public BrickWall setSlotsMaterial(Material material) {
        for (Geometry slotWig : slots) {
            slotWig.setMaterial(material);
        }
        return this;
    }

    public BrickWall setBricksEmission(Color color) {
        for (Geometry face : bricks) {
            face.setEmission(color);
        }
        return this;
    }

    public BrickWall setSlotsEmission(Color color) {
        for (Geometry face : slots) {
            face.setEmission(color);
        }
        return this;
    }
}