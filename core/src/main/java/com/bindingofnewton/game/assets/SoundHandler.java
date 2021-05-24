package com.bindingofnewton.game.assets;

import com.badlogic.gdx.Gdx;
import java.util.HashMap;

/**
 * Handles all sounds and music in the game. Like {@link AssetsHandler} also a Singleton-Pattern. Instance gets created at start of the game, because loading sounds and music needs a lot of time.
 */
public class SoundHandler {
    private static SoundHandler instance;
    private HashMap<Sound, com.badlogic.gdx.audio.Sound> sounds;
    private HashMap<Music, com.badlogic.gdx.audio.Music> musics;

    public enum Sound {
        SHOOT,
        ELECTRIC,
        HIT,
        PAPER_THROW,
        DOOR_OPEN
    }

    public enum Music {
        MAIN_MENU
    }

    private SoundHandler() {
        //Load all required Sound
        sounds = new HashMap<>();
        musics = new HashMap<>();

        sounds.put(Sound.SHOOT, Gdx.audio.newSound(Gdx.files.internal("sounds/newton-shot.mp3")));
        sounds.put(Sound.HIT, Gdx.audio.newSound(Gdx.files.internal("sounds/aua.wav")));
        sounds.put(Sound.ELECTRIC, Gdx.audio.newSound(Gdx.files.internal("sounds/electric.mp3")));
        sounds.put(Sound.PAPER_THROW, Gdx.audio.newSound(Gdx.files.internal("sounds/paper-throw.wav")));
        sounds.put(Sound.DOOR_OPEN, Gdx.audio.newSound(Gdx.files.internal("sounds/door-open.mp3")));

        musics.put(Music.MAIN_MENU, Gdx.audio.newMusic(Gdx.files.internal("sounds/main_menu.wav")));
    }

    public static SoundHandler getInstance() {
        if (instance == null) {
            instance = new SoundHandler();
        }
        return instance;
    }

    public void playMusic(Music music, Boolean loop, float volume) {
        musics.get(music).setLooping(loop);
        musics.get(music).setVolume(volume);
        musics.get(music).play();
    }

    public void playSound(Sound sound) {
        sounds.get(sound).play(0.1f);
    }

    public void stopMusic(Music music) {
        musics.get(music).stop();
    }
}
