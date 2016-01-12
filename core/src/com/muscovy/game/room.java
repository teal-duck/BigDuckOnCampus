package com.muscovy.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class Room extends OnscreenDrawable{

    /* variables indicate if there is a door on that wall */
    Boolean upDoor = false;
    Boolean rightDoor = false;
    Boolean downDoor = false;
    Boolean leftDoor = false;
    /* roomType indicates the type of room
     * options: "" (default), "start", "boss", "item", "shop" */
    String roomType = "";

    @Override
    public Sprite getSprite() {
        return super.getSprite();
    }
    @Override
    public void setSprite(Sprite sprite) {
        sprite.setX(32);
        sprite.setY(32);
        super.setSprite(sprite);
    }
    @Override
    public float getX() {
        return super.getX();
    }
    @Override
    public void setX(float x) {
        super.setX(x);
    }
    @Override
    public float getY() {
        return super.getY();
    }
    @Override
    public void setY(float y) {
        super.setY(y);
    }
}
