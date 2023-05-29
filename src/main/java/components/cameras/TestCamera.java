package components.cameras;

import brunostengine.Camera;
import brunostengine.Game;
import brunostengine.GameObject;
import components.BasePlayerController;
import components.Component;
import org.joml.Vector4f;

public class TestCamera extends Component {
    private transient GameObject player;
    private Camera gameCamera;

    private Vector4f skyColor = new Vector4f(0f , 0f, 0f, 0f);
    private Vector4f undergroundColor = new Vector4f(0, 0, 0, 1);

    public TestCamera(Camera gameCamera) {
        this.gameCamera = gameCamera;
    }

    @Override
    public void onStart() {
        this.gameCamera.clearColor.set(skyColor);
        this.player = Game.getScene().getGameObjectWith(BasePlayerController.class);
        if (player != null) {
            gameCamera.position.x = player.transform.position.x - 1f;
            gameCamera.position.y = player.transform.position.y - 1.75f;
        }
    }

    @Override
    public void onUpdate(float dt) {

    }
}
