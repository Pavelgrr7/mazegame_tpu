package editor;

import imgui.ImGui;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;

public class MenuBar {

    private boolean deadMenuBar = false;

    public void imgui() {
        if (deadMenuBar) return;
        ImGui.beginMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Save", "Ctrl+S")) {
                EventSystem.notify(new Event(EventType.SaveLevel));
            }

            if (ImGui.menuItem("Load", "Ctrl+O")) {
                EventSystem.notify(new Event(EventType.LoadLevel));
            }

            ImGui.endMenu();
        }

        ImGui.endMenuBar();
    }

    public boolean isDeadMenuBar() {
        return deadMenuBar;
    }
    public void setDeadMenuBar(boolean b) {
        deadMenuBar = b;
    }
}