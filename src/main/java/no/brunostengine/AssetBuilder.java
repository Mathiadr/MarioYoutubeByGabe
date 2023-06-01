package no.brunostengine;


import no.brunostengine.components.*;
import no.brunostengine.util.ResourcePool;
import no.brunostengine.physics.components.CapsuleCollider;
import no.brunostengine.physics.components.Rigidbody;
import no.brunostengine.physics.enums.BodyType;

/**
 * The AssetBuilder class is meant to provide a method for easier generation of GameObjects.
 *
 */
public class AssetBuilder {
    final static float DEFAULT_SIZE = 0.25f;

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject block = Game.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

    public static GameObject generateSpriteObject(Sprite sprite) {
        return generateSpriteObject(sprite, DEFAULT_SIZE, DEFAULT_SIZE);
    }

    public static GameObject generateSpriteObject(String filename) {
        Sprite sprite = SpriteHandler.getSprite(filename, 0);
        return generateSpriteObject(sprite, DEFAULT_SIZE, DEFAULT_SIZE);
    }

    public static GameObject generateRatgirl() {
        Spritesheet playerSprites = ResourcePool.getSpritesheet("assets/images/RatGirlSpritesheet.png");
        GameObject ratgirl = generateSpriteObject(playerSprites.getSprite(0), 0.25f, 0.25f);


        Animation run = new Animation();
        run.title = "Run";
        float defaultFrameTime = 0.1f;
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(3), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(4), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(5), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(6), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(7), defaultFrameTime);
        run.setLoop(true);

        Animation switchDirection = new Animation();
        switchDirection.title = "Switch Direction";
        switchDirection.addFrame(playerSprites.getSprite(3), 0.1f);
        switchDirection.setLoop(false);

        Animation idle = new Animation();
        idle.title = "Idle";
        idle.addFrame(playerSprites.getSprite(0), 2f);
        idle.addFrame(playerSprites.getSprite(1), 0.1f);
        idle.setLoop(true);

        Animation jump = new Animation();
        jump.title = "Jump";
        jump.addFrame(playerSprites.getSprite(5), 0.1f);
        jump.setLoop(false);


        Animation die = new Animation();
        die.title = "Die";
        die.addFrame(playerSprites.getSprite(6), 0.1f);
        die.setLoop(false);

        Animator animator = new Animator();
        animator.addState(run);
        animator.addState(idle);
        animator.addState(switchDirection);
        animator.addState(jump);
        animator.addState(die);

        animator.setDefaultState(idle.title);
        animator.addState(run.title, switchDirection.title, "switchDirection");
        animator.addState(run.title, idle.title, "stopRunning");
        animator.addState(run.title, jump.title, "jump");
        animator.addState(switchDirection.title, idle.title, "stopRunning");
        animator.addState(switchDirection.title, run.title, "startRunning");
        animator.addState(switchDirection.title, jump.title, "jump");
        animator.addState(idle.title, run.title, "startRunning");
        animator.addState(idle.title, jump.title, "jump");
        animator.addState(jump.title, idle.title, "stopJumping");

        animator.addState(run.title, die.title, "die");
        animator.addState(switchDirection.title, die.title, "die");
        animator.addState(idle.title, die.title, "die");
        animator.addState(jump.title, die.title, "die");
        ratgirl.addComponent(animator);

        CapsuleCollider pb = new CapsuleCollider();
        pb.width = 0.21f;
        pb.height = 0.25f;
        ratgirl.addComponent(pb);

