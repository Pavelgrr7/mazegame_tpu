package back;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import components.SpriteRenderer;
import imgui.ImGui;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class MenuObject {

    private static int ID_COUNTER = 0;
    private int uid = -1;

    public String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean doSerialization = true;

    public MenuObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i=0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        c.generateId();
        this.components.add(c);
        c.menuObject = this;
    }

    public void update(float dt) {
        for (int i=0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
        //PropertiesWindow.update(dt, scene);
    }

    public void start() {
        for (int i=0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public void imgui() {
        for (Component c : components) {
            if (ImGui.collapsingHeader(c.getClass().getSimpleName()))
                c.imgui();
        }
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

    public int getUid() {
        return this.uid;
    }

    public List<Component> getAllComponents() {
        return this.components;
    }

    public void setNoSerialize() {
        this.doSerialization = false;
    }

    public boolean doSerialization() {
        return this.doSerialization;
    }

//    public void editorUpdate(float dt) {
//        for (int i=0; i < components.size(); i++) {
//            components.get(i).editorUpdate(dt);
//        }
//    }

//    public MenuObject copy() {
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Component.class, new ComponentDeserializer())
//                .registerTypeAdapter(MenuObject.class, new GameObjectDeserializer())
//                .enableComplexMapKeySerialization()
//                .create();
//        String objAsJson = gson.toJson(this);
//        MenuObject obj = gson.fromJson(objAsJson, MenuObject.class);
//        obj.generateUid();
//        for (Component c : obj.getAllComponents()) {
//            c.generateId();
//        }
//
//        SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);
//        if (sprite != null && sprite.getTexture() != null) {
//            sprite.setTexture(
//                    AssetPool.getTexture(
//                            sprite.getTexture()
//                                    .getFilepath()));
//        }
//        return obj;
//    }

    private void generateUid() {
        this.uid = ID_COUNTER++;
    }
}