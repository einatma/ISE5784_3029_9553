package scene;

import lighting.AmbientLight;
import primitives.Color;
import geometries.Geometries;

public class Scene {
    public String name;
    public Color background = Color.BLACK;
    public  AmbientLight ambientLight = AmbientLight.NONE;
    public  Geometries geometries = new Geometries();

    public Scene(String name) {
        this.name = name;
    }

    public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return new SceneBuilder(name);
    }

    //================== SceneBuilder class ==================//
    public static class SceneBuilder {
        private final Scene scene;

        public SceneBuilder(String name) {
            scene = new Scene(name);
        }

        //========= chaining (שירשור) method =========//

        public SceneBuilder setBackground(Color background) {
            scene.background = background;
            return this;
        }

        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            scene.ambientLight = ambientLight;
            return this;
        }

        public SceneBuilder setGeometries(Geometries geometries) {
            scene.geometries = geometries;
            return this;
        }



        public Scene build() {
            return scene;
        }
    }
}