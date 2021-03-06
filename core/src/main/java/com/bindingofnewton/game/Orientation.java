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

    public static Vector2 addVectors(Orientation orientation1, Vector2 v1, Orientation orientation2, Vector2 v2){
        if (orientation1 == orientation2){
            if (orientation1.isHorizontal()){
                return orientation1.moveCoord(v1, v2.x);
            }else{
                return orientation1.moveCoord(v1, v2.y);
            }
        }else if (orientation1.getOpposite() == orientation2){
            if (orientation1.isHorizontal()){
                return orientation1.moveCoord(v1, -v2.x);
            }else{
                return orientation1.moveCoord(v1, -v2.y);
            }
        }else{
            return v1;
        }
    }

    /**
     * Returns a random Orientation
     * @return
     */
    public static Orientation getRandom(){
        return values()[(int) (Math.random() * values().length)];
    }

    public boolean isOpposite(){
        return this.getOpposite() == this;
    }

    public boolean isHorizontal(){
        return this == LEFT || this == RIGHT;
    }

}
