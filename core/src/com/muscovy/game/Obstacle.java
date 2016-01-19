package com.muscovy.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by SeldomBucket on 05-Dec-15.
 * I should hope this is all fairly self-explanatory
 */
public class Obstacle extends Collidable {
    private boolean damaging;
    private float touchDamage;

    public Obstacle(Sprite sprite) {
        this.setSprite(sprite);
        this.initialiseX(0);
        this.initialiseY(0);
        this.setUpBoxes();
    }

    public boolean isDamaging() {
        return damaging;
    }
    public void setDamaging(boolean damaging) {
        this.damaging = damaging;
    }
    public float getTouchDamage() {
        return touchDamage;
    }
    public void setTouchDamage(float touchDamage) {
        this.touchDamage = touchDamage;
    }

    @Override
    public Sprite getSprite() {
        return super.getSprite();
    }

    @Override
    public void setSprite(Sprite sprite) {
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

    @Override
    public boolean collides(Collidable collidable) {
        return Intersector.overlaps(collidable.getCircleHitbox(), this.getRectangleHitbox());
    }
}
