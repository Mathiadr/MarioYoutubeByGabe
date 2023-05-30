package no.brunostengine.components.templates;

import no.brunostengine.KeyListener;
import no.brunostengine.Game;
import no.brunostengine.components.BasePlayerController;

import static org.lwjgl.glfw.GLFW.*;

public class TopDownPlayerController extends BasePlayerController {

    @Override
    public void onStart() {
        super.onStart();
        gravityEnabled = false;
    }

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

            if (this.velocity.y == 0 && this.velocity.x == 0) {
                this.animator.play("stopRunning");
            }
        }


        if (KeyListener.isKeyPressed(GLFW_KEY_UP) || KeyListener.isKeyPressed(GLFW_KEY_W)) {
            this.acceleration.y = walkSpeed;

            if (this.velocity.y < 0) {
                this.velocity.y += slowDownForce;
            } else {
                this.animator.play("startRunning");
            }
        }else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN) || KeyListener.isKeyPressed(GLFW_KEY_S)) {
            this.acceleration.y = -walkSpeed;

            if (this.velocity.y > 0) {
                this.velocity.y -= slowDownForce;
            } else {
                this.animator.play("startRunning");
            }
        } else {
            this.acceleration.y = 0;
            if (this.velocity.y > 0) {
                this.velocity.y = Math.max(0, this.velocity.y - slowDownForce);
            } else if (this.velocity.y < 0) {
                this.velocity.y = Math.min(0, this.velocity.y + slowDownForce);
            }

            if (this.velocity.y == 0 && this.velocity.x == 0) {
                this.animator.play("stopRunning");
            }
        }

        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.maxVelocity.x), -this.maxVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.maxVelocity.y), -this.maxVelocity.y);
        this.rb.setVelocity(this.velocity);
    }
}
