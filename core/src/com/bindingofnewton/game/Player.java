package com.bindingofnewton.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Entity{

    public Player(World world, int startX, int startY, Sprite[] sprites) {
        super(world, startX, startY, sprites);
    }

    /*
    public Player(World world, int startX, int startY, Sprite sprite) {
        super(world, startX, startY, sprite);
    }
     */

}
