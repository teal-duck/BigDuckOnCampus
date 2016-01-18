package com.muscovy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class Enemy extends Collidable {
    private Random random;

    private boolean collidingWithSomething;

    private int attackType;     //0 = touch damage, 1 = ranged attack, 2 = both
    private float touchDamage;
    private float currentHealth = 90;
    private boolean dead = false;

    private ArrayList<Projectile> rangedAttack;
    private int shotType = 3;   //0 = single shot in direction of movement, 1 = shoot towards player if in range, 2 = double shot towards player if in range, 3 = triple shot towards player if in range, 4 = random shot direction
    private float attackTimer, attackInterval = 0.5f; //MuscovyGame.java checks these and does an attack if attack timer is greater than attack interval.
    private float projectileRange = 400, projectileVelocity = 150, projectileLife = projectileRange/projectileVelocity;
    private float attackRange = 480;

    private int movementType;   //0 = static, 1 = following, 2 = random movement
    private float movementRange = 480;
    private float xVelocity = 0, yVelocity = 0, defaultVelocity = 200, maxVelocity = 200;
    private float direction;    //0 to 2*pi, 0 being vertically upwards
    float directionCounter = 0;

    private float upperXBounds = 1280-32, upperYBounds = 720-128, lowerYBounds = 32, lowerXBounds = 32, spriteWidth, spriteHeight;

    public Enemy(Sprite sprite) {
        random = new Random();
        spriteWidth = sprite.getRegionWidth();
        spriteHeight = sprite.getRegionHeight();
        rangedAttack = new ArrayList<Projectile>();
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

    public boolean isCollidingWithSomething() {
        return collidingWithSomething;
    }

    public void setCollidingWithSomething(boolean collidingWithSomething) {
        this.collidingWithSomething = collidingWithSomething;
    }

    public void update(PlayerCharacter player){
        rangedAttack.clear();
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
    public int getAttackType() {
        return attackType;
    }
    public void setAttackType(int attackType) {
        this.attackType = attackType;
    }
    public ArrayList<Projectile> rangedAttack(PlayerCharacter playerCharacter){
        float x = (this.getX()+this.getWidth()/2);
        float y = (this.getY()+this.getHeight()/2);
        float dist = getDistanceTo(playerCharacter);
        switch (shotType){
            case 0:
                rangedAttack.add(new Projectile(x, y,this.direction, projectileRange, this.projectileVelocity, this.xVelocity, this.yVelocity,0));
                break;
            case 1:
                if (dist<attackRange) {
                    rangedAttack.add(new Projectile(x, y, this.getAngleTo(playerCharacter), projectileRange, this.projectileVelocity, this.xVelocity, this.yVelocity,0));
                }
                break;
            case 2:
                if (dist<attackRange) {
                    rangedAttack.add(new Projectile(x, y, (float) (this.getAngleTo(playerCharacter) - (Math.PI / 24)), projectileRange, this.projectileVelocity, this.xVelocity, this.yVelocity,0));
                    rangedAttack.add(new Projectile(x, y, (float) (this.getAngleTo(playerCharacter) + (Math.PI / 24)), projectileRange, this.projectileVelocity, this.xVelocity, this.yVelocity,0));
                }
                break;
            case 3:
                if (dist<attackRange) {
                    rangedAttack.add(new Projectile(x, y, (float) (this.getAngleTo(playerCharacter) - (Math.PI / 12)), projectileRange, this.projectileVelocity, this.xVelocity, this.yVelocity,0));
                    rangedAttack.add(new Projectile(x, y, (float) (this.getAngleTo(playerCharacter) + (Math.PI / 12)), projectileRange, this.projectileVelocity, this.xVelocity, this.yVelocity,0));
                    rangedAttack.add(new Projectile(x, y, this.getAngleTo(playerCharacter), projectileRange, this.projectileVelocity, this.xVelocity, this.yVelocity,0));
                }
                break;
            case 4:
                rangedAttack.add(new Projectile(x, y,(float)(random.nextFloat()*Math.PI*2), projectileRange, this.projectileVelocity, this.xVelocity, this.yVelocity,0));
                break;
        }
        return rangedAttack;
    }
    public boolean checkRangedAttack(){
        if (attackTimer > attackInterval){
            attackTimer = 0;
            return true;
        }else {
            return false;
        }
    }
    public void setShotType(int shotType) {
        this.shotType = shotType;
    }
    public void damage(float damage){
        currentHealth-=damage;
        if (currentHealth<=0){this.kill();}
    }
    public void kill(){
        dead = true;
    }
    public boolean lifeOver(){
        return dead;
    }
    public float getAttackRange() {
        return attackRange;
    }
    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }
    public float getProjectileRange() {
        return projectileRange;
    }
    public void setProjectileRange(float projectileRange) {
        this.projectileRange = projectileRange;
        this.projectileLife = projectileRange/projectileVelocity;
    }
    public float getProjectileLife() {
        return projectileLife;
    }
    public void setProjectileLife(float projectileLife) {
        this.projectileLife = projectileLife;
        this.projectileRange = projectileVelocity*projectileLife;
    }
    public float getProjectileVelocity() {
        return projectileVelocity;
    }
    public void setProjectileVelocity(float projectileVelocity) {
        this.projectileVelocity = projectileVelocity;
        this.projectileLife = projectileRange/projectileVelocity;
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
    public float getDefaultVelocity() {
        return defaultVelocity;
    }
    public void setDefaultVelocity(float defaultVelocity) {
        this.defaultVelocity = defaultVelocity;
    }
    public void setMovementType(int movementType) {
        this.movementType = movementType;
    }
    public int getMovementType() {
        return movementType;
    }
    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }
    public void resetMaxVelocity(){
        this.maxVelocity = defaultVelocity;
    }
    public void movement(PlayerCharacter player){
        switch (movementType){
            case 0:
                setXVelocity(0);
                setYVelocity(0);
                break;
            case 1:
                if (getDistanceTo(player) < movementRange){
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
    }
/*
    public float getAngleTo(Collidable collidable){
        float x = ((collidable.getX()+collidable.getCircleHitbox().radius/2)-(this.getX()+this.getWidth()/2));
        float y = ((collidable.getY()+collidable.getCircleHitbox().radius/2)-(this.getY()+this.getHeight()/2));
        float angle = (float) Math.atan(x/y);
        if(x >= 0){
            if (y >= 0){
                return angle;
            }else if (y < 0){
                return (float)(Math.PI + angle);
            }
        }else if (x < 0){
            if (y >= 0){
                return (float)(2*Math.PI + angle);
            }else if (y < 0){
                return (float)(Math.PI + angle);
            }
        }
        return angle;
    }*/
    public void pointTo(Collidable collidable){
        float x = ((collidable.getX()+collidable.getCircleHitbox().radius/2)-(this.getX()+this.getWidth()/2));
        float y = ((collidable.getY()+collidable.getCircleHitbox().radius/2)-(this.getY()+this.getHeight()/2));
        float angle = (float) Math.atan(x/y);
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
        this.xVelocity = (float)(defaultVelocity *Math.sin(direction));
        this.yVelocity = (float)(defaultVelocity *Math.cos(direction));
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
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
