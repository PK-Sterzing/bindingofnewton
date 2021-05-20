package com.bindingofnewton.game.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.character.Player;

public abstract class Item {
    protected Body body;
    protected PolygonShape polygonShape;
    protected int HEIGHT = 2;
    protected int WIDTH = 2;


    protected boolean shouldBeRemoved = false;

    public Item(World world, float posX, float posY) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(posX, posY);
        def.linearDamping = 100f;

        body = world.createBody(def);

        // Create shape for Bullet
        polygonShape = new PolygonShape();

        Vector2 position = new Vector2(WIDTH / 2, HEIGHT / 2);
        polygonShape.setAsBox(WIDTH * 0.5f,
                HEIGHT * 0.5f,
                position,
                0.0f);

        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;

        Fixture fixture = body.createFixture(fixtureDef);

        polygonShape.dispose();
    }

    public abstract void render(SpriteBatch batch);


    public abstract void use(Player player);

    public abstract void remove(Player player);

    public boolean isShouldBeRemoved() {
        return shouldBeRemoved;
    }

    public void setShouldBeRemoved(boolean shouldBeRemoved) {
        this.shouldBeRemoved = shouldBeRemoved;
    }
    public Body getBody() {
        return body;
    }

}
