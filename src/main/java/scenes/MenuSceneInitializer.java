package scenes;

import back.MouseListener;
import back.Window;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.TimeUnit;

public class MenuSceneInitializer extends SceneInitializer {

    private int textureID;
    private boolean dead = true;

    public MenuSceneInitializer() {
        textureID = loadTexture("assets/images/menu1.png");
        this.dead = false;
    }

    public void renderMenu(int width, int height) {
        int x = (int)MouseListener.getX();
        int y = (int)MouseListener.getY();
        if ((160 < x) && (x < 280) && MouseListener.mouseButtonDown(0)) {
            if ((y > 214) && (y < 250) ) {
                EventSystem.notify(new Event(EventType.LoadLevel));
                Window.get().setPlay(true);
//                System.out.println(play + "is now true !!!");
            } else if ((y > 324) && (y < 360) ) {
                System.out.println("Editor!");
                EventSystem.notify(new Event(EventType.LoadLevel));
            } else if ((y > 424) && (y < 470)) {
                System.out.println("Properties!");
            } else if ((y > 536) && (y < 575)) {
                System.out.println("Exit!");
                Window.get().closeWindow();
            }
        }

    }

    private int loadTexture(String filePath) {
        int textureID;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load(filePath, width, height, channels, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load image: " + filePath);
            }

            textureID = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

            // Задаем параметры текстуры
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

            // Загружаем текстуру в OpenGL
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width.get(), height.get(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

            // Освобождаем память изображения
            STBImage.stbi_image_free(image);
        }

        return textureID;
    }

    @Override
    public void init(Scene scene) {

    }

    @Override
    public void loadResources(Scene scene) {

    }

    @Override
    public void imgui() {

    }

    @Override
    public boolean isMenu() {
        return true;
    }

    public void destroy() {
        this.dead = true;
    }

    public boolean isDead() {
        return this.dead;
    }
}


//
//public class MenuSceneInitializer extends SceneInitializer{
//
//    private MenuObject menuStuff;
//
//    public MenuSceneInitializer() {
//    }
//
//    @Override
//    public void renderMenu() {
//
//    }
//
//    public void init(Scene scene) {
//        Window.getScene().setMenu(true);
//        menuStuff = scene.createMenuObject("Menu");
//        menuStuff.setNoSerialize();
//        menuStuff.addComponent(new MouseControls());
//        menuStuff.addComponent(new KeyControls());
//        scene.addMenuObjectToScene(menuStuff);
//    }
//
//    public void loadResources(Scene scene) {
//        AssetPool.getShader("assets/shaders/default.glsl");
//
//        AssetPool.addSpritesheet("assets/images/menusheet.png",
//                new Spritesheet(AssetPool.getTexture("assets/images/menusheet.png"),
//                        38, 16, 1, 0));
//        Spritesheet buttons = AssetPool.getSpritesheet("assets/images/menusheet.png");
//        generateSpriteObject(buttons.getSprite(0), 1.25f, 1.25f);
//
//        for (MenuObject m : scene.getMenuObjects()) {
//            if (m.getComponent(SpriteRenderer.class) != null) {
//                SpriteRenderer spr = m.getComponent(SpriteRenderer.class);
//                if (spr.getTexture() != null) {
//                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
//                }
//            }
//        }
//    }
//
//    public void imgui() {
//        ImGui.begin(" ", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize );
//
//        if (ImGui.button("Play!")) {
//            EventSystem.notify(new Event(EventType.LoadLevel));
//            EventSystem.notify(new Event(EventType.GameEngineStartPlay));
//        }
//        if (ImGui.button("Editor")) {
//            EventSystem.notify(new Event(EventType.LoadLevel));
//        }
//        ImGui.end();
//    }
//
//    @Override
//    public boolean isMenu() {
//        return true;
//    }
//
//    public static MenuObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
//        MenuObject button = Window.getScene().createMenuObject("Menu_Object_Gen");
//        button.transform.scale.x = sizeX;
//        button.transform.scale.y = sizeY;
//        SpriteRenderer renderer = new SpriteRenderer();
//        renderer.setSprite(sprite);
//        button.addComponent(renderer);
//
//        return button;
//    }
//}