package com.bindingofnewton.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.assets.SoundHandler;
import com.bindingofnewton.game.character.Player;
import com.bindingofnewton.game.map.Level;

import java.util.HashMap;

/**
 * Handles the keyboard input. Implements the InputProcessor to get notified when a key was pressed
 */
public class InputHandler implements InputProcessor {
    private HashMap<Integer, Boolean> movingKeys;
    private HashMap<Integer, Boolean> shootingKeys;

    private int x;
    private int y;

    private long lastShot = 0;

    private Level level;

    /**
     * @param level The level
     */
    public InputHandler(Level level){
        movingKeys = new HashMap<>();
        shootingKeys = new HashMap<>();

        movingKeys.put(Input.Keys.W, false);
        movingKeys.put(Input.Keys.S, false);
        movingKeys.put(Input.Keys.A, false);
        movingKeys.put(Input.Keys.D, false);

        shootingKeys.put(Input.Keys.UP, false);
        shootingKeys.put(Input.Keys.DOWN, false);
        shootingKeys.put(Input.Keys.LEFT, false);
        shootingKeys.put(Input.Keys.RIGHT, false);

        this.level = level;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.SHIFT_LEFT){
            BindingOfNewton.getInstance().showDebugInfo = !BindingOfNewton.getInstance().showDebugInfo;
        }

        if (movingKeys.containsKey(keycode)){
            movingKeys.put(keycode, true);
        }
        if (shootingKeys.containsKey(keycode)){
            shootingKeys.put(keycode, true);
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (movingKeys.containsKey(keycode)){
            movingKeys.put(keycode, false);
        }
        if (shootingKeys.containsKey(keycode)){
            shootingKeys.put(keycode, false);
        }

        return true;
    }

    /**
     * Gets the movement of the player.
     * @return The vector of the movement. If the player is not moving it returns (0,0).
     */
    public Vector2 getPlayerMovement(){
        Player player = level.getCurrentRoom().getPlayer();

        int x = 0;
        int y = 0;

        for (Integer keycode : movingKeys.keySet()){
            if (movingKeys.get(keycode)){
                switch (keycode){
                    case Input.Keys.W:
                        y += player.getSpeed();
                        break;
                    case Input.Keys.S:
                        y -= player.getSpeed();
                        break;
                    case Input.Keys.A:
                        x -= player.getSpeed();
                        break;
                    case Input.Keys.D:
                        x += player.getSpeed();
                        break;
                }
            }
        }

        return new Vector2(x, y);

    }

    /**
     * Handles the bullets
     */
    public void handleBullets() {
        Player player = level.getCurrentRoom().getPlayer();

        float posX = player.getBody().getPosition().x;
        float posY = player.getBody().getPosition().y;
        float width = AssetsHandler.getInstance().getPlayerSprite(player.getPlayerName(), player.getOrientation()).getWidth();
        float height = AssetsHandler.getInstance().getPlayerSprite(player.getPlayerName(), player.getOrientation()).getHeight();

        for (Integer keycode : shootingKeys.keySet()){
            if (shootingKeys.get(keycode)){
                switch (keycode){
                    case Input.Keys.UP:
                        shootBullet(Orientation.UP,
                                new Vector2((int) (posX + width / 2 ), (int) (posY + height)));
                        break;
                    case Input.Keys.DOWN:
                        shootBullet(Orientation.DOWN,
                                new Vector2((int)( posX + width/2), (int)posY - 8));
                        break;
                    case Input.Keys.LEFT:
                        shootBullet(Orientation.LEFT,
                                new Vector2((int)posX - 8, (int) (posY + height/2)));
                        break;
                    case Input.Keys.RIGHT:
                        shootBullet(Orientation.RIGHT,
                                new Vector2((int) (posX + width + 8), (int) (posY + height/ 2)));
                        break;
                }
            }
        }

    }

    /**
     * Shoots a bullet
     * @param orientation the orientation where the bullet gets shot at
     * @param pos the start position of the bullet
     */
    private void shootBullet(Orientation orientation, Vector2 pos){
        Player player = level.getCurrentRoom().getPlayer();
        player.setOrientation(orientation);
        if (System.currentTimeMillis() - lastShot >= Bullet.fireRate) {
            Bullet bullet = new Bullet(level.getWorld(),
                    (int) pos.x,
                    (int) pos.y);
            //SoundHandler.getInstance().playSound(SoundHandler.Sound.SHOOT);
            bullet.setMovement(orientation.moveCoord(new Vector2(0,0), bullet.getSpeed()));
            level.getCurrentRoom().addBullet(bullet);

            lastShot = System.currentTimeMillis();
        }
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
