package scenes;

import components.*;
import components.cameras.GameCamera;
import brunostEngine.*;
import util.ResourcePool;

public class LevelSceneBuilder extends SceneBuilder {
    public LevelSceneBuilder() {

    }


    @Override
    public String assignTitleToScene() {
        return "level";
    }

    @Override
    public void init(Scene scene) {
        Spritesheet sprites = ResourcePool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");

        GameObject cameraObject = scene.createGameObject("GameCamera");
        cameraObject.addComponent(new GameCamera(scene.camera()));
        cameraObject.onStart();
        scene.addGameObjectToScene(cameraObject);
    }

    @Override
    public void loadResources(Scene scene) {
        ResourcePool.getShader("assets/shaders/default.glsl");

        ResourcePool.addSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new Spritesheet(ResourcePool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
        ResourcePool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(ResourcePool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));
        ResourcePool.addSpritesheet("assets/images/turtle.png",
                new Spritesheet(ResourcePool.getTexture("assets/images/turtle.png"),
                        16, 24, 4, 0));
        ResourcePool.addSpritesheet("assets/images/bigSpritesheet.png",
                new Spritesheet(ResourcePool.getTexture("assets/images/bigSpritesheet.png"),
                        16, 32, 42, 0));
        ResourcePool.addSpritesheet("assets/images/pipes.png",
                new Spritesheet(ResourcePool.getTexture("assets/images/pipes.png"),
                        32, 32, 4, 0));
        ResourcePool.addSpritesheet("assets/images/items.png",
                new Spritesheet(ResourcePool.getTexture("assets/images/items.png"),
                        16, 16, 43, 0));
        ResourcePool.addSpritesheet("assets/images/gizmos.png",
                new Spritesheet(ResourcePool.getTexture("assets/images/gizmos.png"),
                        24, 48, 3, 0));
        ResourcePool.getTexture("assets/images/blendImage2.png");

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

        ResourcePool.getSound(("assets/sounds/main-theme-overworld.ogg")).play();

        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(ResourcePool.getTexture(spr.getTexture().getFilepath()));
                }
            }

            if (g.getComponent(Animator.class) != null) {
                Animator animator = g.getComponent(Animator.class);
                animator.refreshTextures();
            }
        }
    }

    @Override
    public void imgui() {

    }
}
