package com.muscovy.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class Room extends OnscreenDrawable{
    private ArrayList<Obstacle> obstacleList;
    private ArrayList<Enemy> enemyList;
    private Rectangle[] walls;

    public Room() {
        this.setSprite(new Sprite(new Texture("core/assets/testMap.png")));
        obstacleList = new ArrayList<Obstacle>();
        enemyList = new ArrayList<Enemy>();
        walls = new Rectangle[4];
        walls[0] = new Rectangle(0,0,1280,64);//bottom wall
        walls[1] = new Rectangle(0,0,64,608);//left wall
        walls[2] = new Rectangle(1280-64,0,64,608);//right wall
        walls[3] = new Rectangle(0,608-64,1280,64);//top wall
    }
    public void addEnemy(Enemy enemy){
        enemyList.add(enemy);
    }
    public void addObstacle(Obstacle obstacle){
        obstacleList.add(obstacle);
    }
    public Rectangle getBottomRectangle(){
        return walls[0];
    }
    public Rectangle getLeftRectangle(){
        return walls[1];
    }
    public Rectangle getRightRectangle(){
        return walls[2];
    }
    public Rectangle getTopRectangle(){
        return walls[3];
    }
    @Override
    public Sprite getSprite() {
        return super.getSprite();
    }
    @Override
    public void setSprite(Sprite sprite) {
        sprite.setX(0);
        sprite.setY(0);
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
