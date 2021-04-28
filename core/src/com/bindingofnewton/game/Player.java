package com.bindingofnewton.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Entity{
    Animation<TextureRegion> up;
    Animation<TextureRegion> down;
    Animation<TextureRegion> left;
    Animation<TextureRegion> right;
    TextureAtlas textureAtlas;

    private float deltaTime = 0f;
    private float SPEEDANIMATION = 0.04f;

    public Player(World world, int startX, int startY, Sprite[] sprites) {
        super(world, startX, startY, sprites);
        textureAtlas = new TextureAtlas("./newton_run/packed/newton-run.atlas");

        up = new Animation<TextureRegion>(SPEEDANIMATION, textureAtlas.findRegions("run-back"), Animation.PlayMode.LOOP);
        down = new Animation<TextureRegion>(SPEEDANIMATION, textureAtlas.findRegions("run-front"), Animation.PlayMode.LOOP);
        right = new Animation<TextureRegion>(SPEEDANIMATION, textureAtlas.findRegions("run-right"), Animation.PlayMode.LOOP);
        left = new Animation<TextureRegion>(SPEEDANIMATION, textureAtlas.findRegions("run-left"), Animation.PlayMode.LOOP);
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

    /*
    public Player(World world, int startX, int startY, Sprite sprite) {
        super(world, startX, startY, sprite);
    }
     */

}
