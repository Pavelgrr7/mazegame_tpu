package components;

import back.Component;
import graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Sprite sprite;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new  Vector4f(1,1,1,1);
    }


    public Vector4f getColor() {
        return this.color;
    }

    @Override
    public void start() {
//        System.out.println("Starting gameobjs");
    }

    @Override
    public void update(float dt) {

    }

    public Texture getTexture(){
        return sprite.getTexture();
    }

    public Vector2f[] getTexCords() {
        return sprite.getCords();
    }
}
