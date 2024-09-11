package scenes;

public abstract class SceneInitializer {
    public abstract void init(Scene scene);
    public abstract void loadResources(Scene scene);
    public abstract void imgui();


    public abstract boolean isMenu();
    public abstract boolean isEditor();
    public abstract boolean isRunning();
}