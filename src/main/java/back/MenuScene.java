package back;

import org.lwjgl.system.CallbackI;

import java.awt.event.KeyEvent;

public class MenuScene extends Scene{

    private boolean isSceneChanging = false;
    private float sceneChangeTime = 1.5f;

    public MenuScene() {
        System.out.println("MenuScene");
    }

    @Override
    public void update(float dt) {

        if (!isSceneChanging && KeyboardListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            isSceneChanging = true;
        }

        if (isSceneChanging && sceneChangeTime > 0) {
            sceneChangeTime -= dt;
            Window.get().r -= dt  * 5.0f;
            Window.get().g -= dt  * 5.0f;
            Window.get().b -= dt  * 5.0f;
        } else if (isSceneChanging) {
            Window.changeScene(1);
        }
    }
}
