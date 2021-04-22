package com.bindingofnewton.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Door {
    private World world;
    private Orientation orientation;
    private TiledMap map;

    private boolean isOpen;
    private Body body;

    public Door(World world, TiledMap map, Orientation orientation){
        this.world = world;
        this.orientation = orientation;
        this.map = map;
        isOpen = false;
        setDoorOnMap();
    }

    private void setDoorOnMap(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("ground");
        TiledMapTileSet tileSet = map.getTileSets().getTileSet(0);

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tileSet.getTile(4));

        int width = layer.getWidth();
        int height = layer.getHeight();

        switch (orientation){
            case DOWN:
                layer.setCell(width/2, 0, cell);
                break;
            case UP:
                layer.setCell(width/2, height-1, cell);
                break;
            case LEFT:
                layer.setCell(0, height/2, cell);
                break;
            case RIGHT:
                layer.setCell(width-1, height/2, cell);

        }
        //map.getLayers().remove(map.getLayers().get("doors-" + orientation.name()));
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
    }

    public void close(){
        isOpen = false;
        if (body != null)
            body.setActive(true);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Body getBody() {
        return body;
    }
}
