package com.muscovy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by ewh502 on 11/01/2016.
 */
public class Projectile extends OnscreenDrawable{
    private float damage = 15;
    private float xVelocity = 0, yVelocity = 0, maxVelocity = 150;
    private float direction = 0;
    private float lifeTimer = 0, life = 1.5f;
    private Rectangle collisionBox;

    public Projectile(float x, float y, float direction, float life, float xVelocity, float yVelocity, float maxVelocity) {
        this.setSprite(new Sprite(new Texture("core/assets/breadBullet.png")));
        this.setX(x);
        this.setY(y);
        this.direction = direction;
        this.life = life;
        this.maxVelocity = maxVelocity;
        updateVelocities();
        this.xVelocity += xVelocity;
        this.yVelocity += yVelocity;
    }

    public void update(){
        lifeTimer += Gdx.graphics.getDeltaTime();
        movement();
        lifeOver();
    }
    public void movement(){
        setX(getX() + xVelocity * Gdx.graphics.getDeltaTime());
        setY(getY() + yVelocity * Gdx.graphics.getDeltaTime());
    }
    public void updateVelocities(){
        this.xVelocity = (float)(maxVelocity*Math.sin(direction));
        this.yVelocity = (float)(maxVelocity*Math.cos(direction));
    }
    public void kill(){
        life = 0;
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

    public Rectangle getCollisionBox() {
        return collisionBox;
    }

    public void setCollisionBox(Rectangle collisionBox) {
        this.collisionBox = collisionBox;
    }

    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
        collisionBox = new Rectangle((int)this.getX(),(int)this.getY(),this.getSprite().getRegionWidth(), this.getSprite().getRegionHeight());
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        collisionBox.setX(x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        collisionBox.setY(y);
    }
}
