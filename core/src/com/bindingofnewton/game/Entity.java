package com.bindingofnewton.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class Entity {
    private final Body body;
    private final Polygon polygon;

    protected int x;
    protected int y;
    private int characterHeight;
    private int characterWidth;

    protected Sprite[] sprites;

    protected Orientation orientation;


    protected int speed = 100;
    protected int health;

    //<editor-fold desc="Constructors">-


    public Entity(World world, int startX, int startY, Sprite[] sprites) {
        orientation = Orientation.DOWN;
        this.sprites = sprites;
        x = startX;
        y = startY;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x ,y);

        characterWidth = 22;
        characterHeight = 28;

        body = world.createBody(def);

        PolygonShape polygonShape = new PolygonShape();
        Vector2 position = new Vector2((getSprite().getX() + characterWidth * 0.5f ),
                (getSprite().getY() + characterHeight * 0.5f ));

        System.out.println((float) (45/360) * 2* Math.PI);
        /*
        polygonShape.setAsBox(characterWidth * 0.5f ,
                characterHeight * 0.5f ,
                position,
                0.0f);

         */
        float[] vertices = new float[] {
                7.7f, 0.0f,
                14.0f, 0.0f,
                20.3f, 18.2f,
                16.0f, 28.0f,
                5.6f, 28.0f,
                1.4f, 18.2f,
        };

        polygonShape.set(vertices);

        polygon = new Polygon(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        //fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);

        polygonShape.dispose();
    }


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

    public Body getBody() {
        return body;
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
        polygon.setPosition(body.getPosition().x, body.getPosition().y);
    }

    public int getSpeed() {
        return speed;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;

        body.setTransform(x, y,0);
        sprites[0].setPosition(x, y);
        sprites[1].setPosition(x, y);
        sprites[2].setPosition(x, y);
        sprites[3].setPosition(x, y);
        polygon.setPosition(x, y);
    }
}
