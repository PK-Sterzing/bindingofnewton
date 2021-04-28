package com.bindingofnewton.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Bullet {
    private int speed = 120;
    private final int WIDTH = 15;
    private final int HEIGHT = 13;
    private final Body body;
    private final PolygonShape polygonShape;
    private final Sprite sprite;
    private Vector2 movement;
    public static int fireRate = 500;

    private boolean remove = false;

    public Bullet(World world, int startX, int startY){
        // Create Box2d body
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(startX , startY );
        body = world.createBody(def);

        // Create shape for Bullet
        polygonShape = new PolygonShape();

        Vector2 position = new Vector2(WIDTH / 2, HEIGHT/2 );
        polygonShape.setAsBox(WIDTH * 0.5f ,
                HEIGHT * 0.5f ,
                position,
                0.0f);

        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;


        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("bullet");

        polygonShape.dispose();

        // Load Sprite
        this.sprite = AssetsHandler.getInstance().getSingleSprite("bullet.png");
        this.sprite.setSize(WIDTH, HEIGHT);
        this.sprite.setPosition(body.getPosition().x, body.getPosition().y);

        // Set default movement
        movement = new Vector2(0, 0);
    }

    public void setMovement(Vector2 vector){
        this.movement = vector;
    }

    public void update(){
        body.setLinearVelocity(movement);
        if(body.getPosition().x > Gdx.graphics.getWidth() || body.getPosition().x < 0){
            this.remove = true;
        }
        if(body.getPosition().y > Gdx.graphics.getHeight() || body.getPosition().y < 0){
            this.remove = true;
        }
        this.sprite.setPosition(body.getPosition().x, body.getPosition().y);
    }

    public Sprite getSprite(){
        return this.sprite;
    }

    public int getSpeed() {
        return speed;
    }

    public Body getBody() {
        return body;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }



}