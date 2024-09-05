package back;

import observers.EventSystem;
import observers.Observer;
import observers.events.Event;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;
import graphics.*;
import physics2d.Physics2D;
import scenes.*;
import util.AssetPool;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Observer {
    private int width, height;
    private final String title;
    private long glfwWindow;
    private ImGuiLayer imguiLayer;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;

    private static Window window = null;

    //OpenAL: vars for sound handling
    private long audioContext;
    private long audioDevice;

    private static Scene currentScene;
    private boolean runtimePlay = false;
    private boolean test;

    private Window() {
        this.width = 1280;
        this.height = 720;
        this.title = "Maze";
        EventSystem.addObserver(this);
    }

    public static void changeScene(SceneInitializer sceneInitializer, boolean menu) {
        if (currentScene != null) {
            currentScene.destroy();
        }
        //getImguiLayer().getPropertiesWindow().setActiveGameObject(null);

        currentScene = new Scene(sceneInitializer);
        currentScene.load();
        currentScene.init();
        System.out.printf("%s %s\n",currentScene.isMenu(), currentScene.isEditor());
        if (currentScene.isEditor()) {
            System.out.println("Starting editor!");
            currentScene.startGame();

        } else {
            System.out.println("Starting menu!");
            currentScene.startMenu();
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public static Physics2D getPhysics() { return currentScene.getPhysics(); }


    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        //Destroy the audio context
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(glfwWindow);
        //Setup audio context: initialize the audio device
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);
        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapibilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapibilities);

        if (!alCapabilities.OpenAL10) {
            assert false : "Audio librart not supported.";
        }

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.framebuffer = new Framebuffer(1280 , 720);
        this.pickingTexture = new PickingTexture(1280 , 720);
        glViewport(0, 0, 1280 , 720);

        this.imguiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
        this.imguiLayer.initImGui();
        Window.changeScene(new EditorSceneInitializer(), false);
        //Window.changeScene(new MenuSceneInitializer(), true);
    }

    public void loop() {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            // Render pass 1. Render to picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, 1280 , 720);
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            // Render pass 2. Render actual game
            DebugDraw.beginFrame();

            this.framebuffer.bind();
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                DebugDraw.draw();

                Renderer.bindShader(defaultShader);
                if (runtimePlay) {currentScene.update(dt);}
                else if (!this.imguiLayer.isGuiDestroyed()) {currentScene.editorUpdate(dt);}
                else {currentScene.menuUpdate(dt);}

                currentScene.render();
            }
            this.framebuffer.unbind();

            this.imguiLayer.update(dt, currentScene);

            MouseListener.endFrame();
            KeyListener.endFrame();

            glfwSwapBuffers(glfwWindow);
            //

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        currentScene.removeGameCamera();
        currentScene.save();
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    public static Framebuffer getFramebuffer() {
        return get().framebuffer;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }

    public static ImGuiLayer getImguiLayer() {
        return get().imguiLayer;
    }

    @Override
    public void onNotify(Event event) {
        switch (event.type) {
            case LoadMenu:
                currentScene.setMenu(true);
                test = true;
                System.out.println("Menu!");
//                this.imguiLayer.destroyGui();
//                currentScene.save();
                Window.changeScene(new MenuSceneInitializer(), true);
                break;
            case GameEngineStartPlay:
                System.out.println("Starting play");
                this.runtimePlay = true;
                currentScene.save();
                Window.changeScene(new LevelSceneInitializer(), false);
                break;
            case GameEngineStopPlay:
                System.out.println("Stop!");
                this.runtimePlay = false;
                Window.changeScene(new EditorSceneInitializer(), false);
                break;
            case LoadLevel:
                Window.changeScene(new EditorSceneInitializer(), false);
                break;
            case SaveLevel:
                currentScene.removeGameCamera();
                currentScene.save();
                break;
            default:
                Window.changeScene(new MenuSceneInitializer(), true);
                break;
        }
    }
    public ImGuiLayer getImGui(){
        return this.imguiLayer;
    }
}