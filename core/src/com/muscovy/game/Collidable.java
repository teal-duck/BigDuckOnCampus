package com.muscovy.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by SeldomBucket on 05-Jan-16.
 */
public abstract class Collidable extends OnscreenDrawable{
    private Rectangle[] collisionBoxes;
    private float rectangleBorder = 10;
    private float bufferBorder = 0;

    public void setUpBoxes(float x, float y, float width, float height){
        collisionBoxes = new Rectangle[4];
        collisionBoxes[0] = new Rectangle(x + bufferBorder + 1,                        y + bufferBorder,                                    width - 2 - bufferBorder*2,     rectangleBorder);//bottom rectangle
        collisionBoxes[1] = new Rectangle(x + bufferBorder,                            y + bufferBorder + 1,                                rectangleBorder*2,               height - 32 - 2 - bufferBorder*2); //left rectangle
        collisionBoxes[2] = new Rectangle(x - bufferBorder + width - rectangleBorder,  y + bufferBorder + 1,                                rectangleBorder,                 height - 32 - 2 - bufferBorder*2); //right rectangle
        collisionBoxes[3] = new Rectangle(x + bufferBorder + 1,                        y - bufferBorder + height - rectangleBorder - 32,    width - 2,                       rectangleBorder); //top rectangle
    }

    public void updateBoxesPosition(){
        collisionBoxes[0].setX(this.getX());
        collisionBoxes[0].setY(this.getY());
        collisionBoxes[1].setX(this.getX());
        collisionBoxes[1].setY(this.getY());
        collisionBoxes[2].setX(this.getX() + this.getWidth() - rectangleBorder);
        collisionBoxes[2].setY(this.getY());
        collisionBoxes[3].setX(this.getX());
        collisionBoxes[3].setY(this.getY() + this.getHeight() - rectangleBorder-32);
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
    public float getBufferBorder() {
        return bufferBorder;
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

