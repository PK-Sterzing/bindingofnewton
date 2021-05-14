package com.bindingofnewton.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.assets.AssetsHandler;

import java.util.ArrayList;

public class Player extends Entity {
    private static final float MAX_HEALTH = 10;
    private final float SPEED_ANIMATION = 0.04f;

    private Animation<TextureRegion> up;
    private Animation<TextureRegion> down;
    private Animation<TextureRegion> left;
    private Animation<TextureRegion> right;
    private TextureAtlas textureAtlas;

    private float deltaTime = 0f;
    private String playerName;

    public Player(World world, String playerName, int startX, int startY) {
        orientation = Orientation.DOWN;
        this.playerName = playerName;
        x = startX;
        y = startY;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x ,y);
        orientation = Orientation.DOWN;

        characterWidth = 22;
        characterHeight = 28;

        body = world.createBody(def);
        body.setUserData(this);

        PolygonShape polygonShape = new PolygonShape();

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

        textureAtlas = new TextureAtlas(AssetsHandler.NEWTON_RUN);

        up = new Animation<TextureRegion>(SPEED_ANIMATION, textureAtlas.findRegions("run-back"), Animation.PlayMode.LOOP);
        down = new Animation<TextureRegion>(SPEED_ANIMATION, textureAtlas.findRegions("run-front"), Animation.PlayMode.LOOP);
        right = new Animation<TextureRegion>(SPEED_ANIMATION, textureAtlas.findRegions("run-right"), Animation.PlayMode.LOOP);
        left = new Animation<TextureRegion>(SPEED_ANIMATION, textureAtlas.findRegions("run-left"), Animation.PlayMode.LOOP);

        health = 3.5f;
    }

    public TextureRegion getTextureRegion() {
        deltaTime += Gdx.graphics.getDeltaTime();
        switch (orientation) {
            case DOWN: return down.getKeyFrame(deltaTime, true);
            case LEFT: return left.getKeyFrame(deltaTime, true);
            case RIGHT: return right.getKeyFrame(deltaTime, true);
            default: return up.getKeyFrame(deltaTime, true);
        }
    }

    /**
     * Returns the sprites of the current health of the player
     * @return array with sprites
     */
    public Sprite[] getHealthSprites(){
        Sprite[] sprites = new Sprite[(int) MAX_HEALTH];

        for (int i=0; i<Math.ceil(health); i++){
            if (i == Math.floor(health) && Math.ceil(health) - health == 0.5f){
                sprites[i] = AssetsHandler.getInstance().getSingleSprite("half_heart.png");
            }else{
                sprites[i] = AssetsHandler.getInstance().getSingleSprite("full_heart.png");
            }
        }

        for (int i = (int) Math.ceil(health); i<MAX_HEALTH; i++){
            sprites[i] = AssetsHandler.getInstance().getSingleSprite("empty_heart.png");
        }

        return sprites;
    }


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

        polygon.setPosition(body.getPosition().x, body.getPosition().y);
    }

    public void transform(Vector2 vector){
        this.body.setLinearVelocity(vector);
        this.move(new Vector2(0, 0));
    }

    @Override
    public Sprite getSprite(){
        if (sprites.size() < 4){
            return AssetsHandler.getInstance().getPlayerSprite(this).get(0);
        }
        switch (orientation){
            case DOWN: return AssetsHandler.getInstance().getPlayerSprite(this).get(1);
            case LEFT: return AssetsHandler.getInstance().getPlayerSprite(this).get(2);
            case RIGHT: return AssetsHandler.getInstance().getPlayerSprite(this).get(3);
            case UP:
            default: return AssetsHandler.getInstance().getPlayerSprite(this).get(0);
        }
    }



    public String getPlayerName() {
        return playerName;
    }

}
