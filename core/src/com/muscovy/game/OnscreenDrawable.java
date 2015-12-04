package com.muscovy.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by ewh502 on 04/12/2015.
 */
public abstract class OnscreenDrawable {
    private Sprite sprite;
    private int x, y, layer;

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

}
