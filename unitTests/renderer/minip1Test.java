package renderer;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.*;
import scene.Scene;

public class minip1Test {

    public static void main(String[] args) {
        Scene scene = new Scene("Mickey Mouse")
                .setBackground(new Color(0, 255, 0)) // Green background
                .setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), new Double3(0.15)));

        // Mickey Mouse's head and face
        scene.geometries.add(
                new Sphere(new Point(0, 0, -100), 50) // Head (black)
                        .setEmission(new Color(java.awt.Color.BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),

                new Sphere(new Point(0, 0, -98), 48) // Face (flesh color)
                        .setEmission(new Color(255, 224, 189))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),

                // Left ear
                new Sphere(new Point(-55, 55, -70), 30)
                        .setEmission(new Color(java.awt.Color.BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),

                // Right ear
                new Sphere(new Point(55, 55, -70), 30)
                        .setEmission(new Color(java.awt.Color.BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),

                // Left eye
                new Sphere(new Point(-15, 10, -60), 10)
                        .setEmission(new Color(java.awt.Color.WHITE))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),

                // Right eye
                new Sphere(new Point(15, 10, -60), 10)
                        .setEmission(new Color(java.awt.Color.WHITE))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),

                // Left eye pupil
                new Sphere(new Point(-15, 10, -55), 2)
                        .setEmission(new Color(java.awt.Color.BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),

                // Right eye pupil
                new Sphere(new Point(15, 10, -55), 2)
                        .setEmission(new Color(java.awt.Color.BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),

                // Nose
                new Sphere(new Point(0, -10, -55), 7)
                        .setEmission(new Color(java.awt.Color.BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),

                // Mouth (creating a smile using two triangles)
                new Triangle(new Point(-20, -30, -50), new Point(20, -30, -50), new Point(0, -20, -45))
                        .setEmission(new Color(java.awt.Color.BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),
                new Triangle(new Point(-20, -30, -50), new Point(20, -30, -50), new Point(0, -40, -55))
                        .setEmission(new Color(java.awt.Color.BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100))
        );

        // Adding lights
        scene.lights.add(new PointLight(new Color(700, 400, 400), new Point(-100, 100, -50))
                .setKl(4E-4).setKq(2E-5));
        scene.lights.add(new SpotLight(new Color(400, 400, 700), new Point(100, 100, -50), new Vector(-1, -1, -2))
                .setKl(4E-4).setKq(2E-5));
        scene.lights.add(new DirectionalLight(new Color(300, 150, 150), new Vector(0, -1, -1)));

        // Camera setup
        Camera camera = Camera.getBuilder()
                .setRayTracer(new SimpleRayTracer(scene))
                .setLocation(new Point(0, 0, 1000))
                .setDirection(Point.ZERO, Vector.Y)
                .setVpSize(200, 200)
                .setVpDistance(1000)
                .setImageWriter(new ImageWriter("mickey_mouse", 500, 500))
                .build();

        camera.renderImage();
        camera.writeToImage();
    }
}

