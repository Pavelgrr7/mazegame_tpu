package back;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardListener {
    private static KeyboardListener inst;
    private boolean[] keyPressed = new boolean[180];

    private KeyboardListener() {

    }

    public static KeyboardListener get() {
        if (inst == null) {
            inst = new KeyboardListener();
        }
        return inst;
    }

    public static void keyCallBack(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode < get().keyPressed.length) {
            return get().keyPressed[keyCode];
        } else { return false; }
    }
}
