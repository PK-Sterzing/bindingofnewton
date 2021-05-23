package com.bindingofnewton.game.assets;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;

public class SoundHandler {
    private static SoundHandler instance;
    private HashMap<Sound, com.badlogic.gdx.audio.Sound> sounds;
    private HashMap<Music, com.badlogic.gdx.audio.Music> musics;

    public enum Sound {
        SHOOT
    }

    public enum Music {
        MAIN_MENU
    }

    private SoundHandler() {
        //Load all required Sound
        sounds = new HashMap<>();
        musics = new HashMap<>();
        sounds.put(Sound.SHOOT, Gdx.audio.newSound(Gdx.files.internal("sounds/shiasn.mp3")));
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
        sounds.get(sound).play();
    }

    public void stopMusic(Music music) {
        musics.get(music).stop();
    }
}
