package com.bindingofnewton.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.bindingofnewton.game.assets.AssetsHandler;
import javafx.scene.shape.Rectangle;

/**
 * The class for the minimap on the top left corner of the game.
 */
public class Minimap {

    //<editor-fold desc="Enum">

    /**
     * Enum for the State of a room
     */
    private enum RoomState{
        BOSS(AssetsHandler.getInstance().getSingleSprite("./minimap/boss.png")),
        CLEARED(AssetsHandler.getInstance().getSingleSprite("./minimap/cleared.png")),
        CURRENT(AssetsHandler.getInstance().getSingleSprite("./minimap/current.png")),
        UNCLEARED(AssetsHandler.getInstance().getSingleSprite("./minimap/uncleared.png"));

        private Sprite sprite;

        RoomState(Sprite sprite) {
            this.sprite = sprite;
        }
    }
    //</editor-fold>

    //<editor-fold desc="Members">
    private Level level;
    private Room currentRoom;

    private int height;
    private int width;

    private int spriteHeight;
    private int spriteWidth;
    //</editor-fold>

    //<editor-fold desc="Constructor">

    /**
     * Creates a new minimap of a level.
     * @param level the level which the minimap should show.
     */
    public Minimap(Level level) {
        this.level = level;

        TiledMapTileLayer layer = (TiledMapTileLayer) level.getCurrentRoom().getMap().getLayers().get("ground");
        width = layer.getTileWidth() * layer.getWidth();
        height = layer.getTileHeight() * layer.getHeight();

        spriteHeight = (int) RoomState.CURRENT.sprite.getHeight();
        spriteWidth = (int) RoomState.CURRENT.sprite.getWidth();

    }
    //</editor-fold>

    //<editor-fold desc="Methods">

    /**
     * Renders the minimap.
     * @param batch
     */
    public void render(Batch batch){
        currentRoom = level.currentRoom;

        float posX = width-spriteWidth * 3.5f;
        float posY = height-spriteHeight * 2.5f;

        //Current room gets drawn
        batch.draw(RoomState.CURRENT.sprite, posX, posY);

        //Check if rooms in near of current room
        for (int x = currentRoom.getX() - 2; x < currentRoom.getX()+3; x++){
            for (int y = currentRoom.getY()-1; y < currentRoom.getY() + 2; y++) {
                for (Room room : level.rooms){
                    if (room.getX() == x && room.getY() == y && room != currentRoom){
                        RoomState state;
                        if (room == level.rooms.get(level.rooms.size()-1)){
                            state = RoomState.BOSS;
                        }else if (room.isCleared()){
                            state = RoomState.CLEARED;
                        }else{
                            state = RoomState.UNCLEARED;
                        }
                        batch.draw(state.sprite, posX - spriteWidth * (currentRoom.getX() - x), posY - spriteHeight * (currentRoom.getY() - y));
                        break;
                    }
                }
            }
        }

    }


    //</editor-fold>
}
