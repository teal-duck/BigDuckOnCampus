package com.muscovy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ewh502 on 04/12/2015.
 * Good luck
 */
public class PlayerCharacter extends Collidable {
    private Random random;

    private float xVelocity, yVelocity;
    private float maxVelocity = 350, defaultVelocity = 350, accel = maxVelocity * 6, decel = maxVelocity * 5.5f;
    private float direction = 0, shotDirection = 0; // 0 - 2*PI
    private ArrayList<Texture> downWalkCycle, leftWalkCycle, rightWalkCycle, upWalkCycle;
    private int animationCycle, animationCounter;

    private int shotType = 0;   //0 = single shot, 1 = double shot, 2 = triple shot
    private float attackInterval = 0.25f, timeSinceLastAttack = attackInterval; //MuscovyGame.java checks these and does an attack if attack timer is greater than attack interval.
    private float projectileVelocity = 450, projectileRange = 600, projectileLife = projectileRange / projectileVelocity;

    private float currentHealth = 100, maxHealth = 100;
    private boolean invincible = false;
    private float invincibilityCounter = 0;

    private int score = 0;

    public PlayerCharacter() {
        random = new Random();
        animationCycle = 0;
        xVelocity = 0;
        yVelocity = 0;
        Sprite playerSprite;
        downWalkCycle = new ArrayList<Texture>();
        upWalkCycle = new ArrayList<Texture>();
        rightWalkCycle = new ArrayList<Texture>();
        leftWalkCycle = new ArrayList<Texture>();
        //Commented out due to animation not actually existing yet
        /*Texture tempTexture;
        for (int i = 1; i < 8; i++) {
            tempTexture = new Texture(Gdx.files.internal(String.format("BasicDuckWalkCycle/downduck%d.png", i)));
            downWalkCycle.add(tempTexture);
        }
        for (int i = 1; i < 8; i++) {
            tempTexture = new Texture(Gdx.files.internal(String.format("BasicDuckWalkCycle/upduck%d.png", i)));
            upWalkCycle.add(tempTexture);
        }
        for (int i = 1; i < 12; i++) {
            tempTexture = new Texture(Gdx.files.internal(String.format("BasicDuckWalkCycle/leftduck%d.png", i)));
            leftWalkCycle.add(tempTexture);
        }
        for (int i = 1; i < 12; i++) {
            tempTexture = new Texture(Gdx.files.internal(String.format("BasicDuckWalkCycle/rightduck%d.png", i)));
            rightWalkCycle.add(tempTexture);
        }*/
        playerSprite = new Sprite();
        playerSprite.setRegion(new Texture(Gdx.files.internal("duck.png")));
        this.setSprite(playerSprite);
        this.initialiseX(0);
        this.initialiseY(0);
        this.setUpBoxes();
        this.setHitboxYOffset(-6);      //Just to get the hitbox in line with that fat fuck of a duck's body
        this.setHitboxRadius(74 / 2 - 2);
    }

    /**
     * Getters and Setters
     */
    public float getHealth() {
        return currentHealth;
    }

