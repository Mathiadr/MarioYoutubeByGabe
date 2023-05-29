package no.brunostengine.components.templates;

import no.brunostengine.components.Component;
import no.brunostengine.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class Flagpole extends Component {
    private boolean isTop = false;

    public Flagpole(boolean isTop) {
        this.isTop = isTop;
    }

    @Override
    public void onCollisionEnter(GameObject obj, Contact contact, Vector2f contactNormal) {
        MarioController playerController = obj.getComponent(MarioController.class);
        if (playerController != null) {
            playerController.playWinAnimation(this.gameObject);
        }
    }
}
