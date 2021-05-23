package com.bindingofnewton.game.character;

import com.bindingofnewton.game.assets.AssetsHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.BindingOfNewton;
import com.bindingofnewton.game.Bullet;
import com.bindingofnewton.game.Orientation;

import java.util.List;

public class BossEnemy extends Enemy {

    private boolean isShooting  = false;
    private boolean shootsBullet  = false;
    private Bullet bullet;

    public BossEnemy(World world, int startX, int startY, int speed) {
        super(world, Properties.BOSS, startX, startY, speed);
        animations.put(Orientation.DOWN, AssetsHandler.getInstance().getAnimation(Properties.BOSS.toString().toLowerCase() + "-run", SPEED_ANIMATION, 1f));
        animations.put(Orientation.LEFT, AssetsHandler.getInstance().getAnimation(Properties.BOSS.toString().toLowerCase() + "-standup", SPEED_ANIMATION, 1f));
        animations.get(Orientation.LEFT).setPlayMode(Animation.PlayMode.NORMAL);
    }

    @Override
    public void render(SpriteBatch batch, boolean isMoving) {
        move(new Vector2(0, 0));
        deltaTime += Gdx.graphics.getDeltaTime();

        if (deltaTime > 4){
            //System.out.println("Sollte schießen");
            shoot(batch);
            isShooting = true;
        }else{
            go(batch);
            isShooting = false;
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
            sprite = AssetsHandler.getInstance().getSingleSpriteFromFile("./character/boss/packed/damage.png");
            sprite.setScale(0.5f);
            if (orientation == Orientation.LEFT && !sprite.isFlipX()) {
                sprite.flip(true, false);
            }
            else if (orientation == Orientation.RIGHT && sprite.isFlipX()) {
                sprite.flip(true, false);
            }

            this.setNextDamageSprite(-1);
        }
        if (sprite.isFlipX()){
            batch.draw(sprite, body.getPosition().x, body.getPosition().y, sprite.getWidth(), sprite.getHeight());
        }else{
            batch.draw(sprite, body.getPosition().x-10, body.getPosition().y, sprite.getWidth(), sprite.getHeight());
        }
    }

    private void shoot(SpriteBatch batch) {
        if (animations.get(Orientation.LEFT).isAnimationFinished(deltaTime-3.9f)){
            isShooting = false;
            deltaTime = 0;
        }else{
            Sprite sprite;
            Animation<Sprite> animation = animations.get(Orientation.LEFT);
            sprite = AssetsHandler.getInstance().getAnimationFrame(animation, deltaTime);
            if (animation.getKeyFrameIndex(deltaTime-4) == 5 && !shootsBullet){
                List<Bullet> bullets = BindingOfNewton.getInstance().level.getCurrentRoom().getBullets();
                bullets.remove(bullet);

                if (orientation == Orientation.LEFT && !sprite.isFlipX()) {
                    bullet = new Bullet(
                            body.getWorld(),
                            null,
                            (int) (body.getPosition().x),
                            (int) (body.getPosition().y + sprite.getHeight() / 2));
                    bullet.setSprite(AssetsHandler.getInstance().getSingleSpriteFromFile("./character/boss/rat_rocket.png"));
                } else if (orientation == Orientation.RIGHT && sprite.isFlipX()) {
                    bullet = new Bullet(body.getWorld(),
                            null,
                            (int) (body.getPosition().x + 100),
                            (int) (body.getPosition().y + sprite.getHeight() / 2));
                    bullet.setSprite(AssetsHandler.getInstance().getSingleSpriteFromFile("./character/boss/rat_rocket.png"));
                }
                bullets.add(bullet);

                shootsBullet = true;
            }else if (animation.getKeyFrameIndex(deltaTime-4) == 6){
                shootsBullet = false;
            }

            if (bullet != null) {
                Vector2 vector = calculateVectorToPlayer(BindingOfNewton.getInstance().level.getCurrentRoom().getPlayer());
                sprite.setRotation(vector.angleDeg());
                bullet.setMovement(vector);
            }

            if (orientation == Orientation.LEFT && !sprite.isFlipX()) {
                sprite.flip(true, false);
            }else if (orientation == Orientation.RIGHT && sprite.isFlipX()) {
                sprite.flip(true, false);
            }

            batch.draw(sprite, body.getPosition().x, body.getPosition().y, sprite.getWidth(), sprite.getHeight());
        }
    }

    @Override
    public void calculateMoveToPlayer(Player player) {
        if (isShooting){
            //System.out.println("Moved nicht");
            move(new Vector2(0.0001f,0.0001f));
        }else{
            super.calculateMoveToPlayer(player);
        }
    }
}
