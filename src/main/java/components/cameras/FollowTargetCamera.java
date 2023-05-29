package components.cameras;

import components.BasePlayerController;
import components.Component;
import brunostengine.Camera;
import brunostengine.GameObject;
import brunostengine.Game;
import org.joml.Vector4f;


/**
 * @author Mathias Ratdal
 */
public class FollowTargetCamera extends Component {
    private transient GameObject player;
    private Camera gameCamera;

    private Vector4f skyColor = new Vector4f(92.0f / 255.0f, 148.0f / 255.0f, 252.0f / 255.0f, 1.0f);
    private Vector4f undergroundColor = new Vector4f(0, 0, 0, 1);

    public FollowTargetCamera(Camera gameCamera) {
        this.gameCamera = gameCamera;
    }

    @Override
    public void onStart() {
        this.player = Game.getScene().getGameObjectWith(BasePlayerController.class);
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
