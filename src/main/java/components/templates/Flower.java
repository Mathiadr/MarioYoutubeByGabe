package components.templates;

import components.Component;
import components.DefaultSideScrollerPlayerController;
import brunostEngine.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.Rigidbody;
import brunostEngine.ResourcePool;

public class Flower extends Component {
    private transient Rigidbody rb;

    @Override
    public void onStart() {
        this.rb = gameObject.getComponent(Rigidbody.class);
        ResourcePool.getSound("assets/sounds/powerup_appears.ogg").play();
        this.rb.setIsSensor();
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        DefaultSideScrollerPlayerController defaultSideScrollerPlayerController = obj.getComponent(DefaultSideScrollerPlayerController.class);
        if (defaultSideScrollerPlayerController != null) {
            this.gameObject.destroy();
        }
    }
}
