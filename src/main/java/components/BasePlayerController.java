package components;

import brunostEngine.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.Rigidbody2D;

public abstract class BasePlayerController extends Component{

    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);

    public float walkSpeed = 1.9f;
    public float jumpBoost = 1.0f;
    public float jumpImpulse = 2.0f;
    public float slowDownForce = 0.05f;

    public transient boolean isGrounded = false;
    protected transient boolean isDead = false;
    protected transient float playerWidth = 0.25f;
    protected transient int jumpTime = 0;
    protected transient Vector2f acceleration = new Vector2f();
    protected transient Vector2f velocity = new Vector2f();

    protected transient SpriteRenderer spr;
    protected transient Rigidbody2D rb;
    protected transient Animator animator;


    @Override
    public void onStart() {
        this.spr = gameObject.getComponent(SpriteRenderer.class);
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        this.animator = gameObject.getComponent(Animator.class);
    }

    @Override
    public abstract void onUpdate(float deltaTime);

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
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

    public void checkIfPlayerIsGrounded() {
        float innerPlayerWidth = this.playerWidth * 0.6f;
        float yVal = -0.14f;
        isGrounded = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    public void setPosition(Vector2f newPos) {
        this.gameObject.transform.position.set(newPos);
        this.rb.setPosition(newPos);
    }

    public boolean isDead() {
        return this.isDead;
    }

}
