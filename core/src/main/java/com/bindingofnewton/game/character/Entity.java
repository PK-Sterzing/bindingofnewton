package com.bindingofnewton.game.character;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.Orientation;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class Entity {
    protected Body body;
    protected Polygon polygon;

    protected int characterHeight;
    protected int characterWidth;
    protected float deltaTime = 0f;

    protected ArrayList<Sprite> sprites;
    protected HashMap<Orientation, Animation<Sprite>> animations = new HashMap<>();

    protected Orientation orientation;


    protected int speed = 50;
    protected float health;

    protected long lastSustainedDamage = 0;

    // When the entity is hit, set him invincible for a few milliseconds
    protected long invincibilityCooldown = 500;

    protected int fireRate = 500;


    protected boolean isDead = false;

    // If over 0 set damage sprite for one frame
    protected int nextDamageSprite = 0;


    //<editor-fold desc="Getter and Setter">


    public Body getBody() {
        return body;
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
        if(delta < 0){
            this.setNextDamageSprite(1);
        }
        health += delta;
        if (health > 10) health = 10;
    }

    /**
     * Get speed
     * @return speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets speed of entity
     * @param speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    /**
     * Get Polygon shape of hitbox
     * @return Polygon
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * Get current Orientation of Entity
     * @return Orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Set Orientation
     * @param orientation
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Check if Entity is dead
     * Check if dead flag is set
     * @return
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Set dead flag
     * @param dead
     */
    public void setDead(boolean dead) {
        isDead = dead;
    }

    /**
     * Renders the entity
     * @param batch the batch to be rendered with
     * @param isMoving true - the entity is moving
     *                 false - the entity is not moving
     */
    public abstract void render(SpriteBatch batch, boolean isMoving);


    public long getInvincibilityCooldown() {
        return invincibilityCooldown;
    }

    public long getLastSustainedDamage() {
        return this.lastSustainedDamage;
    }

    public void setLastSustainedDamage(long lastSustainedDamage) {
        this.lastSustainedDamage = lastSustainedDamage;
    }

    public int getNextDamageSprite() {
        return nextDamageSprite;
    }

    public void setNextDamageSprite(int delta) {
        if(delta > 0){
            delta =+ 15;
        }
        this.nextDamageSprite = this.nextDamageSprite + delta;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }


}
