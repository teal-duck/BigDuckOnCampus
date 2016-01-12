package com.muscovy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class Enemy extends Collidable {
    private float touchDamage;
    private Projectile bullet;
    private ArrayList<Projectile> rangedAttack;
    private int shotType = 0;   //0 = single shot, 1 = double shot, 2 = triple shot, 4 = random shot direction
    private float attackTimer, attackInterval = 2; //MuscovyGame.java checks these and does an attack if attack timer is greater than attack interval.
    private Random random;
    private float xVelocity = 0, yVelocity = 0, maxVelocity = 150, currentMaxVelocity = 150;
    public float direction;    //-pi/2 to pi/2
    private float detectionDistance = 480;
    float directionCounter = 0;
    private int movementType;   //0 = static, 1 = following, 2 = random movement
    private int attackType;     //0 = touch damage, 1 = ranged attack, 2 = both
    private float upperXBounds = 1280-32, upperYBounds = 720-128, lowerYBounds = 32, lowerXBounds = 32, spriteWidth, spriteHeight;
    private Sprite bulletSprite;
    public Enemy(Sprite sprite) {
        random = new Random();
        spriteWidth = sprite.getRegionWidth();
        spriteHeight = sprite.getRegionHeight();
        bullet = new Projectile();
        rangedAttack = new ArrayList<Projectile>();
        bulletSprite = new Sprite();
        bulletSprite.setTexture(new Texture("core/assets/breadBullet.png"));
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
        attackTimer += Gdx.graphics.getDeltaTime();
    }

    /**
     * Attack & damage methods
     */
    public float getTouchDamage() {
        return touchDamage;
    }
    public void setTouchDamage(float touchDamage) {
        this.touchDamage = touchDamage;
    }
    public Projectile getBullet() {
        return bullet;
    }
    public void setBullet(Projectile bullet) {
        this.bullet = bullet;
    }
    public int getAttackType() {
        return attackType;
    }
    public void setAttackType(int attackType) {
        this.attackType = attackType;
    }
    public ArrayList<Projectile> rangedAttack(){
        switch (shotType){
            case 0:
                bullet = new Projectile();
                bullet.setSprite(bulletSprite);
                bullet.setX(this.getX());
                bullet.setY(this.getY());
                bullet.setDirection(this.direction);
                rangedAttack.add(bullet);
                break;
            case 1:
                bullet = new Projectile();
                bullet.setSprite(bulletSprite);
                bullet.setX(0);
                bullet.setY(0);
                bullet.setDirection((float)(this.direction-(Math.PI/6)));
                rangedAttack.add(bullet);
                bullet = new Projectile();
                bullet.setSprite(bulletSprite);
                bullet.setX(0);
                bullet.setY(0);
                bullet.setDirection((float)(this.direction+(Math.PI/6)));
                rangedAttack.add(bullet);
                break;
            case 2:
                bullet = new Projectile();
                bullet.setSprite(bulletSprite);
                bullet.setX(0);
                bullet.setY(0);
                bullet.setDirection((float) (this.direction - (Math.PI / 4)));
                rangedAttack.add(bullet);
                bullet = new Projectile();
                bullet.setSprite(bulletSprite);
                bullet.setX(0);
                bullet.setY(0);
                bullet.setDirection((float) (this.direction + (Math.PI / 4)));
                rangedAttack.add(bullet);
                bullet = new Projectile();
                bullet.setSprite(bulletSprite);
                bullet.setX(0);
                bullet.setY(0);
                bullet.setDirection(this.direction);
                rangedAttack.add(bullet);
                break;
            case 3:
                bullet = new Projectile();
                bullet.setSprite(bulletSprite);
                bullet.setX(0);
                bullet.setY(0);
                bullet.setDirection((float)(random.nextFloat()*Math.PI*2));
                rangedAttack.add(bullet);
                break;
        }
        return rangedAttack;
    }
    public boolean checkAttack(){
        if (attackTimer > attackInterval){
            attackTimer = 0;
            return true;
        }else {
            return false;
        }
    }
    /**
     * Movement methods
     */
    public float getXVelocity() {
        return xVelocity;
    }
    public void setXVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }
    public float getYVelocity() {
        return yVelocity;
    }
    public void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }
    public float getMaxVelocity() {
        return maxVelocity;
    }
    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }
    public void setMovementType(int movementType) {
        this.movementType = movementType;
    }
    public int getMovementType() {
        return movementType;
    }
    public void setCurrentMaxVelocity(float currentMaxVelocity) {
        this.currentMaxVelocity = currentMaxVelocity;
    }
    public void resetMaxVelocity(){
        this.currentMaxVelocity = maxVelocity;
    }
    public void movement(PlayerCharacter player){
        switch (movementType){
            case 0:
                setXVelocity(0);
                setYVelocity(0);
                break;
            case 1:
                if (getDistanceTo(player) < detectionDistance){
                    pointTo(player);
                    updateVelocities();
                }else{
                    setXVelocity(0);
                    setYVelocity(0);
                }
                break;
            case 2:
                if (directionCounter > 0.3){
                    directionCounter = 0;
                    if(random.nextBoolean()){
                        direction = (float)((direction + random.nextFloat()) % Math.PI*2);
                    }else{
                        direction = (float)((direction - random.nextFloat()) % Math.PI*2);
                    }
                }else{directionCounter += Gdx.graphics.getDeltaTime();}
                updateVelocities();
                break;
        }
        setX(getX() + xVelocity * Gdx.graphics.getDeltaTime());
        setY(getY() + yVelocity * Gdx.graphics.getDeltaTime());
        checkEdgeCollision();
    }
    private void checkEdgeCollision(){
        if(getX() < lowerXBounds) {setX(lowerXBounds); setXVelocity(0);}
        if(getX() > upperXBounds) {setX(upperXBounds); setXVelocity(0);}
        if(getY() < lowerYBounds) {setY(lowerYBounds); setYVelocity(0);}
        if(getY() > upperYBounds) {setY(upperYBounds); setYVelocity(0);}
    }
    public float getAngleTo(Collidable collidable){
        return (float) Math.atan((collidable.getX()-this.getX())/(collidable.getY()-this.getY()));
    }
    public void pointTo(Collidable collidable){
        float x = (collidable.getX()-this.getX());
        float y = (collidable.getY()-this.getY());
        float angle = getAngleTo(collidable);
        if(x >= 0){
            if (y >= 0){
                direction = angle;
            }else if (y < 0){
                direction = (float)(Math.PI + angle);
            }
        }else if (x < 0){
            if (y >= 0){
                direction = (float)(2*Math.PI + angle);
            }else if (y < 0){
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
