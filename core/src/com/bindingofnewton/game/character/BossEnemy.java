package com.bindingofnewton.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.assets.AssetsHandler;

public class BossEnemy extends Enemy{

    private float timeLastShot = 0;
    private boolean isShooting  = false;

    public BossEnemy(World world, int startX, int startY, int speed) {
        super(world, Properties.BOSS, startX, startY, speed);
        animations.put(Orientation.DOWN, AssetsHandler.getInstance().getAnimation(Properties.BOSS.toString().toLowerCase() + "-run", SPEED_ANIMATION, 1f));
        animations.put(Orientation.LEFT, AssetsHandler.getInstance().getAnimation(Properties.BOSS.toString().toLowerCase() + "-standup", SPEED_ANIMATION*2, 1f));
        animations.get(Orientation.LEFT).setPlayMode(Animation.PlayMode.NORMAL);
    }

    @Override
    public void render(SpriteBatch batch, boolean isMoving) {
        move(new Vector2(0, 0));
        deltaTime += Gdx.graphics.getDeltaTime();

        if (deltaTime - timeLastShot > 4){
            System.out.println("Sollte schießen");
            timeLastShot = deltaTime;
            isShooting = true;
        }

        if (isShooting){
            shoot(batch);
        }else{
            go(batch);
        }



    }

    private void go(SpriteBatch batch) {
        Sprite sprite;
        if (this.getNextDamageSprite() == 0){

            sprite = AssetsHandler.getInstance().getAnimationFrame(animations.get(Orientation.DOWN), deltaTime);
            if (orientation == Orientation.LEFT && !sprite.isFlipX()) {
                sprite.flip(true, false);
            }
            else if (orientation == Orientation.RIGHT && sprite.isFlipX()) {
                sprite.flip(true, false);
            }
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

    private void shoot(SpriteBatch batch) {
        if (animations.get(Orientation.LEFT).isAnimationFinished(deltaTime-timeLastShot)){
            isShooting = false;
            timeLastShot = deltaTime;
        }else{
            Sprite sprite;
            sprite = AssetsHandler.getInstance().getAnimationFrame(animations.get(Orientation.LEFT), deltaTime);

            if (orientation == Orientation.LEFT && !sprite.isFlipX()) {
                sprite.flip(true, false);
            }
            else if (orientation == Orientation.RIGHT && sprite.isFlipX()) {
                sprite.flip(true, false);
            }

            batch.draw(sprite, body.getPosition().x, body.getPosition().y, sprite.getWidth(), sprite.getHeight());
        }
    }

    @Override
    public void calculateMoveToPlayer(Player player) {
        if (isShooting){
            System.out.println("MOved nicht");
            move(new Vector2(0.0001f,0.0001f));
        }else{
            super.calculateMoveToPlayer(player);
        }
    }
}
