package components.templates;

import components.Component;
import components.DefaultSideScrollerPlayerController;
import components.Animator;
import brunostEngine.Camera;
import brunostEngine.GameObject;
import brunostEngine.Game;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics.PhysicsHandler;
import physics.components.Rigidbody;
import brunostEngine.ResourcePool;

public class GoombaAI extends Component {

    private transient boolean goingRight = false;
    private transient Rigidbody rb;
    private transient float walkSpeed = 0.6f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f();
    private transient boolean onGround = false;
    private transient boolean isDead = false;
    private transient float timeToKill = 0.5f;
    private transient Animator animator;

    @Override
    public void onStart() {
        this.animator = gameObject.getComponent(Animator.class);
        this.rb = gameObject.getComponent(Rigidbody.class);
        this.acceleration.y = Game.getPhysics().getGravity().y * 0.7f;
    }

    @Override
    public void onUpdate(float dt) {
        Camera camera = Game.getScene().camera();
        if (this.gameObject.transform.position.x >
                camera.position.x + camera.getProjectionSize().x * camera.getZoom()) {
            return;
        }

        if (isDead) {
            timeToKill -= dt;
            if (timeToKill <= 0) {
                this.gameObject.destroy();
            }
            this.rb.setVelocity(new Vector2f());
            return;
        }

        if (goingRight) {
            velocity.x = walkSpeed;
        } else {
            velocity.x = -walkSpeed;
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
        if (this.gameObject.transform.position.x < Game.getScene().camera().position.x - 0.5f) {
            this.gameObject.destroy();
        }
    }

    public void checkOnGround() {
        float innerPlayerWidth = 0.25f * 0.7f;
        float yVal = -0.14f;
        onGround = PhysicsHandler.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    @Override
    public void preSolve(GameObject obj, Contact contact, Vector2f contactNormal) {
        if (isDead) {
            return;
        }

        DefaultSideScrollerPlayerController defaultSideScrollerPlayerController = obj.getComponent(DefaultSideScrollerPlayerController.class);
         if (Math.abs(contactNormal.y) < 0.1f) {
            goingRight = contactNormal.x < 0;
        }

        if (obj.getComponent(Fireball.class) != null) {
            stomp();
            obj.getComponent(Fireball.class).disappear();
        }
    }

    public void stomp() {
        stomp(true);
    }

    public void stomp(boolean playSound) {
        this.isDead = true;
        this.velocity.zero();
        this.rb.setVelocity(new Vector2f());
        this.rb.setAngularVelocity(0.0f);
        this.rb.setGravityScale(0.0f);
        this.animator.play("squashMe");
        this.rb.setIsSensor();
        if (playSound) {
            ResourcePool.getSound("assets/sounds/bump.ogg").play();
        }
    }
}
