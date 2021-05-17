package com.bindingofnewton.game.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class BossEnemy extends Enemy{

    public BossEnemy(World world, int startX, int startY, int speed, Sprite sprite) {
        super(world, startX, startY, speed, sprite);

    }

    @Override
    public void render(SpriteBatch batch, boolean isMoving) {
        move(new Vector2(0, 0));
        getSprite().draw(batch);
    }
}
