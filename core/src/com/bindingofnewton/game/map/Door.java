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
    private World world;
    private Orientation orientation;
    private TiledMap map;
    private Body body;

    private boolean isOpen;
    private boolean isLastDoor;

    /**
     * Creates a new Door
     * @param world the world of the door
     * @param map the map where the door should be
     * @param orientation the orientation where the door should be
     */
    public Door(World world, TiledMap map, Orientation orientation){
        this(world, map, orientation, false);
    }

    /**
     * Creates a new Door
     * @param world the world of the door
     * @param map the map where the door should be
     * @param orientation the orientation where the door should be
     * @param isLastDoor true - the door is the door to the next level
     */
    public Door(World world, TiledMap map, Orientation orientation, boolean isLastDoor){
        this.world = world;
        this.orientation = orientation;
        this.map = map;
        this.isLastDoor = isLastDoor;
        close();
    }

    private void setDoorOnMap(int id){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("ground");
        TiledMapTileSet tileSet = map.getTileSets().getTileSet(0);

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tileSet.getTile(id));

        int width = layer.getWidth();
        int height = layer.getHeight();

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

    public void setBody(Body body){
        this.body = body;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        isOpen = true;
        if (body != null)
            body.setActive(false);
        setDoorOnMap(isLastDoor ? 0 : 18);
    }

    public void close(){
        isOpen = false;
        if (body != null)
            body.setActive(true);
        setDoorOnMap(isLastDoor ? 0 : 17);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Body getBody() {
        return body;
    }

    public boolean isLastDoor() {
        return isLastDoor;
    }
}
