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

public class Enemy extends Entity {

    Animation<TextureRegion> up;
    TextureAtlas textureAtlas;

    private float deltaTime = 0f;
    private final float SPEED_ANIMATION = 0.04f;



    public Enemy(World world, int startX, int startY, Sprite sprite) {
        this.orientation = Orientation.UP;
        health = 3.5f;
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

        textureAtlas = new TextureAtlas(AssetsHandler.BAT_RUN);

        up = new Animation<TextureRegion>(SPEED_ANIMATION, textureAtlas.findRegions("run-front"), Animation.PlayMode.LOOP);

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

        body.setLinearVelocity(vector);

        this.sprites.get(0).setPosition(body.getPosition().x, body.getPosition().y);
        polygon.setPosition(body.getPosition().x, body.getPosition().y);
    }
}
