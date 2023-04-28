package components.templates;

import components.MarioController;
import components.PlayerController;
import util.ResourcePool;

public class BreakableBrick extends Block {

    @Override
    void playerHit(PlayerController playerController) {
        ResourcePool.getSound("assets/sounds/break_block.ogg").play();
        gameObject.destroy();
    }
}
