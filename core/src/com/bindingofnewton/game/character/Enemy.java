package com.bindingofnewton.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.assets.AssetsHandler;

import java.util.ArrayList;

public class Enemy extends Entity {

    protected Animation<TextureRegion> up;
    protected TextureAtlas textureAtlas;

    // How many times should the enemy update the path to the player
    protected static int pathChangingRate = 500;
    private static long lastPathChange = 0;

    private float deltaTime = 0f;
    private final float SPEED_ANIMATION = 0.04f;



    public Enemy(World world, int startX, int startY, int speed, Sprite sprite) {
        this.speed = speed;
        this.orientation = Orientation.UP;
        orientation = Orientation.DOWN;
        this.sprites = new ArrayList<>();
        this.sprites.add(sprite);
        x = startX;
        y = startY;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x ,y);

        characterWidth = 22;
        characterHeight = 28;

        body = world.createBody(def);
        body.setUserData(this);

        PolygonShape polygonShape = new PolygonShape();

        float[] vertices = new float[] {
                5.0f, 2.0f,
                24.0f, 2.0f,
                27.0f, 18.0f,
                4.0f, 18.0f,
        };

        polygonShape.set(vertices);

        polygon = new Polygon(vertices);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        //fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);

        polygonShape.dispose();

        textureAtlas = new TextureAtlas(AssetsHandler.BAT_RUN);

        up = new Animation<TextureRegion>(SPEED_ANIMATION, textureAtlas.findRegions("run-front"), Animation.PlayMode.LOOP);
        health = 1.0f;

    }

    public TextureRegion getTextureRegion() {
        deltaTime += Gdx.graphics.getDeltaTime();
        switch (orientation) {
            case UP: return up.getKeyFrame(deltaTime, true);
            default: return up.getKeyFrame(deltaTime, true);
        }
    }

    /**
     * Moves the Enemy by a Vector2
     * @param vector 2D Vector
     */
    public void move(Vector2 vector){
        float x = vector.x;
        float y =  vector.y;


        this.x += x;
        this.y += y;
        if(!(x == 0.0f && y == 0.0f)){
            body.setLinearVelocity(vector);
        }


        this.sprites.get(0).setPosition(body.getPosition().x, body.getPosition().y);
        polygon.setPosition(body.getPosition().x, body.getPosition().y);
    }

    /**
     * Gets the changing rate of the path
     * @return path changing rate
     */
    public static int getPathChangingRate() {
        return pathChangingRate;
    }

    /**
     * Gets the last change of the path
     * @return last change of the path
     */
    public static long getLastPathChange(){
        return lastPathChange;
    }

    /**
     * Sets the last change of the path
     * @param lastPathChange last change of the path
     */
    public static void setLastPathChange(long lastPathChange){
        Enemy.lastPathChange = lastPathChange;
    }

    @Override
    public void render(SpriteBatch batch, boolean isMoving) {
        move(new Vector2(0, 0));
        getSprite().draw(batch);
    }

    /**
     * Calcultes vector with fixed length to player and moves enemy to that vector
     * @param player to which the enemy has to move
     */
    public void calculateMoveToPlayer(Player player){
        Vector2 move = new Vector2(
                player.getBody().getPosition().x -
                        this.getBody().getPosition().x,
                player.getBody().getPosition().y -
                        this.getBody().getPosition().y);

        // Change length of vector to the speed of the enemy
        move = move.scl(this.getSpeed() / move.len());

        this.move(move);
        Enemy.setLastPathChange(System.currentTimeMillis());
    }
}
