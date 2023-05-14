package components;

import brunostEngine.KeyListener;
import brunostEngine.Game;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class DefaultTopDownPlayerController extends BasePlayerController {


    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);

    public transient boolean gravityEnabled = true;
    private transient float groundDebounce = 0.0f;
    private transient float groundDebounceTime = 0.1f;
    private transient int jumpTime = 0;

    @Override
    public void onUpdate(float dt) {
        if (isDead) {
            Game.changeScene(Game.getCurrentSceneBuilder());
            return;
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

        checkIfPlayerIsGrounded();
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE) && (jumpTime > 0 || isGrounded || groundDebounce > 0)) {
            if ((isGrounded || groundDebounce > 0) && jumpTime == 0) {
                jumpTime = 60;
                this.velocity.y = jumpImpulse;
            } else if (jumpTime > 0) {
                jumpTime--;
                this.velocity.y = ((jumpTime / 2.2f) * jumpBoost);
            } else {
                this.velocity.y = 0;
            }
            groundDebounce = 0;
        } else if (!isGrounded) {
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

        if (!isGrounded) {
            animator.play("jump");
        } else {
            animator.play("stopJumping");
        }
    }

    public boolean isDead() {
        return this.isDead;
    }
}
