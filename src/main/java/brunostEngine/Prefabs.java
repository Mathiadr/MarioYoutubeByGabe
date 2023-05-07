package brunostEngine;

import components.*;
import components.templates.*;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.PillboxCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;
import util.ResourcePool;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject block = Game.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

    public static GameObject generateRatgirl() {
        Spritesheet playerSprites = ResourcePool.getSpritesheet("assets/images/RatGirlSpritesheet.png");
        Spritesheet bigPlayerSprites = ResourcePool.getSpritesheet("assets/images/RatGirlSpritesheet.png");
        GameObject ratgirl = generateSpriteObject(playerSprites.getSprite(0), 0.25f, 0.25f);

        // Little mario animations
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

        PillboxCollider pb = new PillboxCollider();
        pb.width = 0.21f;
        pb.height = 0.25f;
        ratgirl.addComponent(pb);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);
        ratgirl.addComponent(rb);

        ratgirl.addComponent(new DefaultTopDownPlayerController());

        ratgirl.transform.zIndex = 10;

        return ratgirl;
    }

    public static GameObject generateRatgirlRPG() {
        Spritesheet playerSprites = ResourcePool.getSpritesheet("assets/images/RatGirlSpritesheet.png");
        Spritesheet bigPlayerSprites = ResourcePool.getSpritesheet("assets/images/RatGirlSpritesheet.png");
        GameObject ratgirl = generateSpriteObject(playerSprites.getSprite(0), 0.25f, 0.25f);

        // Little mario animations
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

        PillboxCollider pb = new PillboxCollider();
        pb.width = 0.21f;
        pb.height = 0.25f;
        ratgirl.addComponent(pb);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);
        rb.setGravityScale(0.0f);
        ratgirl.addComponent(rb);

        ratgirl.addComponent(new DefaultSideScrollerPlayerController());

        ratgirl.transform.zIndex = 10;

        return ratgirl;
    }

    public static GameObject generateMario() {
        Spritesheet playerSprites = ResourcePool.getSpritesheet("assets/images/spritesheet.png");
        Spritesheet bigPlayerSprites = ResourcePool.getSpritesheet("assets/images/bigSpritesheet.png");
        GameObject mario = generateSpriteObject(playerSprites.getSprite(0), 0.25f, 0.25f);

        // Little mario animations
        Animation run = new Animation();
        run.title = "Run";
        float defaultFrameTime = 0.2f;
        run.addFrame(playerSprites.getSprite(0), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(3), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.setLoop(true);

        Animation switchDirection = new Animation();
        switchDirection.title = "Switch Direction";
        switchDirection.addFrame(playerSprites.getSprite(4), 0.1f);
        switchDirection.setLoop(false);

        Animation idle = new Animation();
        idle.title = "Idle";
        idle.addFrame(playerSprites.getSprite(0), 0.1f);
        idle.setLoop(false);

        Animation jump = new Animation();
        jump.title = "Jump";
        jump.addFrame(playerSprites.getSprite(5), 0.1f);
        jump.setLoop(false);

        // Big mario animations
        Animation bigRun = new Animation();
        bigRun.title = "BigRun";
        bigRun.addFrame(bigPlayerSprites.getSprite(0), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(1), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(2), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(3), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(2), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(1), defaultFrameTime);
        bigRun.setLoop(true);

        Animation bigSwitchDirection = new Animation();
        bigSwitchDirection.title = "Big Switch Direction";
        bigSwitchDirection.addFrame(bigPlayerSprites.getSprite(4), 0.1f);
        bigSwitchDirection.setLoop(false);

        Animation bigIdle = new Animation();
        bigIdle.title = "BigIdle";
        bigIdle.addFrame(bigPlayerSprites.getSprite(0), 0.1f);
        bigIdle.setLoop(false);

        Animation bigJump = new Animation();
        bigJump.title = "BigJump";
        bigJump.addFrame(bigPlayerSprites.getSprite(5), 0.1f);
        bigJump.setLoop(false);

        // Fire mario animations
        int fireOffset = 21;
        Animation fireRun = new Animation();
        fireRun.title = "FireRun";
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 0), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 1), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 2), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 3), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 2), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 1), defaultFrameTime);
        fireRun.setLoop(true);

        Animation fireSwitchDirection = new Animation();
        fireSwitchDirection.title = "Fire Switch Direction";
        fireSwitchDirection.addFrame(bigPlayerSprites.getSprite(fireOffset + 4), 0.1f);
        fireSwitchDirection.setLoop(false);

        Animation fireIdle = new Animation();
        fireIdle.title = "FireIdle";
        fireIdle.addFrame(bigPlayerSprites.getSprite(fireOffset + 0), 0.1f);
        fireIdle.setLoop(false);

        Animation fireJump = new Animation();
        fireJump.title = "FireJump";
        fireJump.addFrame(bigPlayerSprites.getSprite(fireOffset + 5), 0.1f);
        fireJump.setLoop(false);

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

        animator.addState(fireRun);
        animator.addState(fireIdle);
        animator.addState(fireSwitchDirection);
        animator.addState(fireJump);

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

        animator.addState(fireRun.title, fireSwitchDirection.title, "switchDirection");
        animator.addState(fireRun.title, fireIdle.title, "stopRunning");
        animator.addState(fireRun.title, fireJump.title, "jump");
        animator.addState(fireSwitchDirection.title, fireIdle.title, "stopRunning");
        animator.addState(fireSwitchDirection.title, fireRun.title, "startRunning");
        animator.addState(fireSwitchDirection.title, fireJump.title, "jump");
        animator.addState(fireIdle.title, fireRun.title, "startRunning");
        animator.addState(fireIdle.title, fireJump.title, "jump");
        animator.addState(fireJump.title, fireIdle.title, "stopJumping");

        animator.addState(run.title, bigRun.title, "powerup");
        animator.addState(idle.title, bigIdle.title, "powerup");
        animator.addState(switchDirection.title, bigSwitchDirection.title, "powerup");
        animator.addState(jump.title, bigJump.title, "powerup");
        animator.addState(bigRun.title, fireRun.title, "powerup");
        animator.addState(bigIdle.title, fireIdle.title, "powerup");
        animator.addState(bigSwitchDirection.title, fireSwitchDirection.title, "powerup");
        animator.addState(bigJump.title, fireJump.title, "powerup");

        animator.addState(bigRun.title, run.title, "damage");
        animator.addState(bigIdle.title, idle.title, "damage");
        animator.addState(bigSwitchDirection.title, switchDirection.title, "damage");
        animator.addState(bigJump.title, jump.title, "damage");
        animator.addState(fireRun.title, bigRun.title, "damage");
        animator.addState(fireIdle.title, bigIdle.title, "damage");
        animator.addState(fireSwitchDirection.title, bigSwitchDirection.title, "damage");
        animator.addState(fireJump.title, bigJump.title, "damage");

        animator.addState(run.title, die.title, "die");
        animator.addState(switchDirection.title, die.title, "die");
        animator.addState(idle.title, die.title, "die");
        animator.addState(jump.title, die.title, "die");
        animator.addState(bigRun.title, run.title, "die");
        animator.addState(bigSwitchDirection.title, switchDirection.title, "die");
        animator.addState(bigIdle.title, idle.title, "die");
        animator.addState(bigJump.title, jump.title, "die");
        animator.addState(fireRun.title, bigRun.title, "die");
        animator.addState(fireSwitchDirection.title, bigSwitchDirection.title, "die");
        animator.addState(fireIdle.title, bigIdle.title, "die");
        animator.addState(fireJump.title, bigJump.title, "die");
        mario.addComponent(animator);

        PillboxCollider pb = new PillboxCollider();
        pb.width = 0.21f;
        pb.height = 0.25f;
        mario.addComponent(pb);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);
        mario.addComponent(rb);

        mario.addComponent(new DefaultTopDownPlayerController());

        mario.transform.zIndex = 10;
        return mario;
    }

    public static GameObject generateQuestionBlock() {
        Spritesheet playerSprites = ResourcePool.getSpritesheet("assets/images/items.png");
        GameObject questionBlock = generateSpriteObject(playerSprites.getSprite(0), 0.25f, 0.25f);

        Animation flicker = new Animation();
        flicker.title = "Question";
        float defaultFrameTime = 0.23f;
        flicker.addFrame(playerSprites.getSprite(0), 0.57f);
        flicker.addFrame(playerSprites.getSprite(1), defaultFrameTime);
        flicker.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        flicker.setLoop(true);

        Animation inactive = new Animation();
        inactive.title = "Inactive";
        inactive.addFrame(playerSprites.getSprite(3), 0.1f);
        inactive.setLoop(false);

        Animator animator = new Animator();
        animator.addState(flicker);
        animator.addState(inactive);
        animator.setDefaultState(flicker.title);
        animator.addState(flicker.title, inactive.title, "setInactive");
        questionBlock.addComponent(animator);
        questionBlock.addComponent(new QuestionBlock());

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Static);
        questionBlock.addComponent(rb);
        Box2DCollider b2d = new Box2DCollider();
        b2d.setHalfSize(new Vector2f(0.25f, 0.25f));
        questionBlock.addComponent(b2d);
        questionBlock.addComponent(new Ground());

        return questionBlock;
    }

    public static GameObject generateBlockCoin() {
        Spritesheet items = ResourcePool.getSpritesheet("assets/images/items.png");
        GameObject coin = generateSpriteObject(items.getSprite(7), 0.25f, 0.25f);

        Animation coinFlip = new Animation();
        coinFlip.title = "CoinFlip";
        float defaultFrameTime = 0.23f;
        coinFlip.addFrame(items.getSprite(7), 0.57f);
        coinFlip.addFrame(items.getSprite(8), defaultFrameTime);
        coinFlip.addFrame(items.getSprite(9), defaultFrameTime);
        coinFlip.setLoop(true);

        Animator animator = new Animator();
        animator.addState(coinFlip);
        animator.setDefaultState(coinFlip.title);
        coin.addComponent(animator);
        coin.addComponent(new QuestionBlock());

        coin.addComponent(new BlockCoin());

        return coin;
    }

    public static GameObject generateCoin() {
        Spritesheet items = ResourcePool.getSpritesheet("assets/images/items.png");
        GameObject coin = generateSpriteObject(items.getSprite(7), 0.25f, 0.25f);

        Animation coinFlip = new Animation();
        coinFlip.title = "CoinFlip";
        float defaultFrameTime = 0.23f;
        coinFlip.addFrame(items.getSprite(7), 0.57f);
        coinFlip.addFrame(items.getSprite(8), defaultFrameTime);
        coinFlip.addFrame(items.getSprite(9), defaultFrameTime);
        coinFlip.setLoop(true);

        Animator animator = new Animator();
        animator.addState(coinFlip);
        animator.setDefaultState(coinFlip.title);
        coin.addComponent(animator);
        coin.addComponent(new Coin());

        CircleCollider circleCollider = new CircleCollider();
        circleCollider.setRadius(0.12f);
        coin.addComponent(circleCollider);
        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Static);
        coin.addComponent(rb);

        return coin;
    }

    public static GameObject generateGoomba() {
        Spritesheet sprites = ResourcePool.getSpritesheet("assets/images/spritesheet.png");
        GameObject goomba = generateSpriteObject(sprites.getSprite(14), 0.25f, 0.25f);

        Animation walk = new Animation();
        walk.title = "Walk";
        float defaultFrameTime = 0.23f;
        walk.addFrame(sprites.getSprite(14), defaultFrameTime);
        walk.addFrame(sprites.getSprite(15), defaultFrameTime);
        walk.setLoop(true);

        Animation squashed = new Animation();
        squashed.title = "Squashed";
        squashed.addFrame(sprites.getSprite(16), 0.1f);
        squashed.setLoop(false);

        Animator animator = new Animator();
        animator.addState(walk);
        animator.addState(squashed);
        animator.setDefaultState(walk.title);
        animator.addState(walk.title, squashed.title, "squashMe");
        goomba.addComponent(animator);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setMass(0.1f);
        rb.setFixedRotation(true);
        goomba.addComponent(rb);
        CircleCollider circle = new CircleCollider();
        circle.setRadius(0.12f);
        goomba.addComponent(circle);

        goomba.addComponent(new GoombaAI());

        return goomba;
    }

    public static GameObject generateTurtle() {
        Spritesheet turtleSprites = ResourcePool.getSpritesheet("assets/images/turtle.png");
        GameObject turtle = generateSpriteObject(turtleSprites.getSprite(0), 0.25f, 0.35f);

        Animation walk = new Animation();
        walk.title = "Walk";
        float defaultFrameTime = 0.23f;
        walk.addFrame(turtleSprites.getSprite(0), defaultFrameTime);
        walk.addFrame(turtleSprites.getSprite(1), defaultFrameTime);
        walk.setLoop(true);

        Animation squashed = new Animation();
        squashed.title = "TurtleShellSpin";
        squashed.addFrame(turtleSprites.getSprite(2), 0.1f);
        squashed.setLoop(false);

        Animator animator = new Animator();
        animator.addState(walk);
        animator.addState(squashed);
        animator.setDefaultState(walk.title);
        animator.addState(walk.title, squashed.title, "squashMe");
        turtle.addComponent(animator);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setMass(0.1f);
        rb.setFixedRotation(true);
        turtle.addComponent(rb);
        CircleCollider circle = new CircleCollider();
        circle.setRadius(0.13f);
        circle.setOffset(new Vector2f(0, -0.05f));
        turtle.addComponent(circle);

        turtle.addComponent(new TurtleAI());

        return turtle;
    }

    public static GameObject generateFireball(Vector2f position) {
        Spritesheet items = ResourcePool.getSpritesheet("assets/images/items.png");
        GameObject fireball = generateSpriteObject(items.getSprite(32), 0.18f, 0.18f);
        fireball.transform.position = position;

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setFixedRotation(true);
        rb.setContinuousCollision(false);
        fireball.addComponent(rb);

        CircleCollider circleCollider = new CircleCollider();
        circleCollider.setRadius(0.08f);
        fireball.addComponent(circleCollider);
        fireball.addComponent(new Fireball());

        return fireball;
    }

    public static GameObject generateFlagtop() {
        Spritesheet items = ResourcePool.getSpritesheet("assets/images/items.png");
        GameObject flagtop = generateSpriteObject(items.getSprite(6), 0.25f, 0.25f);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setFixedRotation(true);
        rb.setContinuousCollision(false);
        flagtop.addComponent(rb);

        Box2DCollider boxCollider = new Box2DCollider();
        boxCollider.setHalfSize(new Vector2f(0.1f, 0.25f));
        boxCollider.setOffset(new Vector2f(-0.075f, 0.0f));
        flagtop.addComponent(boxCollider);
        flagtop.addComponent(new Flagpole(true));

        return flagtop;
    }

    public static GameObject generateFlagPole() {
        Spritesheet items = ResourcePool.getSpritesheet("assets/images/items.png");
        GameObject flagtop = generateSpriteObject(items.getSprite(33), 0.25f, 0.25f);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setFixedRotation(true);
        rb.setContinuousCollision(false);
        flagtop.addComponent(rb);

        Box2DCollider boxCollider = new Box2DCollider();
        boxCollider.setHalfSize(new Vector2f(0.1f, 0.25f));
        boxCollider.setOffset(new Vector2f(-0.075f, 0.0f));
        flagtop.addComponent(boxCollider);
        flagtop.addComponent(new Flagpole(false));

        return flagtop;
    }

    public static GameObject generateMushroom() {
        Spritesheet items = ResourcePool.getSpritesheet("assets/images/items.png");
        GameObject mushroom = generateSpriteObject(items.getSprite(10), 0.25f, 0.25f);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setFixedRotation(true);
        rb.setContinuousCollision(false);
        mushroom.addComponent(rb);

        CircleCollider circleCollider = new CircleCollider();
        circleCollider.setRadius(0.14f);
        mushroom.addComponent(circleCollider);
        mushroom.addComponent(new MushroomAI());

        return mushroom;
    }

    public static GameObject generateFlower() {
        Spritesheet items = ResourcePool.getSpritesheet("assets/images/items.png");
        GameObject flower = generateSpriteObject(items.getSprite(20), 0.25f, 0.25f);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Static);
        rb.setFixedRotation(true);
        rb.setContinuousCollision(false);
        flower.addComponent(rb);

        CircleCollider circleCollider = new CircleCollider();
        circleCollider.setRadius(0.14f);
        flower.addComponent(circleCollider);
        flower.addComponent(new Flower());

        return flower;
    }

    public static GameObject generatePipe(Direction direction) {
        Spritesheet pipes = ResourcePool.getSpritesheet("assets/images/pipes.png");
        int index = direction == Direction.Down ? 0 :
                    direction == Direction.Up ? 1 :
                    direction == Direction.Right ? 2 :
                    direction == Direction.Left ? 3 : -1;
        assert index != -1 : "Invalid pipe direction.";
        GameObject pipe = generateSpriteObject(pipes.getSprite(index), 0.5f, 0.5f);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Static);
        rb.setFixedRotation(true);
        rb.setContinuousCollision(false);
        pipe.addComponent(rb);

        Box2DCollider b2d = new Box2DCollider();
        b2d.setHalfSize(new Vector2f(0.5f, 0.5f));
        pipe.addComponent(b2d);
        pipe.addComponent(new Pipe(direction));
        pipe.addComponent(new Ground());

        return pipe;
    }
}
