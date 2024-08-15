package editor;

import back.GameObject;
import back.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneHierarchyWindow {

    public void imgui() {
        ImGui.begin("Scene Hierarchy");
        List<GameObject> gameObjects = Window.getScene().getGameObjects();
        int index = 0;
        for (GameObject go : gameObjects) {
            if (!go.doSerialization()) {
                continue;
            }
            ImGui.pushID(index);
            boolean treeNodeOpen = ImGui.treeNodeEx(
                    go.name,
                    ImGuiTreeNodeFlags.DefaultOpen |
                            ImGuiTreeNodeFlags.FramePadding |
                            ImGuiTreeNodeFlags.OpenOnArrow |
                            ImGuiTreeNodeFlags.SpanAvailWidth,
                    go.name
            );
            ImGui.popID();

            if (treeNodeOpen) {
                ImGui.treePop();
            }
            index ++;
        }
        ImGui.end();
    }
}
