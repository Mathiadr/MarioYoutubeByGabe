package no.brunostengine.debuggingtools;


import no.brunostengine.*;
import no.brunostengine.components.Animator;
import no.brunostengine.components.Component;
import no.brunostengine.components.NonInteractable;
import no.brunostengine.components.Tile;
import no.brunostengine.util.PixelToGameObjectReader;
import org.joml.Vector2f;
import no.brunostengine.util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

/**
 * The DebugTools class holds basic and non-essential debugging tools to allow placement of GameObjects in real-time,
 * or print out the World position being accessed to the terminal.
 */
public class GameObjectPlacementTool extends Component {
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
                GameObject gameObjectAtPos = getGameObjectAtClick();
                placeObjectAtMousePos(gameObjectAtPos);
                debounce = debounceTime;
            }
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && debounce < 0) {
                GameObject gameObjectAtPos = getGameObjectAtClick();
                removeObjectAtMousePos(gameObjectAtPos);
                debounce = debounceTime;
            }
        }
    }

    public GameObject getGameObjectAtClick(){
        int x = (int) MouseListener.getScreenX();
        int y = (int) MouseListener.getScreenY();
        int gameObjectId = pixelToGameObjectReader.readPixel(x, y);
        if (gameObjectId == -1)
            return null;

        GameObject pickedObj = Game.getScene().getGameObject(gameObjectId);
        if (pickedObj == null || pickedObj.getComponent(NonInteractable.class) != null) return null;
        return pickedObj;
    }

    private void placeObjectAtMousePos(GameObject gameObjectAtPos){
        if (gameObjectAtPos == null || gameObjectToPlace.getComponent(Tile.class) == null){
            toWorld();
        } else if (gameObjectToPlace.getComponent(Tile.class) != null) {
            toTilemap();
        }
    }

    private void toTilemap(){
        try {
            Vector2f worldPos = getWorldPositionAtClick();
            Tilemap.get().replaceTile(worldPos.x, worldPos.y, gameObjectToPlace);
        }
        catch (Exception e){
            System.err.println("Attempted to place GameObject to tilemap but failed! Did you forget to add a Tile component?");
            e.printStackTrace();
        }
    }

    private void toWorld(){
        try {
            GameObject newObj = gameObjectToPlace.copy();
            newObj.transform.position = getWorldPositionAtClick();
            if (newObj.getComponent(Animator.class) != null) {
                newObj.getComponent(Animator.class).refreshTextures();
            }
            Game.getScene().addGameObjectToScene(newObj);
        }
        catch (Exception e){
            System.err.println("Attempted to place GameObject to world but failed!");
            e.printStackTrace();
        }
    }


    private void removeObjectAtMousePos(GameObject gameObjectAtPos){
        if (gameObjectAtPos == null)
            return;
        if (gameObjectAtPos.getComponent(Tile.class) == null){
            gameObjectAtPos.destroy();
        } if (gameObjectAtPos.getComponent(Tile.class) != null) {
            Tilemap.get().destroyTile(gameObjectAtPos.transform.position.x, gameObjectAtPos.transform.position.y);
        }
    }


    public Vector2f getWorldPositionAtClick(){
        float x = MouseListener.getWorldX();
        float y = MouseListener.getWorldY();
        float worldCorrectedX = ((int)Math.floor(x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2.0f;
        float worldCorrectedY = ((int)Math.floor(y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2.0f;
        return new Vector2f(worldCorrectedX, worldCorrectedY);
    }
}
