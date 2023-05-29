package physics.components;

import components.Component;
import brunostengine.Game;
import org.joml.Vector2f;

public class CapsuleCollider extends Component {
    private transient CircleCollider bottomCircle = new CircleCollider();
    private transient BoxCollider box = new BoxCollider();
    private transient boolean resetFixtureNextFrame = false;

    public float width = 0.1f;
    public float height = 0.2f;
    public Vector2f offset = new Vector2f();

    @Override
    public void onStart() {
        this.bottomCircle.gameObject = this.gameObject;
        this.box.gameObject = this.gameObject;
        recalculateColliders();
    }

    @Override
    public void onUpdate(float dt) {
        if (resetFixtureNextFrame) {
            resetFixture();
        }
    }

    public void setWidth(float newVal) {
        this.width = newVal;
        recalculateColliders();
        resetFixture();
    }

    public void setHeight(float newVal) {
        this.height = newVal;
        recalculateColliders();
        resetFixture();
    }

    public void resetFixture() {
        if (Game.getPhysics().isLocked()) {
            resetFixtureNextFrame = true;
            return;
        }
        resetFixtureNextFrame = false;

        if (gameObject != null) {
            Rigidbody rb = gameObject.getComponent(Rigidbody.class);
            if (rb != null) {
                Game.getPhysics().resetCapsuleCollider(rb, this);
            }
        }
    }

    public void recalculateColliders() {
        float circleRadius = width / 2.0f;
        float boxHeight = height - circleRadius;
        bottomCircle.setRadius(circleRadius);
        bottomCircle.setOffset(new Vector2f(offset).sub(0, (height - circleRadius * 2.0f) / 2.0f));
        box.setHalfSize(new Vector2f(width - 0.01f, boxHeight));
        box.setOffset(new Vector2f(offset).add(0, (height - boxHeight) / 2.0f));
    }

    public CircleCollider getBottomCircle() {
        return bottomCircle;
    }

    public BoxCollider getBox() {
        return box;
    }
}
