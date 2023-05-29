package no.brunostengine.components;

import no.brunostengine.KeyListener;
import no.brunostengine.Game;
import no.brunostengine.observers.EventSystem;
import no.brunostengine.observers.events.Event;
import no.brunostengine.observers.events.EventType;

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

        if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
            Game.get().onNotify(null, new Event(EventType.GameEngineStopPlay));
        }
    }
}
