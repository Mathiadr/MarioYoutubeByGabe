package no.brunostengine;

import no.brunostengine.GameObject;
import no.brunostengine.components.SpriteRenderer;
import no.brunostengine.renderer.Texture;
import org.joml.Vector2f;

/**
 * The Sprite class represents the visible image in a Scene.
 * For it to be rendered within a scene, however,
 * the parent {@link GameObject} requires a {@link SpriteRenderer}
 * to be assigned to it, with the Sprite in it.
 *
 */
public class Sprite {

    private float width, height;

    private Texture texture = null;
    private Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setTexture(Texture tex) {
        this.texture = tex;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    public int getTexId() {
        return texture == null ? -1 : texture.getId();
    }
}
