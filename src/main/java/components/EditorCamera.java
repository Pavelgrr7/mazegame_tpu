package components;

import back.Camera;
//import back.KeyListener;
import back.KeyListener;
import back.MouseListener;
import editor.GameViewWindow;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class EditorCamera extends Component {

    private float dragDebounce = 0.32f;

    private Camera levelEditorCamera;
    private Vector2f clickOrigin;
    private boolean reset = false;
    private float dragSensitivity = 32f;
    private float scrollSensitivity = 0.1f;
    private float previousScrollVal = -1f;
    private GameViewWindow gameViewWindow;

    public EditorCamera(Camera levelEditorCamera) {
        this.gameViewWindow = new GameViewWindow();
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void editorUpdate(float dt) {
//        System.out.println("Camera updating");
//        System.out.printf("%s %s\n", MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE), (dragDebounce > 0));
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce > 0) {
            this.clickOrigin = new Vector2f(MouseListener.getWorldX(), MouseListener.getWorldY());
//            System.out.println("Click origin: " + this.clickOrigin);
            dragDebounce -= dt;
            return;
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            Vector2f mousePos = new Vector2f(MouseListener.getWorldX(), MouseListener.getWorldY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);
            levelEditorCamera.position.sub(delta.mul(dt).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, dt);
        }

        if (dragDebounce <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            dragDebounce = 0.1f;
        }

        if (MouseListener.getScrollY() != 0.0f) {
            if (GameViewWindow.getWantCaptureMouse()) {
                previousScrollVal = MouseListener.getScrollY();
                float addValue = (float) Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensitivity),
                        1 / levelEditorCamera.getZoom());
                addValue *= -Math.signum(MouseListener.getScrollY());
                levelEditorCamera.addZoom(addValue);
            }
            MouseListener.setScrollY(0.0f);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_ENTER)) {
            reset = true;
        }


        if (reset) {
            //levelEditorCamera.position.lerp(new Vector2f(), lerpTime);
            levelEditorCamera.setZoom(this.levelEditorCamera.getZoom() +
                    ((1.0f - levelEditorCamera.getZoom())));
            //this.lerpTime += 0.1f * dt;
            if (Math.abs(levelEditorCamera.position.x) <= 5.0f &&
                    Math.abs(levelEditorCamera.position.y) <= 5.0f) {
                //this.lerpTime = 0.0f;
                levelEditorCamera.position.set(0f, 0f);
                this.levelEditorCamera.setZoom(1.0f);

            }
            reset = false;
        }
    }
}
