package components.templates;

import components.Component;
import components.PlayerController;
import jade.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.Rigidbody2D;
import util.ResourcePool;

public class Flower extends Component {
    private transient Rigidbody2D rb;

    @Override
    public void onStart() {
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        ResourcePool.getSound("assets/sounds/powerup_appears.ogg").play();
        this.rb.setIsSensor();
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = obj.getComponent(PlayerController.class);
        if (playerController != null) {
            this.gameObject.destroy();
        }
    }
}
