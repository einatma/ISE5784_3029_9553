//package renderer;
//
//import geometries.Polygon;
//import primitives.*;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import static primitives.Util.isZero;
//
//public class Turret {
//    private final List<Polygon> turretGeometries;
//
//    public Turret(Point frontBottomLeft, Vector directionWidth, Vector directionLength, double baseWidth, double baseHeight, double baseLength, double baseDepth, double roofHeight, int numOfCrenellationsEachSide) {
//        if (baseWidth <= 0 || baseHeight <= 0 || baseLength <= 0 || baseDepth <= 0 || roofHeight <= 0)
//            throw new IllegalArgumentException("Width, height, length, depth and roof height must be positive");
//        if (directionWidth == null || directionLength == null || !isZero(directionWidth.dotProduct(directionLength)))
//            throw new IllegalArgumentException("Direction vectors must be orthogonal");
//        Box base = new Box(frontBottomLeft, baseWidth, baseHeight, baseLength, directionWidth, directionLength);
//        Box roofBase = new Box(new Point(frontBottomLeft.getX(), frontBottomLeft.getY() + baseHeight, frontBottomLeft.getZ()), baseWidth, roofHeight, baseLength, directionWidth, directionLength);
//        for (int i = 0; i < numOfCrenellationsEachSide; i++) {
//            double crenellationWidth = baseWidth / (2 * numOfCrenellationsEachSide);
//            double crenellationHeight = baseHeight / 2;
//            double crenellationLength = baseLength / (2 * numOfCrenellationsEachSide);
//            double crenellationDepth = baseDepth / (2 * numOfCrenellationsEachSide);
//            Box crenellation = new Box(new Point(frontBottomLeft.getX() + crenellationWidth, frontBottomLeft.getY() + crenellationHeight, frontBottomLeft.getZ() + crenellationLength), crenellationWidth, crenellationHeight, crenellationLength, directionWidth, directionLength);
//            Box crenellationRoof = new Box(new Point(frontBottomLeft.getX() + crenellationWidth, frontBottomLeft.getY() + baseHeight + roofHeight, frontBottomLeft.getZ() + crenellationLength), crenellationWidth, crenellationHeight, crenellationLength, directionWidth, directionLength);
//        }
//        turretGeometries = new LinkedList<>();
//        turretGeometries.addAll(base.getCubeWigs());
//        turretGeometries.addAll(roofBase.getCubeWigs());
//    }
//}
