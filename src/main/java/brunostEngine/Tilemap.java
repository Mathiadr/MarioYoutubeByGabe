package brunostEngine;

public class Tilemap {
    public int column;
    public int row;
    public GameObject[][] tiles;

    public Tilemap(int column, int row){
        this.column = column;
        this.row = row;
        this.tiles = new GameObject[column][row];
        float offsetX = 0.125f;
        float offsetY = 0.125f;
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles[i].length; j++){
                GameObject newTile = Prefabs.generateQuestionBlock();
                newTile.transform.position.x = offsetX;
                newTile.transform.position.y = offsetY;
                tiles[i][j] = newTile;
                offsetX += 0.25f;
            }
            offsetX = 0.125f;
            offsetY += 0.25f;
        }
    }

    public void addToScene(){
    }

}
