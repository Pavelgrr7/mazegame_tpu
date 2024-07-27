package scenes;


import back.*;
import components.Rigidbody;
import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

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

        this.camera = new Camera(new Vector2f(-250, 0));
        sprites = AssetPool.getSpriteSheet("assets/images/blocksheet.png");

        if (loadedLevel) {
            this.activeGameObject = gameObjects.get(0);
            return;
        }

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
        obj1.addComponent(new Rigidbody());
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

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.getTexture("assets/images/test.jpg");
        AssetPool.addSpriteSheet("assets/images/blocksheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/blocksheet.png"),
                        16, 16, 9, 0)
        );
        AssetPool.getTexture("assets/images/col2.png");
    }

    //    private int spriteIndex = 0;
//    private float spriteFlipTime = 0.2f;
//    private float spriteFlipTimeLeft = 0.0f;
    @Override
    public void update(float dt) {
        MouseListener.getOrthoX();

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
    public void imgui() {
        ImGui.begin("Тест");
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCords = sprite.getCords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCords[0].x, texCords[0].y, texCords[2].x, texCords[2].y)) {
                System.out.println("Button " + i + " clicked");
            }
            ImGui.popID();
            ImVec2 lastBtnPos = new ImVec2();
            ImGui.getItemRectMax(lastBtnPos);
            float lastBtnX2 = lastBtnPos.x;
            float nextBtnX2 = lastBtnX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.size() && nextBtnX2 < windowX2) {
                ImGui.sameLine();
            }
//        ImGui.text("bla bla");

        }
        ImGui.end();
    }

}
