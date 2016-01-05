package com.muscovy.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class Obstacle extends Collidable {

    public Obstacle(Sprite sprite) {
        this.setSprite(sprite);
    }

    @Override
    public Sprite getSprite() {
        return super.getSprite();
    }

    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
        super.setUpBoxes(this.getX(),this.getY(),this.getWidth(),this.getHeight());
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
