import no.brunostengine.*;
import no.brunostengine.components.*;
import no.brunostengine.components.FollowTargetCamera;
import no.brunostengine.debuggingtools.GameObjectPlacementTool;
import no.brunostengine.physics.components.BoxCollider;
import no.brunostengine.util.ResourcePool;
import org.joml.Vector2f;
import no.brunostengine.Scene;
import no.brunostengine.SceneBuilder;

public class Main {
    public static void main(String[] args) {
        Game game = Game.get();
        game.init("Hello World!");
        Game.playScene(new SceneBuilder() {
            @Override
            public String assignTitleToScene() {
                return "LevelRPGTest";
            }

            @Override
            public void init(Scene scene) {
                Tilemap tilemap = Tilemap.generateTilemap(30, 30);
                GameObject levelEditorStuff = scene.createGameObject("LevelEditor");

                levelEditorStuff.setNoSerialize();
                levelEditorStuff.addComponent(new GameObjectPlacementTool());
                levelEditorStuff.addComponent(new KeyControls());
                //levelEditorStuff.addComponent(new GridLines());
                levelEditorStuff.addComponent(new FollowTargetCamera(scene.camera()));
                scene.addGameObjectToScene(levelEditorStuff);

                if (true) {
                    GameObject ratgirl = AssetBuilder.generateRatgirlRPG();
                    ratgirl.transform.position = new Vector2f(2, 10);
                    scene.addGameObjectToScene(ratgirl);

                    Spritesheet tileSprites = ResourcePool.getSpritesheet("assets/images/defaultTiles.png");
                    GameObject tile = AssetBuilder.generateSpriteObject(tileSprites.getSprite(3), 0.25f, 0.25f);
                    tile.addComponent(new NonInteractable());
                    tile.addComponent(new Tile());

                    tilemap.fill(tile);
                    tilemap.addTilemapToScene();

                }
            }

            @Override
            public void loadResources(Scene scene) {

                ResourcePool.addSpritesheet("assets/images/items.png",
                        new Spritesheet(ResourcePool.getTexture("assets/images/items.png"),
                                16, 16, 43, 0));
                ResourcePool.addSpritesheet("assets/images/RatGirlSpritesheet.png",
                        new Spritesheet(ResourcePool.getTexture("assets/images/RatGirlSpritesheet.png"),
                                70, 75, 8, 0));
                ResourcePool.addSpritesheet("assets/images/defaultTiles.png",
                        new Spritesheet(ResourcePool.getTexture("assets/images/defaultTiles.png"),
                                64, 64, 4, 1));
            }
        });
        game.run();
    }
}
