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
        super(world, AssetsHandler.EnemyProperties.BOSS, startX, startY, speed);
        animations.put(Orientation.DOWN, AssetsHandler.getInstance().getAnimation(AssetsHandler.EnemyProperties.BOSS.toString().toLowerCase() + "-run", SPEED_ANIMATION, 1f));
    }

    @Override
    public void render(SpriteBatch batch, boolean isMoving) {
        move(new Vector2(0, 0));
        deltaTime += Gdx.graphics.getDeltaTime();
        Sprite sprite;
        if (this.getNextDamageSprite() == 0){
            sprite = AssetsHandler.getInstance().getAnimationFrame(animations.get(Orientation.DOWN), deltaTime + animation_offset);
            if (orientation == Orientation.LEFT && !sprite.isFlipX()) {
                sprite.flip(true, false);
            }
            else if (orientation == Orientation.RIGHT && sprite.isFlipX())
                sprite.flip(true, false);
        }else{
            sprite = AssetsHandler.getInstance().getSingleSpriteFromFile("./character/newton/newton-damage.png");
            sprite.setScale(0.5f);

            this.setNextDamageSprite(-1);
        }

        if (sprite.isFlipX()){
            batch.draw(sprite, body.getPosition().x, body.getPosition().y, sprite.getWidth(), sprite.getHeight());
        }else{
            batch.draw(sprite, body.getPosition().x-10, body.getPosition().y, sprite.getWidth(), sprite.getHeight());
        }

    }
}
