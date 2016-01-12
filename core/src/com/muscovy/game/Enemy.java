package com.muscovy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class Enemy extends Collidable {
    private Float touchDamage;
    private Projectile bullet;
    private float xVelocity = 0, yVelocity = 0, maxVelocity = 300;
    public float direction;    //-pi/2 to pi/2
    private float detectionDistance = 480;
    private int movementType;   //0 = static, 1 = following, 2 = random movement
    private int attackType;     //0 = touch damage, 1 = ranged attack, 2 = both
    private float upperXBounds = 1280-32, upperYBounds = 720-128, lowerYBounds = 32, lowerXBounds = 32, spriteWidth, spriteHeight;
    public Enemy(Sprite sprite) {
        spriteWidth = sprite.getRegionWidth();
        spriteHeight = sprite.getRegionHeight();
        this.setSprite(sprite);
        this.setHeightOffset(this.getHeight()/3);
        this.touchDamage = 10.0f;
        this.movementType = 0;
        this.attackType = 0;
        upperXBounds = upperXBounds - spriteWidth;
        upperYBounds = upperYBounds - spriteHeight;
        this.initialiseX(0);
        this.initialiseY(0);
        this.setUpBoxes();
    }
    public void update(PlayerCharacter player){
        movement(player);
    }
    public void movement(PlayerCharacter player){
        switch (movementType){
            case 0:
                break;
            case 1:
                if (getDistanceTo(player) < detectionDistance){
                    pointTo(player);
                    //updateVelocities();
                }else{
                    setxVelocity(0);
                    setyVelocity(0);
                }
                break;
            case 2:
                break;
        }
        setX(getX() + xVelocity * Gdx.graphics.getDeltaTime());
        setY(getY() + yVelocity * Gdx.graphics.getDeltaTime());
    }
    private void checkEdgeCollision(){
        if(getX() < lowerXBounds) {setX(lowerXBounds); setxVelocity(0);}
        if(getX() > upperXBounds) {setX(upperXBounds); setxVelocity(0);}
        if(getY() < lowerYBounds) {setY(lowerYBounds); setyVelocity(0);}
        if(getY() > upperYBounds) {setY(upperYBounds); setyVelocity(0);}
    }
    public Float getTouchDamage() {
        return touchDamage;
    }

    public void setTouchDamage(Float touchDamage) {
        this.touchDamage = touchDamage;
    }

    public Projectile getBullet() {
        return bullet;
    }

    public void setBullet(Projectile bullet) {
        this.bullet = bullet;
    }

    public int getMovementType() {
        return movementType;
    }

    public void setMovementType(int movementType) {
        this.movementType = movementType;
    }

    public int getAttackType() {
        return attackType;
    }

    public void setAttackType(int attackType) {
        this.attackType = attackType;
    }

    public float getAngleTo(Collidable collidable){
        return (float) Math.atan((collidable.getX()-this.getX())/(collidable.getY()-this.getY()));
    }
    public void pointTo(Collidable collidable){
        float x = getDistanceTo(collidable);
        float y = getDistanceTo(collidable);
        float angle = getAngleTo(collidable);
        if(x > 0){
            if (y > 0){
                direction = angle;
            }else{
                direction = (float)(Math.PI + angle);
            }
        }else{
            if (y > 0){
                direction = (float)(2*Math.PI + angle);
            }else{
                direction = (float)(Math.PI + angle);
            }
        }
    }

    public float getDistanceTo(Collidable collidable){
        float xdist = (collidable.getX()-this.getX());
        float ydist = (collidable.getY()-this.getY());
        return (float) Math.sqrt((xdist*xdist) + (ydist*ydist));
    }
    public void updateVelocities(){
        this.xVelocity = (float)(maxVelocity*Math.sin(direction));
        this.yVelocity = (float)(maxVelocity*Math.cos(direction));
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

}
