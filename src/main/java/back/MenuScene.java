package back;


import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class MenuScene extends Scene {

    private GameObject obj1;
    private SpriteSheet sprites;

    public MenuScene() {

        System.out.println("MenuScene");
    }
    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        sprites = AssetPool.getSpriteSheet("assets/images/sheet_shroom.png");
        obj1 = new GameObject("Obj - 1",
                new Transform(
                        new Vector2f(100, 100),
                        new Vector2f(100, 100)),
                0
        );
        //obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        obj1.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/images/col1.png")
        )));
        this.addGameObjectToScene(obj1);
        GameObject obj2 = new GameObject("Obj - 2",
                new Transform(
                        new Vector2f(140, 100),
                        new Vector2f(150, 150)),
                -1
        );
        obj2.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/images/col2.png")
        )));
        this.addGameObjectToScene(obj2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.getTexture("assets/images/test.jpg");
        AssetPool.addSpriteSheet("assets/images/sheet_shroom.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/sheet_shroom.png"),
                        16, 16, 5, 0)
        );
    }

    //    private int spriteIndex = 0;
//    private float spriteFlipTime = 0.2f;
//    private float spriteFlipTimeLeft = 0.0f;
    @Override
    public void update(float dt) {
        //obj1.transform.position.x += 10 * dt;
//        System.out.println("FPS: " + 1.0f / dt);
//        spriteFlipTimeLeft -= dt;
//        if (spriteFlipTimeLeft <= 0) {
//            spriteFlipTimeLeft = spriteFlipTime;
//            spriteIndex++;
//            if (spriteIndex > 4) {
//                spriteIndex = 0;
//            }
//            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
//
//        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imgui(){
        ImGui.begin("Тест");
        ImGui.text("bla bla");
        ImGui.end();
    }
}
