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

    //TODO: gleiche Methode mit x-, y-Übergabeparametern
    public Vector2 moveCoord(Vector2 vector, float value){
        Vector2 vector2 = new Vector2(vector);
        switch (this){
            case UP:
                vector2.y += value;
                break;
            case DOWN:
                vector2.y -= value;
                break;
            case RIGHT:
                vector2.x += value;
                break;
            case LEFT:
                vector2.x -= value;
                break;
        }
        return vector2;
    }

}
