package com.bindingofnewton.game.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AssetsHandler {
    public static final String START_MAP = "mapStart.tmx";
    public static final String END_MAP = "mapEnd.tmx";
    public static final String MAP = "map1.tmx";

    public static final String NEWTON = "./character/newton/packed/newton.atlas";
    public static final String NEWTON_RUN = "./character/newton_run/packed/newton-run.atlas";
    public static final String EDISON = "./character/edison/packed/edison.atlas";
    public static final String EDISON_RUN = "./character/edison_run/packed/edison_run.atlas";
    public static final String MAP_TILED_ABSOLUTE = Paths.get("").toAbsolutePath().toString() + "\\core\\assets\\" + "map\\tiled\\";
    public static final String MAP_TILED= "./map/tiled/";
    public static final String HEARTS = "./hearts";


    private static AssetsHandler instance;
    private final TextureAtlas textureAtlas;

    private AssetsHandler(){
        textureAtlas = new TextureAtlas(NEWTON);
    }

    public static AssetsHandler getInstance(){
        if (instance == null){
            instance = new AssetsHandler();
        }
        return instance;
    }

    public Sprite[] getEntitySprites(String name){
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



    public List<String> getMaps(){
        ArrayList<String> list = new ArrayList<>();
        File file = new File(MAP_TILED_ABSOLUTE + START_MAP);

        System.out.println(MAP_TILED_ABSOLUTE + START_MAP);
        if (file.exists()) list.add(MAP_TILED + file.getName());

        file = new File(MAP_TILED_ABSOLUTE + MAP);
        while(file.exists()){
            list.add(MAP_TILED + file.getName());
            String name = file.getName();

            //TODO: catch exceptions
            int number = Integer.parseInt(name.replaceAll("\\D+", ""));
            number++;
            name = name.replace(String.valueOf(number-1), String.valueOf(number));

            file = new File(MAP_TILED_ABSOLUTE + name);
        }

        file = new File(MAP_TILED_ABSOLUTE + END_MAP);
        if (file.exists()) list.add(MAP_TILED + file.getName());

        return list;
    }

    public Sprite getSingleSprite(String name){
        Texture texture = new Texture(name);
        return  new Sprite(texture);
    }

}
