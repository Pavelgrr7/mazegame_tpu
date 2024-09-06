package scenes;

import back.GameObject;
import back.ImGuiLayer;
import back.MenuObject;
import back.Window;
import components.*;
import imgui.ImGui;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import util.AssetPool;

public class MenuSceneInitializer extends SceneInitializer{

    private MenuObject menuStuff;

    public MenuSceneInitializer() {
    }

    public void init(Scene scene) {
//        Window.getImguiLayer().destroyGui();
        Window.getScene().setMenu(true);
        menuStuff = scene.createMenuObject("Menu");
        menuStuff.setNoSerialize();
        menuStuff.addComponent(new MouseControls());
        menuStuff.addComponent(new KeyControls());
//        menuStuff.addComponent(new EditorCamera(scene.camera()));
        scene.addMenuObjectToScene(menuStuff);
    }

    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/menusheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/menusheet.png"),
                        38, 16, 1, 0));
//        AssetPool.addSpritesheet("assets/images/spritesheet.png",
//                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
//                        17, 27, 13, 0));
//        AssetPool.addSpritesheet("assets/images/spritesheet2.png",
//                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet2.png"),
//                        20, 16, 1, 0));
        Spritesheet buttons = AssetPool.getSpritesheet("assets/images/menusheet.png");
        generateSpriteObject(buttons.getSprite(0), 1.25f, 1.25f);

        for (MenuObject m : scene.getMenuObjects()) {
            if (m.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = m.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
//            if (m.getComponent(StateMachine.class) != null) {
//                StateMachine stateMachine = m.getComponent(StateMachine.class);
//                stateMachine.refreshTextures();
//            }
        }
    }

    public void imgui() {
//        ImGui.begin("Level Editor Stuff");
//        menuStuff.imgui();

//        ImGui.end();
        ImGui.begin("Components");
//        ImGui.beginTabBar("");

        if (ImGui.button("Editor")) {
            EventSystem.notify(new Event(EventType.LoadLevel));
//            Window.changeScene(new EditorSceneInitializer());
        }

        ImGui.end();
    }

    @Override
    public boolean isMenu() {
        return true;
    }

    public static MenuObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        MenuObject button = Window.getScene().createMenuObject("Menu_Object_Gen");
        button.transform.scale.x = sizeX;
        button.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        button.addComponent(renderer);

        return button;
    }
}
