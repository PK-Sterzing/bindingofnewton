package com.bindingofnewton.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.assets.AssetsHandler;

public class Enemy extends Entity {

    // How many times should the enemy update the path to the player
    protected static int pathChangingRate = 500;
    private static long lastPathChange = 0;

    protected float deltaTime = 0f;
    //TODO: SpeedAnimation should be property of enemy (enum in assetshandler)
    protected final float SPEED_ANIMATION = 0.08f;
    protected float animation_offset;

    private AssetsHandler.EnemyProperties enemyName;

    public Enemy(World world, AssetsHandler.EnemyProperties enemyName, int startX, int startY, int speed) {
        this.enemyName = enemyName;
        this.invincibilityCooldown = 0;
        this.health = 2.0f;
        this.speed = speed;
        this.animation_offset = (float) Math.random() * 5f;

        this.orientation = Orientation.UP;
        orientation = Orientation.DOWN;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(startX ,startY);

        characterWidth = 22;
        characterHeight = 28;

        body = world.createBody(def);
        body.setUserData(this);

        animations.put(Orientation.DOWN, AssetsHandler.getInstance().getAnimation(enemyName.toString().toLowerCase() + "-run-front", SPEED_ANIMATION, 1f));

        PolygonShape polygonShape = new PolygonShape();

        polygonShape.set(enemyName.getVertices());
        polygon = new Polygon(enemyName.getVertices());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        //fixtureDef.density = 1f;

        body.createFixture(fixtureDef);

        polygonShape.dispose();
    }

    /**
     * Moves the Enemy by a Vector2
     * @param vector 2D Vector
     */
    public void move(Vector2 vector){
        float x = vector.x;
        float y =  vector.y;

        if(!(x == 0.0f && y == 0.0f)){
            body.setLinearVelocity(vector);
        }

        //this.sprites.get(0).setPosition(body.getPosition().x, body.getPosition().y);
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
        deltaTime += Gdx.graphics.getDeltaTime();
        Sprite sprite;
        if(this.getNextDamageSprite() == 0){
            sprite = AssetsHandler.getInstance().getAnimationFrame(animations.get(Orientation.DOWN), deltaTime + animation_offset);
        }else{
            //TODO:
            sprite = AssetsHandler.getInstance().getSingleSpriteFromFile("./character/newton/newton-damage.png");
            sprite.setScale(0.5f);
            this.setNextDamageSprite(-1);
        }
        batch.draw(sprite, body.getPosition().x-10, body.getPosition().y-10, sprite.getWidth(), sprite.getHeight());
    }

    /**
     * Calculates vector with fixed length to player and moves enemy to that vector
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

        if (move.x > 0){
            orientation = Orientation.RIGHT;
        }else if (move.x < 0){
            orientation = Orientation.LEFT;
        }

        this.move(move);
        Enemy.setLastPathChange(System.currentTimeMillis());
    }
}
