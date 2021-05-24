package com.bindingofnewton.game.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.assets.AssetsHandler;

public class BossBullet extends Bullet {

    private long startBullet;

    public BossBullet(World world, AssetsHandler.PlayerName playerName, int startX, int startY) {
        super(world, playerName, startX, startY);

        speed = 200;

        startBullet = System.currentTimeMillis();
    }

    @Override
    public void setMovement(Vector2 vector) {
        long deltaTime = System.currentTimeMillis();
        if (deltaTime - startBullet < 500){
            vector.y = 0;
        }
        super.setMovement(vector);
    }
}
