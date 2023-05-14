package components.templates;

import components.Animator;
import components.DefaultTopDownPlayerController;
import brunostEngine.GameObject;
import brunostEngine.AssetBuilder;
import brunostEngine.Game;

public class QuestionBlock extends Block {
    private enum BlockType {
        Coin,
        Powerup,
        Invincibility
    }

    public BlockType blockType = BlockType.Coin;

    @Override
    void playerHit(DefaultTopDownPlayerController defaultTopDownPlayerController) {
        switch(blockType) {
            case Coin:
                doCoin(defaultTopDownPlayerController);
                break;
            case Powerup:
                doPowerup(defaultTopDownPlayerController);
                break;
            case Invincibility:
                doInvincibility(defaultTopDownPlayerController);
                break;
        }

        Animator animator = gameObject.getComponent(Animator.class);
        if (animator != null) {
            animator.trigger("setInactive");
            this.setInactive();
        }
    }

    private void doInvincibility(DefaultTopDownPlayerController defaultTopDownPlayerController) {
    }

    private void doPowerup(DefaultTopDownPlayerController defaultTopDownPlayerController) {
        spawnFlower();
    }

    private void doCoin(DefaultTopDownPlayerController defaultTopDownPlayerController) {
        GameObject coin = AssetBuilder.generateBlockCoin();
        coin.transform.position.set(this.gameObject.transform.position);
        coin.transform.position.y += 0.25f;
        Game.getScene().addGameObjectToScene(coin);
    }

    private void spawnMushroom() {
        GameObject mushroom = AssetBuilder.generateMushroom();
        mushroom.transform.position.set(gameObject.transform.position);
        mushroom.transform.position.y += 0.25f;
        Game.getScene().addGameObjectToScene(mushroom);
    }

    private void spawnFlower() {
        GameObject flower = AssetBuilder.generateFlower();
        flower.transform.position.set(gameObject.transform.position);
        flower.transform.position.y += 0.25f;
        Game.getScene().addGameObjectToScene(flower);
    }
}
