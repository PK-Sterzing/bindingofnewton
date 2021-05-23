package com.bindingofnewton.game.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.character.Player;

/**
 * Class for speed-boost item; Makes the player faster
 */
public class SpeedBoostItem extends Item{
    private static int amountUsed = 0;
    private int speedBoost = 100;

    public SpeedBoostItem(World world, float posX, float posY){
        super(world, posX, posY);
        body.setUserData(this);
    }


    @Override
    public void use(Player player) {
        if(amountUsed == 0) {
            player.setSpeed(player.getSpeed() + this.speedBoost);
            amountUsed = 1;
        }
    }

    @Override
    public void remove(Player player) {
        player.setSpeed(player.getSpeed() - this.speedBoost);
    }

    @Override
    public void render(SpriteBatch batch) {
        Sprite sprite;
        sprite = AssetsHandler.getInstance().getSingleSpriteFromFile("./character/speeditem.png");
        batch.draw(sprite, body.getPosition().x-7, body.getPosition().y-7, sprite.getWidth(), sprite.getHeight());
    }
}
