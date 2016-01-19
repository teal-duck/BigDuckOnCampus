package com.muscovy.game;


import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Map;

/**
 * Created by SeldomBucket on 05-Jan-16.
 */
public abstract class Collidable extends OnscreenDrawable{
    private Circle circleHitbox;
    private Rectangle rectangleHitbox;
    //Offset from centre of collidable: offset of 6 means centre of circle hitbox is 6 pixels above centre of collidable
    private float hitboxYOffset = 0;
    private int XTiles, YTiles;
    private int widthTiles, heightTiles;

    /**
     * Getters and Setters
     */
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
    public int getYTiles() {
        return YTiles;
    }
    /**
     * setXTiles and setYTiles moves the collidable to fit on the grid directly.
     * Clamps to walls of dungeon room, assuming a 64x64 collidable
     * Useful for placing stuff in the dungeon rooms
     */
    public void setXTiles(int XTiles) {
        /**
         * Use this when setting something in the playable space to make sure it is on the grid.
         */
        if (XTiles > 37-widthTiles){
            XTiles = 37-widthTiles;}
        this.XTiles = XTiles;
        setX(XTiles *32+64);
    }
    public void setYTiles(int YTiles) {
        /**
         * Use this when setting something in the playable space to make sure it is on the grid.
         */
        if (YTiles > 21 - heightTiles) {
            YTiles = 21-heightTiles;}
        this.YTiles = YTiles;
        setY(YTiles * 32 + 64);
    }
    public void setHitboxCentre(float x, float y){
        /**
         * Calculates the x and y of the bottom left corner using radius and y offset of the hitbox
         */
        setX((x - circleHitbox.radius) - ((getWidth() / 2) - circleHitbox.radius));
        setY((y - circleHitbox.radius)-((getHeight()/2)- circleHitbox.radius)-hitboxYOffset);
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
    public Circle getCircleHitbox() {
        return circleHitbox;
    }
    /**
     * Initialisation Methods
     */
    //initialise X and Y for moving the collidable before the hitboxes are set up
    public void initialiseX(float x){
        super.setX(x);
    }
    public void initialiseY(float y){
        super.setY(y);
    }
    public void setUpBoxes(){
        /**
         * Initialises circle and rectangle hitboxes based on current x and y, width and height
         * Circle hitbox automatically has radius of width/2
         * Rectangle hitbox automatically same size as the sprite image
         */
        float x = this.getX();
        float y = this.getY();
        float width = this.getWidth();
        float height = this.getHeight();
        this.widthTiles = (int)Math.floorDiv((long)width,(long)32)+1;
        this.heightTiles = (int)Math.floorDiv((long)height,(long)32)+1;
        circleHitbox = new Circle(x + (width/2),y+(height/2)+ hitboxYOffset,width/2);
        rectangleHitbox = new Rectangle(x,y,width,height);
    }
    /**
     * Collision Methods
     */
    public void moveToNearestEdgeCircle(Collidable collidable){
        /**
         * Calculates angle between the centre of the circles, and calculates the x & y distance needed so the centres
         * are this radius + collidable radius away.
         * (MOVES THIS AWAY FROM THE GIVEN COLLIDABLE)
         */
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
        /**
         * Checks where the centre of the circle of the other collidable is in relation to the rectangle, and moves it
         * accordingly.
         * (MOVES THIS AWAY FROM THE GIVEN COLLIDABLE)
         */
        float angle = getAngleFrom(collidable);
        float thisX = getCircleHitbox().x;
        float thisY = getCircleHitbox().y;
        float thatX = collidable.getCircleHitbox().x;
        float thatY = collidable.getCircleHitbox().y;
        float rectDistance = 0;
        float distanceToEdge = 0;
        float x,y;
        if (thisX>collidable.getX() && thisX<collidable.getX()+collidable.getWidth() && thisY>thatY){
            y = collidable.getY()+collidable.getRectangleHitbox().getHeight()+this.circleHitbox.radius+1;
            setHitboxCentre(thisX,y);
        }else if (thisX>collidable.getX() && thisX<collidable.getX()+collidable.getWidth() && thisY<thatY){
            y = collidable.getY()-this.circleHitbox.radius;
            setHitboxCentre(thisX,y);
        }else if (thisY>collidable.getY() && thisY<collidable.getY()+collidable.getHeight() && thisX>thatX){
            x = collidable.getRectangleHitbox().getX()+collidable.getRectangleHitbox().getWidth()+this.circleHitbox.radius;
            setHitboxCentre(x, thisY);
        }else if (thisY>collidable.getY() && thisY<collidable.getY()+collidable.getHeight() && thisX<thatX){
            x = collidable.getRectangleHitbox().getX()-this.circleHitbox.radius;
            setHitboxCentre(x,thisY);
        }else if (thisY<collidable.getRectangleHitbox().getY() && thisX<collidable.getRectangleHitbox().getX()){
            angle = (float)Math.atan((thisY-collidable.getRectangleHitbox().getY())/(thisX-collidable.getRectangleHitbox().getX()));
            y = collidable.getRectangleHitbox().getY()-(float)(this.circleHitbox.radius*Math.sin(angle));
            x = collidable.getRectangleHitbox().getX()-(float)(this.circleHitbox.radius*Math.cos(angle));
            setHitboxCentre(x,y);
        }else if (thisY<collidable.getRectangleHitbox().getY() && thisX>collidable.getRectangleHitbox().getX()+collidable.getRectangleHitbox().getWidth()){
            angle = (float)Math.atan((collidable.getRectangleHitbox().getY()-thisY)/(thisX-(collidable.getRectangleHitbox().getX()+collidable.getRectangleHitbox().getWidth())));
            y = collidable.getRectangleHitbox().getY()-(float)(this.circleHitbox.radius*Math.sin(angle));
            x = collidable.getRectangleHitbox().getX()+collidable.getRectangleHitbox().getWidth()+(float)(this.circleHitbox.radius*Math.cos(angle));
            setHitboxCentre(x,y);
        }else if (thisY>collidable.getRectangleHitbox().getY()+collidable.getRectangleHitbox().getHeight() && thisX>collidable.getRectangleHitbox().getX()+collidable.getRectangleHitbox().getWidth()){
            angle = (float)Math.atan((thisY-(collidable.getRectangleHitbox().getY()+collidable.getRectangleHitbox().getHeight()))/(thisX-(collidable.getRectangleHitbox().getX()+collidable.getRectangleHitbox().getWidth())));
            y = collidable.getRectangleHitbox().getY()+collidable.getRectangleHitbox().getHeight() +(float)(this.circleHitbox.radius*Math.sin(angle));
            x = collidable.getRectangleHitbox().getX()+collidable.getRectangleHitbox().getWidth()+(float)(this.circleHitbox.radius*Math.cos(angle));
            setHitboxCentre(x,y);
        }else if (thisY>collidable.getRectangleHitbox().getY()+collidable.getRectangleHitbox().getHeight() && thisX<collidable.getRectangleHitbox().getX()){
            angle = (float)Math.atan((thisY-(collidable.getRectangleHitbox().getY()+collidable.getRectangleHitbox().getHeight()))/(collidable.getRectangleHitbox().getX()-thisX));
            y = collidable.getRectangleHitbox().getY()+collidable.getRectangleHitbox().getHeight() +(float)(this.circleHitbox.radius*Math.sin(angle));
            x = collidable.getRectangleHitbox().getX()-(float)(this.circleHitbox.radius*Math.cos(angle));
            setHitboxCentre(x,y);
        }
    }
    /**
     * Angle methods use trig to calculate angles, then it's position to calculate the angle clockwise from the vertical
     */
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
        circleHitbox.setX(this.getX()+(this.getWidth()/2));
        circleHitbox.setY(this.getY()+(this.getHeight()/2)+ hitboxYOffset);
        rectangleHitbox.setX(this.getX());
        rectangleHitbox.setY(this.getY());
    }
    public boolean collides(Collidable collidable){
        return Intersector.overlaps(this.circleHitbox, collidable.getCircleHitbox());
    }


}

