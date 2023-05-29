package components.templates;

import brunostEngine.Game;
import brunostEngine.KeyListener;
import components.BasePlayerController;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class PlayerBlockController extends BasePlayerController {

    @Override
    public void onUpdate(float deltaTime) {
        if (isDead) {
            Game.changeScene(Game.getCurrentSceneBuilder());
            return;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_UP) || KeyListener.isKeyPressed(GLFW_KEY_W)) {
            this.acceleration.y = walkSpeed;

            if (this.velocity.y < 0) {
                this.velocity.y += slowDownForce;
            }
        }else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN) || KeyListener.isKeyPressed(GLFW_KEY_S)) {
            this.acceleration.y = -walkSpeed;

            if (this.velocity.y > 0) {
                this.velocity.y -= slowDownForce;
            }
        } else {
            this.acceleration.y = 0;
            if (this.velocity.y > 0) {
                this.velocity.y = Math.max(0, this.velocity.y - slowDownForce);
            } else if (this.velocity.y < 0) {
                this.velocity.y = Math.min(0, this.velocity.y + slowDownForce);
            }
        }

        this.velocity.x += this.acceleration.x * deltaTime;
        this.velocity.y += this.acceleration.y * deltaTime;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.maxVelocity.x), -this.maxVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.maxVelocity.y), -this.maxVelocity.y);
        this.rb.setVelocity(this.velocity);
    }
}
