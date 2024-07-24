package back;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.CallbackI;
import util.Time;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private String title;
    private long glfwWindow;
    private static Window window = null;

    private static Scene currentScene = null;

    public float r, g, b, a;



    private Window() {
        width = 1280 ;
        height = 800 ;
        title = "Test";
        g = 0.1f;
        a = 1.0f;
    }
    public static Window get(){
        if (Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }
    public void run(){
        System.out.println("hello" + Version.getVersion() + "|");

        init();
        loop();
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
    public void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        //glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create");
        }
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mouseCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseBtnCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::scrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyboardListener::keyCallBack);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1); //v-sync

        glfwShowWindow(glfwWindow);
        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop() {
        float startTime = Time.getTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);


            if (dt >= 0) { currentScene.update(dt); }

            if (KeyboardListener.isKeyPressed(GLFW_KEY_E)) {System.out.println("E IS PRESSED");}
            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - startTime;
            startTime = endTime;
        }
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new MenuScene();
                currentScene.init();
                break;

            case 1:
                currentScene = new MazeScene();
                currentScene.init();
                break;

            default:
                assert false: "Unknown scene " + newScene;
                break;
        }

    }
}
