package physics2d.components;

import components.Component;
import back.Window;
import org.joml.Vector2f;
import physics2d.Physics2D;
import graphics.DebugDraw;

import components.Component;

public class CircleCollider extends Component {
    private float radius = 1f;
    private Vector2f offset = new Vector2f();

    public Vector2f getOffset() {
        return this.offset;
    }

    public void setOffset(Vector2f newOffset) { this.offset.set(newOffset); }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void editorUpdate(float dt) {
        //｡°(°.◜ᯅ◝°)°｡
        if (this.gameObject == null) return;

        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.addCircle(center, this.radius);
    }
}