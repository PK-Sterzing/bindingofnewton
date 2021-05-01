package com.bindingofnewton.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.assets.AssetsHandler;

public class Player extends Entity {
    Animation<TextureRegion> up;
    Animation<TextureRegion> down;
    Animation<TextureRegion> left;
    Animation<TextureRegion> right;
    TextureAtlas textureAtlas;

    private float deltaTime = 0f;
    private final float SPEED_ANIMATION = 0.04f;

    public Player(World world, int startX, int startY, Sprite[] sprites) {
        super(world, startX, startY, sprites);
        textureAtlas = new TextureAtlas(AssetsHandler.NEWTON_RUN);

        up = new Animation<TextureRegion>(SPEED_ANIMATION, textureAtlas.findRegions("run-back"), Animation.PlayMode.LOOP);
        down = new Animation<TextureRegion>(SPEED_ANIMATION, textureAtlas.findRegions("run-front"), Animation.PlayMode.LOOP);
        right = new Animation<TextureRegion>(SPEED_ANIMATION, textureAtlas.findRegions("run-right"), Animation.PlayMode.LOOP);
        left = new Animation<TextureRegion>(SPEED_ANIMATION, textureAtlas.findRegions("run-left"), Animation.PlayMode.LOOP);

        health = 3.5f;
    }

    public TextureRegion getTextureRegion() {
        deltaTime += Gdx.graphics.getDeltaTime();
        switch (orientation) {
            case UP: return up.getKeyFrame(deltaTime, true);
            case DOWN: return down.getKeyFrame(deltaTime, true);
            case LEFT: return left.getKeyFrame(deltaTime, true);
            case RIGHT: return right.getKeyFrame(deltaTime, true);
            // TODO: Add also for up down right and left animations!
            default: return up.getKeyFrame(deltaTime, true);
        }
    }

    public Sprite[] getHealthSprites(){
        Sprite[] sprites = new Sprite[(int) Math.ceil(health)];

        for (int i=0; i<Math.ceil(health); i++){
            if (i+1 == Math.ceil(health) && health % i < 1){
                sprites[i] = AssetsHandler.getInstance().getSingleSprite("half_heart.png");
            }else{
                sprites[i] = AssetsHandler.getInstance().getSingleSprite("full_heart.png");
            }
        }

        return sprites;
    }

    /*
    public Player(World world, int startX, int startY, Sprite sprite) {
        super(world, startX, startY, sprite);
    }
     */

}
