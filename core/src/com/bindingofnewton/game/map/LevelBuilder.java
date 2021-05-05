package com.bindingofnewton.game.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.character.Enemy;

import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {
    private Level level;

    public LevelBuilder(){
        level = new Level();
        level.rooms = new ArrayList<>();
    }

    public LevelBuilder setWorld(World world){
        level.world = world;
        return this;
    }

    public LevelBuilder setLevelWidth(int width){
        level.width = width;
        return this;
    }

    public LevelBuilder setLevelHeight(int height){
        level.height = height;
        return this;
    }

    public LevelBuilder setLevelWidthHeight(int width, int height){
        setLevelWidth(width);
        setLevelHeight(height);
        return this;
    }

    public LevelBuilder setMinRooms(int min){
        level.minRooms = min;
        return this;
    }

    public LevelBuilder setAmountRandomRooms(int min, int max){
        level.randomMaxRooms = max;
        level.randomMinRooms = min;
        return this;
    }

    public void setStartRoom(Room room){
        if (level.rooms.contains(room)) level.currentRoom = room;
    }

    public Level build(){
        int amountRooms = (int) (level.minRooms +  Math.random() * (level.randomMaxRooms - level.randomMinRooms) + level.randomMinRooms);

        RoomBuilder builder = new RoomBuilder();
        int x = (int) (Math.random() * level.width);
        int y = (int) (Math.random() * level.height);


        Room startRoom = builder
                .setPosition(x, y)
                .setMap(AssetsHandler.MAP_TILED + AssetsHandler.START_MAP)
                .setWorld(level.world)
                .build();

        level.rooms.add(startRoom);

        for (int i=1; i<amountRooms; i++){
            //Randomly decides which room gets a neighbor
            int roomIndex = 0;
            int counter = 0;

            boolean doAgain=false;
            while (true){
                if (counter > 100){
                    doAgain = true;

                    break;
                }
                roomIndex = (int) (Math.random() * level.rooms.size());

                if (level.rooms.get(roomIndex).getDoors().size() != 4){
                    break;
                }
                counter++;
            }
            if (doAgain){
                i--;
                continue;
            }

            Room currentRoom = level.rooms.get(roomIndex);

            List<Orientation> possibleNeighbors = currentRoom.getPossibleDoors();

            Vector2 vector;
            Orientation neighbor;
            counter = 0;
            do{
                neighbor = possibleNeighbors.get((int) (Math.random() * possibleNeighbors.size()));
                vector = neighbor.moveCoord(new Vector2(currentRoom.getX(), currentRoom.getY()), 1);
                counter++;
            }while((vector.x < 0 || vector.y < 0) && counter < 20);
            if (counter > 20){
                i--;
                continue;
            }

            currentRoom.addDoor(new Door(level.world, currentRoom.getMap(), neighbor));

            builder = new RoomBuilder();

            if (i == amountRooms-1){
                builder.setMap( AssetsHandler.MAP_TILED + AssetsHandler.END_MAP);
            }
            Room room = builder
                    .setPosition(
                            (int) vector.x,
                            (int) vector.y
                    )
                    .setWorld(level.world)
                    .build();

            room.addDoor(new Door(level.world, room.getMap(), neighbor.getOpposite()));

            level.rooms.add(room);
        }

        //Adds all doors to neighbor rooms that did not get added yet
        for (Room room : level.rooms){
            List<Orientation> possibleNeighbors = room.getPossibleDoors();

            for (Orientation orientation : possibleNeighbors){

                x = room.getX();
                y = room.getY();

                x = (int) orientation.moveCoord(new Vector2(x, y), 1).x;
                y = (int) orientation.moveCoord(new Vector2(x, y), 1).y;

                for (Room neighborRoom : level.rooms){
                    if (neighborRoom.getX() == x && neighborRoom.getY() == y){
                        room.addDoor(new Door(level.world, room.getMap(), orientation));
                        neighborRoom.addDoor(new Door(level.world, neighborRoom.getMap(), orientation.getOpposite()));
                    }
                }

            }
        }
        return level;
    }
}
