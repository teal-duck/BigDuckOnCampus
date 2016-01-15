package com.muscovy.game;


import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Map;

/**
 * Created by SeldomBucket on 05-Jan-16.
 */
public abstract class Collidable extends OnscreenDrawable{
    private Rectangle[] collisionBoxes;
    private Circle circleHitbox;
    private Rectangle rectangleHitbox;
    private float rectangleBorderSize = 10;
    private float bufferBorder = 0;
    private float heightOffset = 64;
    private int heightOffsetTiles = 2;
    private float hitboxYOffset = 0;
    private int XTiles, YTiles;
    private int widthTiles, heightTiles;

    public void setUpBoxes(){
        float x = this.getX();
        float y = this.getY();
        float width = this.getWidth();
        float height = this.getHeight();
        this.widthTiles = (int)Math.floorDiv((long)width,(long)32)+1;
        this.heightTiles = (int)Math.floorDiv((long)height,(long)32)+1;
        this.heightOffset = this.getHeight()/3;
        this.heightOffsetTiles = (int)Math.floorDiv((long)heightOffset,(long)32);
        circleHitbox = new Circle(x + (width/2),y+(height/2)+ hitboxYOffset,width/2);
        rectangleHitbox = new Rectangle(x,y,width,height);
        collisionBoxes = new Rectangle[4];
        collisionBoxes[0] = new Rectangle(x + bufferBorder + 1,                        y + bufferBorder,                                    width - 2 - bufferBorder*2, rectangleBorderSize);//bottom rectangle
        collisionBoxes[1] = new Rectangle(x + bufferBorder,                            y + bufferBorder + 1,                                rectangleBorderSize *2,               height - heightOffset - 2 - bufferBorder*2); //left rectangle
        collisionBoxes[2] = new Rectangle(x - bufferBorder + width - rectangleBorderSize,  y + bufferBorder + 1, rectangleBorderSize,                 height - heightOffset - 2 - bufferBorder*2); //right rectangle
        collisionBoxes[3] = new Rectangle(x + bufferBorder + 1,                        y - bufferBorder + height - rectangleBorderSize - heightOffset,    width - 2, rectangleBorderSize); //top rectangle
    }

    public void moveToNearestEdgeCircle(Collidable collidable){
        float angle = getAngleFrom(collidable);
        float distance = collidable.getCircleHitbox().radius + circleHitbox.radius;
        if(this.getCircleHitbox().x >= collidable.getCircleHitbox().x){
            if(this.getCircleHitbox().y >= collidable.getCircleHitbox().y){

            }else{
                angle = (float)Math.PI-angle;
            }
        }else if(this.getCircleHitbox().x < collidable.getCircleHitbox().x) {
            if (this.getCircleHitbox().y >= collidable.getCircleHitbox().y) {
                angle = (float)(angle - Math.PI);
            } else {
                angle = (float)(2*Math.PI - angle);
            }
        }
        if(this.getCircleHitbox().x >= collidable.getCircleHitbox().x){
            if(this.getCircleHitbox().y >= collidable.getCircleHitbox().y){
                float x = collidable.getCircleHitbox().x+(float)(distance*Math.sin(angle));
                float y = collidable.getCircleHitbox().y+(float)(distance*Math.cos(angle));
                setHitboxCentre(x,y);
            }else{
                float x = collidable.getCircleHitbox().x+(float)(distance*Math.sin(angle));
                float y = collidable.getCircleHitbox().y-(float)(distance*Math.cos(angle));
                setHitboxCentre(x,y);
            }
        }else if(this.getCircleHitbox().x < collidable.getCircleHitbox().x) {
            if (this.getCircleHitbox().y >= collidable.getCircleHitbox().y) {
                float x = collidable.getCircleHitbox().x-(float)(distance*Math.sin(angle));
                float y = collidable.getCircleHitbox().y-(float)(distance*Math.cos(angle));
                setHitboxCentre(x,y);
            } else {
                float x = collidable.getCircleHitbox().x-(float)(distance*Math.sin(angle));
                float y = collidable.getCircleHitbox().y+(float)(distance*Math.cos(angle));
                setHitboxCentre(x,y);
            }
        }
    }
    public void moveToNearestEdgeRectangle(Collidable collidable){
        float angle = getAngleFrom(collidable);
        float xc = getCircleHitbox().x;
        float yc = getCircleHitbox().y;
        float rectDistance = 0;
        float distanceToEdge = 0;
        if ((angle >= ((Math.PI*2) - (Math.PI/4)) || angle < Math.PI/4)||(angle >= Math.PI*3/4 && angle < Math.PI*5/4)){
            rectDistance = collidable.getRectangleHitbox().getHeight()/2;
        }else if ((angle>=Math.PI/4 && angle < Math.PI*3/4)||(angle>=Math.PI*5/4 && angle < ((Math.PI*2) - (Math.PI/4)))){
            rectDistance = collidable.getRectangleHitbox().getWidth()/2;
        }

        if(xc >= 0){
            if (yc >= 0){
                if (angle<Math.PI/4){
                    distanceToEdge = rectDistance/(float)Math.acos(angle);
                }else{
                    distanceToEdge = rectDistance/(float) Math.acos((Math.PI/2)-angle);
                }
            }else if (yc < 0){
                if (angle<Math.PI*3/4){
                    distanceToEdge = rectDistance/(float) Math.acos(angle-(Math.PI/2));
                }else{
                    distanceToEdge = rectDistance/(float) Math.acos((Math.PI)-angle);
                }
            }
        }else if (xc < 0){
            if (yc >= 0){
                if (angle<Math.PI*5/4){
                    distanceToEdge = rectDistance/(float) Math.acos(angle-(Math.PI/2));
                }else{
                    distanceToEdge = rectDistance/(float) Math.acos((Math.PI*3/2)-angle);
                }
            }else if (yc < 0){
                if (angle<Math.PI*7/4){
                    distanceToEdge = rectDistance/(float) Math.acos(angle-(Math.PI*3/2));
                }else{
                    distanceToEdge = rectDistance/(float) Math.acos((Math.PI*2)-angle);
                }
            }
        }

        float distance = distanceToEdge + circleHitbox.radius;
        if(this.getCircleHitbox().x >= collidable.getCircleHitbox().x){
            if(this.getCircleHitbox().y >= collidable.getCircleHitbox().y){
                float x = collidable.getCircleHitbox().x+(float)(distance*Math.sin(angle));
                float y = collidable.getCircleHitbox().y+(float)(distance*Math.cos(angle));
                setHitboxCentre(x,y);
            }else{
                angle = (float)Math.PI-angle;
                float x = collidable.getCircleHitbox().x+(float)(distance*Math.sin(angle));
                float y = collidable.getCircleHitbox().y-(float)(distance*Math.cos(angle));
                setHitboxCentre(x,y);
            }
        }else if(this.getCircleHitbox().x < collidable.getCircleHitbox().x) {
            if (this.getCircleHitbox().y >= collidable.getCircleHitbox().y) {
                angle = (float)(angle - Math.PI);
                float x = collidable.getCircleHitbox().x-(float)(distance*Math.sin(angle));
                float y = collidable.getCircleHitbox().y-(float)(distance*Math.cos(angle));
                setHitboxCentre(x,y);
            } else {
                angle = (float)(2*Math.PI - angle);
                float x = collidable.getCircleHitbox().x-(float)(distance*Math.sin(angle));
                float y = collidable.getCircleHitbox().y+(float)(distance*Math.cos(angle));
                setHitboxCentre(x,y);
            }
        }
    }
    public void setHitboxCentre(float x, float y){
        setX((x - circleHitbox.radius));
        setY((y - circleHitbox.radius)-((getHeight()/2)- circleHitbox.radius));
    }
    public float getAngleFrom(Collidable collidable){
        float x = (this.getCircleHitbox().x-collidable.getCircleHitbox().x);
        float y = (this.getCircleHitbox().y-collidable.getCircleHitbox().y);
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
    }
    public float getAngleTo(Collidable collidable){
        float x = (collidable.getCircleHitbox().x-this.circleHitbox.x);
        float y = (collidable.getCircleHitbox().y-this.circleHitbox.y);
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
    }
    public void updateBoxesPosition(){
        collisionBoxes[0].setX(this.getX());
        collisionBoxes[0].setY(this.getY());
        collisionBoxes[1].setX(this.getX());
        collisionBoxes[1].setY(this.getY());
        collisionBoxes[2].setX(this.getX() + this.getWidth() - rectangleBorderSize);
        collisionBoxes[2].setY(this.getY());
        collisionBoxes[3].setX(this.getX());
        collisionBoxes[3].setY(this.getY() + this.getHeight() - rectangleBorderSize -heightOffset);
        circleHitbox.setX(this.getX()+(this.getWidth()/2));
        circleHitbox.setY(this.getY()+(this.getHeight()/2)+ hitboxYOffset);
        rectangleHitbox.setX(this.getX());
        rectangleHitbox.setY(this.getY());
    }

