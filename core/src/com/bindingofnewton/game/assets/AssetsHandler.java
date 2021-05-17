package com.bindingofnewton.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.character.Player;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AssetsHandler {
    public enum PlayerName {
        NEWTON,
        EDISON
    }

    public static final String START_MAP = "mapStart.tmx";
    public static final String END_MAP = "mapEnd.tmx";
    public static final String MAP = "map1.tmx";

    public static final String BIG_ATLAS = "./big_atlas/packed/big_atlas.atlas";

    public static final String NEWTON_RUN = "./character/newton_run/packed/newton-run.atlas";
    public static final String BAT_RUN = "./character/bat_run/packed/enemy1.atlas";
    public static final String EDISON = "./character/edison/packed/edison.atlas";
    public static final String EDISON_RUN = "./character/edison_run/packed/edison_run.atlas";
    public static final String MAP_TILED_ABSOLUTE = Paths.get("").toAbsolutePath().toString() + "\\core\\assets\\" + "map\\tiled\\";
    public static final String MAP_TILED = "./map/tiled/";
    public static final String HEARTS = "./hearts";

    private static AssetsHandler instance;

    private final TextureAtlas textureAtlas;

    private static float deltaTime = 0f;

    private AssetsHandler() {
        textureAtlas = new TextureAtlas(BIG_ATLAS);
    }

    public static AssetsHandler getInstance() {
        if (instance == null) {
            instance = new AssetsHandler();
        }
        return instance;
    }

    public ArrayList<Sprite> getPlayerSprites(PlayerName player) {
        ArrayList<Sprite> array = new ArrayList<>();

        array.add(0, textureAtlas.createSprite(player.name().toLowerCase() + "-back"));
        array.add(1, textureAtlas.createSprite(player.name().toLowerCase() + "-front"));
        array.add(2, textureAtlas.createSprite(player.name().toLowerCase() + "-left"));
        array.add(3, textureAtlas.createSprite(player.name().toLowerCase() + "-right"));

        for (Sprite sprite : array) {
            //sprite.setSize(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
            sprite.scale(0.5f);
        }

        return array;
    }

    public Sprite getPlayerSprite(PlayerName playerName, Orientation orientation) {
        Sprite sprite;

        switch (orientation) {
            case DOWN:
                sprite = getPlayerSprites(playerName).get(1);
                break;
            case LEFT:
                sprite = getPlayerSprites(playerName).get(2);
                break;
            case RIGHT:
                sprite = getPlayerSprites(playerName).get(3);
                break;
            case UP:
            default:
                sprite = getPlayerSprites(playerName).get(0);
        }

        //sprite.setSize(sprite.getWidth() * 0.7f, sprite.getHeight() * 0.7f);
        sprite.scale(0.5f);
        return sprite;
    }

    public Sprite getPlayerAnimationFrame(Animation<Sprite> animation) {
        deltaTime += Gdx.graphics.getDeltaTime();
        return animation.getKeyFrame(deltaTime, true);
    }


    public List<String> getMaps() {
        ArrayList<String> list = new ArrayList<>();
        File file = new File(MAP_TILED_ABSOLUTE + START_MAP);

        if (file.exists()) list.add(MAP_TILED + file.getName());

        file = new File(MAP_TILED_ABSOLUTE + MAP);
        while (file.exists()) {
            list.add(MAP_TILED + file.getName());
            String name = file.getName();

            //TODO: catch exceptions
            int number = Integer.parseInt(name.replaceAll("\\D+", ""));
            number++;
            name = name.replace(String.valueOf(number - 1), String.valueOf(number));

            file = new File(MAP_TILED_ABSOLUTE + name);
        }

        file = new File(MAP_TILED_ABSOLUTE + END_MAP);
        if (file.exists()) list.add(MAP_TILED + file.getName());

        return list;
    }

    public Sprite getSingleSprite(String name) {
        Texture texture = new Texture(name);
        return new Sprite(texture);
    }

    public Sprite getSprite(String name) {
        return textureAtlas.createSprite(name);
    }
}
