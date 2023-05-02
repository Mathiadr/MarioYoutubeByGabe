package brunostEngine;

import components.*;
import org.joml.Vector2f;

public class Tilemap {
    private static Tilemap tilemap = null;
    public int column;
    public int row;
    public transient GameObject tilemapBackground;
    public GameObject[][] tiles;

    private Tilemap(int column, int row){
        this.column = column;
        this.row = row;
        this.tiles = new GameObject[column][row];
        float offsetX = 0.125f;
        float offsetY = 0.125f;
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles[i].length; j++){
                GameObject newTile = Window.getScene().createGameObject("Tile("+i+", "+j+")");
                newTile.addComponent(new Tile());
                newTile.transform.position.x = offsetX;
                newTile.transform.position.y = offsetY;
                tiles[i][j] = newTile;
                offsetX += 0.25f;
            }
            offsetX = 0.125f;
            offsetY += 0.25f;
        }
    }


    public static Tilemap generateTilemap(int column, int row){
        if (tilemap == null){
            tilemap = new Tilemap(column, row);
        } else System.err.println("Tilemap instance already exists!");
        return get();
    }

    public void setTilemapBackground(Sprite sprite){
        tilemapBackground = Prefabs.generateSpriteObject(sprite, 0.25f * column, 0.25f * row);
        tilemapBackground.addComponent(new NonPickable());
        tilemapBackground.transform.zIndex = -10;
        tilemapBackground.transform.position.x = 0.125f;
        tilemapBackground.transform.position.y = 0.125f;
        tilemapBackground.transform.position.mul( column, row);

        Window.getScene().addGameObjectToScene(tilemapBackground);
    }

    public static Tilemap get(){
        return tilemap;
    }

    public void addTilemapToScene(){
        for (int i = 0; i < get().tiles.length; i++){
            for (int j = 0; j < get().tiles[i].length; j++){
                Window.getScene().addGameObjectToScene(get().tiles[i][j]);
            }
        }
    }

    public void fill(GameObject gameObject){
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles[i].length; j++){
                Vector2f position = tiles[i][j].transform.position;
                tiles[i][j] = gameObject.copy();
                tiles[i][j].transform.position = position;
                if (tiles[i][j].getComponent(StateMachine.class) != null) {
                    tiles[i][j].getComponent(StateMachine.class).refreshTextures();
                }
            }
        }
    }

    public void fillBorder(GameObject gameObject){
        gameObject.addComponent(new Tile());
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
                    Vector2f position = tiles[i][j].transform.position;
                    tiles[i][j] = gameObject.copy();
                    tiles[i][j].transform.position = position;
                    if (tiles[i][j].getComponent(StateMachine.class) != null) {
                        tiles[i][j].getComponent(StateMachine.class).refreshTextures();
                    }
                }
            }
        }
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
        tile.addComponent(new Tile());
        Vector2f position = new Vector2f(x, y);
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles[i].length; j++){
                if (tiles[i][j].transform.position.x == position.x && tiles[i][j].transform.position.y == position.y ){
                    // is "return Window.getScene().getGameObject("Tile("+i+", "+j+")");" a more optimal way??
                    GameObject replacement = tile.copy();
                    replacement.transform.position = position;
                    tiles[i][j].destroy();
                    tiles[i][j] = replacement;
                    if (replacement.getComponent(StateMachine.class) != null) {
                        replacement.getComponent(StateMachine.class).refreshTextures();
                    }
                    Window.getScene().addGameObjectToScene(tiles[i][j]);
                }
            }
        }
    }
}
