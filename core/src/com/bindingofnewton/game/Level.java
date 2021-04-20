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

    public List getRooms(){
        return rooms;
    }

    //<editor-fold desc="Builder">

    public static class Builder {

        private Level level;

        public Builder(){
            level = new Level();
        }

        public void build(){
            
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
