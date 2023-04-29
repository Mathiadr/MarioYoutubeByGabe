package scenes;

import components.*;
import components.cameras.EditorCamera;
import components.editorTools.GizmoSystem;
import components.editorTools.GridLines;
import components.templates.BreakableBrick;
import imgui.ImGui;
import imgui.ImVec2;
import brunostEngine.*;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;
import util.ResourcePool;

import java.io.File;
import java.util.Collection;

public class LevelEditorSceneBuilder extends SceneBuilder {

    private Spritesheet sprites;
    private Spritesheet defaultTilesSpritesheet;
    private Spritesheet spritesSpritesheet;
    private GameObject levelEditorStuff;

    public LevelEditorSceneBuilder() {

    }

    @Override
    public String assignTitleToScene() {
        return "level";
    }

    @Override
    public void init(Scene scene) {
        sprites = ResourcePool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");
        Spritesheet gizmos = ResourcePool.getSpritesheet("assets/images/gizmos.png");
        spritesSpritesheet = ResourcePool.getSpritesheet("assets/images/RatGirlSpritesheet.png");
        defaultTilesSpritesheet = ResourcePool.getSpritesheet("assets/images/defaultTiles.png");

        levelEditorStuff = scene.createGameObject("LevelEditor");
        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new KeyControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));
        scene.addGameObjectToScene(levelEditorStuff);
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

        ResourcePool.getSound(("assets/sounds/main-theme-overworld.ogg")).stop();

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
    public void imgui() {
        ImGui.begin("Level Editor Stuff");
        levelEditorStuff.imgui();
        ImGui.end();

        ImGui.begin("Test window");

        if (ImGui.beginTabBar("WindowTabBar")) {
            if (ImGui.beginTabItem("Solid Blocks")) {

                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 0; i < sprites.size(); i++) {
                    if (i == 34) continue;
                    if (i >= 38 && i < 61) continue;

                    Sprite sprite = sprites.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 4;
                    float spriteHeight = sprite.getHeight() * 4;
                    int id = sprite.getTexId();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                        Rigidbody2D rb = new Rigidbody2D();
                        rb.setBodyType(BodyType.Static);
                        object.addComponent(rb);
                        Box2DCollider b2d = new Box2DCollider();
                        b2d.setHalfSize(new Vector2f(0.25f, 0.25f));
                        object.addComponent(b2d);
                        object.addComponent(new Ground());
                        if (i == 12) {
                            object.addComponent(new BreakableBrick());
                        }
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Decoration Blocks")) {
                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 34; i < 61; i++) {
                    if (i >= 35 && i < 38) continue;
                    if (i >= 42 && i < 45) continue;

                    Sprite sprite = sprites.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 4;
                    float spriteHeight = sprite.getHeight() * 4;
                    int id = sprite.getTexId();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Prefabs")) {
                int uid = 0;
                Spritesheet playerSprites = ResourcePool.getSpritesheet("assets/images/spritesheet.png");
                Sprite sprite = playerSprites.getSprite(0);
                float spriteWidth = sprite.getWidth() * 4;
                float spriteHeight = sprite.getHeight() * 4;
                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateMario();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                Spritesheet ratgirlSprites = ResourcePool.getSpritesheet("assets/images/RatGirlSpritesheet.png");
                sprite = ratgirlSprites.getSprite(0);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateRatgirl();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                Spritesheet items = ResourcePool.getSpritesheet("assets/images/items.png");
                sprite = items.getSprite(0);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateQuestionBlock();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = items.getSprite(7);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateCoin();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = playerSprites.getSprite(14);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateGoomba();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                Spritesheet turtle = ResourcePool.getSpritesheet("assets/images/turtle.png");
                sprite = turtle.getSprite(0);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateTurtle();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = items.getSprite(6);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateFlagtop();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = items.getSprite(33);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateFlagPole();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                Spritesheet pipes = ResourcePool.getSpritesheet("assets/images/pipes.png");
                sprite = pipes.getSprite(0);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generatePipe(Direction.Down);
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = pipes.getSprite(1);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generatePipe(Direction.Up);
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = pipes.getSprite(2);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generatePipe(Direction.Right);
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = pipes.getSprite(3);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generatePipe(Direction.Left);
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Sounds")) {
                Collection<Sound> sounds = ResourcePool.getAllSounds();
                for (Sound sound : sounds) {
                    File tmp = new File(sound.getFilepath());
                    if (ImGui.button(tmp.getName())) {
                        if (!sound.isPlaying()) {
                            sound.play();
                        } else {
                            sound.stop();
                        }
                    }

                    if (ImGui.getContentRegionAvailX() > 100) {
                        ImGui.sameLine();
                    }
                }

                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }

        ImGui.end();
    }
}
