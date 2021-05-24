package com.bindingofnewton.game.character;

import com.bindingofnewton.game.assets.AssetsHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.BindingOfNewton;
import com.bindingofnewton.game.bullets.BossBullet;
import com.bindingofnewton.game.bullets.Bullet;
import com.bindingofnewton.game.Orientation;

import java.util.List;

public class BossEnemy extends Enemy {

    private boolean isShooting  = false;
    private boolean shootsBullet  = false;
    private BossBullet bullet;

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
            shoot(batch);
            isShooting = true;
        }else{
            go(batch);
            isShooting = false;
        }

        if (bullet != null) {
            Vector2 vector = calculateVectorToPlayer(BindingOfNewton.getInstance().level.getCurrentRoom().getPlayer());
            vector = vector.scl(bullet.getSpeed() / vector.len());
            bullet.setMovement(vector);
            bullet.getSprite().setRotation(bullet.getMovement().angleDeg());
        }
    }

    /**
     * The BossEnemy goes
     * @param batch
     */
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

    /**
     * The BossEnemy shoots
     * @param batch
     */
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
                    bullet = new BossBullet(
                            body.getWorld(),
                            null,
                            (int) (body.getPosition().x),
                            (int) (body.getPosition().y + sprite.getHeight() / 2));
                    bullet.setSprite(AssetsHandler.getInstance().getSingleSpriteFromFile("./character/boss/rat_rocket.png"));
                } else if (orientation == Orientation.RIGHT && sprite.isFlipX()) {
                    bullet = new BossBullet(body.getWorld(),
                            null,
                            (int) (body.getPosition().x + 100),
                            (int) (body.getPosition().y + sprite.getHeight() / 2));
                    bullet.setSprite(AssetsHandler.getInstance().getSingleSpriteFromFile("./character/boss/rat_rocket.png"));
                    bullet.getSprite().setX(bullet.getSprite().getX()-100);
                }
                bullet.getSprite().setFlip(true, false);
                bullets.add(bullet);

                shootsBullet = true;
            }else if (animation.getKeyFrameIndex(deltaTime-4) == 6){
                shootsBullet = false;
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
            move(new Vector2(0.0001f,0.0001f));
        }else{
            Vector2 move = calculateVectorToPlayer(player);
            //move.x *= -1;
            //move.y *= -1;

            // Change length of vector to the speed of the enemy
            move = move.scl(this.getSpeed() / move.len());

            this.move(move);
            setLastPathChange(System.currentTimeMillis());
        }
    }
}
