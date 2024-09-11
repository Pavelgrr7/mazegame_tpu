package back;

import editor.GameViewWindow;
import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import scenes.Scene;

import java.io.File;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class ImGuiPlaying {

        private long glfwWindow;

        // LWJGL3 renderer (SHOULD be initialized)
        private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
        private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();

        private GameViewWindow gameViewWindow;
        boolean isGuiDestroyed = true;

        public ImGuiPlaying(long glfwWindow) {
            this.isGuiDestroyed = false;
            this.glfwWindow = glfwWindow;
            this.gameViewWindow = new GameViewWindow();
        }

        // Initialize Dear ImGui.
        public void initImGui() {
            // IMPORTANT!!
            // This line is critical for Dear ImGui to work.
            ImGui.createContext();

            // Initialize ImGuiIO config
            final ImGuiIO io = ImGui.getIO();
            io.setIniFilename("playing.ini"); // We don't want to save .ini file
            //io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
            io.setConfigFlags(ImGuiConfigFlags.DockingEnable);
//        io.setConfigFlags(ImGuiConfigFlags.ViewportsEnable);
//        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
            io.setBackendPlatformName("imgui_java_impl_glfw");
            // ------------------------------------------------------------
            // GLFW callbacks to handle user input
            glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
                if (action == GLFW_PRESS) {
                    io.setKeysDown(key, true);
                } else if (action == GLFW_RELEASE) {
                    io.setKeysDown(key, false);
                }

                io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
                io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
                io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
                io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

                if (!io.getWantCaptureKeyboard()) {
                    KeyListener.keyCallback(w, key, scancode, action, mods);
                }
            });

            glfwSetCharCallback(glfwWindow, (w, c) -> {
                if (c != GLFW_KEY_DELETE) {
                    io.addInputCharacter(c);
                }
            });

            glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
                final boolean[] mouseDown = new boolean[5];

                mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
                mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
                mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
                mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
                mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

                io.setMouseDown(mouseDown);

                if (!io.getWantCaptureMouse() && mouseDown[1]) {
                    ImGui.setWindowFocus(null);
                }

                if (!io.getWantCaptureMouse() || gameViewWindow.getWantCaptureMouse()) {
                    MouseListener.mouseButtonCallback(w, button, action, mods);
                }
            });

            glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
                io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
                io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
                if (!io.getWantCaptureMouse() || gameViewWindow.getWantCaptureMouse()) {
                    MouseListener.mouseScrollCallback(w, xOffset, yOffset);
                } else {
                    MouseListener.clear();
                }
            });

            io.setSetClipboardTextFn(new ImStrConsumer() {
                @Override
                public void accept(final String s) {
                    glfwSetClipboardString(glfwWindow, s);
                }
            });

            io.setGetClipboardTextFn(new ImStrSupplier() {
                @Override
                public String get() {
                    final String clipboardString = glfwGetClipboardString(glfwWindow);
                    if (clipboardString != null) {
                        return clipboardString;
                    } else {
                        return "";
                    }
                }
            });

            // ------------------------------------------------------------
            // Fonts configuration
            // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

            if (new File("assets/fonts/HomeVideo.ttf").isFile()) {
                final ImFontAtlas fontAtlas = io.getFonts();
                final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed
                fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());
                // Fonts merge example
                fontConfig.setPixelSnapH(true);
                fontAtlas.addFontFromFileTTF("assets/fonts/HomeVideo.ttf", 10, fontConfig);
                fontConfig.destroy(); // After all fonts were added we don't need this config more
            }
            // ------------------------------------------------------------
            // Use freetype instead of stb_truetype to build a fonts texture
            //ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting);

            // Method initializes LWJGL3 renderer.
            // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
            // ImGui context should be created as well.
            imGuiGlfw.init(glfwWindow, false);
            imGuiGl3.init("#version 330 core");
        }

        public void update(float dt, Scene currentScene) {
            startFrame(dt);
//        System.out.println(currentScene.isMenu());
            System.out.println(" player imgui");
            currentScene.imgui();
            //System.out.printf("%s %s %s gui playin \n", currentScene.isEditor(), currentScene.isMenu(), currentScene.isPlayScene());

            if (gameViewWindow != null) {
                gameViewWindow.setDeadWindow(false);
                System.out.println("not null" + gameViewWindow);
                if (!gameViewWindow.isDead()) gameViewWindow.imgui(true); }
            else {gameViewWindow = new GameViewWindow();
                System.out.println("null" + gameViewWindow);}
            System.out.println("circle ended");
            endFrame();
        }

        private void startFrame(final float deltaTime) {
            imGuiGlfw.newFrame();
            ImGui.newFrame();
        }

        private void endFrame() {
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
            glViewport(0, 0, Window.getWidth(), Window.getHeight());
            glClearColor(0, 0, 0, 0);
            glClear(GL_COLOR_BUFFER_BIT);
            ImGui.render();
            // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
            // At that moment ImGui will be rendered to the current OpenGL context.
            imGuiGl3.renderDrawData(ImGui.getDrawData());
//        long backupWindowPtr = glfwGetCurrentContext();
//        ImGui.updatePlatformWindows();
//        ImGui.renderPlatformWindowsDefault();
//        glfwMakeContextCurrent(backupWindowPtr);
        }

        // If you want to clean a room after yourself - do it by yourself
        public void destroyImGui() {
            imGuiGl3.dispose();
            ImGui.destroyContext();
        }

        public GameViewWindow getGameViewWindow() {
            return this.gameViewWindow;
        }

        public void destroyGui() {
//        ImGui.destroyPlatformWindows();
            if (gameViewWindow != null)
                this.gameViewWindow.setDeadWindow(true);
            this.gameViewWindow = null;
            boolean isGuiDestroyed = true;
        }

        public boolean isGuiDestroyed() {
            return isGuiDestroyed;
        }
    }
