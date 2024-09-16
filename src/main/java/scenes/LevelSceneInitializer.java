package scenes;

import components.*;
import back.*;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import util.AssetPool;

import java.util.concurrent.TimeUnit;
import java.io.FileWriter;
import java.io.IOException;
public class LevelSceneInitializer extends SceneInitializer {

    public LevelSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        Spritesheet sprites = AssetPool.getSpritesheet("assets/images/blocksheet.png");
        GameObject cameraObject = scene.createGameObject("Game Camera");
        cameraObject.addComponent(new GameCamera(scene.camera()));
        cameraObject.start();
        scene.addGameObjectToScene(cameraObject);
        try {
            TimeUnit.MILLISECONDS.sleep(100);
            showNameInputPopup = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        showNameInputPopup();


    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/blocksheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/blocksheet.png"),
                        16, 16, 24, 0));
        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        17, 27, 13, 0));
        AssetPool.addSpritesheet("assets/images/spritesheet2.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet2.png"),
                        20, 16, 1, 0));
        AssetPool.addSound("assets/sounds/cave.ogg", false);
        AssetPool.addSound("assets/sounds/pipe.ogg", false);
        AssetPool.addSound("assets/sounds/main_theme_overworld.ogg", true);
        AssetPool.addSound("assets/sounds/hurt.ogg", false);


        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
            if (g.getComponent(StateMachine.class) != null) {
                StateMachine stateMachine = g.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }

    }


    public void addRecordToLeaderboard(String playerName, String completionTime) {
        try (FileWriter writer = new FileWriter("leaderboard.txt", true)) {
            writer.write(playerName + " " + completionTime + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean showNameInputPopup = false;
    private ImString playerName = new ImString(64);

    // Метод для показа модального окна
    public void showNameInputPopup() {
        if (showNameInputPopup) {
            ImGui.openPopup("Enter Player Name");
            showNameInputPopup = false; // Открываем только один раз
        }

        if (ImGui.beginPopupModal("Enter Player Name", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoCollapse)) {
            ImGui.text("Please enter your name:");

            // Поле для ввода имени
            if (ImGui.inputText("##playerName", playerName)) {
                // Теперь playerName содержит введенное пользователем имя
                System.out.println("Player name: " + playerName.get());
            }

            // Кнопка "ОК"
            if (ImGui.button("OK", 100, 30)) {
                if (!playerName.get().isEmpty()) {
                    System.out.println("Player name: " + playerName.get());
                    ImGui.closeCurrentPopup(); // Закрыть окно
                } else {
                    ImGui.textColored(1, 0, 0, 1, "Name cannot be empty!"); // Вывод ошибки
                }
            }

            ImGui.endPopup();
        }
        }

    @Override
    public void imgui() {

    }



    @Override
    public boolean isMenu() {
        return false;
    }
}