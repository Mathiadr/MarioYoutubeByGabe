package components.cameras;

import components.Component;
import components.PlayerController;
import jade.Camera;
import jade.GameObject;
import jade.Window;
import org.joml.Vector4f;


/**
 * @author Mathias Ratdal
 */
public class FollowTargetCamera extends Component {
    private transient GameObject player;
    private transient Camera gameCamera;
    private transient float highestX = Float.MIN_VALUE;
    private transient float highestY = Float.MIN_VALUE;
    private transient float cameraBuffer = 1.5f;
    private transient float playerBuffer = 0.25f;

    private Vector4f skyColor = new Vector4f(92.0f / 255.0f, 148.0f / 255.0f, 252.0f / 255.0f, 1.0f);
    private Vector4f undergroundColor = new Vector4f(0, 0, 0, 1);

    public FollowTargetCamera(Camera gameCamera) {
        this.gameCamera = gameCamera;
    }

    @Override
    public void onStart() {
        this.player = Window.getScene().getGameObjectWith(PlayerController.class);
        this.gameCamera.clearColor.set(skyColor);
    }

    @Override
    public void onUpdate(float dt) {
        if (player != null) {
            gameCamera.position.x = player.transform.position.x - 3f;
            gameCamera.position.y = player.transform.position.y - 1.75f;
        }
    }
}
