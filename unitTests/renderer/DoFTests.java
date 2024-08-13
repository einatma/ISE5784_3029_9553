package renderer;

import geometries.*;
import lighting.DirectionalLight;
import org.junit.jupiter.api.Test;

import lighting.AmbientLight;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Unit tests for Depth of Field (DoF) rendering in the renderer package.
 */
public class DoFTests {

	/**
	 * Test for rendering a scene with depth of field (DoF) effect. This test sets
	 * up a scene with multiple spheres at varying distances and a plane as the
	 * background. It configures the camera with DoF settings including focal length
	 * and aperture radius, and verifies the rendering output to check the DoF
	 * effect.
	 */
	@Test
	public void testDepthOfField() {

		Scene scene = new Scene("DoF");



		final Camera.Builder cameraBuilder = Camera.getBuilder().setLocation(new Point(0, 0, 2500))
				.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setVpSize(200, 200).setVpDistance(850)
				.setDoFActive(true).setFocalSize(20, 1600, 200).setRayTracer(new SimpleRayTracer(scene))
				.setMultiThreading(3).setDebugPrint(0.1);
//		final Camera.Builder cameraBuilder1 = Camera.getBuilder().setLocation(new Point(0, 0, 2500))
//				.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setVpSize(200, 200).setVpDistance(850)
//				.setDoFActive(false).setFocalSize(20, 1600, 1).setRayTracer(new SimpleRayTracer(scene));

		AmbientLight ambientLight = new AmbientLight(new Color(30, 30, 30), 0.1);
		scene.setAmbientLight(ambientLight);

		Geometry plane = new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(0, 20, 20));
		plane.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKr(0.02));

		scene.geometries.add(

				plane,
				new Sphere(new Point(150, 100, 300), 75).setEmission(new Color(210, 4, 45)) // Most distant
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),

				new Sphere(new Point(100, 50, 500), 75).setEmission(new Color(222, 49, 99)) // Most distant
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),

				new Sphere(new Point(50, 0, 700), 75).setEmission(new Color(169, 92, 104)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),

				new Sphere(new Point(0, -50, 900), 75).setEmission(new Color(220, 20, 60)) // Middle Sphere
						.setMaterial(new Material().setKd(0.25).setKs(0.20).setShininess(20).setKt(0.5).setKr(0.3)),

				new Sphere(new Point(-50, -100, 1100), 75).setEmission(new Color(250, 160, 160)) // Right close
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),

				new Sphere(new Point(-100, -150, 1300), 75).setEmission(new Color(215, 0, 64)) // Closest Sphere
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)));
		//scene.geometries.makeBVH();


		scene.lights.add(new DirectionalLight(new Color(70, 172, 21), new Vector(-1, 0, 0)));

		//cameraBuilder1.setImageWriter(new ImageWriter("NoDoF", 1200, 1200)).build().renderImage().writeToImage();

		cameraBuilder.setImageWriter(new ImageWriter("DoF", 1200, 1200)).build().renderImage().writeToImage();

	}
}
