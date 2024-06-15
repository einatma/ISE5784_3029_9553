package scene;

import lighting.AmbientLight;
import primitives.Color;
import geometries.Geometries;
/**
 * The Scene class represents a scene in a 3D space with geometries, ambient light, and background color.
 */
public class Scene {
    public String name;
    public Color background = Color.BLACK;
    public  AmbientLight ambientLight = AmbientLight.NONE;
    public  Geometries geometries = new Geometries();
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
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }






}