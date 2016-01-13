package com.muscovy.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by ewh502 on 04/12/2015.
 */
public abstract class OnscreenDrawable {
    private Sprite sprite;
    private float x, y;

    public float getHeight(){
        return this.sprite.getTexture().getHeight();
    }
    public float getWidth(){
        return this.sprite.getTexture().getWidth();
    }
    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setTexture(Texture texture){
        this.sprite.setTexture(texture);
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
