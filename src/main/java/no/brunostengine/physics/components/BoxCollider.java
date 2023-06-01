package no.brunostengine.physics.components;

import no.brunostengine.components.Component;
import org.joml.Vector2f;

public class BoxCollider extends Component {
    private Vector2f halfSize = new Vector2f(0.25f, 0.25f);
    private Vector2f origin = new Vector2f();
    private Vector2f offset = new Vector2f();

    public Vector2f getOffset() {
        return this.offset;
    }

    public void setOffset(Vector2f newOffset) { this.offset.set(newOffset); }

    public void setOffset(float x, float y) { this.offset.set(new Vector2f(x, y)); }

    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    public void setHalfSize(float x, float y) {
        this.halfSize = new Vector2f(x, y);
    }

    public Vector2f getOrigin() {
        return this.origin;
    }
}
