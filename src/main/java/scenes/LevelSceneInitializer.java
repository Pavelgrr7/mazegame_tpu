package scenes;

import components.*;
import back.*;

import util.AssetPool;

public class LevelSceneInitializer extends SceneInitializer {

    public LevelSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        Spritesheet sprites = AssetPool.getSpritesheet("assets/images/blocksheet2.png");
        GameObject cameraObject = scene.createGameObject("Game Camera");
        cameraObject.addComponent(new GameCamera(scene.camera()));
        cameraObject.start();
        scene.addGameObjectToScene(cameraObject);
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/blocksheet2.png",
                new Spritesheet(AssetPool.getTexture("assets/images/blocksheet2.png"),
                        16, 16, 12, 0));
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

    }
}