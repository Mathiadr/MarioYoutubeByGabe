package components.cameras;

import components.Component;
import components.DefaultTopDownPlayerController;
import brunostEngine.Camera;
import brunostEngine.GameObject;
import brunostEngine.Game;
import org.joml.Vector4f;

public class GameCamera extends Component {
    private transient GameObject player;
    private transient Camera gameCamera;
    private transient float highestX = Float.MIN_VALUE;
    private transient float cameraBuffer = 1.5f;

    private Vector4f skyColor = new Vector4f(92.0f / 255.0f, 148.0f / 255.0f, 252.0f / 255.0f, 1.0f);

    public GameCamera(Camera gameCamera) {
        this.gameCamera = gameCamera;
    }

    @Override
    public void onStart() {
        this.player = Game.getScene().getGameObjectWith(DefaultTopDownPlayerController.class);
        this.gameCamera.clearColor.set(skyColor);
    }

    @Override
    public void onUpdate(float dt) {
        if (player != null) {
            gameCamera.position.x = Math.max(player.transform.position.x - 2.5f, highestX);
            highestX = Math.max(highestX, gameCamera.position.x);
        }
    }
}
