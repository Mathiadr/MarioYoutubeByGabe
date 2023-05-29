package no.brunostengine;

import no.brunostengine.components.Animation;
import no.brunostengine.components.BasePlayerController;
import no.brunostengine.components.Spritesheet;

import java.util.ArrayList;

public interface CharacterBuilder {

    String getName();
    Spritesheet getSpritesheet(float width, float height);
    ArrayList<Animation> getAnimations();
    boolean gravityEnabled();
    BasePlayerController getPlayerController();
}
