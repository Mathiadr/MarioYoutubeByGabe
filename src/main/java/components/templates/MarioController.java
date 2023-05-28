package components.templates;

import components.Animator;
import components.Collideable;
import components.Component;
import components.SpriteRenderer;
import brunostEngine.GameObject;
import brunostEngine.KeyListener;
import brunostEngine.AssetBuilder;
import brunostEngine.Game;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics.PhysicsHandler;
import physics.components.CylinderCollider;
import physics.components.Rigidbody;
import physics.enums.BodyType;
import scenes.LevelSceneBuilder;
import brunostEngine.ResourcePool;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class MarioController extends Component {
    private enum PlayerState {
        Small,
        Big,
        Fire,
        Invincible
    }

    public float walkSpeed = 1.9f;
    public float jumpBoost = 1.0f;
    public float jumpImpulse = 3.0f;
    public float slowDownForce = 0.05f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);

    private MarioController.PlayerState playerState = MarioController.PlayerState.Small;
    public transient boolean onGround = false;
    private transient float groundDebounce = 0.0f;
    private transient float groundDebounceTime = 0.1f;
    private transient Rigidbody rb;
    private transient Animator animator;
    private transient float bigJumpBoostFactor = 1.05f;
    private transient float playerWidth = 0.25f;
    private transient int jumpTime = 0;
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead = false;
    private transient int enemyBounce = 0;

    private transient float hurtInvincibilityTimeLeft = 0;
    private transient float hurtInvincibilityTime = 1.4f;
    private transient float deadMaxHeight = 0;
    private transient float deadMinHeight = 0;
    private transient boolean deadGoingUp = true;
    private transient float blinkTime = 0.0f;
    private transient SpriteRenderer spr;

    private transient boolean playWinAnimation = false;
    private transient float timeToCastle = 4.5f;
    private transient float walkTime = 2.2f;

    @Override
    public void onStart() {
        this.spr = gameObject.getComponent(SpriteRenderer.class);
        this.rb = gameObject.getComponent(Rigidbody.class);
        this.animator = gameObject.getComponent(Animator.class);
    }

    @Override
    public void onUpdate(float dt) {
        if (playWinAnimation) {
            checkOnGround();
            if (!onGround) {
                gameObject.transform.scale.x = -0.25f;
                gameObject.transform.position.y -= dt;
                animator.play("stopRunning");
                animator.play("stopJumping");
            } else {
                if (this.walkTime > 0) {
                    gameObject.transform.scale.x = 0.25f;
                    gameObject.transform.position.x += dt;
                    animator.play("startRunning");
                }
                if (!ResourcePool.getSound("assets/sounds/stage_clear.ogg").isPlaying()) {
                    ResourcePool.getSound("assets/sounds/stage_clear.ogg").play();
                }
                timeToCastle -= dt;
                walkTime -= dt;

                if (timeToCastle <= 0) {
                }
            }

            return;
        }

        if (isDead) {
            if (this.gameObject.transform.position.y < deadMaxHeight && deadGoingUp) {
                this.gameObject.transform.position.y += dt * walkSpeed / 2.0f;
            } else if (this.gameObject.transform.position.y >= deadMaxHeight && deadGoingUp) {
                deadGoingUp = false;
            } else if (!deadGoingUp && gameObject.transform.position.y > deadMinHeight) {
                this.rb.setBodyType(BodyType.Kinematic);
                this.acceleration.y = Game.getPhysics().getGravity().y * 0.7f;
                this.velocity.y += this.acceleration.y * dt;
                this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
                this.rb.setVelocity(this.velocity);
                this.rb.setAngularVelocity(0);
            } else if (!deadGoingUp && gameObject.transform.position.y <= deadMinHeight) {
                Game.changeScene(new LevelSceneBuilder());
            }
            return;
        }

        if (hurtInvincibilityTimeLeft > 0) {
            hurtInvincibilityTimeLeft -= dt;
            blinkTime -= dt;

            if (blinkTime <= 0) {
                blinkTime = 0.2f;
                if (spr.getColor().w == 1) {
                    spr.setColor(new Vector4f(1, 1, 1, 0));
                } else {
                    spr.setColor(new Vector4f(1, 1, 1, 1));
                }
            } else {
                if (spr.getColor().w == 0) {
                    spr.setColor(new Vector4f(1, 1, 1, 1));
                }
            }
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D)) {
            this.gameObject.transform.scale.x = -playerWidth;
            this.acceleration.x = walkSpeed;

            if (this.velocity.x < 0) {
                this.animator.play("switchDirection");
                this.velocity.x += slowDownForce;
            } else {
                this.animator.play("startRunning");
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.gameObject.transform.scale.x = playerWidth;
            this.acceleration.x = -walkSpeed;

            if (this.velocity.x > 0) {
                this.animator.play("switchDirection");
                this.velocity.x -= slowDownForce;
            } else {
                this.animator.play("startRunning");
            }
        } else {
            this.acceleration.x = 0;
            if (this.velocity.x > 0) {
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            } else if (this.velocity.x < 0) {
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }

            if (this.velocity.x == 0) {
                this.animator.play("stopRunning");
            }
        }

        if (KeyListener.keyBeginPress(GLFW_KEY_E) && playerState == MarioController.PlayerState.Fire &&
                Fireball.canSpawn()) {
            Vector2f position = new Vector2f(this.gameObject.transform.position)
                    .add(this.gameObject.transform.scale.x > 0
                            ? new Vector2f(0.26f, 0)
                            : new Vector2f(-0.26f, 0));
            GameObject fireball = AssetBuilder.generateFireball(position);
            fireball.getComponent(Fireball.class).goingRight =
                    this.gameObject.transform.scale.x > 0;
            Game.getScene().addGameObjectToScene(fireball);
        }

        checkOnGround();
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE) && (jumpTime > 0 || onGround || groundDebounce > 0)) {
            if ((onGround || groundDebounce > 0) && jumpTime == 0) {
                ResourcePool.getSound("assets/sounds/jump-small.ogg").play();
                jumpTime = 28;
                this.velocity.y = jumpImpulse;
            } else if (jumpTime > 0) {
                jumpTime--;
                this.velocity.y = ((jumpTime / 2.2f) * jumpBoost);
            } else {
                this.velocity.y = 0;
            }
            groundDebounce = 0;
        } else if (enemyBounce > 0) {
            enemyBounce--;
            this.velocity.y = ((enemyBounce / 2.2f) * jumpBoost);
        }else if (!onGround) {
            if (this.jumpTime > 0) {
                this.velocity.y *= 0.35f;
                this.jumpTime = 0;
            }
            groundDebounce -= dt;
            this.acceleration.y = Game.getPhysics().getGravity().y * 0.7f;
        } else {
            this.velocity.y = 0;
            this.acceleration.y = 0;
            groundDebounce = groundDebounceTime;
        }

        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);

        if (!onGround) {
            animator.play("jump");
        } else {
            animator.play("stopJumping");
        }
    }

    public void checkOnGround() {
        float innerPlayerWidth = this.playerWidth * 0.6f;
        float yVal = playerState == MarioController.PlayerState.Small ? -0.14f : -0.24f;
        onGround = PhysicsHandler.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    public void setPosition(Vector2f newPos) {
        this.gameObject.transform.position.set(newPos);
        this.rb.setPosition(newPos);
    }

    public void powerup() {
        if (playerState == MarioController.PlayerState.Small) {
            playerState = MarioController.PlayerState.Big;
            ResourcePool.getSound("assets/sounds/powerup.ogg").play();
            gameObject.transform.scale.y = 0.42f;
            CylinderCollider pb = gameObject.getComponent(CylinderCollider.class);
            if (pb != null) {
                jumpBoost *= bigJumpBoostFactor;
                walkSpeed *= bigJumpBoostFactor;
                pb.setHeight(0.42f);
            }
        } else if (playerState == MarioController.PlayerState.Big) {
            playerState = MarioController.PlayerState.Fire;
            ResourcePool.getSound("assets/sounds/powerup.ogg").play();
        }

        animator.play("powerup");
    }

    public void playWinAnimation(GameObject flagpole) {
        if (!playWinAnimation) {
            playWinAnimation = true;
            velocity.set(0.0f, 0.0f);
            acceleration.set(0.0f, 0.0f);
            rb.setVelocity(velocity);
            rb.setIsSensor();
            rb.setBodyType(BodyType.Static);
            gameObject.transform.position.x = flagpole.transform.position.x;
            ResourcePool.getSound("assets/sounds/main-theme-overworld.ogg").stop();
            ResourcePool.getSound("assets/sounds/flagpole.ogg").play();
        }
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (isDead) return;

        if (collidingObject.getComponent(Collideable.class) != null) {
            if (Math.abs(contactNormal.x) > 0.8f) {
                this.velocity.x = 0;
            } else if (contactNormal.y > 0.8f) {
                this.velocity.y = 0;
                this.acceleration.y = 0;
                this.jumpTime = 0;
            }
        }
    }

    public void enemyBounce() {
        this.enemyBounce = 8;
    }

    public boolean isDead() {
        return this.isDead;
    }

    public boolean isHurtInvincible() {
        return this.hurtInvincibilityTimeLeft > 0 || playWinAnimation;
    }

    public boolean isInvincible() {
        return this.playerState == MarioController.PlayerState.Invincible ||
                this.hurtInvincibilityTimeLeft > 0 || playWinAnimation;
    }

    public void die() {
        this.animator.play("die");
        if (this.playerState == MarioController.PlayerState.Small) {
            this.velocity.set(0, 0);
            this.acceleration.set(0, 0);
            this.rb.setVelocity(new Vector2f());
            this.isDead = true;
            this.rb.setIsSensor();
            ResourcePool.getSound("assets/sounds/main-theme-overworld.ogg").stop();
            ResourcePool.getSound("assets/sounds/mario_die.ogg").play();
            deadMaxHeight = this.gameObject.transform.position.y + 0.3f;
            this.rb.setBodyType(BodyType.Static);
            if (gameObject.transform.position.y > 0) {
                deadMinHeight = -0.25f;
            }
        } else if (this.playerState == MarioController.PlayerState.Big) {
            this.playerState = MarioController.PlayerState.Small;
            gameObject.transform.scale.y = 0.25f;
            CylinderCollider pb = gameObject.getComponent(CylinderCollider.class);
            if (pb != null) {
                jumpBoost /= bigJumpBoostFactor;
                walkSpeed /= bigJumpBoostFactor;
                pb.setHeight(0.25f);
            }
            hurtInvincibilityTimeLeft = hurtInvincibilityTime;
            ResourcePool.getSound("assets/sounds/pipe.ogg").play();
        } else if (playerState == MarioController.PlayerState.Fire) {
            this.playerState = MarioController.PlayerState.Big;
            hurtInvincibilityTimeLeft = hurtInvincibilityTime;
            ResourcePool.getSound("assets/sounds/pipe.ogg").play();
        }
    }

    public boolean hasWon() {
        return false;
    }

    public boolean isSmall() {
        return this.playerState == MarioController.PlayerState.Small;
    }
}
