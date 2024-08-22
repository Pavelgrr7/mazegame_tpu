package physics2d.components;

import components.Component;
import back.Window;
import org.joml.Vector2f;
import physics2d.Physics2D;
import graphics.DebugDraw;

import components.Component;

public class CircleCollider extends Collider {
    private float radius = 1f;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}