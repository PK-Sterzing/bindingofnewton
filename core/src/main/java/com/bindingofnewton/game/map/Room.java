package com.bindingofnewton.game.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.Bullet;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.character.Enemy;
import com.bindingofnewton.game.character.Player;
import com.bindingofnewton.game.items.HealthBoostItem;
import com.bindingofnewton.game.items.Item;
import com.bindingofnewton.game.items.ReloadSpeedItem;
import com.bindingofnewton.game.items.SpeedBoostItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for all rooms in a level
 */
public class Room {
    protected int x;
    protected int y;

    protected TiledMap map;
    protected World world;

    protected ArrayList<Door> doors;
    protected ArrayList<Bullet> bullets;

    protected ArrayList<Enemy> enemies;

    protected ArrayList<Item> droppedItems;
    protected Player player;

    private boolean isCleared;

    public Room(){
        ArrayList<String> mapList = (ArrayList<String>) AssetsHandler.getInstance().getMaps();
        int index = (int) (Math.random() * (mapList.size()-2) + 1);
        map = new TmxMapLoader().load(mapList.get(index));

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        droppedItems = new ArrayList<>();
        isCleared = false;
    }

    /**
     * Gets all possible positons for doors in a room
     */
    public List<Orientation> getPossibleDoors(){
        List<Orientation> orientations = new ArrayList<>();
        List<Orientation> possibleOrientations = new ArrayList<>();

        for (Door door : doors){
            orientations.add(door.getOrientation());
        }

        for (Orientation orientation : Orientation.values()){
            if (!orientations.contains(orientation)) possibleOrientations.add(orientation);
        }
        return possibleOrientations;
    }

    public void setPlayer(Player player){
        this.player = player;
    }
    public void addEnemies(ArrayList<Enemy> enemies){
        this.enemies = enemies;
    }
    public void addBullet(Bullet bullet){
        this.bullets.add(bullet);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TiledMap getMap() {
        return map;
    }

    /**
     * Sets door sprites
     */
    public void setDoorBodies(){
        for (Orientation orientation : Orientation.values()){
            String layerName = "doors-" + orientation.name();
            MapLayer layer = map.getLayers().get(layerName);
            if (layer == null){
                break;
            }

            MapObjects objects = layer.getObjects();
            for (MapObject object : objects) {
                // Make sure the found object is a rectangle
                if (object instanceof RectangleMapObject) {
                    Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                    PolygonShape shape = new PolygonShape();
                    Vector2 position = new Vector2((rectangle.x + rectangle.width * 0.5f ),
                            (rectangle.y + rectangle.height * 0.5f ));

                    shape.setAsBox(rectangle.width * 0.5f ,
                            rectangle.height * 0.5f ,
                            position,
                            0.0f);


                    // Create box2d body
                    BodyDef def = new BodyDef();
                    def.type = BodyDef.BodyType.StaticBody;
                    Body body = world.createBody(def);
                    body.createFixture(shape, 1);
                    body.setActive(true);

                    for (Door door : doors){
                        if (door.getOrientation() == orientation){
                            door.setBody(body);
                            body.setUserData(door);
                        }
                    }
                    shape.dispose();
                }
            }
        }

    }

    public void addDoor(Door door){
        doors.add(door);
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }


    public Player getPlayer() {
        return player;
    }

    public boolean isCleared() {
        return isCleared;
    }

    public void update() {
        updateBullets();
        removeDeadEnemies();
        moveAllEnemies();
        removeDroppedItems();
    }

    /**
     * Moves enemy after cooldown
     */
    private void moveAllEnemies() {
        // Move Enemy after cooldown
        for(int i = 0; i < enemies.size(); i++){
            if (System.currentTimeMillis() - enemies.get(i).getLastPathChange() >= enemies.get(i).getPathChangingRate()){
                // Get vector from enemy to player
                enemies.get(i).calculateMoveToPlayer(player);
            }
        }

    }


    /**
     * Create item and add to array
     * @param posX
     * @param posY
     */
    private void dropItem(float posX, float posY){
        // Not all enemies drop items
        double randInt = Math.random();
        // 15 % droprate
        if(randInt <= 0.15){
            if(randInt < 0.049){
                HealthBoostItem h = new HealthBoostItem(this.world, posX, posY);
                droppedItems.add(h);
            }else if(randInt >= 0.049 && randInt <= 0.099){
                SpeedBoostItem h = new SpeedBoostItem(this.world, posX, posY);
                droppedItems.add(h);
            }else if(randInt > 0.099){
                ReloadSpeedItem h = new ReloadSpeedItem(this.world, posX, posY);
                droppedItems.add(h);
            }
    }
}

    private void removeDroppedItems(){
        for(int i = 0; i < droppedItems.size(); i++){
            if(droppedItems.get(i).isShouldBeRemoved()){
                world.destroyBody(droppedItems.get(i).getBody());
                droppedItems.remove(i);
            }
        }
    }


    /**
     * Goes trough enemies and deletes the dead ones
     */
    private void removeDeadEnemies() {
        for(int i = 0; i < enemies.size(); i++){
            if(enemies.get(i).isDead()){
                dropItem(enemies.get(i).getBody().getPosition().x, enemies.get(i).getBody().getPosition().y);
                world.destroyBody(enemies.get(i).getBody());
                enemies.remove(i);
            }
        }

        if (enemies.isEmpty()) {
            isCleared = true;
            for (Door door : doors){
                door.open();
            }
        }

    }

    /**
     * Removes all bullets that have to be removed
     */
    private void updateBullets(){
        for(int i = 0; i < bullets.size(); i++){
            if(bullets.get(i).isRemove()){
                // Destroy body, remove bullet
                world.destroyBody(bullets.get(i).getBody());
                bullets.remove(i);
            }else{
                bullets.get(i).update();
            }

        }
    }

    public ArrayList<Item> getDroppedItems() {
        return droppedItems;
    }

}
