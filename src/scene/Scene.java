package scene;

import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;
import geometries.Geometries;

import java.util.LinkedList;
import java.util.List;

/**
 * The Scene class represents a scene in a 3D space with geometries, ambient light, and background color.
 */
public class Scene {
    public String name;
    public Color background = Color.BLACK;
    public  AmbientLight ambientLight = AmbientLight.NONE;
    public  Geometries geometries = new Geometries();
    public List<LightSource> lights = new LinkedList<>();
    /**
     * Constructs a Scene with the given name.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }



    /**
     * Sets the ambient light for the scene.
     *
     * @param ambientLight the ambient light to set
     * @return a SceneBuilder instance for chaining
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight=ambientLight;
        return this;
    }
    /**
     * Sets the geometries of the scene.
     *
     * @param geometries the geometries to set
     * @return the Scene object itself for chaining
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
    /**
     * Sets the background color of the scene.
     *
     * @param background the background color to set
     * @return the Scene object itself for chaining
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }


    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }






}