package com.bindingofnewton.game.map;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/**
 * Class that manages rooms
 */
public class RoomBuilder {
    private Room room;

    public RoomBuilder(){
        room = new Room();
        room.doors = new ArrayList<>();
    }

    public RoomBuilder setWorld(World world){
        room.world = world;
        return this;
    }

    public Room build(){
        return room;
    }

    public RoomBuilder setPosition(int x, int y){
        room.x = x;
        room.y = y;
        return this;
    }

    public RoomBuilder setMap(String tmxFilename){
        room.map = new TmxMapLoader().load(tmxFilename);
        room.map.getProperties().put("file", tmxFilename);
        return this;
    }


}
