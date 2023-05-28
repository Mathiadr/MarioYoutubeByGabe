package components.templates;

import components.DefaultSideScrollerPlayerController;
import brunostEngine.ResourcePool;

public class BreakableBrick extends Block {

    @Override
    void playerHit(DefaultSideScrollerPlayerController defaultSideScrollerPlayerController) {
        ResourcePool.getSound("assets/sounds/break_block.ogg").play();
        gameObject.destroy();
    }
}
