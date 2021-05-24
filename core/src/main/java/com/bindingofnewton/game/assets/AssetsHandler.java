package com.bindingofnewton.game.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.bindingofnewton.game.Orientation;
import com.bindingofnewton.game.character.Enemy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all Sprites and Animations. Is a Singleton to reduce loading speed for the big_atlas file where all Sprites are.
 */
public class AssetsHandler {

    //<editor-fold desc="Enums">

    public enum PlayerName {
        NEWTON(SoundHandler.Sound.SHOOT),
        EDISON(SoundHandler.Sound.ELECTRIC),
        EINSTEIN(SoundHandler.Sound.PAPER_THROW);

        private SoundHandler.Sound shot;

        PlayerName(SoundHandler.Sound sound) {
            this.shot = sound;
        }

        public SoundHandler.Sound getShot() {
            return shot;
        }
    }

    //</editor-fold>

    public static final String ASSETS_ABSOLUTE = "./core/assets/";
    public static final String START_MAP = "mapStart.tmx";
    public static final String END_MAP = "mapEnd.tmx";
    public static final String MAP = "map1.tmx";

    public static final String BIG_ATLAS = ASSETS_ABSOLUTE + "./big_atlas/packed/big_atlas.atlas";

    public static final String MAP_TILED = ASSETS_ABSOLUTE + "map/tiled/";
    public static String MAP_CURRENT_LEVEL = "level1/";

    private static AssetsHandler instance;

    private final TextureAtlas textureAtlas;

    private AssetsHandler() {
        textureAtlas = new TextureAtlas(BIG_ATLAS);
    }

    /**
     * Handles Instance (Singleton)
     *
     * @return the instance
     */
    public static AssetsHandler getInstance() {
        if (instance == null) {
            instance = new AssetsHandler();
        }
        return instance;
    }

    /**
     * Gets all sprites for a player
     *
     * @param playerName Name of the player with the sprites
     * @return ArrayList of all player sprites
     */
    public ArrayList<Sprite> getPlayerSprites(PlayerName playerName) {
        ArrayList<Sprite> array = new ArrayList<>();

        array.add(0, textureAtlas.createSprite(playerName.name().toLowerCase() + "-back"));
        array.add(1, textureAtlas.createSprite(playerName.name().toLowerCase() + "-front"));
        array.add(2, textureAtlas.createSprite(playerName.name().toLowerCase() + "-left"));
        array.add(3, textureAtlas.createSprite(playerName.name().toLowerCase() + "-right"));

        for (Sprite sprite : array) {
            //sprite.setSize(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
            sprite.scale(0.5f);
        }

        return array;
    }

    /**
     * Gets a Sprite by it's orientation and the playername
     *
     * @param playerName  playerName of the player object
     * @param orientation orientation always of the player
     * @return a single Sprite
     */
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

    /**
     * Gets an animation with Sprites. Sprites in assets folder need to be named like name-x.jpg, where x starts with 1
     *
     * @param name        the name of the sprite. If it's in a subfolder it's the path
     * @param duration    how long each sprite should be shown
     * @param scaleFactor if scaling is need it can be done here
     * @return Sprite Animation
     */
    public Animation<Sprite> getAnimation(String name, float duration, float scaleFactor) {
        Array<Sprite> sprites = new Array<>();
        int counter = 1;

        while (true) {
            Sprite sprite = getSingleSpriteFromAtlas(name + "-" + counter);
            if (sprite == null) break;
            sprite.setSize(sprite.getWidth() * scaleFactor, sprite.getHeight() * scaleFactor);
            sprites.add(sprite);
            counter++;
        }
        return new Animation<>(duration, sprites, Animation.PlayMode.LOOP);
    }

    /**
     * Calls the {@link #getAnimation(String, float, float) getAnimation}-method. Is specially designed for a player.
     *
     * @param playerName  the playerName of the player
     * @param orientation the orientation of the player, because every player has an animation in each direction
     * @param duration    the duration of each sprite
     * @param scaleFactor if scaling is need it can be done here
     * @return Sprite Animation for player
     */
    public Animation<Sprite> getPlayerRunAnimation(PlayerName playerName, Orientation orientation, float duration, float scaleFactor) {
        switch (orientation) {
            case DOWN:
                return getAnimation(playerName.toString().toLowerCase() + "-run-front", duration, scaleFactor);
            case LEFT:
                return getAnimation(playerName.toString().toLowerCase() + "-run-left", duration, scaleFactor);
            case RIGHT:
                return getAnimation(playerName.toString().toLowerCase() + "-run-right", duration, scaleFactor);
            default:
                return getAnimation(playerName.toString().toLowerCase() + "-run-back", duration, scaleFactor);
        }
    }

    /**
     * Gets the sprite of an animation with the deltaTime.
     *
     * @param animation an animation with Sprites given by {@link #getAnimation(String, float, float) getAnimation}-method
     * @param deltaTime every enemy has a deltaTime attribute. Needs to be given by a parameter
     * @return single Sprite
     */
    public Sprite getAnimationFrame(Animation<Sprite> animation, float deltaTime) {
        return animation.getKeyFrame(deltaTime, true);
    }

    /**
     * Gets all Maps
     *
     * @return List of Strings with all maps
     */
    public List<String> getMaps() {
        ArrayList<String> list = new ArrayList<>();

        File file = new File(MAP_TILED + MAP_CURRENT_LEVEL + START_MAP);

        if (file.exists()) list.add(MAP_TILED + MAP_CURRENT_LEVEL + file.getName());

        file = new File(MAP_TILED + MAP_CURRENT_LEVEL + MAP);

        while (file.exists()) {
            list.add(MAP_TILED + MAP_CURRENT_LEVEL + file.getName());
            String name = file.getName();

            //TODO: catch exceptions
            int number = Integer.parseInt(name.replaceAll("\\D+", ""));
            number++;
            name = name.replace(String.valueOf(number - 1), String.valueOf(number));

            file = new File(MAP_TILED + MAP_CURRENT_LEVEL + name);
        }

        file = new File(MAP_TILED + MAP_CURRENT_LEVEL + END_MAP);
        if (file.exists()) list.add(MAP_TILED + MAP_CURRENT_LEVEL + file.getName());

        return list;
    }

    /**
     * Get's a Sprite by it's file name. Shouldn't be used to often (Performance)
     *
     * @param name of the Sprite (with extension e.x. .jpg, .png)
     * @return single Sprite
     */
    public Sprite getSingleSpriteFromFile(String name) {
        Texture texture = new Texture(ASSETS_ABSOLUTE + name);
        return new Sprite(texture);
    }

    /**
     * Get's a Sprite by big_atlas. Should always be used if Sprite is already in the atlas
     *
     * @param name of the Sprite (IMPORTANT: without extension e.x. .jpg, .png)
     * @return single Sprite
     */
    public Sprite getSingleSpriteFromAtlas(String name) {
        return textureAtlas.createSprite(name);
    }
}