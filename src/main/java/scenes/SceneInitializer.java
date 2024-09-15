package scenes;

public abstract class SceneInitializer {
    public abstract void renderMenu(int width, int height);

    public abstract void init(Scene scene);
    public abstract void loadResources(Scene scene);
    public abstract void imgui();

    public abstract boolean isMenu();
}