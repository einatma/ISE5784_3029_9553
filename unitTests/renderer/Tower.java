//package renderer;
//
//import geometries.Geometry;
//import primitives.*;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class Tower extends House{
//        private final HouseBase base;
//        private final Roof roof;
//        private final List<Geometry> towerWigs;
//
//        public Tower(Point frontBottomLeft, double lengthAndWidth, double height, double roofHeight, Vector directionWidth, Vector directionDepth)  {
//            super(frontBottomLeft, lengthAndWidth, lengthAndWidth, height, roofHeight, directionWidth, directionDepth);
//            towerWigs = new LinkedList<>();
//            towerWigs.addAll(super.getHouseWigs());
//            base = super.getBase();
//            roof = super.getRoof();
//        }
//        public Tower setBaseMaterial(Material material) {
//            base.setMaterial(material);
//            return this;
//        }
//
//        public Tower setRoofMaterial(Material material) {
//            roof.setMaterial(material);
//            return this;
//        }
//
//        public Tower setBaseEmission(Color emission) {
//            base.setEmission(emission);
//            return this;
//        }
//
//        public Tower setRoofEmission(Color emission) {
//            roof.setEmission(emission);
//            return this;
//        }
//        public List<Geometry> getHouseWigs() {
//            return towerWigs;
//        }
//        public HouseBase getBase() {
//            return base;
//        }
//}
