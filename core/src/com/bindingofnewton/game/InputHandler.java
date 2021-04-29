package com.bindingofnewton.game;

import com.badlogic.gdx.InputProcessor;
import com.bindingofnewton.game.character.Player;

import java.util.ArrayList;

public class InputHandler implements InputProcessor {

    private Player player;
    protected boolean isMoving;
    private ArrayList<Integer> activeKeys;

    public InputHandler(Player player){
        activeKeys = new ArrayList<>();
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        activeKeys.add(keycode);
        isMoving = true;
        /*
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

                break;
            case Input.Keys.UP:

            case Input.Keys.LEFT:

            case Input.Keys.DOWN:

            case Input.Keys.RIGHT:
        }

         */

        return false;

    }

    @Override
    public boolean keyUp(int keycode) {
        activeKeys.remove((Object) keycode);
        if (activeKeys.size() == 0) {
            isMoving = false;
        }
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