        Rigidbody rb = new Rigidbody();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);
        ratgirl.addComponent(rb);

        ratgirl.addComponent(new SideScrollerPlayerController());

        ratgirl.transform.zIndex = 10;

        return ratgirl;
    }

    public static GameObject generateRatgirlRPG() {
        Spritesheet playerSprites = ResourcePool.getSpritesheet("assets/images/RatGirlSpritesheet.png");
        Spritesheet bigPlayerSprites = ResourcePool.getSpritesheet("assets/images/RatGirlSpritesheet.png");
        GameObject ratgirl = generateSpriteObject(playerSprites.getSprite(0), 0.25f, 0.25f);

        Animation run = new Animation();
        run.title = "Run";
        float defaultFrameTime = 0.1f;
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(3), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(4), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(5), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(6), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(7), defaultFrameTime);
        run.setLoop(true);

        Animation switchDirection = new Animation();
        switchDirection.title = "Switch Direction";
        switchDirection.addFrame(playerSprites.getSprite(3), 0.1f);
        switchDirection.setLoop(false);

        Animation idle = new Animation();
        idle.title = "Idle";
        idle.addFrame(playerSprites.getSprite(0), 2f);
        idle.addFrame(playerSprites.getSprite(1), 0.1f);
        idle.setLoop(true);

        Animation jump = new Animation();
        jump.title = "Jump";
        jump.addFrame(playerSprites.getSprite(5), 0.1f);
        jump.setLoop(false);

        // Big mario animations
        Animation bigRun = new Animation();
        bigRun.title = "BigRun";
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(3), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(4), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(5), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(6), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(7), defaultFrameTime);
        bigRun.setLoop(true);

        Animation bigSwitchDirection = new Animation();
        bigSwitchDirection.title = "Big Switch Direction";
        bigSwitchDirection.addFrame(bigPlayerSprites.getSprite(2), 0.1f);
        bigSwitchDirection.setLoop(false);

        Animation bigIdle = new Animation();
        bigIdle.title = "BigIdle";
        bigIdle.addFrame(bigPlayerSprites.getSprite(0), 0.1f);
        bigIdle.setLoop(false);

        Animation bigJump = new Animation();
        bigJump.title = "BigJump";
        bigJump.addFrame(bigPlayerSprites.getSprite(6), 0.1f);
        bigJump.setLoop(false);


        Animation die = new Animation();
        die.title = "Die";
        die.addFrame(playerSprites.getSprite(6), 0.1f);
        die.setLoop(false);

        Animator animator = new Animator();
        animator.addState(run);
        animator.addState(idle);
        animator.addState(switchDirection);
        animator.addState(jump);
        animator.addState(die);

        animator.addState(bigRun);
        animator.addState(bigIdle);
        animator.addState(bigSwitchDirection);
        animator.addState(bigJump);

        animator.setDefaultState(idle.title);
        animator.addState(run.title, switchDirection.title, "switchDirection");
        animator.addState(run.title, idle.title, "stopRunning");
        animator.addState(run.title, jump.title, "jump");
        animator.addState(switchDirection.title, idle.title, "stopRunning");
        animator.addState(switchDirection.title, run.title, "startRunning");
        animator.addState(switchDirection.title, jump.title, "jump");
        animator.addState(idle.title, run.title, "startRunning");
        animator.addState(idle.title, jump.title, "jump");
        animator.addState(jump.title, idle.title, "stopJumping");

        animator.addState(bigRun.title, bigSwitchDirection.title, "switchDirection");
        animator.addState(bigRun.title, bigIdle.title, "stopRunning");
        animator.addState(bigRun.title, bigJump.title, "jump");
        animator.addState(bigSwitchDirection.title, bigIdle.title, "stopRunning");
        animator.addState(bigSwitchDirection.title, bigRun.title, "startRunning");
        animator.addState(bigSwitchDirection.title, bigJump.title, "jump");
        animator.addState(bigIdle.title, bigRun.title, "startRunning");
        animator.addState(bigIdle.title, bigJump.title, "jump");
        animator.addState(bigJump.title, bigIdle.title, "stopJumping");

        animator.addState(run.title, bigRun.title, "powerup");
        animator.addState(idle.title, bigIdle.title, "powerup");
        animator.addState(switchDirection.title, bigSwitchDirection.title, "powerup");
        animator.addState(jump.title, bigJump.title, "powerup");

        animator.addState(bigRun.title, run.title, "damage");
        animator.addState(bigIdle.title, idle.title, "damage");
        animator.addState(bigSwitchDirection.title, switchDirection.title, "damage");
        animator.addState(bigJump.title, jump.title, "damage");

        animator.addState(run.title, die.title, "die");
        animator.addState(switchDirection.title, die.title, "die");
        animator.addState(idle.title, die.title, "die");
        animator.addState(jump.title, die.title, "die");
        animator.addState(bigRun.title, run.title, "die");
        animator.addState(bigSwitchDirection.title, switchDirection.title, "die");
        animator.addState(bigIdle.title, idle.title, "die");
        animator.addState(bigJump.title, jump.title, "die");
        ratgirl.addComponent(animator);

        CapsuleCollider pb = new CapsuleCollider();
        pb.width = 0.21f;
        pb.height = 0.25f;
        ratgirl.addComponent(pb);

        Rigidbody rb = new Rigidbody();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);
        rb.setGravityScale(0.0f);
        ratgirl.addComponent(rb);

        ratgirl.addComponent(new TopDownPlayerController());

        ratgirl.transform.zIndex = 10;

        return ratgirl;
    }
}
