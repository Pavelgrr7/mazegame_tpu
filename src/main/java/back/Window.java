package back;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import scenes.MazeScene;
import scenes.MenuScene;
import scenes.Scene;

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
    private ImGUILayer imGUILayer;

    private static Scene currentScene = null;

    public float r, g, b, a;



    private Window() {
        width = 1400 ;
        height = 800 ;
        title = "Test";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }
    public static Window get(){
        if (Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public static int getWidth() {
        return get().width;
    }
    public static int getHeight() {
        return get().height;
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
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) ->{
            Window.setWidth(newWidth);
            Window.setHeigth(newHeight);
        });

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1); //v-sync

        glfwShowWindow(glfwWindow);
        GL.createCapabilities();
        //System.out.println("Debug #2");
        this.imGUILayer = new ImGUILayer(glfwWindow);
        this.imGUILayer.initImGui();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        Window.changeScene(0);
    }

    private static void setHeigth(int newHeight) {
        get().height = newHeight;
    }
    private static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public void loop() {
        float startTime =(float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);


            if (dt >= 0) { currentScene.update(dt); }
            this.imGUILayer.update(dt, currentScene);

            if (KeyboardListener.isKeyPressed(GLFW_KEY_E)) {System.out.println("E IS PRESSED");}
            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - startTime;
            startTime = endTime;
        }
        currentScene.saveExit();
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new MenuScene();
                break;

            case 1:
                currentScene = new MazeScene();
                break;

            default:
                assert false: "Unknown scene " + newScene;
                break;
        }
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }
}
