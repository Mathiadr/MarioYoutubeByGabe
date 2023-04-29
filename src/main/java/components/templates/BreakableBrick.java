package components.templates;

import components.DefaultPlayerController;
import util.ResourcePool;

public class BreakableBrick extends Block {

    @Override
    void playerHit(DefaultPlayerController defaultPlayerController) {
        ResourcePool.getSound("assets/sounds/break_block.ogg").play();
        gameObject.destroy();
    }
}
