package components;

import brunostEngine.KeyListener;
import brunostEngine.Game;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class DefaultSideScrollerPlayerController extends BasePlayerController {


    public Vector2f terminalVelocity = new Vector2f(1f, 1f);

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
                this.animator.trigger("switchDirection");
                this.velocity.x += slowDownForce;
            } else {
                this.animator.trigger("startRunning");
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.gameObject.transform.scale.x = playerWidth;
            this.acceleration.x = -walkSpeed;

            if (this.velocity.x > 0) {
                this.animator.trigger("switchDirection");
                this.velocity.x -= slowDownForce;
            } else {
                this.animator.trigger("startRunning");
            }
        } else {
            this.acceleration.x = 0;
            if (this.velocity.x > 0) {
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            } else if (this.velocity.x < 0) {
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }

            if (this.velocity.y == 0 && this.velocity.x == 0) {
                this.animator.trigger("stopRunning");
            }
        }


        if (KeyListener.isKeyPressed(GLFW_KEY_UP) || KeyListener.isKeyPressed(GLFW_KEY_W)) {
            this.acceleration.y = walkSpeed;

            if (this.velocity.y < 0) {
                this.velocity.y += slowDownForce;
            } else {
                this.animator.trigger("startRunning");
            }
        }else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN) || KeyListener.isKeyPressed(GLFW_KEY_S)) {
            this.acceleration.y = -walkSpeed;

            if (this.velocity.y > 0) {
                this.velocity.y -= slowDownForce;
            } else {
                this.animator.trigger("startRunning");
            }
        } else {
            this.acceleration.y = 0;
            if (this.velocity.y > 0) {
                this.velocity.y = Math.max(0, this.velocity.y - slowDownForce);
            } else if (this.velocity.y < 0) {
                this.velocity.y = Math.min(0, this.velocity.y + slowDownForce);
            }

            if (this.velocity.y == 0 && this.velocity.x == 0) {
                this.animator.trigger("stopRunning");
            }
        }

        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(this.velocity);
    }

    public boolean isDead() {
        return this.isDead;
    }
}
