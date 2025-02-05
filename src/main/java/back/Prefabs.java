package back;

import components.*;
import org.joml.Vector2f;
import util.AssetPool;
import physics2d.components.*;
import physics2d.enums.BodyType;
import components.PlayerController;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject block = Window.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

    public static GameObject generatePlayer() {
        Spritesheet playerSprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");
        GameObject player = generateSpriteObject(playerSprites.getSprite(0), 0.266f, 0.371875f);

        AnimationState run = new AnimationState();
        run.title = "Run";
        float defaultFrameTime = 0.2f;
        run.addFrame(playerSprites.getSprite(0), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(3), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.setLoop(true);

        AnimationState switchDirection = new AnimationState();
        switchDirection.title = "Switch Direction";
        switchDirection.addFrame(playerSprites.getSprite(4), 0.1f);
        switchDirection.setLoop(false);

        AnimationState idle = new AnimationState();
        idle.title = "Idle";
        idle.addFrame(playerSprites.getSprite(0), 0.1f);
        idle.setLoop(false);

        AnimationState die = new AnimationState();
        die.title = "Die";
        die.addFrame(playerSprites.getSprite(6), 0.1f);
        die.setLoop(false);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.addState(idle);
        stateMachine.addState(switchDirection);
        stateMachine.addState(die);

        stateMachine.setDefaultState(idle.title);
        stateMachine.addState(run.title, switchDirection.title, "switchDirection");
        stateMachine.addState(run.title, idle.title, "stopRunning");
        stateMachine.addState(switchDirection.title, idle.title, "stopRunning");
        stateMachine.addState(switchDirection.title, run.title, "startRunning");
        stateMachine.addState(idle.title, run.title, "startRunning");

        stateMachine.addState(run.title, die.title, "die");
        stateMachine.addState(switchDirection.title, die.title, "die");
        stateMachine.addState(idle.title, die.title, "die");

        player.addComponent(stateMachine);
        PillboxCollider pb = new PillboxCollider();
        pb.width = 0.40f;
        pb.height = 0.71f;

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);

        player.addComponent(rb);
        player.addComponent(pb);
        player.addComponent(new PlayerController());

        return player;
    }

    public static GameObject generateGhost() {
        Spritesheet sprites = AssetPool.getSpritesheet("assets/images/spritesheet2.png");
        GameObject ghost = generateSpriteObject(sprites.getSprite(0), 0.27f, 0.25f);


        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setMass(0.1f);
        rb.setFixedRotation(true);
        ghost.addComponent(rb);
        CircleCollider circle = new CircleCollider();
        circle.setRadius(0.12f);
        ghost.addComponent(circle);

        ghost.addComponent(new Ghost());

        return ghost;
    }
}