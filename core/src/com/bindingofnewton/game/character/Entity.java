package com.bindingofnewton.game.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.Orientation;

import java.util.ArrayList;


public abstract class Entity {
    protected Body body;
    protected Polygon polygon;

    protected int x;
    protected int y;
    protected int characterHeight;
    protected int characterWidth;

    protected ArrayList<Sprite> sprites;

    protected Orientation orientation;


    protected int speed = 100;
    protected float health;


    protected boolean isDead = false;


    //<editor-fold desc="Constructors">-



    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    /**
     * Gets the x-coordinate of the Entity
     * @return x-coordinate
     */
    public int getX() {
        return x;
    }

    public Body getBody() {
        return body;
    }

    /**
     * Gets the y-coordinate of the Entity
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns sprite for given orientation
     * Returns first sprite if only one is present
     * @return Sprite
     */
    public Sprite getSprite(){
        if (sprites.size() < 4){
            return sprites.get(0);
        }
        switch (orientation){
            case DOWN: return sprites.get(1);
            case LEFT: return sprites.get(2);
            case RIGHT: return sprites.get(3);
            case UP:
            default: return sprites.get(0);
        }
    }

    /**
     * Returns the health of the current entity
     * @return health
     */
    public float getHealth() {
        return health;
    }

    //</editor-fold>

    /**
     * Adds/subtracts the health by delta
     * @param delta
     */
    public void setHealth(float delta){
        health += delta;
    }

    public int getSpeed() {
        return speed;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
