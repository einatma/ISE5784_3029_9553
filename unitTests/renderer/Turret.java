//package renderer;
//
//import geometries.Geometry;
//import primitives.Point;
//import primitives.Vector;
//import primitives.Util;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import static primitives.Util.isZero;
//
//public class Turret {
//    private final List<Geometry> turretGeometries;
//
//    public Turret(Point frontBottomLeft, Vector directionWidth, Vector directionLength, double baseWidth, double baseHeight, double baseLength, double baseDepth, double crenellationHeight, int numOfCrenellationsEachSide) {
//        if (baseWidth <= 0 || baseHeight <= 0 || baseLength <= 0 || baseDepth <= 0 || crenellationHeight <= 0)
//            throw new IllegalArgumentException("Width, height, length, depth and roof height must be positive");
//        if (directionWidth == null || directionLength == null || !isZero(directionWidth.dotProduct(directionLength)))
//            throw new IllegalArgumentException("Direction vectors must be orthogonal");
//
//        turretGeometries = new LinkedList<>();
//
//        Box base = new Box(frontBottomLeft, baseWidth, baseHeight, baseLength, directionWidth, directionLength);
//        turretGeometries.addAll(base.getCubeWigs());
//        Point roofFrontBottomLeft = new Point(frontBottomLeft.getX(), frontBottomLeft.getY(), frontBottomLeft.getZ() + baseLength - baseDepth);
//        Point roofFrontBottomRight = new Point(frontBottomLeft.getX() + baseWidth, frontBottomLeft.getY(), frontBottomLeft.getZ() + baseLength - baseDepth);
//        Point roofFrontTopLeft = new Point(frontBottomLeft.getX(), frontBottomLeft.getY(), frontBottomLeft.getZ() + baseLength);
//        Point roofFrontTopRight = new Point(frontBottomLeft.getX() + baseWidth, frontBottomLeft.getY(), frontBottomLeft.getZ() + baseLength);
//        double crenellationWidth = baseWidth / (2 * numOfCrenellationsEachSide);
//        double crenellationLength = crenellationWidth / 2;
//        double scaleVectorWidth = 1;
//        for (int i = 0; i < numOfCrenellationsEachSide; i++) {
//            if (i != 0)
//                scaleVectorWidth = i * crenellationWidth * 2;
//            //add front crenellations
//            Box crenellationFront = new Box(roofFrontBottomLeft.add(directionWidth.scale(scaleVectorWidth)), crenellationWidth, crenellationHeight, crenellationLength, directionWidth, directionLength);
//            turretGeometries.addAll(crenellationFront.getCubeWigs());
//            //add back crenellations
//            Box crenellationBack = new Box(roofFrontBottomLeft.add(directionWidth.scale(scaleVectorWidth)), crenellationWidth, crenellationHeight, crenellationLength, directionWidth, directionLength);
//            turretGeometries.addAll(crenellationBack.getCubeWigs());
//            //add left crenellations
//            Box crenellationLeft = new Box(roofFrontBottomLeft.add(directionWidth.scale(scaleVectorWidth)), crenellationLength, crenellationHeight, crenellationWidth, directionWidth, directionLength);
//            turretGeometries.addAll(crenellationLeft.getCubeWigs());
//            //add right crenellations
//            Box crenellationRight = new Box(roofFrontBottomLeft.add(directionWidth.scale(scaleVectorWidth)), crenellationLength, crenellationHeight, crenellationWidth, directionWidth, directionLength);
//            turretGeometries.addAll(crenellationRight.getCubeWigs());
//        }
//    }
//
//    public List<Geometry> getTurretGeometries() {
//        return turretGeometries;
//    }
//}
