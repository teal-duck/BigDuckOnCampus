package com.muscovy.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by SeldomBucket on 05-Jan-16.
 */
public abstract class Collidable extends OnscreenDrawable{
    private Rectangle[] collisionBoxes;

    public void setUpBoxes(){
        collisionBoxes = new Rectangle[4];
        collisionBoxes[0] = new Rectangle(this.getX(), this.getY(), this.getSprite().getHeight(),1);//bottom rectangle
        collisionBoxes[1] = new Rectangle(this.getX(), this.getY(), 1, this.getSprite().getHeight() - 30); //left rectangle
        collisionBoxes[2] = new Rectangle(this.getX()+ this.getSprite().getWidth() - 1, this.getY(), 1, this.getSprite().getHeight() - 30); //right rectangle
        collisionBoxes[3] = new Rectangle(this.getX(), this.getY() + this.getSprite().getHeight() - 31 , this.getSprite().getWidth(), 1); //top rectangle
    }
    public void updateBoxesPosition(){
        collisionBoxes[0].setX(this.getX());
        collisionBoxes[0].setY(this.getY());
        collisionBoxes[1].setX(this.getX());
        collisionBoxes[1].setY(this.getY());
        collisionBoxes[2].setX(this.getX()+ this.getSprite().getWidth() - 1);
        collisionBoxes[2].setY(this.getY());
        collisionBoxes[3].setX(this.getX());
        collisionBoxes[3].setY(this.getY() + this.getSprite().getHeight() - 31);
    }
    public Rectangle getBottomRectangle(){
        return collisionBoxes[0];
    }
    public Rectangle getLeftRectangle(){
        return collisionBoxes[1];
    }
    public Rectangle getRightRectangle(){
        return collisionBoxes[2];
    }
    public Rectangle getTopRectangle(){
        return collisionBoxes[3];
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
        updateBoxesPosition();
    }

    @Override
    public float getY() {
        return super.getY();
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        updateBoxesPosition();
    }
}

