package scenes;

import back.GameObject;
import back.Window;
import components.*;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import util.AssetPool;

import static back.Prefabs.generateSpriteObject;

public class PlayingSceneInitializer extends SceneInitializer {

    private Spritesheet sprites;

    private GameObject playingStuff;

    public PlayingSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        Window.getScene().setPlaying(true);
        Window.getScene().setMenu(false);
        playingStuff = scene.createGameObject("PlayingScene");
        playingStuff.setNoSerialize();
        playingStuff.addComponent(new MouseControls());
        playingStuff.addComponent(new KeyControls());
//        playingStuff.addComponent(new EditorCamera(scene.camera()));
        scene.addGameObjectToScene(playingStuff);

        Spritesheet sprites = AssetPool.getSpritesheet("assets/images/blocksheet.png");
        GameObject cameraObject = scene.createGameObject("Game Camera - Playing");
        cameraObject.addComponent(new GameCamera(scene.camera()));
        cameraObject.start();
        scene.addGameObjectToScene(cameraObject);
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
        if (ImGui.begin("test", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize |ImGuiWindowFlags.NoMove)) {
            //generateSpriteObject(sprites.getSprite(0), 2000.25f, 700.25f);
        }

        if (ImGui.button("Play!")) {
            System.out.println("play!");
//            EventSystem.notify(new Event(EventType.GameEngineStartPlay));
            EventSystem.notify(new Event(EventType.StartPlay));

        }

        if (ImGui.button("Editor")) {
            EventSystem.notify(new Event(EventType.LoadLevel));

        }

        if (ImGui.button("Settings")) {
//            EventSystem.notify(new Event(EventType.LoadLevel));
            EventSystem.notify(new Event(EventType.StartPlay));

        }

        if (ImGui.button("Exit")) {
            Window.get().exit();
        }
        ImGui.end();
    }

    @Override
    public boolean isMenu() {
        return false;
    }

    @Override
    public boolean isEditor() {
        return false;
    }

    @Override
    public boolean isRunning() {
        return true;
    }
}