package components;

import back.Camera;
import back.GameObject;
import back.Window;
import org.joml.Vector4f;

public class GameCamera extends Component {
    private transient GameObject player;
    private transient Camera gameCamera;
    private transient float highestX = Float.MIN_VALUE;
    private transient float yLevel = 0.0f;
    private transient float cameraBuffer = 1.5f;
    private transient float playerBuffer = 0.25f;

    private Vector4f backgroundColor = new Vector4f(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 1.0f);

    public GameCamera(Camera gameCamera) {
        this.gameCamera = gameCamera;
    }

    @Override
    public void start() {
        this.player = Window.getScene().getGameObject("Player");
        //this.gameCamera.clearColor.set(backgroundColor);
        //this.yLevel = this.gameCamera.position.y -
                //this.gameCamera.getProjectionSize().y - this.cameraBuffer;
    }

    @Override
    public void update(float dt) {
        if (player != null && !player.getComponent(PlayerController.class).hasWon()) {
            gameCamera.position.x = Math.max(player.transform.position.x - 2.5f, highestX);
            highestX = Math.max(highestX, gameCamera.position.x);

            if (player.transform.position.y < -playerBuffer) {
                this.gameCamera.position.y = yLevel;
            } else if (player.transform.position.y >= 0.0f) {
                this.gameCamera.position.y = 0.0f;
                this.gameCamera.clearColor.set(backgroundColor);
            }
        }
    }
}