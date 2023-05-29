package no.brunostengine.components.debug_tools;


import no.brunostengine.*;
import no.brunostengine.components.Animator;
import no.brunostengine.components.Component;
import no.brunostengine.components.NonInteractable;
import no.brunostengine.util.PixelToGameObjectReader;
import org.joml.Vector2f;
import no.brunostengine.util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

/**
 * The DebugTools class holds basic and non-essential debugging tools to allow placement of GameObjects in real-time,
 * or print out the World position being accessed to the terminal.
 */
public class DebugTools extends Component {
    public PixelToGameObjectReader pixelToGameObjectReader = Game.getPixelToGameObjectReader();
    public GameObject gameObjectToPlace = null;
    private float debounceTime = 0.2f;
    private float debounce = debounceTime;

    @Override
    public void onUpdate(float dt) {
        debounce -= dt;

        if (gameObjectToPlace == null) {
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
                Vector2f worldPos = getWorldPositionAtClick();
                if (!MouseListener.isDragging()) {
                    Tilemap.get().getTileAtPosition(worldPos.x,worldPos.y);
                    System.out.println("X: " + worldPos.x + ",\tY: " + worldPos.y);
                }
                debounce = debounceTime;
            }
        }
        if (gameObjectToPlace != null) {
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
                placeObjectAtMousePos();
                debounce = debounceTime;
            }
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && debounce < 0) {
                removeObjectAtMousePos();
                this.debounce = debounceTime;
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

    private void placeObjectAtMousePos(){
        int x = (int) MouseListener.getScreenX();
        int y = (int) MouseListener.getScreenY();
        Vector2f worldPos = getWorldPositionAtClick();
        int gameObjectId = pixelToGameObjectReader.readPixel(x, y);
        GameObject pickedObj = Game.getScene().getGameObject(gameObjectId);
        if (pickedObj != null && pickedObj.getComponent(NonInteractable.class) == null && debounce < 0) {
            if (pickedObj.getComponent(Tile.class) != null) {
                Tilemap.get().replaceTile(worldPos.x, worldPos.y, gameObjectToPlace);
            } else {
                //pickedObj.destroy();
                //placeObjectAtPos(worldPos);
            }
        } else if (pickedObj != null){
            if (pickedObj.getComponent(Tile.class) != null) {
                Tilemap.get().replaceTile(worldPos.x, worldPos.y, gameObjectToPlace);
            } else {
                //placeObjectAtPos(worldPos);
            }
        }
    }

    private void removeObjectAtMousePos(){
        int x = (int) MouseListener.getScreenX();
        int y = (int) MouseListener.getScreenY();
        Vector2f worldPos = getWorldPositionAtClick();
        int gameObjectId = pixelToGameObjectReader.readPixel(x, y);
        GameObject pickedObj = Game.getScene().getGameObject(gameObjectId);
        if (pickedObj != null && pickedObj.getComponent(NonInteractable.class) == null && debounce < 0) {
            if (pickedObj.getComponent(Tile.class) != null){
                Tilemap.get().destroyTileAtPos(worldPos.x, worldPos.y);
            }
        }
    }

    public void removeObjectAtPos(Vector2f position){

    }

    public void placeObjectAtPos(Vector2f position) {
        GameObject newObj = gameObjectToPlace.copy();
        newObj.transform.position = position;
        if (newObj.getComponent(Animator.class) != null) {
            newObj.getComponent(Animator.class).refreshTextures();
        }
        Game.getScene().addGameObjectToScene(newObj);
    }

    public void replaceObjectAtPos(Vector2f vector2f) {
        GameObject newObj = gameObjectToPlace.copy();
        if (newObj.getComponent(Animator.class) != null) {
            newObj.getComponent(Animator.class).refreshTextures();
        }
        Game.getScene().addGameObjectToScene(newObj);
    }
}
