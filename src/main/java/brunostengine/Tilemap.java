package brunostengine;

import components.*;
import org.joml.Vector2f;
import scenes.Scene;

import java.util.ArrayList;

/**
 * A tilemap is a way to partition the world or an area into grids, which can then be used to fill with GameObjects.
 * This allows for easier placement and interaction without relying on specifying XY-Coordinates.
 * This class is a single instance class / singleton, and to create it you must use the static "generateTilemap" method.
 * Any scene using a tilemap must generate a tilemap in the init() method of the SceneBuilder for it to load back properly.
 */
public class Tilemap{
    private static Tilemap tilemap = null;
    public int column;
    public int row;
    public GameObject tilemapBackground;
    public transient GameObject[][] tiles;

    private Tilemap(int column, int row){
        this.column = column;
        this.row = row;
        this.tiles = new GameObject[column][row];
        float offsetX = 0.125f;
        float offsetY = 0.125f;
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles[i].length; j++){
                GameObject newTile = Game.getScene().createGameObject("Tile("+i+", "+j+")");
                Tile tile = new Tile();
                tile.parentTilemap = this;
                tile.column = i;
                tile.row = j;
                newTile.addComponent(tile);
                newTile.transform.position.x = offsetX;
                newTile.transform.position.y = offsetY;
                tiles[i][j] = newTile;
                offsetX += 0.25f;
            }
            offsetX = 0.125f;
            offsetY += 0.25f;
        }
    }

    public void onStart() {
        Scene scene = Game.getScene();
        ArrayList<GameObject> tilesFromScene = scene.getAllGameObjectsWith(Tile.class);
        if (!tilesFromScene.isEmpty()){
            System.out.println("Loading tiles from file...");
            for (GameObject tile : tilesFromScene){
                if (tile.getComponent(NonInteractable.class) == null) {
                    replaceTile(tile.transform.position.x, tile.transform.position.y, tile);
                    tile.destroy();
                } else tilemapBackground = tile;
            }
            System.out.println("Finished loading tiles from file");
        }
    }

    public static Tilemap generateTilemap(int column, int row){
        if (tilemap == null){
            tilemap = new Tilemap(column, row);
        } else System.err.println("Tilemap instance already exists!");
        return get();
    }

    public void setTilemapBackground(Sprite sprite){
        tilemapBackground = AssetBuilder.generateSpriteObject(sprite, 0.25f * column, 0.25f * row);
        tilemapBackground.addComponent(new NonInteractable());
        tilemapBackground.addComponent(new Tile());
        tilemapBackground.transform.zIndex = -10;
        tilemapBackground.transform.position.x = 0.125f;
        tilemapBackground.transform.position.y = 0.125f;
        tilemapBackground.transform.position.mul( column, row);

        Game.getScene().addGameObjectToScene(tilemapBackground);
    }

    public static Tilemap get(){
        return tilemap;
    }

    public void addTilemapToScene(){
        for (int i = 0; i < get().tiles.length; i++){
            for (int j = 0; j < get().tiles[i].length; j++){
                Game.getScene().addGameObjectToScene(get().tiles[i][j]);
            }
        }
    }

    public void fill(GameObject gameObject){
        for (int i = 0; i < get().tiles.length; i++){
            for (int j = 0; j < get().tiles[i].length; j++){
                Vector2f position = get().tiles[i][j].transform.position;
                if (gameObject.getComponent(Tile.class) != null)
                    gameObject.removeComponent(Tile.class);
                gameObject.addComponent(tiles[i][j].getComponent(Tile.class));
                tiles[i][j] = gameObject.copy();
                tiles[i][j].transform.position = position;
                if (tiles[i][j].getComponent(Animator.class) != null) {
                    tiles[i][j].getComponent(Animator.class).refreshTextures();
                }
            }
        }
    }

    public void fillBorder(GameObject gameObject){
        System.out.println("Commencing filling Tilemap borders...");
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles[i].length; j++){
                boolean tileIsBorder = false;
                if (i == 0 || i == tiles.length-1){
                    tileIsBorder = true;
                }
                if (j == 0 || j == tiles[i].length-1){
                    tileIsBorder = true;
                }
                if (tileIsBorder) {
                    replaceTile(tiles[i][j].transform.position.x, tiles[i][j].transform.position.y, gameObject);
                }
            }
        }
        System.out.println("Finished filling Tilemap borders");
    }

    public void fillRandom(){

    }

    public GameObject getTileAtPosition(float x, float y){
        Vector2f position = new Vector2f(x, y);
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles[i].length; j++){
                if (tiles[i][j].transform.position.x == position.x && tiles[i][j].transform.position.y == position.y ){
                    // is "return Window.getScene().getGameObject("Tile("+i+", "+j+")");" a more optimal way??
                    return tiles[i][j];
                }
            }
        }
        System.err.println("Could not find Tile in x: " + x + ",\ty: " + y);
        return null;
    }

    public void replaceTile(float x, float y, GameObject tile){
        Vector2f position = new Vector2f(x, y);
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles[i].length; j++){
                if (tiles[i][j].transform.position.x == position.x && tiles[i][j].transform.position.y == position.y ){
                    // is "return Window.getScene().getGameObject("Tile("+i+", "+j+")");" a more optimal way??
                    GameObject replacement = tile.copy();
                    if (replacement.getComponent(Tile.class) != null)
                        replacement.removeComponent(Tile.class);
                    replacement.addComponent(tiles[i][j].getComponent(Tile.class));
                    replacement.transform.position = position;
                    if (tiles[i][j].getComponent(NonInteractable.class) == null)
                        tiles[i][j].destroy();
                    tiles[i][j] = replacement;
                    if (replacement.getComponent(Animator.class) != null) {
                        replacement.getComponent(Animator.class).refreshTextures();
                    }
                    Game.getScene().addGameObjectToScene(tiles[i][j]);
                }
            }
        }
    }

    public void destroyTileAtPos(float x, float y){
        Vector2f position = new Vector2f(x, y);
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles[i].length; j++){
                if (tiles[i][j].transform.position.x == position.x && tiles[i][j].transform.position.y == position.y ){
                    // is "return Window.getScene().getGameObject("Tile("+i+", "+j+")");" a more optimal way??
                    GameObject replacement = Game.getScene().createGameObject("Tile("+i+", "+j+")");
                    replacement.addComponent(tiles[i][j].getComponent(Tile.class));
                    replacement.transform.position = position;
                    tiles[i][j].destroy();
                    tiles[i][j] = replacement;
                    Game.getScene().addGameObjectToScene(tiles[i][j]);
                    System.out.println("Removed " + tiles[i][j].name);
                    return;
                }
            }
        }
        System.err.println("Could not find tile in specified location("+x+", "+y+")");
    }
}
