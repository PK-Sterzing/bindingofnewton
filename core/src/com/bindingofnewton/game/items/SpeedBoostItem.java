package com.bindingofnewton.game.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.character.Player;

public class SpeedBoostItem extends Item{
    private int speedBoost = 50;

    public SpeedBoostItem(World world, float posX, float posY){
        super(world, posX, posY);
        body.setUserData(this);
    }


    @Override
    public void use(Player player) {
        player.setSpeed(player.getSpeed() + this.speedBoost);
    }

    @Override
    public void remove(Player player) {
        player.setSpeed(player.getSpeed() - this.speedBoost);
    }

    @Override
    public void render(SpriteBatch batch) {
        Sprite sprite;
        sprite = AssetsHandler.getInstance().getSingleSpriteFromFile("./bullets/magic_bullet.png");
        batch.draw(sprite, body.getPosition().x, body.getPosition().y, sprite.getWidth(), sprite.getHeight());
    }
}
