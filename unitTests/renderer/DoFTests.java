package renderer;

import static java.awt.Color.*;

import geometries.*;
import lighting.DirectionalLight;
import lighting.LightSource;
import org.junit.jupiter.api.Test;

import lighting.AmbientLight;
import primitives.*;
import renderer.*;
import scene.Scene;

public class DoFTests {

	@Test
	public void testDepthOfField() {

		Scene scene = new Scene("DoF");

		final Camera.Builder cameraBuilder = Camera.getBuilder().setLocation(new Point(0, 0, 2500))
				.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setVpSize(200, 200).setVpDistance(850)
				.setDoFActive(true).setFocalLength(1600).setApertureRadius(20).setRayTracer(new SimpleRayTracer(scene));

		AmbientLight ambientLight = new AmbientLight(new Color(30, 30, 30), 0.1);
		scene.setAmbientLight(ambientLight);

		Geometry plane = new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(0, 20, 20));
		plane.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKr(0.02));
		scene.geometries.add(plane);

		scene.geometries.add(
				/**
				 * 
				 * new Sphere(new Point(100, 50, 300), 70).setEmission(new Color(220, 20, 60))
				 * .setMaterial(new
				 * Material().setKd(0.6).setKs(0.4).setShininess(100).setKr(0.3)),
				 * 
				 * new Sphere(new Point(75, -500, -300), 200).setEmission(new Color(220, 20,
				 * 60)) .setMaterial(new
				 * Material().setKd(0.6).setKs(0.4).setShininess(100).setKr(0.3)),
				 * 
				 * new Sphere(new Point(0, 0, 900), 70).setEmission(new Color(220, 20, 60))
				 * .setMaterial(new
				 * Material().setKd(0.6).setKs(0.4).setShininess(100).setKr(0.3)),
				 * 
				 * new Sphere(new Point(0, 0, -900), 50).setEmission(new Color(220, 20, 60))
				 * .setMaterial(new
				 * Material().setKd(0.6).setKs(0.4).setShininess(100).setKr(0.3)),
				 * 
				 * new Sphere(new Point(-100, -50, 1500), 70).setEmission(new Color(220, 20,
				 * 60)) .setMaterial(new
				 * Material().setKd(0.6).setKs(0.4).setShininess(100).setKr(0.3)),
				 * 
				 * new Sphere(new Point(100, 50, -1500), 50).setEmission(new Color(220, 20, 60))
				 * .setMaterial(new
				 * Material().setKd(0.6).setKs(0.4).setShininess(100).setKr(0.3)));
				 */

				new Sphere(new Point(100,700,0), 75).setEmission(new Color(255, 0, 0))
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),

				new Sphere(new Point(0, 0, 900), 75).setEmission(new Color(0, 255, 0))
						.setMaterial(new Material().setKd(0.25).setKs(0.20).setShininess(20).setKt(0.5).setKr(0.3)),

				new Sphere(new Point(-100, -50, 1500), 75).setEmission(new Color(0, 0, 255))
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),

				new Sphere(new Point(30, 50, -1600), 20).setEmission(new Color(0, 0, 255))
						.setMaterial(new Material().setKd(0.3).setKs(0.3).setShininess(40).setKt(0.4).setKr(0.2)),

				new Sphere(new Point(50, 99, 50), 30).setEmission(new Color(173, 216, 230))
						.setMaterial(new Material().setKd(0.12).setKs(0.25).setShininess(100).setKt(0.5).setKr(0.3)),

				new Sphere(new Point(400, 0, 310), 20).setEmission(new Color(255, 0, 0))
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20).setKt(0.5).setKr(0.4)),

				new Sphere(new Point(100, 50, 300), 70).setEmission(new Color(200, 10, 50))
						.setMaterial(new Material().setKd(0.1).setKs(0.8).setShininess(20).setKt(0.1).setKr(0.6)));

		/**
		 * new Sphere(new Point(0, -30, 50), 30).setEmission(new Color(123, 63, 0)) //
		 * Teddy bear head .setMaterial(new
		 * Material().setKd(0.2).setKs(0.2).setShininess(10)), // Material properties
		 * 
		 * new Sphere(new Point(0, 30, 50), 40).setEmission(new Color(123, 63, 0)) //
		 * Teddy bear belly .setMaterial(new
		 * Material().setKd(0.2).setKs(0.4).setShininess(10)),
		 * 
		 * new Sphere(new Point(-25, -55, 50), 10).setEmission(new Color(24, 4, 4)) //
		 * Teddy bear left ear .setMaterial(new
		 * Material().setKd(0.2).setKs(0.2).setShininess(80)),
		 * 
		 * new Sphere(new Point(25, -55, 50), 10).setEmission(new Color(24, 4, 4)) //
		 * Teddy bear right ear .setMaterial(new
		 * Material().setKd(0.2).setKs(0.4).setShininess(80)),
		 * 
		 * new Sphere(new Point(-45, 10, 50), 15).setEmission(new Color(123, 63, 0)) //
		 * Teddy bear left arm .setMaterial(new
		 * Material().setKd(0.2).setKs(0.4).setShininess(10)),
		 * 
		 * new Sphere(new Point(45, 10, 50), 15).setEmission(new Color(123, 63, 0)) //
		 * Teddy bear right arm .setMaterial(new
		 * Material().setKd(0.2).setKs(0.4).setShininess(10)),
		 * 
		 * new Sphere(new Point(-20, 70, 50), 20).setEmission(new Color(123, 63, 0)) //
		 * Teddy bear left leg .setMaterial(new
		 * Material().setKd(0.2).setKs(0.4).setShininess(10)),
		 * 
		 * new Sphere(new Point(20, 70, 50), 20).setEmission(new Color(123, 63, 0)) //
		 * Teddy bear right leg .setMaterial(new
		 * Material().setKd(0.2).setKs(0.4).setShininess(10)),
		 * 
		 * new Sphere(new Point(0, -20, -50), 6).setEmission(new Color(92, 64, 51)) //
		 * Teddy bear nose .setMaterial(new
		 * Material().setKd(0.1).setKs(0.2).setShininess(90)),
		 * 
		 * new Sphere(new Point(15, -30, -40), 4).setEmission(Color.BLACK) // Teddy bear
		 * right eye .setMaterial(new
		 * Material().setKd(0.0).setKs(0.0).setShininess(90)),
		 * 
		 * new Sphere(new Point(-15, -30, -40), 4).setEmission(Color.BLACK) // Teddy
		 * bear left eye .setMaterial(new
		 * Material().setKd(0.0).setKs(0.0).setShininess(90)),
		 * 
		 * new Sphere(new Point(0, -7, -100), 3).setEmission(new Color(255, 49, 49)) //
		 * Teddy bear mouth .setMaterial(new
		 * Material().setKd(0.0).setKs(0.0).setShininess(90)),
		 * 
		 * new Triangle(new Point(0, -15, -40), new Point(1, -9, -70), new Point(-10,
		 * -9, 70)). // Teddy bear left // part nose. setEmission(new Color(255, 0, 0))
		 * .setMaterial(new Material().setKd(0.0).setKs(0.0).setShininess(90)),
		 * 
		 * new Triangle(new Point(0, -15, -40), new Point(-1, -9, -70), new Point(10,
		 * -9, 70)) // Teddy bear right // part nose .setEmission(new Color(255, 0, 0))
		 * .setMaterial(new Material().setKd(0.0).setKs(0.0).setShininess(90)));
		 */

		scene.lights.add(new DirectionalLight(new Color(70, 172, 21), new Vector(-1, 0, 0)));
		cameraBuilder.setImageWriter(new ImageWriter("DoF", 1200, 1200)).build().renderImage();
		cameraBuilder.build().writeToImage();
	}

}
