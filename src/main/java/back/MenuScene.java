package back;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private SpriteRenderer obj2sr;
    public MenuScene() {

        System.out.println("MenuScene");
    }
    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        sprites = AssetPool.getSpriteSheet("assets/images/sheet_shroom.png");
        obj1 = new GameObject("Obj 1",
                new Transform(
                        new Vector2f(100, 100),
                        new Vector2f(100, 100)),
                0
        );
        SpriteRenderer obj1Sprite = new SpriteRenderer();
        //obj1sp.setTexture(AssetPool.getTexture("assets/images/col1.png")
        //));
        //obj1sp.setTexCords();
        obj1Sprite.setColor(new Vector4f(1,0,0,1));
        //obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        obj1.addComponent((obj1Sprite));
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;
        GameObject obj2 = new GameObject("Obj 2",
                new Transform(
                        new Vector2f(140, 100),
                        new Vector2f(150, 150)),
                -1
        );
        obj2sr = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2sr.setSprite(obj2Sprite);
        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/col2.png")
        );
        obj2.addComponent(obj2sr);
        this.addGameObjectToScene(obj2);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Component.class, new GameObjectDeserializer())
                .create();
        String serialized = gson.toJson(obj1);
        System.out.println(serialized);

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

//        System.out.println(gson.toJson(obj2sr));
        //String serialized = gson.toJson();
        //System.out.println(serialized);

        //GameObject obj = gson.fromJson(se);
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
