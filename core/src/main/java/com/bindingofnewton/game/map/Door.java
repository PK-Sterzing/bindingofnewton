package com.bindingofnewton.game.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.physics.box2d.*;
import com.bindingofnewton.game.Orientation;

/**
 * A door of the map
 */
public class Door {

    /**
     * An enum for the ids of the tileset. It is needed to replace the walls/floor with the open and closed doors
     */
    public enum Id{
        NORMAL(17, 18),
        BOSS(27, 28),
        PORTAL(3, 31),

        NORMAL2(32, 33),
        BOSS2(29, 30),
        PORTAL2(19, 36),

        NORMAL3(10, 11),
        BOSS3(12, 9),
        PORTAL3(3, 14);

        private final int closed;
        private final int open;

        Id(int closed, int open){
            this.open = open;
            this.closed = closed;
        }
    }

    private World world;
    private Orientation orientation;
    private TiledMap map;
    private Body body;

    private Id id;
    private boolean isOpen;

    /**
     * Creates a new Door
     * @param world the world of the door
     * @param map the map where the door should be
     * @param orientation the orientation where the door should be
     * @param id the tiled id of the door
     */
    public Door(World world, TiledMap map, Orientation orientation, Id id){
        this.world = world;
        this.orientation = orientation;
        this.map = map;
        this.id = id;
        close();
    }

    /**
     * Sets the door on the map
     * @param id the id of the tile of the door
     */
    private void setDoorOnMap(int id){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("ground");
        TiledMapTileSet tileSet = map.getTileSets().getTileSet(0);

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tileSet.getTile(id));

        int width = layer.getWidth();
        int height = layer.getHeight();

        if (id == Id.PORTAL.open || id == Id.PORTAL.closed || id == Id.PORTAL2.open || id == Id.PORTAL2.closed || id == Id.PORTAL3.open || id == Id.PORTAL3.closed ){
            layer.setCell(width/2, height/2, cell);
        }else{
            switch (orientation){
                case DOWN:
                    cell.setFlipVertically(true);
                    layer.setCell(width/2, 0, cell);
                    break;
                case UP:
                    layer.setCell(width/2, height-1, cell);
                    break;
                case LEFT:
                    cell.setRotation(TiledMapTileLayer.Cell.ROTATE_90);
                    layer.setCell(0, height/2, cell);
                    break;
                case RIGHT:
                    cell.setRotation(TiledMapTileLayer.Cell.ROTATE_270);
                    layer.setCell(width-1, height/2, cell);

            }
        }
    }

    /**
     * Sets the body
     * @param body body
     */
    public void setBody(Body body){
        this.body = body;
        if (id == Id.PORTAL || id == Id.PORTAL2 || id == Id.PORTAL3){
            body.setActive(false);
        }
    }

    /**
     * @return true - door is open
     *          false - door is closed
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * opens the door
     */
    public void open() {
        isOpen = true;
        if (body != null){
            if (id == Id.PORTAL || id == Id.PORTAL2 || id == Id.PORTAL3){
                body.setActive(true);
            }else{
                body.setActive(false);
            }
        }

        setDoorOnMap(id.open);
    }

    /**
     * closes the door
     */
    public void close(){
        isOpen = false;
        if (body != null){
            if (id == Id.PORTAL || id == Id.PORTAL2 || id == Id.PORTAL3){
                body.setActive(false);
            }else{
                body.setActive(true);
            }
        }

        setDoorOnMap(id.closed);
    }

    /**
     * Gets the orientation
     * @return the orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Gets the body
     * @return the body
     */
    public Body getBody() {
        return body;
    }

    /**
     * Gets the doors id
     * @return the ID
     */
    public Id getId() {
        return id;
    }
}
