import components.*;
import components.cameras.FollowTargetCamera;
import components.editorTools.GridLines;
import jade.Direction;
import jade.GameObject;
import jade.Prefabs;
import jade.Window;
import org.joml.Vector2f;
import scenes.Scene;
import scenes.SceneBuilder;
import util.ResourcePool;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Window window = Window.get();
        window.init();
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

                for (GameObject g : scene.getGameObjects()) {
                    if (g.getComponent(SpriteRenderer.class) != null) {
                        SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                        if (spr.getTexture() != null) {
                            spr.setTexture(ResourcePool.getTexture(spr.getTexture().getFilepath()));
                        }
                    }

                    if (g.getComponent(StateMachine.class) != null) {
                        StateMachine stateMachine = g.getComponent(StateMachine.class);
                        stateMachine.refreshTextures();
                    }
                }
            }

            @Override
            public void init(Scene scene) {
                GameObject levelEditorStuff = scene.createGameObject("LevelEditor");

                levelEditorStuff.addComponent(new MouseControls());
                levelEditorStuff.addComponent(new KeyControls());
                levelEditorStuff.addComponent(new GridLines());
                levelEditorStuff.addComponent(new FollowTargetCamera(scene.camera()));
                scene.addGameObjectToScene(levelEditorStuff);

                ArrayList<GameObject> gameObjects = new ArrayList<>();
                GameObject ratgirl = Prefabs.generateRatgirl();
                ratgirl.transform.position = new Vector2f(250, 2);
                gameObjects.add(ratgirl);
                scene.addGameObjectToScene(ratgirl);

                GameObject block = Prefabs.generateQuestionBlock();
                block.transform.position.x = 250f;
                block.transform.position.y = 1f;
                gameObjects.add(block);
                scene.addGameObjectToScene(block);

                for (int i=0; i < 3; i++){
                    GameObject newBlock = Prefabs.generateQuestionBlock();
                    scene.placeGameObjectRelativeToLastPlacement(newBlock, Direction.Right);
                }
                for (int i=0; i < 2; i++){
                    GameObject newBlock = Prefabs.generateQuestionBlock();
                    scene.placeGameObjectRelativeToLastPlacement(newBlock, Direction.Down);
                }
                for (int i=0; i < 3; i++){
                    GameObject newBlock = Prefabs.generateQuestionBlock();
                    scene.placeGameObjectRelativeToLastPlacement(newBlock, Direction.Left);
                }

                GameObject cameraObject = scene.createGameObject("GameCamera");
                //cameraObject.addComponent(new GameCamera(scene.camera()));
                //cameraObject.onStart();
                //scene.addGameObjectToScene(cameraObject);




            }

            @Override
            public void imgui() {

            }
        });

        window.run();
    }
}
