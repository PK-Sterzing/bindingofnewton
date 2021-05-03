package com.bindingofnewton.game;

import com.badlogic.gdx.Game;

public class BindingOfNewtonGame extends Game {
    @Override
    public void create() {
        setScreen(new MainMenu(this));
    }
}
