package com.bindingofnewton.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entity {

    private final BodyDef def;
    private final Body body;

    protected int x;
    protected int y;

    protected Orientation orientation;
    protected int speed;

    protected int health;

    public Entity(World world, int startX, int startY){
        x = startX;
        y = startY;
        def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x ,y);
        body = world.createBody(def);

        orientation = Orientation.DOWN;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(Vector2 vector){
        float x = vector.x;
        float y =  vector.y;

        if (y>0){
            orientation = Orientation.UP;
        }else if (y<0){
            orientation = Orientation.DOWN;
        }

        if (x>0){
            orientation = Orientation.RIGHT;
        }else if(x<0){
            orientation = Orientation.LEFT;
        }

        this.x += x;
        this.y += y;

        body.setTransform(vector, 0);
    }


}
