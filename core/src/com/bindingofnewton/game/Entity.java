package com.bindingofnewton.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class Entity {

    private final Body body;

    protected int x;
    protected int y;

    protected Sprite[] sprites;

    protected Orientation orientation;
    protected int speed;

    protected int health;

    //<editor-fold desc="Constructors">-


    public Entity(World world, int startX, int startY, Sprite[] sprites) {
        this.sprites = sprites;
        x = startX;
        y = startY;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x ,y);


        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprites[0].getWidth()/2, sprites[0].getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();

        sprites[0].setPosition(body.getPosition().x, body.getPosition().y);
        sprites[1].setPosition(body.getPosition().x, body.getPosition().y);
        sprites[2].setPosition(body.getPosition().x, body.getPosition().y);
        sprites[3].setPosition(body.getPosition().x, body.getPosition().y);

        orientation = Orientation.DOWN;

		/*
		character = new Sprite(imgCharacterDown);
		character.setPosition(x, y);
		world = new World(new Vector2(0, 0), true);
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(character.getX(), character.getY());

		body = world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(character.getWidth()/2, character.getHeight()/2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;

		Fixture fixture = body.createFixture(fixtureDef);

		shape.dispose();
		 */
    }

    /*
    public Entity(World world, int startX, int startY, Sprite sprite) {
        //this(world, startX, startY);
        sprites = new Sprite[1];
        sprites[0] = sprite;
    }

     */

    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    /**
     * Gets the x-coordinate of the Entity
     * @return x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the Entity
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    public Sprite getSprite(){
        if (sprites.length <= 4){
            return sprites[0];
        }

        switch (orientation){
            case DOWN: return sprites[1];
            case LEFT: return sprites[2];
            case RIGHT: return sprites[3];
            case UP:
            default: return sprites[0];
        }
    }

    //</editor-fold>



    /**
     * Moves the player by a Vector2
     * @param vector 2D Vector
     */
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

        body.setLinearVelocity(vector);
        sprites[0].setPosition(body.getPosition().x, body.getPosition().y);
        sprites[1].setPosition(body.getPosition().x, body.getPosition().y);
        sprites[2].setPosition(body.getPosition().x, body.getPosition().y);
        sprites[3].setPosition(body.getPosition().x, body.getPosition().y);
    }


}
