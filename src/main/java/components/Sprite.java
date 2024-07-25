package components;

import graphics.Texture;
import org.joml.Vector2f;

public class Sprite {

    private Texture tex;
    private Vector2f[] texCords;

    public Sprite(Texture tex){
        this.tex = tex;
        Vector2f[] texCords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        this.texCords = texCords;
    }

    public Sprite(Texture tex, Vector2f[] texCords) {
        this.tex = tex;
        this.texCords = texCords;
    }

    public Texture getTexture() {
        return this.tex;
    }

    public Vector2f[] getCords() {
        return this.texCords;
    }

}
