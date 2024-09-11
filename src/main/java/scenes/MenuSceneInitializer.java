package scenes;

import back.*;
import back.Window;
import components.*;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import util.AssetPool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static back.Prefabs.generateSpriteObject;

public class MenuSceneInitializer extends SceneInitializer{

    private MenuObject menuStuff;
    private Spritesheet buttons;
    public MenuSceneInitializer() {
    }

    public void init(Scene scene) {
//        GameMenu gameMenu = new GameMenu();
//        gameMenu.createMenu();
//        GameTimer timer = new GameTimer(100);
//        Window.getImguiLayer().destroyGui();
        Window.getScene().setMenu(true);
        menuStuff = scene.createMenuObject("Menu");
        menuStuff.setNoSerialize();
        menuStuff.addComponent(new MouseControls());
        menuStuff.addComponent(new KeyControls());
//        menuStuff.addComponent(new EditorCamera(scene.camera()));
        scene.addMenuObjectToScene(menuStuff);
    }

    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/menusheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/menusheet.png"),
                        38, 16, 1, 0));
//        AssetPool.addSpritesheet("assets/images/spritesheet.png",
//                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
//                        17, 27, 13, 0));
//        AssetPool.addSpritesheet("assets/images/spritesheet2.png",
//                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet2.png"),
//                        20, 16, 1, 0));
        buttons = AssetPool.getSpritesheet("assets/images/menusheet.png");


        for (MenuObject m : scene.getMenuObjects()) {
            if (m.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = m.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
//            if (m.getComponent(StateMachine.class) != null) {
//                StateMachine stateMachine = m.getComponent(StateMachine.class);
//                stateMachine.refreshTextures();
//            }
        }
    }

    public void imgui() {

//        ImGui.begin("Level Editor Stuff");
//        menuStuff.imgui();

//        ImGui.end();
//        ImGui.begin("A");
        if (ImGui.begin("Components", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize |ImGuiWindowFlags.NoMove)) {
            //generateSpriteObject(buttons.getSprite(0), 2000.25f, 700.25f);
        }

        if (ImGui.button("Play!")) {

//            EventSystem.notify(new Event(EventType.LoadLevel));
//            Window.changeScene(new EditorSceneInitializer());
            EventSystem.notify(new Event(EventType.StartPlay));
//            Window.changeScene(new LevelSceneInitializer());
        }

        if (ImGui.button("Editor")) {
            Window.changeScene(new EditorSceneInitializer());
            Window.getScene().setMenu(false);
            Window.getScene().setPlaying(false);
            Window.getScene().setRunning(false);

//            EventSystem.notify(new Event(EventType.LoadLevel));
//
        }

        if (ImGui.button("Settings")) {
//            EventSystem.notify(new Event(EventType.LoadLevel));
//            EventSystem.notify(new Event(EventType.StartPlay));
//            Window.changeScene(new EditorSceneInitializer());
        }

        if (ImGui.button("Exit")) {
            Window.get().exit();
            //EventSystem.notify(new Event(EventType.LoadLevel));
//            Window.changeScene(new EditorSceneInitializer());
        }
        ImGui.end();
    }

    @Override
    public boolean isMenu() {
        return true;
    }

    @Override
    public boolean isEditor() {
        return false;
    }

    @Override
    public boolean isRunning() {
        return false;
    }


//    public class GameMenu {
//        public void createMenu() {
//            // Создаем основное окно (JFrame)
//            JFrame frame = new JFrame("Game Menu");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(400, 300);
//            frame.setLayout(new BorderLayout());
//
//            // Создаем панель для кнопок
//            JPanel panel = new JPanel();
//            panel.setLayout(new GridLayout(3, 1, 10, 10));
//
//            // Создаем кнопки
//            JButton startButton = new JButton("Start");
//            JButton settingsButton = new JButton("Settings");
//            JButton exitButton = new JButton("Exit");
//
//            // Добавляем кнопки на панель
//            panel.add(startButton);
//            panel.add(settingsButton);
//            panel.add(exitButton);
//
//            // Добавляем панель с кнопками на основное окно
//            frame.add(panel, BorderLayout.CENTER);
//
//            // Делаем окно видимым
//            frame.setVisible(true);
//
//            // Добавляем обработчики событий для кнопок
//            startButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    System.out.println("Start game clicked");
//                    // Здесь можно запустить игровую логику
//                }
//            });
//
//            settingsButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    System.out.println("Settings clicked");
//                    // Открыть меню настроек
//                }
//            });
//
//            exitButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    System.exit(0); // Закрыть приложение
//                }
//            });
//        }
//    }
}
