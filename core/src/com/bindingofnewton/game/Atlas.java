package com.bindingofnewton.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;
import java.util.List;

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

        return array;
    }

}
