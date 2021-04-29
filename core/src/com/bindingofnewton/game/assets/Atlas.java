package com.bindingofnewton.game.assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Atlas {

    private static Atlas instance;
    private TextureAtlas textureAtlas;

    private Atlas(){
        textureAtlas = new TextureAtlas("data.txt");
    }

    public static Atlas getInstance(){
        if (instance == null){
            instance = new Atlas();
        }
        return instance;
    }

    public Sprite[] getPlayerSprite(String name){
        Sprite[] array = new Sprite[4];

        array[0] = textureAtlas.createSprite(name + "-back");
        array[1] = textureAtlas.createSprite(name + "-front");
        array[2] = textureAtlas.createSprite(name + "-left");
        array[3] = textureAtlas.createSprite(name + "-right");

        // Scaled sprite
        for(int i = 0; i < array.length; i++){
            array[i].setSize(array[i].getWidth()*0.7f, array[i].getHeight()*0.7f);
        }

        return array;
    }

}
