package com.bindingofnewton.game.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.assets.AssetsHandler;

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

    /**
     * Builds the level with all the rooms
     * @return the level that gets built
     */
    public Level build(){
        int amountRooms = (int) (level.minRooms + level.randomMinRooms + Math.random() * (level.randomMaxRooms - level.randomMinRooms));

        int x = (int) (Math.random() * level.width);
        int y = (int) (Math.random() * level.height);

        level.rooms.add(
                new RoomBuilder()
                    .setWorld(level.world)
                    .setMap(AssetsHandler.MAP_TILED + AssetsHandler.START_MAP)
                    .setPosition(x, y)
                    .build()
        );

        for (int i=0; i<amountRooms-2; i++){
            createNewRoom();
        }

        generateBossRoom();

        for (int i=0; i<level.rooms.size()-1; i++){
            Room room = level.rooms.get(i);
            for (Orientation orientation : room.getPossibleDoors()){
                Vector2 pos = orientation.moveCoord(new Vector2(room.x, room.y), 1);
                for (Room nextRoom : level.rooms){
                    if (nextRoom.x == pos.x && nextRoom.y == pos.y && nextRoom != level.rooms.get(level.rooms.size()-1)){
                        nextRoom.addDoor(new Door(level.world, nextRoom.map, orientation.getOpposite(), Door.Id.NORMAL));
                        room.addDoor(new Door(level.world, room.map, orientation, Door.Id.NORMAL));
                    }
                }
            }
        }

        return level;
    }

    /**
     * Creates a new room, with rand position
     */
    private void createNewRoom(){
        //Gets a random room
        Room room = level.rooms.get((int) (Math.random()*level.rooms.size()));
        while (room.getPossibleDoors().size() == 0){
            room = level.rooms.get((int) (Math.random()*level.rooms.size()));
        }

        Vector2 pos = null;
        String map = null;
        Orientation orientationNextRoom = null;
        int counter = 0;

        do{

            //Gets a random possible orientation of the chosen room
            orientationNextRoom = room.getPossibleDoors().get((int)(Math.random() * room.getPossibleDoors().size()));

            //gets a random map
            List<String> list = AssetsHandler.getInstance().getMaps();
            list.remove(0);
            list.remove(list.size()-1);

            map = list.get((int)(Math.random() * list.size()));
            pos = orientationNextRoom.moveCoord(new Vector2(room.x, room.y), 1);

            counter++;
        }while((pos.x < 0 || pos.x > level.width || pos.y < 0 || pos.y > level.height) && counter < 25);

        //Creating new room
        Room newRoom = new RoomBuilder()
                .setWorld(level.world)
                .setMap(map)
                .setPosition((int)pos.x, (int) pos.y)
                .build();

        //Adds the door in the old and in the new room
        room.addDoor(new Door(level.world, room.getMap(), orientationNextRoom, Door.Id.NORMAL));
        newRoom.addDoor(new Door(level.world, newRoom.getMap(), orientationNextRoom.getOpposite(), Door.Id.NORMAL));

        //Adding the new room to the level
        level.rooms.add(newRoom);
    }

    /**
     * Generates the boss room
     */
    private void generateBossRoom(){
        Orientation orientation = Orientation.getRandom();

        int maxX = level.rooms.get(0).getX();
        int maxY = level.rooms.get(0).getY();
        int x = level.rooms.get(0).getX(), y = level.rooms.get(0).getY();
        RoomBuilder builder = new RoomBuilder();
        Room maxRoom = level.rooms.get(0);
        while(true){
            for (Room room : level.rooms){
                switch (orientation){
                    case RIGHT:
                        if (room.x > maxX) {
                            maxX = room.x;
                            y = room.y;
                            maxRoom = room;
                        }
                        break;
                    case LEFT:
                        if (room.x < maxX){
                            maxX = room.x;
                            y = room.y;
                            maxRoom = room;
                        }
                        break;
                    case DOWN:
                        if (room.y < maxY){
                            maxY = room.y;
                            x = room.x;
                            maxRoom = room;
                        }
                        break;
                    case UP:
                        if (room.y > maxY){
                            maxY = room.y;
                            x = room.x;
                            maxRoom = room;
                        }
                        break;
                }
            }

            //If the maxRoom is the start-room
            if (maxRoom == level.rooms.get(0)){
                orientation = Orientation.getRandom();
            }else{
                break;
            }
        }

        builder
                .setWorld(level.world)
                .setMap(AssetsHandler.MAP_TILED + AssetsHandler.END_MAP);

        Vector2 pos;
        if (orientation.isHorizontal()){
            pos = orientation.moveCoord(new Vector2(maxX, y), 1);
        }else{
            pos = orientation.moveCoord(new Vector2(x, maxY), 1);
        }
        builder.setPosition((int)pos.x, (int)pos.y);
        Room bossRoom = builder.build();

        maxRoom.addDoor(new Door(level.world, maxRoom.getMap(), orientation, Door.Id.BOSS));
        bossRoom.addDoor(new Door(level.world, bossRoom.getMap(), Orientation.UP, Door.Id.PORTAL));
        bossRoom.addDoor(new Door(level.world, bossRoom.getMap(), orientation.getOpposite(), Door.Id.BOSS));

        level.rooms.add(bossRoom);
    }
}
