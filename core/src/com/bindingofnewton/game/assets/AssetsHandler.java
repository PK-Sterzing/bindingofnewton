package com.bindingofnewton.game.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.bindingofnewton.game.Orientation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AssetsHandler {

    //<editor-fold desc="Enums">

    public enum PlayerName {
        NEWTON,
        EDISON
    }

    /**
     * Enum for the properties of the enemy. Every enemy has his own vertices for the body.
     */
    public enum EnemyProperties {

    }
    //</editor-fold>

    public static final String ASSETS_ABSOLUTE = "./core/assets/";
    public static final String START_MAP = "mapStart.tmx";
    public static final String END_MAP = "mapEnd.tmx";
    public static final String MAP = "map1.tmx";

    public static final String BIG_ATLAS = ASSETS_ABSOLUTE + "./big_atlas/packed/big_atlas.atlas";

    public static final String NEWTON_RUN = "./character/newton_run/packed/newton-run.atlas";
    public static final String BAT_RUN = "./character/bat_run/packed/enemy1.atlas";
    public static final String EDISON = "./character/edison/packed/edison.atlas";
    public static final String EDISON_RUN = "./character/edison_run/packed/edison_run.atlas";
    public static final String MAP_TILED = ASSETS_ABSOLUTE + "map/tiled/";

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

        sprite.setSize(sprite.getWidth() * .7f, sprite.getHeight() * .7f);
        return sprite;
    }

    public ArrayList<Sprite> getEnemySprites(EnemyProperties enemyName, Orientation orientation){
        ArrayList<Sprite> array = new ArrayList<>();

        Sprite sprite = textureAtlas.createSprite(enemyName.name().toLowerCase() + "_run1");
        array.add(sprite);
        Sprite sprite1 = new Sprite(sprite);
        sprite1.flip(true, false);
        array.add(sprite1);

        return array;
    }

    public Animation<Sprite> getAnimation(String name, float duration, float scaleFactor) {
        Array<Sprite> sprites = new Array<>();
        int counter = 1;

        while(true) {
            Sprite sprite = getSingleSpriteFromAtlas(name + "-" + counter);
            if (sprite == null) break;
            sprite.setSize(sprite.getWidth() * scaleFactor, sprite.getHeight() * scaleFactor);
            sprites.add(sprite);
            counter++;
        }
        return new Animation<>(duration, sprites, Animation.PlayMode.LOOP);
    }

    public Animation<Sprite> getPlayerRunAnimation(PlayerName playerName, Orientation orientation, float duration, float scaleFactor) {
        switch (orientation) {
            case DOWN: return getAnimation(playerName.toString().toLowerCase() + "-run-front", duration, scaleFactor);
            case LEFT: return getAnimation(playerName.toString().toLowerCase() + "-run-left", duration, scaleFactor);
            case RIGHT: return getAnimation(playerName.toString().toLowerCase() + "-run-right", duration, scaleFactor);
            default: return getAnimation(playerName.toString().toLowerCase() + "-run-back", duration, scaleFactor);
        }
    }

    public Sprite getAnimationFrame(Animation<Sprite> animation, float deltaTime) {
        return animation.getKeyFrame(deltaTime, true);
    }

    public List<String> getMaps() {
        ArrayList<String> list = new ArrayList<>();

        File file = new File(MAP_TILED + START_MAP);

        if (file.exists()) list.add(MAP_TILED + file.getName());

        file = new File(MAP_TILED + MAP);

        while (file.exists()) {
            list.add(MAP_TILED + file.getName());
            String name = file.getName();

            //TODO: catch exceptions
            int number = Integer.parseInt(name.replaceAll("\\D+", ""));
            number++;
            name = name.replace(String.valueOf(number - 1), String.valueOf(number));

            file = new File(MAP_TILED + name);
        }

        file = new File(MAP_TILED + END_MAP);
        if (file.exists()) list.add(MAP_TILED + file.getName());

        return list;
    }

    public Sprite getSingleSpriteFromFile(String name) {
        Texture texture = new Texture(ASSETS_ABSOLUTE + name);
        return new Sprite(texture);
    }

    public Sprite getSingleSpriteFromAtlas(String name) {
        return textureAtlas.createSprite(name);
    }
}
