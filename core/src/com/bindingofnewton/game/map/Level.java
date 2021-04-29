package com.bindingofnewton.game.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.Orientation;

import java.util.ArrayList;
import java.util.List;

public class Level {
    protected ArrayList<Room> rooms;
    protected Room currentRoom;

    protected int width;
    protected int height;

    protected int minRooms;
    protected int randomMinRooms;
    protected int randomMaxRooms;

    protected World world;

    /*
    public Level(int width, int height, int minRooms, int randomMinRooms, int randomMaxRooms, ArrayList<Room> rooms){
        this.width = width;
        this.height = height;
        this.minRooms = minRooms;
        this.randomMinRooms = randomMinRooms;
        this.randomMaxRooms = randomMaxRooms;
        this.rooms = rooms;
    }

     */

    public Room getNextRoom(Orientation orientation){
        if (currentRoom == null) {
            currentRoom = rooms.get(0);
            return currentRoom;
        }

        if (orientation == null) return null;

        int x = currentRoom.getX();
        int y = currentRoom.getY();
        Vector2 vector = orientation.moveCoord(new Vector2(x, y), 1);

        for (Room room : rooms){
            if (vector.x == room.getX() && vector.y == room.getY()){
                currentRoom = room;
                return currentRoom;
            }
        }

        return currentRoom;
    }

    public Room getCurrentRoom(){
        if (currentRoom == null){
            currentRoom = rooms.get(0);
        }
        return currentRoom;
    }

}