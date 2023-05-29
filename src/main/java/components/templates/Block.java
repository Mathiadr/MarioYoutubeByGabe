package components.templates;

import components.Component;
import components.DefaultSideScrollerPlayerController;
import brunostengine.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import brunostengine.ResourcePool;

public abstract class Block extends Component {
    private transient boolean bopGoingUp = true;
    private transient boolean doBopAnimation = false;
    private transient Vector2f bopStart;
    private transient Vector2f topBopLocation;
    private transient boolean active = true;

    public float bopSpeed = 0.4f;

    @Override
    public void onStart() {
        this.bopStart = new Vector2f(this.gameObject.transform.position);
        this.topBopLocation = new Vector2f(bopStart).add(0.0f, 0.02f);
    }

    @Override
    public void onUpdate(float dt) {
        if (doBopAnimation) {
            if (bopGoingUp) {
                if (this.gameObject.transform.position.y < topBopLocation.y) {
                    this.gameObject.transform.position.y += bopSpeed * dt;
                } else {
                    bopGoingUp = false;
                }
            } else {
                if (this.gameObject.transform.position.y > bopStart.y) {
                    this.gameObject.transform.position.y -= bopSpeed * dt;
                } else {
                    this.gameObject.transform.position.y = this.bopStart.y;
                    bopGoingUp = true;
                    doBopAnimation = false;
                }
            }
        }
    }

    @Override
    public void onCollisionEnter(GameObject obj, Contact contact, Vector2f contactNormal) {
        DefaultSideScrollerPlayerController defaultSideScrollerPlayerController = obj.getComponent(DefaultSideScrollerPlayerController.class);
        if (active && defaultSideScrollerPlayerController != null && contactNormal.y < -0.8f) {
            doBopAnimation = true;
            ResourcePool.getSound("assets/sounds/bump.ogg").play();
            playerHit(defaultSideScrollerPlayerController);
        }
    }

    public void setInactive() { this.active = false; }

    abstract void playerHit(DefaultSideScrollerPlayerController defaultSideScrollerPlayerController);
}
