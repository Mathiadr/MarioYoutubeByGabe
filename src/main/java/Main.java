import brunostengine.*;
import components.*;
import components.cameras.FollowTargetCamera;
import components.debug_tools.DebugTools;
import org.joml.Vector2f;
import scenes.Scene;
import scenes.SceneBuilder;
import brunostengine.ResourcePool;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Game game = Game.get();
        game.init("Hello World!");
        /*
        Window.changeScene(new SceneBuilder() {


            @Override
            public String assignTitleToScene() {
                return "levelTest";
            }

            @Override
            public void loadResources(Scene scene) {
                ResourcePool.getShader("assets/shaders/default.glsl");

                ResourcePool.addSpritesheet("assets/images/items.png",
                        new Spritesheet(ResourcePool.getTexture("assets/images/items.png"),
                                16, 16, 43, 0));
                ResourcePool.addSpritesheet("assets/images/RatGirlSpritesheet.png",
                        new Spritesheet(ResourcePool.getTexture("assets/images/RatGirlSpritesheet.png"),
                                70, 75, 8, 0));
                ResourcePool.addSpritesheet("assets/images/defaultTiles.png",
                        new Spritesheet(ResourcePool.getTexture("assets/images/defaultTiles.png"),
                                64, 64, 4, 1));


                ResourcePool.addSound("assets/sounds/main-theme-overworld.ogg", true);
                ResourcePool.addSound("assets/sounds/flagpole.ogg", false);
                ResourcePool.addSound("assets/sounds/break_block.ogg", false);
                ResourcePool.addSound("assets/sounds/bump.ogg", false);
                ResourcePool.addSound("assets/sounds/coin.ogg", false);
                ResourcePool.addSound("assets/sounds/gameover.ogg", false);
                ResourcePool.addSound("assets/sounds/jump-small.ogg", false);
                ResourcePool.addSound("assets/sounds/mario_die.ogg", false);
                ResourcePool.addSound("assets/sounds/pipe.ogg", false);
                ResourcePool.addSound("assets/sounds/powerup.ogg", false);
                ResourcePool.addSound("assets/sounds/powerup_appears.ogg", false);
                ResourcePool.addSound("assets/sounds/stage_clear.ogg", false);
                ResourcePool.addSound("assets/sounds/stomp.ogg", false);
                ResourcePool.addSound("assets/sounds/kick.ogg", false);
                ResourcePool.addSound("assets/sounds/invincible.ogg", false);

            }

            @Override
            public void init(Scene scene) {
                Tilemap tilemap = Tilemap.generateTilemap(30, 30);
                GameObject levelEditorStuff = scene.createGameObject("LevelEditor");

                levelEditorStuff.setNoSerialize();
                levelEditorStuff.addComponent(new DebugTools());
                levelEditorStuff.addComponent(new KeyControls());
                levelEditorStuff.addComponent(new GridLines());
                levelEditorStuff.addComponent(new FollowTargetCamera(scene.camera()));
                scene.addGameObjectToScene(levelEditorStuff);
                if (false) {
                    ArrayList<GameObject> gameObjects = new ArrayList<>();
                    GameObject ratgirl = Prefabs.generateRatgirl();
                    ratgirl.transform.position = new Vector2f(2, 10);
                    gameObjects.add(ratgirl);
                    scene.addGameObjectToScene(ratgirl);

                    GameObject block = Prefabs.generateQuestionBlock();
                    block.transform.position.x = 0f;
                    block.transform.position.y = 0f;
                    gameObjects.add(block);
                    scene.addGameObjectToScene(block);

                    Spritesheet tileSprites = ResourcePool.getSpritesheet("assets/images/defaultTiles.png");
                    GameObject tile = Prefabs.generateSpriteObject(tileSprites.getSprite(3), 0.25f, 0.25f);
                    Rigidbody2D rb = new Rigidbody2D();
                    rb.setBodyType(BodyType.Static);
                    tile.addComponent(rb);
                    Box2DCollider b2d = new Box2DCollider();
                    b2d.setHalfSize(new Vector2f(0.25f, 0.25f));
                    tile.addComponent(b2d);
                    tile.addComponent(new Ground());

                    tilemap.fillBorder(tile);
                    tilemap.addTilemapToScene();
                    //tilemap.addTilemapToScene();

                    tilemap.setTilemapBackground(ResourcePool.getSpritesheet("assets/images/defaultTiles.png").getSprite(3));

                }
                levelEditorStuff.getComponent(DebugTools.class).gameObjectToPlace = Prefabs.generateQuestionBlock();


            }
        });
        ----------------------------------------------------------------------------------------------------------------

         */
        Game.changeScene(new SceneBuilder() {
            @Override
            public String assignTitleToScene() {
                return "LevelRPGTest";
            }

            @Override
            public void init(Scene scene) {
                Tilemap tilemap = Tilemap.generateTilemap(30, 30);
                GameObject levelEditorStuff = scene.createGameObject("LevelEditor");

                levelEditorStuff.setNoSerialize();
                levelEditorStuff.addComponent(new DebugTools());
                levelEditorStuff.addComponent(new KeyControls());
                //levelEditorStuff.addComponent(new GridLines());
                levelEditorStuff.addComponent(new FollowTargetCamera(scene.camera()));
                scene.addGameObjectToScene(levelEditorStuff);

                if (true) {
                    ArrayList<GameObject> gameObjects = new ArrayList<>();
                    GameObject ratgirl = AssetBuilder.generateRatgirlRPG();
                    ratgirl.transform.position = new Vector2f(2, 10);
                    gameObjects.add(ratgirl);
                    scene.addGameObjectToScene(ratgirl);

                    GameObject block = AssetBuilder.generateQuestionBlock();
                    block.transform.position.x = 0f;
                    block.transform.position.y = 0f;
                    gameObjects.add(block);
                    scene.addGameObjectToScene(block);

                    Spritesheet tileSprites = ResourcePool.getSpritesheet("assets/images/defaultTiles.png");
                    GameObject tile = AssetBuilder.generateSpriteObject(tileSprites.getSprite(3), 0.25f, 0.25f);
                    tile.addComponent(new NonInteractable());

                    tilemap.fill(tile);
                    tilemap.addTilemapToScene();

                }
                levelEditorStuff.getComponent(DebugTools.class).gameObjectToPlace = AssetBuilder.generateQuestionBlock();
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
