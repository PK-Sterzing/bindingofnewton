package com.bindingofnewton.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bindingofnewton.game.character.Player;

import java.util.ArrayList;

public class InputHandler implements InputProcessor {

    private Player player;
    protected boolean isMoving;
    protected boolean isShooting;
    private ArrayList<Integer> activeMoveKeys;
    private ArrayList<Integer> activeShootKeys;
    private int x;
    private int y;

    public InputHandler(Player player){
        activeMoveKeys = new ArrayList<>();
        activeShootKeys = new ArrayList<>();
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {


        if (keycode == Input.Keys.W || keycode == Input.Keys.S || keycode == Input.Keys.A || keycode == Input.Keys.D) {
            activeMoveKeys.add(keycode);
        }

        switch (keycode) {
            case Input.Keys.W: {
                y += 1;
                break;
            }
            case Input.Keys.S: {
                y -= 1;
                break;
            }
            case Input.Keys.A: {
                x -= 1;
                break;
            }
            case Input.Keys.D: {
                x += 1;
                break;
            }
            /*
            case Input.Keys.LEFT: {
                player.setOrientation(Orientation.LEFT);
                break;
            }
            case Input.Keys.RIGHT: {
                player.setOrientation(Orientation.RIGHT);
                break;
            }
            case Input.Keys.UP: {
                player.setOrientation(Orientation.UP);
                break;
            }
            case Input.Keys.DOWN: {
                player.setOrientation(Orientation.DOWN);
                break;
            }

             */
            case Input.Keys.SHIFT_LEFT: {
                BindingOfNewton.showDebugInfo = !BindingOfNewton.showDebugInfo;
                break;
            }
            case Input.Keys.ESCAPE: {
                BindingOfNewton.game.setScreen(new MainMenu(BindingOfNewton.game));
                break;
            }
        }

        isMoving = false;
        if ((activeMoveKeys.contains(Input.Keys.W) ^ activeMoveKeys.contains(Input.Keys.S)) || (activeMoveKeys.contains(Input.Keys.A) ^ activeMoveKeys.contains(Input.Keys.D))) {
            isMoving = true;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (activeShootKeys.contains(keycode)) {
            activeShootKeys.remove((Object) keycode);
        } else if (activeMoveKeys.contains(keycode)) {
            activeMoveKeys.remove((Object) keycode);
        }

        if (activeMoveKeys.isEmpty()) {
            isMoving = false;
        } else if (activeShootKeys.isEmpty()) {
            isShooting = false;
        }

        isMoving = false;
        if ((activeMoveKeys.contains(Input.Keys.W) ^ activeMoveKeys.contains(Input.Keys.S)) || (activeMoveKeys.contains(Input.Keys.A) ^ activeMoveKeys.contains(Input.Keys.D))) {
            isMoving = true;
        }

        return true;
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
