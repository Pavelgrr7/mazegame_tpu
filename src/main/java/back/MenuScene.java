package back;


import components.Sprite;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class MenuScene extends Scene{

    public MenuScene() {

        System.out.println("MenuScene");
    }

    @Override
    public void init() {

        this.camera = new Camera(new Vector2f());

        GameObject obj1 = new GameObject("Obj - 1",
                new Transform(
                        new Vector2f(100, 100),
                        new Vector2f(256, 256))
        );
        obj1.addComponent(new SpriteRenderer( new Sprite(
                AssetPool.getTexture("assets/images/shroom01.png"))
        ));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Obj - 2",
                new Transform(
                        new Vector2f(400, 100),
                        new Vector2f(256, 256))
        );
        obj2.addComponent(new SpriteRenderer( new Sprite(
                AssetPool.getTexture("assets/images/test.png"))
        ));
        this.addGameObjectToScene(obj2);




        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.getTexture("assets/images/test.jpg");
    }


    @Override
    public void update(float dt) {

        System.out.println("FPS: " + 1.0f / dt);

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }
}
