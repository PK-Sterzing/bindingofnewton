package com.bindingofnewton.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.assets.AssetsHandler;

public class BossEnemy extends Enemy{

    public BossEnemy(World world, int startX, int startY, int speed) {
        super(world, AssetsHandler.EnemyProperties.BOSS_RAT, startX, startY, speed);
    }

    @Override
    public void render(SpriteBatch batch, boolean isMoving) {
        move(new Vector2(0, 0));
        deltaTime += Gdx.graphics.getDeltaTime();
        Sprite sprite;
        if (this.getNextDamageSprite() == 0){
            sprite = AssetsHandler.getInstance().getAnimationFrame(animations.get(Orientation.DOWN), deltaTime + animation_offset);
        }else{
            sprite = AssetsHandler.getInstance().getSingleSpriteFromFile("./character/newton/newton-damage.png");
            sprite.setScale(0.5f);
            this.setNextDamageSprite(-1);
        }
        batch.draw(sprite, body.getPosition().x-10, body.getPosition().y-10, sprite.getWidth(), sprite.getHeight());
    }
}
