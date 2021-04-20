package com.bindingofnewton.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private int x;
    private int y;

    private ArrayList<Orientation> neighbors;

    private TiledMap map;

    private Room(){
        ArrayList<String> mapList = (ArrayList<String>) AssetsHandler.getInstance().getMaps();
        int index = (int) (Math.random() * (mapList.size()-2) + 1);
        System.out.println(index);
        map = new TmxMapLoader().load(mapList.get(index));
    }

    private Room(String tmxFilename){
        map = new TmxMapLoader().load(tmxFilename);
    }

    public void setDoors(List<Orientation> orientations){

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        TiledMapTileSet tileSet = map.getTileSets().getTileSet(0);

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tileSet.getTile(4));

        int width = layer.getWidth();
        int height = layer.getHeight();

        for (Orientation orientation : orientations){
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
        }
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

    public void addNeighbor(Orientation orientation){
        if (!neighbors.contains(orientation)) neighbors.add(orientation);
    }

    public List<Orientation> getNeighbors() {
        return neighbors;
    }

    public static class Builder{
        private Room room;

        public Builder(){
            room = new Room();
        }

        public Room build(){
            room.neighbors = new ArrayList<>();
            return room;
        }

        public Builder setPosition(int x, int y){
            room.x = x;
            room.y = y;
            return this;
        }

        public Builder setMap(String tmxFilename){
            room.map = new TmxMapLoader().load(tmxFilename);
            return this;
        }



    }

}
