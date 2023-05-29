package no.brunostengine.components.templates;

import no.brunostengine.components.DefaultSideScrollerPlayerController;
import no.brunostengine.ResourcePool;

public class BreakableBrick extends Block {

    @Override
    void playerHit(DefaultSideScrollerPlayerController defaultSideScrollerPlayerController) {
        ResourcePool.getSound("assets/sounds/break_block.ogg").play();
        gameObject.destroy();
    }
}
