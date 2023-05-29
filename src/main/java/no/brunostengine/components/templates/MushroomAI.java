package no.brunostengine.components.templates;

import no.brunostengine.components.Collideable;
import no.brunostengine.components.Component;
import no.brunostengine.components.DefaultSideScrollerPlayerController;
import no.brunostengine.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import no.brunostengine.physics.components.Rigidbody;
import no.brunostengine.ResourcePool;

public class MushroomAI extends Component {
    private transient boolean goingRight = true;
    private transient Rigidbody rb;
    private transient Vector2f speed = new Vector2f(1.0f, 0.0f);
    private transient float maxSpeed = 0.8f;
    private transient boolean hitPlayer = false;

    @Override
    public void onStart() {
        this.rb = gameObject.getComponent(Rigidbody.class);
        ResourcePool.getSound("assets/sounds/powerup_appears.ogg").play();
    }

    @Override
    public void onUpdate(float dt) {
        if (goingRight && Math.abs(rb.getVelocity().x) < maxSpeed) {
            rb.addVelocity(speed);
        } else if (!goingRight && Math.abs(rb.getVelocity().x) < maxSpeed) {
            rb.addVelocity(new Vector2f(-speed.x, speed.y));
        }
    }
    
    @Override 
    public void preSolve(GameObject obj, Contact contact, Vector2f contactNormal) {
        DefaultSideScrollerPlayerController defaultSideScrollerPlayerController = obj.getComponent(DefaultSideScrollerPlayerController.class);
        if (defaultSideScrollerPlayerController != null) {
            contact.setEnabled(false);
            if (!hitPlayer) {
                ResourcePool.getSound("assets/sounds/coin.ogg").play();
                this.gameObject.destroy();
                hitPlayer = true;
            }
        } else if (obj.getComponent(Collideable.class) == null) {
            contact.setEnabled(false);
            return;
        }

        if (Math.abs(contactNormal.y) < 0.1f) {
            goingRight = contactNormal.x < 0;
        }
    }
}
