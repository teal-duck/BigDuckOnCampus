package com.muscovy.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class Enemy extends Collidable {
    private Float touchDamage;
    private Projectile bullet;
    private int movementType;   //0 = static, 1 = following, 2 = random movement
    private int attackType;     //0 = touch damage, 1 = ranged attack, 2 = both
    public Enemy(Sprite sprite) {
        this.setSprite(sprite);
        this.setHeightOffset(this.getHeight()/3);
        this.touchDamage = 10.0f;
        this.movementType = 0;
        this.attackType = 0;
        this.initialiseX(0);
        this.initialiseY(0);
        this.setUpBoxes();
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

   /** public float getAngleTo(Collidable collidable){
        return Math.atan( (float)(collidable.getX()-this.getX())/(collidable.getY()-this.getY()) );
    }*/

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
