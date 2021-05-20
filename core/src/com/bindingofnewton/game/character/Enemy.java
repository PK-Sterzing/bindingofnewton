package com.bindingofnewton.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.assets.AssetsHandler;

public class Enemy extends Entity {

    /**
     * Enum for the properties of the enemy. Every enemy has his own vertices for the body.
     */
    public enum Properties{
        BAT(
                new float[] {
                        5.0f, 2.0f,
                        24.0f, 2.0f,
                        27.0f, 18.0f,
                        4.0f, 18.0f,
                },
                2,
                500
        ),
        //TODO: Vertices berechnen/ändern
        BOSS(
                new float[]{
                        95.0f, 60.0f,
                        102.0f, 40.0f,
                        95.0f, 18.0f,
                        20.0f, 18.0f,
                        13.0f, 40.0f,
                        20.0f, 60.0f,
                },
                30,
                600
        ),
        MOUSE(
                new float[]{
                        5.0f, 2.0f,
                        24.0f, 2.0f,
                        27.0f, 18.0f,
                        4.0f, 18.0f,
                },
                1,
                300
        );

        private float[] vertices;
        private final float START_HEALTH;
        private final int pathChangingRate;

        Properties(float vertices[], float health, int pathChangingRate){
            this.vertices = vertices;
            this.START_HEALTH = health;
            this.pathChangingRate = pathChangingRate;
        }

        /**
         * Gets the vertices for the body of the enemy
         * @return vertices
         */
        public float[] getVertices() {
            return vertices;
        }

        public float getHealth() {
            return START_HEALTH;
        }
    }

    // How many times should the enemy update the path to the player
    protected int pathChangingRate = 500;
    private long lastPathChange = 0;

    protected float deltaTime = 0f;
    //TODO: SpeedAnimation should be property of enemy (enum in assetshandler)
    protected final float SPEED_ANIMATION = 0.08f;
    protected float animation_offset;

    private Properties enemyName;

    public Enemy(World world, Properties enemyName, int startX, int startY, int speed) {
        this.enemyName = enemyName;
        this.invincibilityCooldown = 0;
        this.health = enemyName.getHealth();
        this.speed = speed;
        this.animation_offset = (float) Math.random() * 5f;
        this.pathChangingRate = enemyName.pathChangingRate;

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
        MassData massData = new MassData();
        massData.mass = 50;
        body.setMassData(massData);

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

        polygon.setPosition(body.getPosition().x, body.getPosition().y);
    }

    /**
     * Gets the changing rate of the path
     * @return path changing rate
     */
    public int getPathChangingRate() {
        return pathChangingRate;
    }

    /**
     * Gets the last change of the path
     * @return last change of the path
     */
    public long getLastPathChange(){
        return lastPathChange;
    }

    /**
     * Sets the last change of the path
     * @param lastPathChange last change of the path
     */
    public void setLastPathChange(long lastPathChange){
        this.lastPathChange = lastPathChange;
    }

    @Override
    public void render(SpriteBatch batch, boolean isMoving) {
        move(new Vector2(0, 0));
        deltaTime += Gdx.graphics.getDeltaTime();
        Sprite sprite;
        if(this.getNextDamageSprite() == 0){
            sprite = AssetsHandler.getInstance().getAnimationFrame(animations.get(Orientation.DOWN), deltaTime + animation_offset);
            if (orientation == Orientation.LEFT && !sprite.isFlipX()){
                sprite.setFlip(true, false);
            }else if (orientation == Orientation.RIGHT && sprite.isFlipX()){
                sprite.setFlip(false, false);
            }
            batch.draw(sprite, body.getPosition().x-10, body.getPosition().y-10, sprite.getWidth(), sprite.getHeight());
        }else{
            sprite = AssetsHandler.getInstance().getSingleSpriteFromAtlas("bat-damage");
            sprite.setScale(0.5f);
            this.setNextDamageSprite(-1);
            batch.draw(sprite, body.getPosition().x - 2, body.getPosition().y - 10, sprite.getWidth(), sprite.getHeight());
        }
    }

    public Vector2 calculateVectorToPlayer(Player player){
        Vector2 move = new Vector2(
                player.getBody().getPosition().x -
                        this.getBody().getPosition().x,
                player.getBody().getPosition().y -
                        this.getBody().getPosition().y);

        if (move.x > 0){
            orientation = Orientation.RIGHT;
        }else if (move.x < 0){
            orientation = Orientation.LEFT;
        }
        return move;
    }

    /**
     * Calculates vector with fixed length to player and moves enemy to that vector
     * @param player to which the enemy has to move
     */
    public void calculateMoveToPlayer(Player player){
        Vector2 move = calculateVectorToPlayer(player);

        // Change length of vector to the speed of the enemy
        move = move.scl(this.getSpeed() / move.len());

        this.move(move);
        setLastPathChange(System.currentTimeMillis());
    }
}
