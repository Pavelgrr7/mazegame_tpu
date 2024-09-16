package editor;

import back.Sound;
import components.Leaderboard;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import back.MouseListener;
import back.Window;
import imgui.type.ImString;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import org.joml.Vector2f;
import util.AssetPool;

import java.util.concurrent.TimeUnit;

import static org.lwjgl.opengl.GL11.*;

public class GameViewWindow {

    private static float leftX;
    private static float rightX;
    private static float topY;
    private static float bottomY;
    private boolean isPlaying = false;
    private boolean deadWindow;


    public void imgui() {

        if (deadWindow) return;
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse
                | ImGuiWindowFlags.MenuBar);
        ImGui.beginMenuBar();
        if (ImGui.menuItem("Play", "", isPlaying, !isPlaying)) {
            isPlaying = true;
            //Sound mainTheme = AssetPool.getSound("assets/sounds/main_theme_overworld.ogg");
            //if (!mainTheme.isPlaying()) mainTheme.play();
            //else mainTheme.stop();
            EventSystem.notify(new Event(EventType.GameEngineStartPlay));
        }
        if (ImGui.menuItem("Stop", "", !isPlaying, isPlaying)) {
            isPlaying = false;
            //Sound mainTheme = AssetPool.getSound("assets/sounds/main_theme_overworld.ogg");
            //if (mainTheme.isPlaying()) mainTheme.stop();
            EventSystem.notify(new Event(EventType.GameEngineStopPlay));
        }

        ImGui.sameLine();

        if (ImGui.menuItem("Leaderboard", "")) {
            Leaderboard leaderboard = new Leaderboard();
            leaderboard.displayLeaderboard();
        }


        ImGui.endMenuBar();


        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();
        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        topY = topLeft.y + windowSize.y;

        int textureId = Window.getFramebuffer().getTextureId();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        MouseListener.setGameViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setGameViewportSize(new Vector2f(windowSize.x, windowSize.y));

        ImGui.end();
    }

    public static boolean getWantCaptureMouse() {
        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX &&
                MouseListener.getY() >= bottomY && MouseListener.getY() <= topY;
    }

    private ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if (aspectHeight > windowSize.y) {
            // We must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(),
                viewportY + ImGui.getCursorPosY());
    }

    public void destroy() {
        ImGui.destroyContext();
    }

    //    public void setDeadWindow(boolean b){
//        this.deadWindow = b;
//    }
    public boolean isDead() {
        return deadWindow;
    }

    public void imgui(boolean b) {
        if (deadWindow) return;

        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse
                | ImGuiWindowFlags.MenuBar );

        ImGui.beginMenuBar();

        if (ImGui.menuItem("Menu", "", isPlaying, !isPlaying)) {
            isPlaying = false;
            Sound mainTheme = AssetPool.getSound("assets/sounds/main_theme_overworld.ogg");
            if (mainTheme.isPlaying()) mainTheme.stop();
            //showNameInputPopup();
//            EventSystem.notify(new Event(EventType.GameEngineStopPlay));
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            EventSystem.notify(new Event(EventType.LoadMenu));
        }
        ImGui.sameLine();

        // Format and display the remaining time
        // method Timef returns time in "min:sec" format
        String formattedTime = Window.get().getTimer().getTimef();
        ImGui.text("                                         Time remaining: " + formattedTime);

        ImGui.sameLine();

        if (ImGui.menuItem("Leaderboard", "")) {
            Leaderboard leaderboard = new Leaderboard();
            leaderboard.displayLeaderboard();
        }

        ImGui.endMenuBar();

        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();
        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        topY = topLeft.y + windowSize.y;

        int textureId = Window.getFramebuffer().getTextureId();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        MouseListener.setGameViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setGameViewportSize(new Vector2f(windowSize.x, windowSize.y));

        ImGui.end();
    }

}