    public Circle getCircleHitbox() {
        return circleHitbox;
    }
    public float getHeightOffset() {
        return heightOffset;
    }
    public void setHeightOffset(float heightOffset) {
        this.heightOffset = heightOffset;
        this.heightOffsetTiles = (int)Math.floorDiv((long)heightOffset,(long)32);
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
    public void setBottomRectangle(float x, float y){
        collisionBoxes[0].setX(x);
        collisionBoxes[0].setY(y);
    }
    public void setLeftRectangle(float x, float y){
        collisionBoxes[1].setX(x);
        collisionBoxes[1].setY(y);
    }
    public void setRightRectangle(float x, float y){
        collisionBoxes[2].setX(x);
        collisionBoxes[2].setY(y);
    }
    public void setTopRectangle(float x, float y){
        collisionBoxes[3].setX(x);
        collisionBoxes[3].setY(y);
    }
    public float getBufferBorder() {
        return bufferBorder;
    }
    public float getRectangleBorderSize() {
        return rectangleBorderSize;
    }
    public boolean collides(Collidable collidable){
        return Intersector.overlaps(this.circleHitbox, collidable.getCircleHitbox());
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
    public void setY(float y) {
        super.setY(y);
        updateBoxesPosition();
    }

    public int getXTiles() {
        return XTiles;
    }
    public void setXTiles(int XTiles) {
        /**
         * Use this when setting something in the playable space to make sure it is on the grid.
         */
        if (XTiles > 37-widthTiles){
            XTiles = 37-widthTiles;}
        this.XTiles = XTiles;
        setX(XTiles *32+64);
    }
    public int getYTiles() {
        return YTiles;
    }
    public void setYTiles(int YTiles) {
        /**
         * Use this when setting something in the playable space to make sure it is on the grid.
         */
        if (YTiles > 15-heightTiles+heightOffsetTiles){YTiles = 15-heightTiles+heightOffsetTiles;}
        this.YTiles = YTiles;
        setY(YTiles *32+64);
    }
    public void setHitboxYOffset(float hitboxYOffset){
        this.hitboxYOffset = hitboxYOffset;
    }
    public void setHitboxRadius(float radius){
        this.circleHitbox.setRadius(radius);
    }

    public Rectangle getRectangleHitbox() {
        return rectangleHitbox;
    }
}

