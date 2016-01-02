package com.muscovy.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by ewh502 on 04/12/2015.
 */
public abstract class OnscreenDrawable {
    private Sprite sprite;
    private float x, y;

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


}
