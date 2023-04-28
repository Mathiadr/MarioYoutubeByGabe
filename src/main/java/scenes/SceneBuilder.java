package scenes;

public abstract class SceneBuilder {
    public abstract String assignTitleToScene();
    public abstract void init(Scene scene);
    public abstract void loadResources(Scene scene);
    public abstract void imgui();
}
