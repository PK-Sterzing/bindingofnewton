package com.bindingofnewton.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class InputHandler implements InputProcessor {

    private Player player;

    public InputHandler(Player player){
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.W:
                player.move(new Vector2(0, player.getSpeed()));
                break;
            case Input.Keys.A:
                player.move(new Vector2(-player.getSpeed(), 0));
                break;
            case Input.Keys.S:
                player.move(new Vector2(0, -player.getSpeed()));
                break;
            case Input.Keys.D:
                player.move(new Vector2(player.getSpeed(), 0));
                break;
            case Input.Keys.UP:

            case Input.Keys.LEFT:

            case Input.Keys.DOWN:

            case Input.Keys.RIGHT:


        }

        return false;

    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
