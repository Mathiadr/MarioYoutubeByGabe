package no.brunostengine.components.templates;

import no.brunostengine.components.Component;
import no.brunostengine.components.DefaultSideScrollerPlayerController;
import no.brunostengine.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import no.brunostengine.physics.components.Rigidbody;
import no.brunostengine.ResourcePool;

public class Flower extends Component {
    private transient Rigidbody rb;

    @Override
    public void onStart() {
        this.rb = gameObject.getComponent(Rigidbody.class);
        ResourcePool.getSound("assets/sounds/powerup_appears.ogg").play();
        this.rb.setIsSensor();
    }

    @Override
    public void onCollisionEnter(GameObject obj, Contact contact, Vector2f contactNormal) {
        DefaultSideScrollerPlayerController defaultSideScrollerPlayerController = obj.getComponent(DefaultSideScrollerPlayerController.class);
        if (defaultSideScrollerPlayerController != null) {
            this.gameObject.destroy();
        }
    }
}
