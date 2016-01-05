package com.muscovy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

/**
 * Created by ewh502 on 04/12/2015.
 */
public class PlayerCharacter extends Collidable{
    private float xVelocity, yVelocity;
    private float maxVelocity = 500, defaultVelocity = 500, accel = maxVelocity*6, decel = maxVelocity*5;
    private ArrayList<Texture> downWalkCycle, leftWalkCycle, rightWalkCycle, upWalkCycle;
    private int animationCycle, animationCounter;
    private int direction = 0; // 0 = up, 1 = right, 2 = down, 3 = left
    private float currentHealth = 100, maxHealth = 100;
    private float upperXBounds = 1280-32, upperYBounds = 720-96, lowerYBounds = 32, lowerXBounds = 32, spriteWidth, spriteHeight;
            // the upper and lower X and Y bounds correlate to the size of the frame used by the gui (32 px border on
            // left right and bottom, and 96 px on top)
    public PlayerCharacter() {
        animationCycle = 0;
        xVelocity = 0;
        yVelocity = 0;
        Sprite playerSprite;
        Texture tempTexture;
        downWalkCycle = new ArrayList<Texture>();
        upWalkCycle = new ArrayList<Texture>();
        rightWalkCycle = new ArrayList<Texture>();
        leftWalkCycle = new ArrayList<Texture>();
        for (int i = 1; i < 8; i++) {
            tempTexture = new Texture(Gdx.files.internal(String.format("core/assets/BasicDuckWalkCycle/downduck%d.png", i)));
            downWalkCycle.add(tempTexture);
        }
        for (int i = 1; i < 8; i++) {
            tempTexture = new Texture(Gdx.files.internal(String.format("core/assets/BasicDuckWalkCycle/upduck%d.png", i)));
            upWalkCycle.add(tempTexture);
        }
        for (int i = 1; i < 12; i++) {
            tempTexture = new Texture(Gdx.files.internal(String.format("core/assets/BasicDuckWalkCycle/leftduck%d.png", i)));
            leftWalkCycle.add(tempTexture);
        }
        for (int i = 1; i < 12; i++) {
            tempTexture = new Texture(Gdx.files.internal(String.format("core/assets/BasicDuckWalkCycle/rightduck%d.png", i)));
            rightWalkCycle.add(tempTexture);
        }
        playerSprite = new Sprite();
        playerSprite.setRegion(downWalkCycle.get(0));
        spriteWidth = playerSprite.getRegionWidth();
        spriteHeight = playerSprite.getRegionHeight();
        upperXBounds = upperXBounds - spriteWidth;
        upperYBounds = upperYBounds - spriteHeight;
        playerSprite.setBounds(upperXBounds / 2 - spriteWidth / 2, 20, spriteWidth, spriteHeight);
        this.setSprite(playerSprite);
        setX(0);
        setY(0);
    }
    public float getXVelocity(){
        return xVelocity;
    }
    public void changeXVelocity(float velocity){
        xVelocity += velocity;
        if (xVelocity > maxVelocity) xVelocity = maxVelocity;
        if (xVelocity < -maxVelocity) xVelocity = -maxVelocity;
    }
    public void setXVelocity(float velocity){
        xVelocity = velocity;
        if (xVelocity > maxVelocity) xVelocity = maxVelocity;
        if (xVelocity < -maxVelocity) xVelocity = -maxVelocity;
    }
    public float getYVelocity(){
        return yVelocity;
    }
    public void changeYVelocity(float velocity){
        yVelocity += velocity;
        if (yVelocity > maxVelocity) yVelocity = maxVelocity;
        if (yVelocity < -maxVelocity) yVelocity = -maxVelocity;
    }
    public void setYVelocity(float velocity){
        yVelocity = velocity;
        if (yVelocity > maxVelocity) yVelocity = maxVelocity;
        if (yVelocity < -maxVelocity) yVelocity = -maxVelocity;
    }
    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }
    public void resetMaxVelocity() {
        this.maxVelocity = defaultVelocity;
    }
    public int getDirection() {
        return direction;
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }
    public int getAnimationCounter() {
        return animationCounter;
    }
    public int getAnimationCycle() {
        return animationCycle;
    }
    /**
    * Animation methods
    */
    public void walkCycleNext(){
        switch (direction){
            case 0:
                this.setTexture(upWalkCycle.get(animationCycle));
                if(animationCycle == 6){
                    animationCycle = 0;
                }else{
                    animationCycle++;
                }
                break;
            case 1:
                this.setTexture(rightWalkCycle.get(animationCycle));
                if(animationCycle == 10){
                    animationCycle = 2;
                }else{
                    animationCycle++;
                }
                break;
            case 2:
                this.setTexture(downWalkCycle.get(animationCycle));
                if(animationCycle == 6){
                    animationCycle = 0;
                }else{
                    animationCycle++;
                }
                break;
            case 3:
                this.setTexture(leftWalkCycle.get(animationCycle));
                if(animationCycle == 10){
                    animationCycle = 2;
                }else{
                    animationCycle++;
                }
                break;
        }
    }
    public void movementAnimation(){
        if (animationCounter == 5){
            animationCounter = 0;
            walkCycleNext();
        }else{
            animationCounter++;
        }
    }
    public void idleAnimation(){
        if ((xVelocity == 0) && (yVelocity == 0)) {
            this.setTexture(downWalkCycle.get(0));
            animationCycle = 0;
        }
    }
    /**
     * Movement methods. Called when the gamestate is 2 and the listener hears W A S or D
     * If opposite directions are pressed, velocity decelerated to 0
     */
    public void Right() {
        if(animationCycle > 10) animationCycle = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            decelXToStop();
        } else {
            changeXVelocity(accel*Gdx.graphics.getDeltaTime());
            checkEdgeCollision();
        }
    }
    public void Left(){
        if(animationCycle > 10) animationCycle = 0;
        changeXVelocity((-accel)*Gdx.graphics.getDeltaTime());
        checkEdgeCollision();
    }
    public void Up() {
        if(animationCycle > 6) animationCycle = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            decelYToStop();
        } else {
            changeYVelocity(accel * Gdx.graphics.getDeltaTime());
            checkEdgeCollision();
        }
    }
    public void Down() {
        if(animationCycle > 6) animationCycle = 0;
        changeYVelocity((-accel) * Gdx.graphics.getDeltaTime());
        checkEdgeCollision();
    }
    public void decelXToStop(){
        if (getXVelocity() > 0){
            if (getXVelocity() - decel*Gdx.graphics.getDeltaTime() < 0){
                setXVelocity(0);
            }else{
                changeXVelocity(-decel*Gdx.graphics.getDeltaTime());
            }
        }
        if (getXVelocity() < 0){
            if (getXVelocity() + decel*Gdx.graphics.getDeltaTime() > 0){
                setXVelocity(0);
            }else{
                changeXVelocity(decel*Gdx.graphics.getDeltaTime());
            }
        }
        checkEdgeCollision();
        idleAnimation();
    }
    public void decelYToStop(){
        if (getYVelocity() > 0){
            if (getYVelocity() - decel*Gdx.graphics.getDeltaTime() < 0){
                setYVelocity(0);
            }else{
                changeYVelocity(-decel*Gdx.graphics.getDeltaTime());
            }
        }
        if (getYVelocity() < 0){
            if (getYVelocity() + decel*Gdx.graphics.getDeltaTime() > 0){
                setYVelocity(0);
            }else{
                changeYVelocity(decel*Gdx.graphics.getDeltaTime());
            }
        }
        checkEdgeCollision();
        idleAnimation();
    }
    public void movement(){
        /**
         * Changes X and Y according to velocity and time elapsed between frames
         */
        setX(getX() + xVelocity * Gdx.graphics.getDeltaTime());
        setY(getY() + yVelocity * Gdx.graphics.getDeltaTime());
    }

    private void checkEdgeCollision(){
        if(getX() < lowerXBounds) {setX(lowerXBounds); setXVelocity(0);}
        if(getX() > upperXBounds) {setX(upperXBounds); setXVelocity(0);}
        if(getY() < lowerYBounds) {setY(lowerYBounds); setYVelocity(0);}
        if(getY() > upperYBounds) {setY(upperYBounds); setYVelocity(0);}
    }

    @Override
    public Sprite getSprite() {
        return super.getSprite();
    }

    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
        super.setUpBoxes(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void setTexture(Texture texture) {
        super.setTexture(texture);
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
