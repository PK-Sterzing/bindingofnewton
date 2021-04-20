package com.bindingofnewton.game;

public class RoomBuilder {

    private String tmxFilename;

    public RoomBuilder(String tmxFilename){
       this.tmxFilename = tmxFilename;
   }

    public void createRoom(){
        manipulate(5, 0);
    }

    private void manipulate(int x, int y){

    }

}
