package components.templates;

import components.DefaultTopDownPlayerController;
import util.ResourcePool;

public class BreakableBrick extends Block {

    @Override
    void playerHit(DefaultTopDownPlayerController defaultTopDownPlayerController) {
        ResourcePool.getSound("assets/sounds/break_block.ogg").play();
        gameObject.destroy();
    }
}
