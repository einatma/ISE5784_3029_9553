//package renderer;
//
//import geometries.*;
//        import primitives.*;
//
//public class Bridge {
//    public Bridge(double length, double width, double baseHight,  int numOfBottomPoles, double bottomPolesHeight, int numOfTopPoles, double topPolesHeight, double roofHeight) {
//        // Create the bottom poles
//        for (int i = 0; i < numOfBottomPoles; i++) {
//            // Create the bottom pole
//            Cylinder bottomPole = new Cylinder(new Point(0, 0, 0), bottomPolesHeight, 1)
//                    .setEmission(new Color(0, 0, 0))
//                    .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
//            // Add the bottom pole to the scene
//            //scene.geometries.add(bottomPole);
//        }
//
//        // Create the top poles
//        for (int i = 0; i < numOfTopPoles; i++) {
//            // Create the top pole
//            Cylinder topPole = new Cylinder(1, new Point(0, 0, 0), topPolesHeight, 1)
//                    .setEmission(new Color(0, 0, 0))
//                    .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
//            // Add the top pole to the scene
//            //scene.geometries.add(topPole);
//        }
//
//        // Create the bridge
//        Box bridge = new Box(new Point(0, 0, 0), length, width, baseHight, new Vector(1, 0, 0), new Vector(0, 1, 0))
//                .setEmission(new Color(0, 0, 0))
//                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
//        // Add the bridge to the scene
//        //scene.geometries.add(bridge);
//
//        // Create the roof
//        Box roof = new Box(new Point(0, 0, 0), length, width, roofHeight, new Vector(1, 0, 0), new Vector(0, 1, 0))
//                .setEmission(new Color(0, 0, 0))
//                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
//        // Add the roof to the scene
//        //scene.geometries.add(roof);
//    }
//}
//
//
