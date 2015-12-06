package com.muscovy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by ewh502 on 04/12/2015.
 */
public class PlayerCharacter extends OnscreenDrawable{
    private float xVelocity, yVelocity;
    private float maxVelocity = 500, accel = maxVelocity*6, decel = maxVelocity*5;
    float upperXBounds = 1280-32, upperYBounds = 720-96, lowerYBounds = 32, lowerXBounds = 32, spriteWidth, spriteHeight;
            // the upper and lower X and Y bounds correlate to the size of the frame used by the gui
    public PlayerCharacter(){
        xVelocity = 0;
        yVelocity = 0;
        setX(0);
        setY(0);
        Sprite tempSprite;
        tempSprite = new Sprite();
        tempSprite.setRegion(new Texture(Gdx.files.internal("core/assets/duckplaceholder.gif")));
        spriteWidth = tempSprite.getRegionWidth();
        spriteHeight = tempSprite.getRegionHeight();
        upperXBounds = upperXBounds-spriteWidth;
        upperYBounds = upperYBounds-spriteHeight;
        tempSprite.setBounds(upperXBounds / 2 - spriteWidth / 2,20,spriteWidth,spriteHeight);
        setSprite(tempSprite);
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

    public void Left() {
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            decelXToStop();
        } else {
            changeXVelocity(accel*Gdx.graphics.getDeltaTime());
            checkEdgeCollision();
        }
    }
    public void Right(){
        changeXVelocity((-accel)*Gdx.graphics.getDeltaTime());
        checkEdgeCollision();
    }
    public void Up() {
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            decelYToStop();
        } else {
            changeYVelocity(accel * Gdx.graphics.getDeltaTime());
            checkEdgeCollision();
        }
    }
    public void Down() {
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
    }
    public void movement(){
        setX(getX() + xVelocity*Gdx.graphics.getDeltaTime());
        setY(getY() + yVelocity*Gdx.graphics.getDeltaTime());
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
