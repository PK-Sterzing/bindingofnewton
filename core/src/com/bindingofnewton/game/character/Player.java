package com.bindingofnewton.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.bindingofnewton.game.BindingOfNewton;
import com.bindingofnewton.game.gameover.GameOverScreen;
import com.bindingofnewton.game.mainmenu.MainMenuScreen;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player extends Entity {
    private static final float MAX_HEALTH = 10;
    private final float SPEED_ANIMATION = 0.04f;

    private HashMap<Orientation, Animation<Sprite>> animations = new HashMap<>();
    private AssetsHandler.PlayerName playerName;
    public static int fireRate = 500;

    private Item currentItem;

    public Player(World world, AssetsHandler.PlayerName playerName, int startX, int startY) {

        this.health = 10.0f;
        this.invincibilityCooldown = 300;

        orientation = Orientation.DOWN;
        this.playerName = playerName;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.linearDamping = 10f;
        def.position.set(startX, startY);
        orientation = Orientation.DOWN;

        characterWidth = 22;
        characterHeight = 28;

        body = world.createBody(def);
        body.setUserData(this);

        PolygonShape polygonShape = new PolygonShape();

        float[] vertices = new float[]{
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

        animations.put(Orientation.UP, AssetsHandler.getInstance().getPlayerRunAnimation(playerName, Orientation.UP, SPEED_ANIMATION, .7f));
        animations.put(Orientation.DOWN, AssetsHandler.getInstance().getPlayerRunAnimation(playerName, Orientation.DOWN, SPEED_ANIMATION, .7f));
        animations.put(Orientation.RIGHT, AssetsHandler.getInstance().getPlayerRunAnimation(playerName, Orientation.RIGHT, SPEED_ANIMATION, .7f));
        animations.put(Orientation.LEFT, AssetsHandler.getInstance().getPlayerRunAnimation(playerName, Orientation.LEFT, SPEED_ANIMATION, .7f));


    }

    /**
     * Returns the sprites of the current health of the player
     *
     * @return array with sprites
     */
    public List<Sprite> getHealthSprites() {
        List<Sprite> sprites = new ArrayList<>();

        for (int i = 0; i < Math.ceil(health); i++) {
            if (i == Math.floor(health) && Math.ceil(health) - health == 0.5f) {
                sprites.add(AssetsHandler.getInstance().getSingleSpriteFromFile("half_heart.png"));
            } else {
                sprites.add(AssetsHandler.getInstance().getSingleSpriteFromFile("full_heart.png"));
            }
        }

        for (int i = (int) Math.ceil(health); i < MAX_HEALTH; i++) {
            sprites.add(AssetsHandler.getInstance().getSingleSpriteFromFile("empty_heart.png"));
        }

        return sprites;
    }


    /**
     * Moves the player by a Vector2
     *
     * @param vector 2D Vector
     */
    public void move(Vector2 vector) {
        float x = vector.x;
        float y = vector.y;

        if (y > 0) {
            orientation = Orientation.UP;
        } else if (y < 0) {
            orientation = Orientation.DOWN;
        }

        if (x > 0) {
            orientation = Orientation.RIGHT;
        } else if (x < 0) {
            orientation = Orientation.LEFT;
        }


        body.applyLinearImpulse(vector, new Vector2(body.getPosition().x, body.getPosition().y), true);

        polygon.setPosition(body.getPosition().x, body.getPosition().y);
    }

    /**
     * Transforms the player to the given position
     * @param vector the new position of the player
     */
    public void transform(Vector2 vector) {
        this.body.setTransform(vector, 0);
        this.move(new Vector2(0, 0));
    }

    /**
     * Gets the name of the player
     * @return The name of the player
     */
    public AssetsHandler.PlayerName getPlayerName() {
        return playerName;
    }

    /**
     * Gets the animation for each direction
     * @return A hashmap with the direction as the keys and the animation as the values
     */
    public HashMap<Orientation, Animation<Sprite>> getAnimations() {
        return animations;
    }

    @Override
    public void render(SpriteBatch batch, boolean isMoving) {
        Sprite sprite;
        if(this.getNextDamageSprite() == 0){
            if (isDead) {
                //sprite = AssetsHandler.getInstance().getSingleSpriteFromFile("./character/" + playerName.toString().toLowerCase() + "/" + playerName.toString().toLowerCase() + "-dead.png");
                sprite = AssetsHandler.getInstance().getSingeSpriteFromAtlas(playerName.toString().toLowerCase() + "-dead");
                sprite.setSize(sprite.getWidth() * .7f, sprite.getHeight() * .7f);
                // Pause game
                BindingOfNewton.getInstance().setPaused(true);
                // Wait and return to main menu
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        BindingOfNewton.getInstance().getGame().setScreen(new GameOverScreen(BindingOfNewton.getInstance().getGame()));
                    }
                }, 3);

            } else if (isMoving){
                deltaTime += Gdx.graphics.getDeltaTime();
                sprite = AssetsHandler.getInstance().getAnimationFrame(animations.get(orientation), deltaTime);
            } else{
                sprite = AssetsHandler.getInstance().getPlayerSprite(playerName, orientation);
            }

        }else {
            sprite = AssetsHandler.getInstance().getSingeSpriteFromAtlas("newton-damage");
            sprite.setSize(sprite.getWidth() * .7f, sprite.getHeight() * .7f);
            this.setNextDamageSprite(-1);
        }
        batch.draw(sprite, body.getPosition().x, body.getPosition().y, sprite.getWidth(), sprite.getHeight());
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

}
