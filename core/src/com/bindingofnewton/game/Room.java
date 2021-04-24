package com.bindingofnewton.game;

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

import java.util.*;

public class Room {
    private int x;
    private int y;

    private TiledMap map;
    private World world;

    private ArrayList<Door> doors;

    private Room(){
        ArrayList<String> mapList = (ArrayList<String>) AssetsHandler.getInstance().getMaps();
        int index = (int) (Math.random() * (mapList.size()-2) + 1);
        map = new TmxMapLoader().load(mapList.get(index));
    }

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TiledMap getMap() {
        return map;
    }

    public void setBodies(){
        int i=0;
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
                            door.open();
                        }
                    }
                    shape.dispose();
                }
            }
            i++;
        }

    }

    public void addDoor(Door door){
        doors.add(door);
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public static class Builder{
        private Room room;

        public Builder(){
            room = new Room();
            room.doors = new ArrayList<>();
        }

        public Builder setWorld(World world){
            room.world = world;
            return this;
        }

        public Room build(){
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
