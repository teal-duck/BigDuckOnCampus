package com.muscovy.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by SeldomBucket on 05-Jan-16.
 */
public abstract class Collidable extends OnscreenDrawable{
    private Rectangle[] collisionBoxes;
    private Rectangle boundingBox;
    private float rectangleBorder = 10;
    private float bufferBorder = 0;
    private float heightOffset = 60;

    public void setUpBoxes(){
        float x = this.getX();
        float y = this.getY();
        float width = this.getWidth();
        float height = this.getHeight();
        boundingBox = new Rectangle(x-1,y-1,width+2,height+2-heightOffset);
        collisionBoxes = new Rectangle[4];
        collisionBoxes[0] = new Rectangle(x + bufferBorder + 1,                        y + bufferBorder,                                    width - 2 - bufferBorder*2,     rectangleBorder);//bottom rectangle
        collisionBoxes[1] = new Rectangle(x + bufferBorder,                            y + bufferBorder + 1,                                rectangleBorder*2,               height - heightOffset - 2 - bufferBorder*2); //left rectangle
        collisionBoxes[2] = new Rectangle(x - bufferBorder + width - rectangleBorder,  y + bufferBorder + 1,                                rectangleBorder,                 height - heightOffset - 2 - bufferBorder*2); //right rectangle
        collisionBoxes[3] = new Rectangle(x + bufferBorder + 1,                        y - bufferBorder + height - rectangleBorder - heightOffset,    width - 2,                       rectangleBorder); //top rectangle
    }

    public void updateBoxesPosition(){
        collisionBoxes[0].setX(this.getX());
        collisionBoxes[0].setY(this.getY());
        collisionBoxes[1].setX(this.getX());
        collisionBoxes[1].setY(this.getY());
        collisionBoxes[2].setX(this.getX() + this.getWidth() - rectangleBorder);
        collisionBoxes[2].setY(this.getY());
        collisionBoxes[3].setX(this.getX());
        collisionBoxes[3].setY(this.getY() + this.getHeight() - rectangleBorder-heightOffset);
        boundingBox.setX(this.getX());
        boundingBox.setY(this.getY());
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
    public float getHeightOffset() {
        return heightOffset;
    }
    public void setHeightOffset(float heightOffset) {
        this.heightOffset = heightOffset;
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
    public float getRectangleBorder() {
        return rectangleBorder;
    }
    public boolean collides(Rectangle rectangle){
        return Intersector.overlaps(rectangle, this.getBoundingBox());
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
    public void initialiseX(float x){
        super.setX(x);
    }
    public void initialiseY(float y){
        super.setY(y);
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

