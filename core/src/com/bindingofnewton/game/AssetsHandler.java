package com.bindingofnewton.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AssetsHandler {
    public static final String START_MAP = "mapStart.tmx";
    public static final String END_MAP = "mapEnd.tmx";
    public static final String MAP = "map1.tmx";

    private String ASSETS_PATH = ".\\core\\assets\\";

    private static AssetsHandler instance;
    private TextureAtlas textureAtlas;

    private AssetsHandler(){
        textureAtlas = new TextureAtlas("data.txt");
        ASSETS_PATH = Paths.get("").toAbsolutePath().toString() + "\\core\\assets\\";
    }

    public static AssetsHandler getInstance(){
        if (instance == null){
            instance = new AssetsHandler();
        }
        return instance;
    }

    public Sprite[] getPlayerSprite(String name){
        Sprite[] array = new Sprite[4];

        array[0] = textureAtlas.createSprite(name + "-back");
        array[1] = textureAtlas.createSprite(name + "-front");
        array[2] = textureAtlas.createSprite(name + "-left");
        array[3] = textureAtlas.createSprite(name + "-right");

        for (Sprite sprite : array) {
            //array[i].setSize(array[i].);
            //array[i].setScale(0.7f);
            sprite.setSize(sprite.getWidth() * 0.7f, sprite.getHeight() * 0.7f);
        }

        return array;
    }

    public Array<TextureAtlas.AtlasRegion> getAnimationRegions(String name) {
        return textureAtlas.findRegions(name);
    }

    public List<String> getMaps(){
        ArrayList<String> list = new ArrayList<>();
        File file = new File(ASSETS_PATH + START_MAP);

        if (file.exists()) list.add(file.getName());

        file = new File(ASSETS_PATH + MAP);
        while(file.exists()){
            list.add(file.getName());
            String name = file.getName();

            //TODO: catch exceptions
            int number = Integer.parseInt(name.replaceAll("\\D+", ""));
            number++;
            name = name.replace(String.valueOf(number-1), String.valueOf(number));

            file = new File(ASSETS_PATH + name);
        }

        file = new File(ASSETS_PATH + END_MAP);
        if (file.exists()) list.add(file.getName());

        return list;
    }

}
