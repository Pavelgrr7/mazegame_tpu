package scenes;

import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import back.*;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import org.joml.Vector2f;

import physics2d.components.Box2DCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;
import util.AssetPool;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class EditorSceneInitializer extends SceneInitializer {

    private Spritesheet sprites;

    private GameObject levelEditorStuff;

    public EditorSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        sprites = AssetPool.getSpritesheet("assets/images/blocksheet.png");
        levelEditorStuff = scene.createGameObject("Level Editor");
        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new KeyControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        scene.addGameObjectToScene(levelEditorStuff);
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/blocksheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/blocksheet.png"),
                        16, 16, 24, 0));
        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        17, 27, 13, 0));
        AssetPool.addSpritesheet("assets/images/spritesheet2.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet2.png"),
                        20, 16, 1, 0));
        AssetPool.addSound("assets/sounds/cave.ogg", false);
        AssetPool.addSound("assets/sounds/pipe.ogg", false);
        AssetPool.addSound("assets/sounds/main_theme_overworld.ogg", true);
        AssetPool.addSound("assets/sounds/hurt.ogg", false);


        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRenderer.class) != null) {

                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));

                }
            }
            if (g.getComponent(StateMachine.class) != null) {
                StateMachine stateMachine = g.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }

    }

    @Override
    public void imgui() {
        if (Window.get().getPlay()) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                EventSystem.notify(new Event(EventType.StartPlay));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Window.get().getPlay() + "play!!!!!!");
        ImGui.begin("Level Editor Stuff");
        levelEditorStuff.imgui();
        ImGui.end();

        ImGui.begin("Components");
        //if (Window.getImguiLayer().isGuiDestroyed()) {ImGui.end(); return;}

        if (ImGui.beginTabBar("WindowTabBar")) {
            if (ImGui.beginTabItem("Floor Blocks")) {

                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 0; i < sprites.size(); i++) {
                    // todo change when I will get my actual sprite sheet
                    //--------------------
                    if (i >= 4) continue;
                    //--------------------
                    Sprite sprite = sprites.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 4;
                    float spriteHeight = sprite.getHeight() * 4;
                    int id = sprite.getTexId();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);

                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }

                ImGui.endTabItem();

            }
            if (ImGui.beginTabItem("Solid Blocks")) {
                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 4; i < 24; i++) {

                    Sprite sprite = sprites.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 4;
                    float spriteHeight = sprite.getHeight() * 4;
                    int id = sprite.getTexId();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                        Rigidbody2D rb = new Rigidbody2D();
                        rb.setBodyType(BodyType.Static);
                        object.addComponent(rb);
                        Box2DCollider b2d = new Box2DCollider();
                        b2d.setHalfSize(new Vector2f(0.25f, 0.25f));
                        object.addComponent(b2d);
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Entities")) {
                int uid = 0;
                Spritesheet playerSprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");
                Sprite sprite = playerSprites.getSprite(0);
                float spriteWidth = sprite.getWidth() * 4;
                float spriteHeight = sprite.getHeight() * 4;
                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generatePlayer();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                Spritesheet items = AssetPool.getSpritesheet("assets/images/spritesheet2.png");
                sprite = items.getSprite(0);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateGhost();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Sounds")) {
                Collection<Sound> sounds = AssetPool.getAllSounds();
                for (Sound sound : sounds) {
                    File tmp = new File(sound.getFilepath());
                    if (ImGui.button(tmp.getName())) {
                        if (!sound.isPlaying()) sound.play();
                        else sound.stop();
                    }
                    if (ImGui.getContentRegionAvailX() > 100) ImGui.sameLine();
                }
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }

        if (ImGui.button("Menu")) {
            System.out.println("Menu loaded");
            EventSystem.notify(new Event(EventType.LoadMenu));
        }

        ImGui.end();
    }

    @Override
    public boolean isMenu() {
        return false;
    }
}