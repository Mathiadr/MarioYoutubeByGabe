package components.editorTools;

import brunostEngine.GameObject;
import brunostEngine.MouseListener;
import brunostEngine.Tilemap;
import brunostEngine.Window;
import components.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class DebugTools extends Component {
    public GameObject gameObjectToPlace = null;
    private float debounceTime = 0.2f;
    private float debounce = debounceTime;

    @Override
    public void onUpdate(float dt) {
        debounce -= dt;

        if (gameObjectToPlace == null) {
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                Vector2f worldPos = getWorldPositionAtClick();
                if (!MouseListener.isDragging() && debounce < 0) {
                    Tilemap.get().getTileAtPosition(worldPos.x,worldPos.y);
                    System.out.println("X: " + worldPos.x + ",\tY: " + worldPos.y);
                    debounce = debounceTime;
                }
            }
        }
        if (gameObjectToPlace != null) {
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                Vector2f worldPos = getWorldPositionAtClick();
                if (!MouseListener.isDragging() && debounce < 0) {
                    if (gameObjectToPlace.getComponent(Ground.class) != null) {
                        Tilemap.get().replaceTile(worldPos.x, worldPos.y, gameObjectToPlace);
                    }
                    debounce = debounceTime;
                }
            }
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
                Vector2f worldPos = getWorldPositionAtClick();
                if (!MouseListener.isDragging() && debounce < 0) {
                    Tilemap.get().getTileAtPosition(worldPos.x, worldPos.y).destroy();
                    System.out.println("Removed object at X: " + worldPos.x + ",\tY: " + worldPos.y);
                    debounce = debounceTime;
                }
            }
        }
    }

    public Vector2f getWorldPositionAtClick(){
        float x = MouseListener.getWorldX();
        float y = MouseListener.getWorldY();
        float worldCorrectedX = ((int)Math.floor(x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2.0f;
        float worldCorrectedY = ((int)Math.floor(y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2.0f;
        return new Vector2f(worldCorrectedX, worldCorrectedY);
    }

    public void placeObjectAtPos() {
        GameObject newObj = gameObjectToPlace.copy();
        if (newObj.getComponent(StateMachine.class) != null) {
            newObj.getComponent(StateMachine.class).refreshTextures();
        }
        newObj.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
        newObj.removeComponent(NonPickable.class);
        Window.getScene().addGameObjectToScene(newObj);
    }
}
