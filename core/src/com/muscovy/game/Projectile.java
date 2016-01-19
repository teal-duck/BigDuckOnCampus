package com.muscovy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by ewh502 on 11/01/2016.
 */
public class Projectile extends OnscreenDrawable{
    private float damage = 10;
    private int damagesWho = 1; //0 = damages player, 1 = damages enemy, 3 = damages both
    private float xVelocity = 0, yVelocity = 0, maxVelocity = 150;
    private float direction = 0;
    private float lifeCounter = 0, life = 1.5f;
    private Circle collisionBox;

    public Projectile(float x, float y, float direction, float life, float maxVelocity, float xVelocity, float yVelocity, int damagesWho) {
        this.setSprite(new Sprite(new Texture("core/assets/breadBullet.png")));
        this.setX(x);
        this.setY(y);
        this.direction = direction;
        this.life = life;
        this.maxVelocity = maxVelocity;
        updateVelocities();
        this.xVelocity += xVelocity;
        this.yVelocity += yVelocity;
        this.damagesWho = damagesWho;
    }

    public void update(){
        movement();
        lifeOver();
    }
    public void movement(){
        setX(getX() + xVelocity * Gdx.graphics.getDeltaTime());
        setY(getY() + yVelocity * Gdx.graphics.getDeltaTime());
        lifeCounter += Gdx.graphics.getDeltaTime();
    }
    public void updateVelocities(){
        this.xVelocity = (float)(maxVelocity*Math.sin(direction));
        this.yVelocity = (float)(maxVelocity*Math.cos(direction));
    }
    public void kill(){
        life = 0;
    }
    public boolean lifeOver(){
        return (lifeCounter > life);
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public int getDamagesWho() {
        return damagesWho;
    }

    public void setDamagesWho(int damagesWho) {
        this.damagesWho = damagesWho;
    }

    public float getLife() {
        return life;
    }

    public void setLife(float life) {
        this.life = life;
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

    public Circle getCollisionBox() {
        return collisionBox;
    }

    public void setCollisionBox(Circle collisionBox) {
        this.collisionBox = collisionBox;
    }

    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
        collisionBox = new Circle((int)this.getX(),(int)this.getY(),this.getSprite().getRegionWidth()/2);
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
