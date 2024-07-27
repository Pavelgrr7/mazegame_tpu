package components;

import graphics.Texture;
import org.joml.Vector2f;

public class Sprite {

    private float width, height;
    private Texture tex = null;
    private Vector2f[] texCords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };;


    public Texture getTexture() {
        return this.tex;
    }
    public Vector2f[] getCords() {
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

    public int getTexId() {
        return tex == null ? -1 : tex.getId();
    }
    public void setHeight(float height) {
        this.height = height;
    }

    public void setTexture(Texture t) {
        this.tex = t;
    }
    public void setTexCords(Vector2f[] tc) {
        this.texCords = tc;
    }

}
