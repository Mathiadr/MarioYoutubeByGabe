package brunostengine;

import components.Animation;
import components.BasePlayerController;
import components.Spritesheet;

import java.util.ArrayList;

public interface CharacterBuilder {

    String getName();
    Spritesheet getSpritesheet(float width, float height);
    ArrayList<Animation> getAnimations();
    boolean gravityEnabled();
    BasePlayerController getPlayerController();
}
