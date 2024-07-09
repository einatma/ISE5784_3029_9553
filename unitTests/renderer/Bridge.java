package renderer;

import geometries.*;
        import primitives.*;

public class Bridge {
    public Bridge(Box bridgeBase, int numOfBottomPoles, double bottomPolesHeight, int numOfTopPoles, double topPolesHeight, double roofHeight) {
        double bridgeWidth = bridgeBase.getWidth();
        double poleRadius = bridgeWidth / (2 * numOfBottomPoles);
        double topPoleWidth = poleRadius/3;
        double topPoleSpacing = (bridgeWidth - numOfTopPoles * topPoleWidth) / (numOfTopPoles + 1);

        // Create the bottom poles (cylinders)
        for (int i = 0; i < numOfBottomPoles; i++) {
            double xPosition = bridgeBase.getFrontBottomLeft().getX() + i * (2 * poleRadius);
            Cylinder bottomPole = new Cylinder(
                    bottomPolesHeight,
                    new Ray(
                            new Point(xPosition, bridgeBase.getFrontBottomLeft().getY(), bridgeBase.getFrontBottomLeft().getZ()),
                            new Vector(0, 1, 0)
                    ),
                    poleRadius
            );
            // Add the bottom pole to the scene (implementation dependent)
        }

        // Create the top poles (cubes)
        for (int i = 0; i < numOfTopPoles; i++) {
            double xPosition = bridgeBase.getFrontBottomLeft().getX() + topPoleSpacing + i * (topPoleWidth + topPoleSpacing);
            Box topPole = new Box(
                    new Point(xPosition, bridgeBase.getFrontBottomLeft().getY() + bottomPolesHeight, bridgeBase.getFrontBottomLeft().getZ()),
                    topPoleWidth,
                    topPolesHeight,
                    topPoleWidth, new Vector(1, 0, 0), new Vector(0, 1, 0)
            );
            // Add the top pole to the scene (implementation dependent)
        }

        // Create the railing (another beam)
        double railingHeight = topPolesHeight / 2;
        Box railing = new Box(
                new Point(bridgeBase.getFrontBottomLeft().getX(), bridgeBase.getFrontBottomLeft().getY() + bottomPolesHeight + topPolesHeight, bridgeBase.getFrontBottomLeft().getZ()),
                bridgeWidth,
                railingHeight,
                topPoleWidth, new Vector(1, 0, 0), new Vector(0, 1, 0)
        );
        //Create the roof
        Polygon roof = new Polygon(
                new Point(bridgeBase.getFrontBottomLeft().getX(), bridgeBase.getFrontBottomLeft().getY() + bottomPolesHeight + topPolesHeight + railingHeight, bridgeBase.getFrontBottomLeft().getZ()),
                new Point(bridgeBase.getFrontBottomLeft().getX() + bridgeWidth, bridgeBase.getFrontBottomLeft().getY() + bottomPolesHeight + topPolesHeight + railingHeight, bridgeBase.getFrontBottomLeft().getZ()),
                new Point(bridgeBase.getFrontBottomLeft().getX() + bridgeWidth, bridgeBase.getFrontBottomLeft().getY() + bottomPolesHeight + topPolesHeight + railingHeight, bridgeBase.getFrontBottomLeft().getZ() + bridgeWidth),
                new Point(bridgeBase.getFrontBottomLeft().getX(), bridgeBase.getFrontBottomLeft().getY() + bottomPolesHeight + topPolesHeight + railingHeight, bridgeBase.getFrontBottomLeft().getZ() + bridgeWidth)
        );
    }
}


