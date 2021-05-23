package com.bindingofnewton.game.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.character.Player;

/**
 * Class for Health boost item; Heals the player
 */
public class HealthBoostItem extends Item{
    private float healthBoost = 2f;


    public HealthBoostItem(World world, float posX, float posY){
        super(world, posX, posY);
        body.setUserData(this);
    }


    @Override
    public void use(Player player) {
        player.setHealth(this.healthBoost);
    }


    @Override
    public void remove(Player player) {
        player.setHealth(-this.healthBoost);
    }

    public void render(SpriteBatch batch) {
        Sprite sprite;
        sprite = AssetsHandler.getInstance().getSingleSpriteFromFile("./character/healthitem.png");
        batch.draw(sprite, body.getPosition().x-7, body.getPosition().y-7, sprite.getWidth(), sprite.getHeight());
    }
}
