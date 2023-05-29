package no.brunostengine.scenes;

/**
 * The SceneBuilder class is necessary for proper initialization of a {@link Scene}.
 * This class contains important methods to be implemented in order for the Brunost framework to
 * properly facilitate necessary functionality.
 */
public abstract class SceneBuilder {
    public abstract String assignTitleToScene();
    public abstract void loadResources(Scene scene);
    public abstract void init(Scene scene);
}
