package components;

import back.GameObject;
import back.KeyListener;
import back.Window;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;
import scenes.LevelSceneInitializer;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;
public class PlayerController extends Component {
    public float walkSpeed = 1.1f;
    public float slowDownForce = 0.16f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 2.1f);

    private transient Rigidbody2D rb;
    private transient StateMachine stateMachine;
    private transient float playerWidth = 0.266f;
    private transient float playerHeight = 0.371875f;
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead = false;
    private transient float deadMaxHeight = 0;
    private transient float deadMinHeight = 0;
    private transient boolean deadGoingUp = true;
    private transient SpriteRenderer spr;

    @Override
    public void start() {
        this.spr = gameObject.getComponent(SpriteRenderer.class);
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb.setGravityScale(0.0f);
    }

    @Override
    public void update(float dt) {
        if (this.stateMachine == null) return;
        if (isDead) {
            if (this.gameObject.transform.position.y < deadMaxHeight && deadGoingUp) {
                this.gameObject.transform.position.y += dt * walkSpeed / 2.0f;
            } else if (this.gameObject.transform.position.y >= deadMaxHeight && deadGoingUp) {
                deadGoingUp = false;
            } else if (!deadGoingUp && gameObject.transform.position.y > deadMinHeight) {
                this.rb.setBodyType(BodyType.Kinematic);
                this.acceleration.y =  /*0.55f **/  walkSpeed ;
                this.velocity.y += acceleration.y * dt;
                this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
                this.rb.setVelocity(this.velocity);
                this.rb.setAngularVelocity(0);
            } else if (!deadGoingUp && gameObject.transform.position.y <= deadMinHeight) {
                Window.changeScene(new LevelSceneInitializer());
            }
            return;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D)) {
            this.gameObject.transform.scale.x = playerWidth;

            this.acceleration.x =  /*0.55f **/  walkSpeed;

            if (this.velocity.x < 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x += slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.gameObject.transform.scale.x = playerWidth;
            this.acceleration.x =  /*0.55f **/  -walkSpeed;

            if (this.velocity.x > 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x -= slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        }  else if (KeyListener.isKeyPressed(GLFW_KEY_UP) || KeyListener.isKeyPressed(GLFW_KEY_W)) {
            this.gameObject.transform.scale.y = playerHeight;
//            this.acceleration.y =  /*0.55f **/  * walkSpeed;

            if (this.velocity.y < 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.y += slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN) || KeyListener.isKeyPressed(GLFW_KEY_S)) {
            this.gameObject.transform.scale.y = playerHeight;
            this.acceleration.y = /*0.55f **/ -walkSpeed;

            if (this.velocity.y > 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.y -= slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        }
         else {
            this.acceleration.x = 0;
            if (this.velocity.x > 0) {
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            } else if (this.velocity.x < 0) {
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }

            if (this.velocity.x == 0) {
                this.stateMachine.trigger("stopRunning");
            }
            this.acceleration.y = 0;
            if (this.velocity.y > 0) {
                this.velocity.y = Math.max(0, this.velocity.y - slowDownForce);
            } else if (this.velocity.y < 0) {
                this.velocity.y = Math.min(0, this.velocity.y + slowDownForce);
            }

            if (this.velocity.x == 0) {
                this.stateMachine.trigger("stopRunning");
            }
        }

        this.velocity.x += acceleration.y * dt;
        this.velocity.y += acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (isDead) return;

        if (collidingObject.getComponent(Box2DCollider.class) != null) {
            if (Math.abs(contactNormal.x) > 0.8f) {
                this.velocity.x = 0;
            } else if (contactNormal.y > 0.8f) {
                this.velocity.y = 0;
                this.acceleration.y = 0;
            }
        }
    }

    public boolean isDead() {
        return isDead;
    }


    public void die() {
        this.stateMachine.trigger("die");

        this.velocity.set(0, 0);
        this.acceleration.set(0, 0);
        this.rb.setVelocity(new Vector2f());
        this.isDead = true;
        this.rb.setIsSensor();
        AssetPool.getSound("assets/sounds/hurt.ogg").play();
        deadMaxHeight = this.gameObject.transform.position.y + 0.3f;
        this.rb.setBodyType(BodyType.Static);
        if (gameObject.transform.position.y > 0) {
            deadMinHeight = -0.25f;
        }
    }

    public boolean hasWon() {
        return false;
    }
}