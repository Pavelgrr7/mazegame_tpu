package back;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener inst;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseBtnPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        scrollX = 0.0; scrollY = 0.0;
        xPos = 0.0; yPos = 0.0;
        lastX = 0.0; lastY = 0.0;
    }

    public static MouseListener get() {
        if (inst == null) {
            inst = new MouseListener();
        }
        return inst;
    }

    public static void mouseCallback( long window, double xpos, double ypos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().isDragging = get().mouseBtnPressed[0] || get().mouseBtnPressed[1] || get().mouseBtnPressed[2];
    }

    public static void mouseBtnCallback(long window, int btn, int action, int mods) {

        if (btn < get().mouseBtnPressed.length) {
            if (action == GLFW_PRESS) {
                get().mouseBtnPressed[btn] = true;
            } else if (action == GLFW_RELEASE) {
                get().mouseBtnPressed[btn] = false;
                get().isDragging = false;
            }
        }
    }

    public static void scrollCallback(long window, double dx, double dy) {
        get().scrollX = dx;
        get().scrollY = dy;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float)get().xPos;
    }
    public static float getY() {
        return (float)get().yPos;
    }

    public static float getDx() {
        return (float)(get().lastX - get().yPos);
    }
    public static float getDy() {
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float)(get().scrollX);
    }
    public static float getScrollY() {
        return (float)(get().scrollY);
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseBtnDown( int btn) {
        if (btn < get().mouseBtnPressed.length){
            return get().mouseBtnPressed[btn];
        } else { return false; }
    }
}
