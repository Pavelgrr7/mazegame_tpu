package back;

import observers.EventSystem;
import observers.Observer;
import observers.events.Event;
import observers.events.EventType;
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
import fonts.Batch;
import fonts.CFont;
import graphics.Shader;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window implements Observer {
    private int width, height;
    private final String title;
    private long glfwWindow;
    private ImGuiLayer imguiLayer;
//    private ImGuiMenu imGuiMenu;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;

    boolean initialized = false;

    private static Window window = null;

    //OpenAL: vars for sound handling
    private long audioContext;
    private long audioDevice;

    private static MenuSceneInitializer menuScene;
    private static Scene currentScene;
    private boolean runtimePlay = false;
    private CFont font;
    private Batch batch = new Batch();
    private boolean play;

    private Window() {
        this.width = 1280;
        this.height = 720;
        this.title = "Maze";
        EventSystem.addObserver(this);
        init();
        font = new CFont("assets/fonts/HomeVideo.ttf", 64);
    }

    public static void changeScene(SceneInitializer sceneInitializer) {
        if (sceneInitializer instanceof EditorSceneInitializer) get().play = true;
        System.out.printf("change scene: \n", sceneInitializer);
        if (currentScene != null) {
            currentScene.destroy();
            if (sceneInitializer instanceof MenuSceneInitializer) {
                get().imguiLayer.getGameViewWindow().destroy();
                get().imguiLayer.getHierarchyWindow().setDeadWindow(true);

                get().imguiLayer = null;
                get().runtimePlay = false;

            }

        }
//        else if (menuScene != null && !menuScene.isDead()) menuScene.destroy();
        if (!(sceneInitializer instanceof MenuSceneInitializer)) {
            currentScene = new Scene(sceneInitializer);
            currentScene.load();
            currentScene.init();
//            currentScene.isEditor();
            System.out.println("Starting editor!");
            currentScene.startGame();
        } else {
            menuScene = new MenuSceneInitializer();

        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public boolean getPlay() {
        return get().play;
    }

    public static Scene getScene() {
        return currentScene;
    }

    public boolean runtimePlay() {
        return this.runtimePlay;
    }

    public static Physics2D getPhysics() {
        return currentScene.getPhysics();
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

//        init();

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
        glfwInit();

        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        if (currentScene != null) {
            glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
            glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
            glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
            glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        } else {
            glfwSetCursorPosCallback(glfwWindow, MouseListener::mouseMenuPosCallback);
            glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseMenuButtonCallback);
            glfwSetScrollCallback(glfwWindow, MouseListener::mouseMenuScrollCallback);
        }
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

        if (currentScene != null) {
            this.imguiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
            this.imguiLayer.initImGui();
            System.out.println("Imgui initialized");
        }
        EventSystem.notify(new Event(EventType.FirstLoadMenu));
    }

    public void loop() {
        int fontWidth = 100;
        int fontHeight = 390;
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;
        boolean check = false;

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        if (currentScene == null) {
            fonts.Shader fontShader = new fonts.Shader("assets/shaders/fontShader.glsl");

            batch.shader = fontShader;
            batch.font = font;
            batch.initBatch();
        }

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Render pass 1. Render to picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, 1280 , 720);
            glClearColor(0.0f, 0.0f, 0.6f, 0.4f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            if (currentScene != null) {
//                System.out.println("currentscene is being rendered");
                currentScene.render();
            } else {
                menuScene.renderMenu(fontWidth, fontHeight);
            }
//            if (currentScene == null) {
//                menuScene.renderMenu(fontWidth, fontHeight);
//            }


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
                if (currentScene != null && !check) {
                    pickingTexture.init(0,0);
                    pickingTexture = new PickingTexture(1280 , 720);
                    check = true;
                    System.out.println("Layer initialized");
                    this.imguiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
                    this.imguiLayer.initImGui();
                    initialized = true;
                }
                if (currentScene != null) {
                    currentScene.setMenu(false);
                    if (runtimePlay && initialized) {

//                        System.out.println("updating scene");
//                        currentScene.editorUpdate(dt);
                        currentScene.update(dt);
                        currentScene.render();
                    } else if (currentScene.isEditor()) {
//                        System.out.println("editor update");
                        currentScene.editorUpdate(dt);
                        currentScene.render();
                    }
                }
            }

            if (currentScene != null) {
//                System.out.println("updating layer");
                this.imguiLayer.update(dt, currentScene);
            }
            MouseListener.endFrame();
            KeyListener.endFrame();
            this.framebuffer.unbind();
            glfwSwapBuffers(glfwWindow);

            batch.addText("Играть!",fontWidth , fontHeight , 0.4f, 0xFF00AB0);
            batch.addText("Редактор", fontWidth, fontHeight - 90, 0.4f, 0xFF00AB0);
            batch.addText("Настройки", fontWidth, fontHeight - 180, 0.4f, 0xFF00AB0);
            batch.addText("Выход", fontWidth, fontHeight - 270, 0.4f, 0xAA01BB);

            batch.flushBatch();
            glfwPollEvents();

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        if (initialized) {
            initialized = false;
            currentScene.removeGameCamera();
            currentScene.save();
        }
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

    public void closeWindow() {
       glfwSetWindowShouldClose(glfwWindow, true);
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
            case FirstLoadMenu:
                System.out.println("First Menu!");
                Window.changeScene(new MenuSceneInitializer());
                break;
            case LoadMenu:
                currentScene.save();
                currentScene.setMenu(true);
                System.out.println("Menu!");
                Window.changeScene(new MenuSceneInitializer());
                break;
            case GameEngineStartPlay:
                System.out.println("Starting play");
                this.runtimePlay = true;
                imguiLayer.getHierarchyWindow().setDeadWindow(true);
                imguiLayer.getMenuBar().setDeadMenuBar(true);
                currentScene.save();
                Window.changeScene(new LevelSceneInitializer());
                break;
            case StartPlay:
                this.runtimePlay = true;
                currentScene.setRuntime(true);
                imguiLayer.getHierarchyWindow().setDeadWindow(true);
                imguiLayer.getMenuBar().setDeadMenuBar(true);
                currentScene.save();
                Window.changeScene(new LevelSceneInitializer());
                break;
            case GameEngineStopPlay:
                System.out.println("Stop!");
                this.runtimePlay = false;
                imguiLayer.getMenuBar().setDeadMenuBar(false);
                imguiLayer.getHierarchyWindow().setDeadWindow(false);
                Window.changeScene(new EditorSceneInitializer());
                break;
            case LoadLevel:
                Window.changeScene(new EditorSceneInitializer());
                break;
            case SaveLevel:
                currentScene.removeGameCamera();
                currentScene.save();
                break;
            default:
                Window.changeScene(new MenuSceneInitializer());
                break;
        }
    }
}