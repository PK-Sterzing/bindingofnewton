package com.bindingofnewton.game;

import com.badlogic.gdx.Game;
import com.bindingofnewton.game.assets.SoundHandler;
import com.bindingofnewton.game.mainmenu.MainMenuScreen;

public class BindingOfNewtonGame extends Game {
    @Override
    public void create() {
        SoundHandler.getInstance().playMusic(SoundHandler.Music.MAIN_MENU, true);
        setScreen(new MainMenuScreen(this));
    }
}
