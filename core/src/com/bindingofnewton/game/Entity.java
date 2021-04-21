package com.bindingofnewton.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class Entity {

    private final Body body;

    protected int x;
    protected int y;
    private int characterHeight;
    private int characterWidth;

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

        characterWidth = 23;
        characterHeight = 31;

        body = world.createBody(def);

        PolygonShape polygonShape = new PolygonShape();
        Vector2 position = new Vector2((getSprite().getX() + characterWidth * 0.5f ),
                (getSprite().getY() + characterHeight * 0.5f ));

        polygonShape.setAsBox(characterWidth * 0.5f ,
                characterHeight * 0.5f ,
                position,
                0.0f);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        //fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);

        polygonShape.dispose();

        orientation = Orientation.DOWN;

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
        if (sprites.length < 4){
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

        //System.out.println(orientation);

        this.x += x;
        this.y += y;

        body.setLinearVelocity(vector);
        sprites[0].setPosition(body.getPosition().x, body.getPosition().y);
        sprites[1].setPosition(body.getPosition().x, body.getPosition().y);
        sprites[2].setPosition(body.getPosition().x, body.getPosition().y);
        sprites[3].setPosition(body.getPosition().x, body.getPosition().y);
    }

    public Sprite adjustSize(Sprite spriteToBeScaled) {
        spriteToBeScaled.setSize(spriteToBeScaled.getWidth() * 0.5f, spriteToBeScaled.getHeight() * 0.5f);
        return spriteToBeScaled;
    }


}
