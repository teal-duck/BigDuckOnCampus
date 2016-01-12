package com.muscovy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.awt.*;

/**
 * Created by ewh502 on 11/01/2016.
 */
public class Projectile extends OnscreenDrawable{
    private float damage = 15;
    private float xVelocity = 0, yVelocity = 0, maxVelocity = 600;
    private float direction = 0;
    private float lifeTimer = 0, life = 3;
    private Rectangle collisionBox;

    public void update(){
        lifeTimer += Gdx.graphics.getDeltaTime();
        movement();
        lifeOver();
    }
    public void movement(){
        updateVelocities();
        setX(getX() + xVelocity * Gdx.graphics.getDeltaTime());
        setY(getY() + yVelocity * Gdx.graphics.getDeltaTime());
    }
    public void updateVelocities(){
        this.xVelocity = (float)(maxVelocity*Math.sin(direction));
        this.yVelocity = (float)(maxVelocity*Math.cos(direction));
    }
    public boolean lifeOver(){
        return (lifeTimer > life);
    }
    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public float getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
        collisionBox = new Rectangle((int)this.getX(),(int)this.getY(),this.getSprite().getRegionWidth(), this.getSprite().getRegionHeight());
    }
}
