package com.bindingofnewton.game.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.character.Player;

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

    protected Level(){ }

    /**
     * Gets the neighbor room of the orientation
     * @param orientation the direction of the neighbor room
     * @return the neighbor room
     */
    public Room getNextRoom(Orientation orientation){
        if (currentRoom == null) {
            currentRoom = rooms.get(0);
            return currentRoom;
        }

        if (orientation == null) return currentRoom;

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

    /**
     * Gets the current room
     * @return current room
     */
    public Room getCurrentRoom(){
        if (currentRoom == null){
            currentRoom = rooms.get(0);
        }
        return currentRoom;
    }

    /**
     * Gets the rooms of the level
     * @return rooms list
     */
    public List<Room> getRooms(){
        return rooms;
    }

    /**
     * Gets the world
     * @return world
     */
    public World getWorld(){
        return world;
    }

}
