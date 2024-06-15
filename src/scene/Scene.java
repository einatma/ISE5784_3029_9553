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
    public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return new SceneBuilder(name);
    }

    //================== SceneBuilder class ==================//

    /**
     * The SceneBuilder class is used for constructing Scene instances with a fluent API.
     */
    public static class SceneBuilder {
        private final Scene scene;

        public SceneBuilder(String name) {
            scene = new Scene(name);
        }

        //========= chaining method =========//

        /**
         * Sets the background color for the scene.
         *
         * @param background the background color to set
         * @return the SceneBuilder instance for chaining
         */
        public SceneBuilder setBackground(Color background) {
            scene.background = background;
            return this;
        }
        /**
         * Sets the ambient light for the scene.
         *
         * @param ambientLight the ambient light to set
         * @return the SceneBuilder instance for chaining
         */
        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            scene.ambientLight = ambientLight;
            return this;
        }
        /**
         * Sets the geometries for the scene.
         *
         * @param geometries the geometries to set
         * @return the SceneBuilder instance for chaining
         */
        public SceneBuilder setGeometries(Geometries geometries) {
            scene.geometries = geometries;
            return this;
        }
        /**
         * Builds the Scene instance.
         *
         * @return the constructed Scene instance
         */
        public Scene build() {
            return scene;
        }
    }
}