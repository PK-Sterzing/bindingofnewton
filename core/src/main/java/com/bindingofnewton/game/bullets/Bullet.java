package com.bindingofnewton.game.bullets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.assets.AssetsHandler;

public class Bullet {
    protected int speed = 150;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    protected Body body;
    protected PolygonShape polygonShape;
    protected Sprite sprite;
    protected Vector2 movement;
    private boolean isEnemyBullet;

    private boolean remove = false;

    public Bullet(World world, AssetsHandler.PlayerName playerName, int startX, int startY, boolean isEnemyBullet){
        this.isEnemyBullet = isEnemyBullet;

        // Create Box2d body
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(startX , startY );
        def.bullet = true;
        body = world.createBody(def);
        body.setUserData(this);

        // Create shape for Bullet
        polygonShape = new PolygonShape();

        Vector2 position = new Vector2(WIDTH / 2.0f, HEIGHT/2.0f );
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
        if(playerName != null){
            this.sprite = AssetsHandler.getInstance().getSingleSpriteFromFile("bullets/" + playerName.name().toLowerCase() + "_bullet.png");
            this.sprite.setScale(1.7f);
            this.sprite.setSize(WIDTH, HEIGHT);
            this.sprite.setPosition(body.getPosition().x, body.getPosition().y);
        }

        // Set default movement
        movement = new Vector2(0, 0);
    }

    public Bullet(World world, AssetsHandler.PlayerName playerName, int startX, int startY){
        this(world, playerName, startX, startY, false);
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

    /**
     * Gets the current movement of the bullet
     * @return a 2d vector of the movement
     */
    public Vector2 getMovement() {
        return movement;
    }

    public Sprite getSprite(){
        return this.sprite;
    }

    public void setSprite(Sprite sprite) {this.sprite = sprite;}

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

    /**
     * Renders the bullet
     * @param batch the batch to be rendered with
     */
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * @return true - if the bullet is a bullet of an enemy
     *          false - if not
     */
    public boolean isEnemyBullet() {
        return isEnemyBullet;
    }
}
