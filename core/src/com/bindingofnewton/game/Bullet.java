package com.bindingofnewton.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.assets.AssetsHandler;

public class Bullet {
    private int speed = 150;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    private final Body body;
    private final PolygonShape polygonShape;
    private final Sprite sprite;
    private Vector2 movement;

    private boolean remove = false;

    public Bullet(World world, AssetsHandler.PlayerName playerName, int startX, int startY){
        // Create Box2d body
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(startX , startY );
        def.bullet = true;
        body = world.createBody(def);
        body.setUserData(this);

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

        polygonShape.dispose();

        // Load Sprite
        this.sprite = AssetsHandler.getInstance().getSingleSpriteFromFile("bullets/" + playerName.name().toLowerCase() + "_bullet.png");
        this.sprite.setScale(1.7f);
        this.sprite.setSize(WIDTH, HEIGHT);
        this.sprite.setPosition(body.getPosition().x, body.getPosition().y);

        // Set default movement
        movement = new Vector2(0, 0);
        System.out.println("Bullet created");
    }

    /**
     * Set constant enemy movement
     * The movement set will be executed ever render
     * @param vector
     */
    public void setMovement(Vector2 vector){
        this.movement = vector;
    }

    /**
     * Move body according to movement set by setMovement
     * removes bullet if out of bounds
     */
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
        System.out.println("Bullet removed");
    }

    /**
     * Renders the bullet
     * @param batch the batch to be rendered with
     */
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