    public void setHealth(float currentHealth) {
        if (currentHealth <= maxHealth) {
            this.currentHealth = currentHealth;
        } else {
            this.currentHealth = maxHealth;
        }
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getInvincibilityCounter() {
        return invincibilityCounter;
    }

    public int getAnimationCounter() {
        return animationCounter;
    }

    public int getAnimationCycle() {
        return animationCycle;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getDirection() {
        return direction;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public float getXVelocity() {
        return xVelocity;
    }

    public void setXVelocity(float velocity) {
        //Clamps velocity to max velocity
        xVelocity = velocity;
        if (xVelocity > maxVelocity) xVelocity = maxVelocity;
        if (xVelocity < -maxVelocity) xVelocity = -maxVelocity;
    }

    public float getYVelocity() {
        return yVelocity;
    }

    public void setYVelocity(float velocity) {
        //Clamps velocity to max velocity
        yVelocity = velocity;
        if (yVelocity > maxVelocity) yVelocity = maxVelocity;
        if (yVelocity < -maxVelocity) yVelocity = -maxVelocity;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    public float getShotDirection() {
        return shotDirection;
    }

    public void setShotDirection(float shotDirection) {
        this.shotDirection = shotDirection;
    }

    public float getTimeSinceLastAttack() {
        return timeSinceLastAttack;
    }

    public void setTimeSinceLastAttack(float timeSinceLastAttack) {
        this.timeSinceLastAttack = timeSinceLastAttack;
    }

    /**
     * Projectile life, range and velocity work so that the range, life and shot speed are consistent with each other.
     */
    public float getProjectileRange() {
        return projectileRange;
    }

    public void setProjectileRange(float projectileRange) {
        this.projectileRange = projectileRange;
        this.projectileLife = projectileRange / projectileVelocity;
    }

    public float getProjectileLife() {
        return projectileLife;
    }

    public void setProjectileLife(float projectileLife) {
        this.projectileLife = projectileLife;
        this.projectileRange = projectileVelocity * projectileLife;
    }

    public float getProjectileVelocity() {
        return projectileVelocity;
    }

    public void setProjectileVelocity(float projectileVelocity) {
        this.projectileVelocity = projectileVelocity;
        this.projectileLife = projectileRange / projectileVelocity;
    }

    public void update() {
        movement();
        if (invincible) {
            invincibilityUpdate();
        }
        if (currentHealth > maxHealth) {
            this.currentHealth = maxHealth;
        }
    }

    /**
     * Score Methods
     */
    public void increaseScore(int score) {
        this.score += score;
    }

    /**
     * Health methods
     */
    public void damage(float damage) {
        if (!invincible) {
            this.currentHealth -= damage;
            this.invincible = true;
        }
    }

    public void gainHealth(float health) {
        currentHealth += health;
        if (currentHealth < maxHealth) {
            currentHealth = maxHealth;
        }
    }

    private void invincibilityUpdate() {
        invincibilityCounter += Gdx.graphics.getDeltaTime();
        if (invincibilityCounter > 2) {
            invincible = false;
            invincibilityCounter = 0;
        }
    }

    public boolean isInvincible() {
        return this.invincible;
    }

    /**
     * Animation methods
     * currently commented out, as we only have one sprite atm, but they should be easy to work out. Might be worth
     * revamping later though
     */
    public void walkCycleNext() {
        int switcher = (int) (direction / (Math.PI / 2));
        switch (switcher) {
            case 0:
                this.setTexture(upWalkCycle.get(animationCycle));
                if (animationCycle == 6) {
                    animationCycle = 0;
                } else {
                    animationCycle++;
                }
                break;
            case 1:
                this.setTexture(rightWalkCycle.get(animationCycle));
                if (animationCycle == 10) {
                    animationCycle = 2;
                } else {
                    animationCycle++;
                }
                break;
            case 2:
                this.setTexture(downWalkCycle.get(animationCycle));
                if (animationCycle == 6) {
                    animationCycle = 0;
                } else {
                    animationCycle++;
                }
                break;
            case 3:
                this.setTexture(leftWalkCycle.get(animationCycle));
                if (animationCycle == 10) {
                    animationCycle = 2;
                } else {
                    animationCycle++;
                }
                break;
        }
    }

    public void movementAnimation() {
        /*if (animationCounter == 5){
            animationCounter = 0;
            walkCycleNext();
        }else{
            animationCounter++;
        }*/
    }

    public void idleAnimation() {
        /*if ((xVelocity == 0) && (yVelocity == 0)) {
            this.setTexture(downWalkCycle.get(0));
            animationCycle = 0;
        }*/
    }

    /**
     * Movement methods.
     * Called when the gamestate is 2 and the listener hears W A S or D
     * If opposite directions are pressed at the same time, velocity decelerated to 0
     * Calculates velocity based on delta time and acceleration
     */
    public void Right() {
        if (animationCycle > 10) animationCycle = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            decelXToStop();
        } else {
            changeXVelocity(accel * Gdx.graphics.getDeltaTime());
        }
    }

    public void Left() {
        if (animationCycle > 10) animationCycle = 0;
        changeXVelocity((-accel) * Gdx.graphics.getDeltaTime());
    }

    public void Up() {
        if (animationCycle > 6) animationCycle = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            decelYToStop();
        } else {
            changeYVelocity(accel * Gdx.graphics.getDeltaTime());
        }
    }

    public void Down() {
        if (animationCycle > 6) animationCycle = 0;
        changeYVelocity((-accel) * Gdx.graphics.getDeltaTime());
    }

    public void decelXToStop() {
        if (getXVelocity() > 0) {
            if (getXVelocity() - decel * Gdx.graphics.getDeltaTime() < 0) {
                setXVelocity(0);
            } else {
                changeXVelocity(-decel * Gdx.graphics.getDeltaTime());
            }
        }
        if (getXVelocity() < 0) {
            if (getXVelocity() + decel * Gdx.graphics.getDeltaTime() > 0) {
                setXVelocity(0);
            } else {
                changeXVelocity(decel * Gdx.graphics.getDeltaTime());
            }
        }
        idleAnimation();
    }

    public void decelYToStop() {
        if (getYVelocity() > 0) {
            if (getYVelocity() - decel * Gdx.graphics.getDeltaTime() < 0) {
                setYVelocity(0);
            } else {
                changeYVelocity(-decel * Gdx.graphics.getDeltaTime());
            }
        }
        if (getYVelocity() < 0) {
            if (getYVelocity() + decel * Gdx.graphics.getDeltaTime() > 0) {
                setYVelocity(0);
            } else {
                changeYVelocity(decel * Gdx.graphics.getDeltaTime());
            }
        }
        idleAnimation();
    }

    public void movement() {
        /**
         * Changes X and Y according to velocity and time elapsed between frames
         */
        setX(getX() + xVelocity * Gdx.graphics.getDeltaTime());
        setY(getY() + yVelocity * Gdx.graphics.getDeltaTime());
    }

    public void changeXVelocity(float velocity) {
        xVelocity += velocity;
        if (xVelocity > maxVelocity) xVelocity = maxVelocity;
        if (xVelocity < -maxVelocity) xVelocity = -maxVelocity;
    }

    public void changeYVelocity(float velocity) {
        yVelocity += velocity;
        if (yVelocity > maxVelocity) yVelocity = maxVelocity;
        if (yVelocity < -maxVelocity) yVelocity = -maxVelocity;
    }

    public void resetMaxVelocity() {
        this.maxVelocity = defaultVelocity;
    }

    /**
     * Attack methods
     * (only shots currently)
     */
    public boolean checkRangedAttack() {
        if (timeSinceLastAttack > attackInterval) {
            timeSinceLastAttack = 0;
            return true;
        } else {
            incrementTimeSinceLastAttack();
            return false;
        }
    }

    public void incrementTimeSinceLastAttack() {
        timeSinceLastAttack += Gdx.graphics.getDeltaTime();
    }

    public void resetAttackTimer() {
        timeSinceLastAttack = attackInterval;
    }

    public ArrayList<Projectile> rangedAttack() {
        /**
         * Returns a different projectile array list depending on the shot type, so that needs to be given directly
         * to the entity manager
         */
        ArrayList<Projectile> rangedAttack = new ArrayList<Projectile>();
        float x = (this.getX() + this.getWidth() / 2 - 8);
        float y = (this.getY() + this.getHeight() - 32);
        switch (shotType) {
            case 0:
                rangedAttack.add(new Projectile(x, y, this.shotDirection, this.projectileLife, this.projectileVelocity, this.xVelocity / 4, this.yVelocity / 4, 1));
                break;
            case 1:
                rangedAttack.add(new Projectile(x, y, (float) (shotDirection - (Math.PI / 24)), projectileLife, this.projectileVelocity, this.xVelocity, this.yVelocity, 1));
                rangedAttack.add(new Projectile(x, y, (float) (shotDirection + (Math.PI / 24)), projectileLife, this.projectileVelocity, this.xVelocity, this.yVelocity, 1));
                break;
            case 2:
                rangedAttack.add(new Projectile(x, y, (float) (shotDirection - (Math.PI / 12)), projectileLife, this.projectileVelocity, this.xVelocity, this.yVelocity, 1));
                rangedAttack.add(new Projectile(x, y, (float) (shotDirection + (Math.PI / 12)), projectileLife, this.projectileVelocity, this.xVelocity, this.yVelocity, 1));
                rangedAttack.add(new Projectile(x, y, shotDirection, projectileLife, this.projectileVelocity, this.xVelocity, this.yVelocity, 1));
                break;
        }
        return rangedAttack;
    }

}
