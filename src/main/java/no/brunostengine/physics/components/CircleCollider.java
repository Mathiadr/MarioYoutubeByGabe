package no.brunostengine.physics.components;

import no.brunostengine.components.Component;
import no.brunostengine.Game;
import org.joml.Vector2f;

public class CircleCollider extends Component {
    private float radius = 1f;
    private transient boolean resetFixtureNextFrame = false;
    protected Vector2f offset = new Vector2f();

    public float getRadius() {
        return radius;
    }

    public Vector2f getOffset() {
        return this.offset;
    }

    public void setOffset(Vector2f newOffset) { this.offset.set(newOffset); }

    public void setRadius(float radius) {
        resetFixtureNextFrame = true;
        this.radius = radius;
    }

    @Override
    public void onUpdate(float dt) {
        if (resetFixtureNextFrame) {
            resetFixture();
        }
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
                Game.getPhysics().resetCircleCollider(rb, this);
            }
        }
    }
}
