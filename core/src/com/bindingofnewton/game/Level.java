package com.bindingofnewton.game;


import java.util.ArrayList;
import java.util.List;

public class Level {
    private ArrayList<Room> rooms;

    private int width;
    private int height;

    private int minRooms;
    private int randomMinRooms;
    private int randomMaxRooms;

    private Level(){
        rooms = new ArrayList<>();
    }

    public ArrayList<Room> getRooms(){
        return rooms;
    }

    //<editor-fold desc="Builder">

    public static class Builder {

        private Level level;

        public Builder(){
            level = new Level();
        }

        //TODO: improve this method
        public Level build(){
            int amountRooms = (int) (level.minRooms +  Math.random() * (level.randomMaxRooms - level.randomMinRooms) + level.randomMinRooms);

            Room.Builder builder = new Room.Builder();
            int x = (int) (Math.random() * level.width);
            int y = (int) (Math.random() * level.height);

            builder.setPosition(x, y);
            builder.setMap(AssetsHandler.START_MAP);
            Room startRoom = builder.build();
            level.rooms.add(startRoom);

            for (int i=1; i<amountRooms; i++){

                //Randomly decides which room gets neighbor
                int roomIndex = 0;
                do{
                    //TODO: runden
                    roomIndex = (int) (Math.random() * level.rooms.size());
                }while (level.rooms.get(roomIndex).getNeighbors().size() == 4);

                Room currentRoom = level.rooms.get(roomIndex);

                List<Orientation> neighbors = currentRoom.getNeighbors();
                List<Orientation> possibleNeighbors = new ArrayList<>();
                for (Orientation orientation : Orientation.values()){
                    if (!neighbors.contains(orientation)) possibleNeighbors.add(orientation);
                }

                Orientation neighbor = possibleNeighbors.get((int) (Math.random() * possibleNeighbors.size()));
                currentRoom.addNeighbor(neighbor);

                builder = new Room.Builder();

                Room room = null;
                switch (neighbor){
                    case UP:
                        builder.setPosition(currentRoom.getX(), currentRoom.getY()+1);
                        room = builder.build();
                        room.addNeighbor(Orientation.DOWN);
                        break;
                    case DOWN:
                        builder.setPosition(currentRoom.getX(), currentRoom.getY()-1);
                        room = builder.build();
                        room.addNeighbor(Orientation.UP);
                        break;
                    case LEFT:
                        builder.setPosition(currentRoom.getX()-1, currentRoom.getY());
                        room = builder.build();
                        room.addNeighbor(Orientation.RIGHT);
                        break;
                    case RIGHT:
                        builder.setPosition(currentRoom.getX()+1, currentRoom.getY());
                        room = builder.build();
                        room.addNeighbor(Orientation.LEFT);
                        break;
                }
                level.rooms.add(room);
            }

            for (Room room : level.rooms){
                List<Orientation> possibleNeighbors = new ArrayList<>();
                for (Orientation orientation : Orientation.values()){
                    if (!room.getNeighbors().contains(orientation)) possibleNeighbors.add(orientation);
                }

                for (Orientation orientation : possibleNeighbors){

                    x = room.getX();
                    y = room.getY();

                    switch(orientation){
                        case DOWN:
                            y--;
                            break;
                        case UP:
                            y++;
                            break;
                        case LEFT:
                            x--;
                            break;
                        case RIGHT:
                            x++;
                            break;
                    }

                    for (Room room1 : level.rooms){
                        if (room1.getX() == x && room1.getY() == y){
                            room.addNeighbor(orientation);
                            switch (orientation){
                                case UP:
                                    room1.addNeighbor(Orientation.DOWN);
                                    break;
                                case DOWN:
                                    room1.addNeighbor(Orientation.UP);
                                    break;
                                case LEFT:
                                    room1.addNeighbor(Orientation.RIGHT);
                                    break;
                                case RIGHT:
                                    room1.addNeighbor(Orientation.LEFT);
                            }
                        }
                    }

                }
                room.setDoors(room.getNeighbors());
            }

            return level;
        }

        public int getOpenDoors(Room room){

            return 0;
        }

        public Builder setWorldWidth(int width){
            level.width = width;
            return this;
        }

        public Builder setWorldHeight(int height){
            level.height = height;
            return this;
        }

        public Builder setWorldWidthHeight(int width, int height){
            setWorldWidth(width);
            setWorldHeight(height);
            return this;
        }

        public Builder setMinRooms(int min){
            level.minRooms = min;
            return this;
        }

        public Builder setAmountRandomRooms(int min, int max){
            level.randomMaxRooms = max;
            level.randomMinRooms = min;
            return this;
        }
    }

    //</editor-fold>
}
