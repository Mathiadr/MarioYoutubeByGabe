package components;

import brunostEngine.GameObject;
import brunostEngine.KeyListener;
import brunostEngine.Game;
import util.Settings;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyControls extends Component {
    private float debounceTime = 0.2f;
    private float debounce = 0.0f;

    @Override
    public void onUpdate(float dt) {
        debounce -= dt;

        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) &&
                KeyListener.keyBeginPress(GLFW_KEY_S)) {
            Game.getScene().save();
        }
    }
}
