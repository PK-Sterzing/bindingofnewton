package com.bindingofnewton.game;

import com.badlogic.gdx.math.Vector2;

public enum Orientation {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Orientation getOpposite(){
        switch (this){
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case UP:
            default:
                return DOWN;
        }
    }

    public Vector2 moveCoord(Vector2 vector, float value){
        switch (this){
            case UP:
                vector.y += value;
                break;
            case DOWN:
                vector.y -= value;
                break;
            case RIGHT:
                vector.x += value;
                break;
            case LEFT:
                vector.x -= value;
                break;
        }
        return vector;
    }

}
