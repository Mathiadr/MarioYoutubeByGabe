package components.templates;

import components.Animator;
import components.Component;
import brunostEngine.Camera;
import brunostEngine.GameObject;
import brunostEngine.Game;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.Rigidbody2D;
import util.ResourcePool;

public class TurtleAI extends Component {
    private transient boolean goingRight = false;
    private transient Rigidbody2D rb;
    private transient float walkSpeed = 0.6f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);
    private transient boolean onGround = false;
    private transient boolean isDead = false;
    private transient boolean isMoving = false;
    private transient Animator animator;
    private float movingDebounce = 0.32f;

    @Override
    public void onStart() {
        this.animator = this.gameObject.getComponent(Animator.class);
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        this.acceleration.y = Game.getPhysics().getGravity().y * 0.7f;
    }

    @Override
    public void onUpdate(float dt) {
        movingDebounce -= dt;
        Camera camera = Game.getScene().camera();
        if (this.gameObject.transform.position.x >
                camera.position.x + camera.getProjectionSize().x * camera.getZoom()) {
            return;
        }

        if (!isDead || isMoving) {
            if (goingRight) {
                gameObject.transform.scale.x = -0.25f;
                velocity.x = walkSpeed;
                acceleration.x = 0;
            } else {
                gameObject.transform.scale.x = 0.25f;
                velocity.x = -walkSpeed;
                acceleration.x = 0;
            }
        } else {
            velocity.x = 0;
        }

        checkOnGround();
        if (onGround) {
            this.acceleration.y = 0;
            this.velocity.y = 0;
        } else {
            this.acceleration.y = Game.getPhysics().getGravity().y * 0.7f;
        }
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -terminalVelocity.y);
        this.rb.setVelocity(velocity);

        if (this.gameObject.transform.position.x <
                Game.getScene().camera().position.x - 0.5f) {// ||
                //this.gameObject.transform.position.y < 0.0f) {
            this.gameObject.destroy();
        }
    }

    public void checkOnGround() {
        float innerPlayerWidth = 0.25f * 0.7f;
        float yVal = -0.2f;
        onGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    public void stomp() {
        this.isDead = true;
        this.isMoving = false;
        this.velocity.zero();
        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0.0f);
        this.rb.setGravityScale(0.0f);
        this.animator.trigger("squashMe");
        ResourcePool.getSound("assets/sounds/bump.ogg").play();
    }

    @Override
    public void preSolve(GameObject obj, Contact contact, Vector2f contactNormal) {
        GoombaAI goomba = obj.getComponent(GoombaAI.class);
        if (isDead && isMoving && goomba != null) {
            goomba.stomp();
            contact.setEnabled(false);
            ResourcePool.getSound("assets/sounds/kick.ogg").play();
        }

        MarioController playerController = obj.getComponent(MarioController.class);
        if (playerController != null) {
            if (!isDead && !playerController.isDead() &&
                    !playerController.isHurtInvincible() &&
                    contactNormal.y > 0.58f) {
                playerController.enemyBounce();
                stomp();
                walkSpeed *= 3.0f;
            } else if (movingDebounce < 0 && !playerController.isDead() &&
                    !playerController.isHurtInvincible() &&
                    (isMoving || !isDead) && contactNormal.y < 0.58f) {
                playerController.die();
                if (!playerController.isDead()) {
                    contact.setEnabled(false);
                }
            } else if (!playerController.isDead() && !playerController.isHurtInvincible()) {
                if (isDead && contactNormal.y > 0.58f) {
                    playerController.enemyBounce();
                    isMoving = !isMoving;
                    goingRight = contactNormal.x < 0;
                } else if (isDead && !isMoving) {
                    isMoving = true;
                    goingRight = contactNormal.x < 0;
                    movingDebounce = 0.32f;
                }
            } else if (!playerController.isDead() && playerController.isHurtInvincible()) {
                contact.setEnabled(false);
            }
        } else if (Math.abs(contactNormal.y) < 0.1f && !obj.isDead() && obj.getComponent(MushroomAI.class) == null) {
            goingRight = contactNormal.x < 0;
            if (isMoving && isDead) {
                ResourcePool.getSound("assets/sounds/bump.ogg").play();
            }
        }

        if (obj.getComponent(Fireball.class) != null) {
            if (!isDead) {
                walkSpeed *= 3.0f;
                stomp();
            } else {
                isMoving = !isMoving;
                goingRight = contactNormal.x < 0;
            }
            obj.getComponent(Fireball.class).disappear();
            contact.setEnabled(false);
        }
    }
}
