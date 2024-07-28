package components;

import graphics.Texture;
import org.joml.Vector2f;

public class Sprite {

    private float width, height;

    private Texture texture = null;
    private Vector2f[] texCords = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getTexCords() {
        return this.texCords;
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

    public void setTexCords(Vector2f[] texCords) {
        this.texCords = texCords;
    }

    public int getTexId() {
        return texture == null ? -1 : texture.getId();
    }
}