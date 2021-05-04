package com.bindingofnewton.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.assets.AssetsHandler;

public class Enemy extends Entity {

    Animation<TextureRegion> up;
    TextureAtlas textureAtlas;

    private float deltaTime = 0f;
    private final float SPEED_ANIMATION = 0.04f;

    public Enemy(World world, int startX, int startY, Sprite[] sprites) {
        super(world, startX, startY, sprites);
        textureAtlas = new TextureAtlas(AssetsHandler.NEWTON_RUN);

        up = new Animation<TextureRegion>(SPEED_ANIMATION, textureAtlas.findRegions("run-back"), Animation.PlayMode.LOOP);

        health = 3.5f;
    }

    public TextureRegion getTextureRegion() {
        deltaTime += Gdx.graphics.getDeltaTime();
        switch (orientation) {
            case UP: return up.getKeyFrame(deltaTime, true);
            default: return up.getKeyFrame(deltaTime, true);
        }
    }
}
