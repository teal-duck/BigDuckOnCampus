package com.muscovy.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class DungeonRoom extends OnscreenDrawable{
    private ArrayList<Obstacle> obstacleList;
    private ArrayList<Enemy> enemyList;
    private Rectangle[] walls, projectileWalls;
    /* variables indicate if there is a door on that wall */
    private boolean upDoor = false, rightDoor = false,downDoor = false, leftDoor = false;
    private Rectangle northDoor, eastDoor, southDoor, westDoor;
    float doorWidth = 70;
    /* roomType indicates the type of room
     * options: "" (default), "start", "boss", "item", "shop" */
    private int roomType = 0; //0 = normal room, 1 = boss room,  2 = item room, 3 = shop room, 4 = start room
    private boolean enemiesDead;

    public DungeonRoom() {
        this.setSprite(new Sprite(new Texture("core/assets/testMap.png")));
        obstacleList = new ArrayList<Obstacle>();
        enemyList = new ArrayList<Enemy>();
        walls = new Rectangle[4];
        walls[0] = new Rectangle(0,0,1280,64);//bottom wall
        walls[1] = new Rectangle(0,0,64,960-192);//left wall
        walls[2] = new Rectangle(1280-64,0,64,960-192);//right wall
        walls[3] = new Rectangle(0,960-192-64,1280,64);//top wall
        projectileWalls = new Rectangle[4];
        projectileWalls[0] = new Rectangle(0,0,1280,32);//bottom wall
        projectileWalls[1] = new Rectangle(0,0,32,960-192);//left wall
        projectileWalls[2] = new Rectangle(1280-32,0,32,960-192);//right wall
        projectileWalls[3] = new Rectangle(0,768-32,1280,32);//top wall
    }
    public void generateRoom(){
        switch (roomType){
            case 0:
                int[][] TileArray = new int[10][18];
                Random rand = new Random();
                int ChosenValue = rand.nextInt(10)+1;
                switch (ChosenValue){
                    case 6:
                        TileArray[5][9] = 1;
                        TileArray[6][9] = 1;
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    case 9:
                        break;
                    case 10:
                        break;
                }
                TileArray[5][9] = 1;
                TileArray[6][9] = 1;
                for (int i = 0; i>TileArray.length; i++){
                    for (int j = 0; j>TileArray[i].length; j++){
                        switch (TileArray[i][j]){
                            case 0:
                                break;
                            case 1:
                                Sprite RockSprite;
                                Obstacle obstacle5;
                                RockSprite = new Sprite();
                                RockSprite.setTexture(new Texture("core/assets/rock.png"));
                                obstacle5 = new Obstacle(RockSprite);
                                obstacle5.setXTiles(j*2);
                                obstacle5.setYTiles(i*2);
                        }
                    }
                }
                break;
            case 1:
                Sprite testSprite1, testSprite2;
                Obstacle obstacle1, obstacle2;
                Enemy enemy1, enemy2;
                testSprite1 = new Sprite();
                testSprite1.setTexture(new Texture("core/assets/rock.png"));
                obstacle1 = new Obstacle(testSprite1);
                obstacle1.setXTiles(10);
                obstacle1.setYTiles(100);
                testSprite2 = new Sprite();
                testSprite2.setTexture(new Texture("core/assets/thing2.gif"));
                obstacle2 = new Obstacle(testSprite2);
                obstacle2.setXTiles(15);
                obstacle2.setYTiles(13);
                obstacle2.setDamaging(true);
                obstacle2.setTouchDamage(10.0f);
                testSprite1 = new Sprite(new Texture("core/assets/testEnemy.png"));
                enemy1 = new Enemy(testSprite1);
                enemy1.setX(1111);
                enemy1.setY(300);
                enemy1.setAttackType(1);
                enemy1.setMovementType(0);
                enemy1.setTouchDamage(30);
                enemy1.setShotType(1);
                enemy1.setScoreOnDeath(500);
                testSprite1 = new Sprite(new Texture("core/assets/testEnemy.png"));
                enemy2 = new Enemy(testSprite1);
                enemy2.setXTiles(0);
                enemy2.setYTiles(1);
                enemy2.setAttackType(0);
                enemy2.setMovementType(0);
                enemy2.setTouchDamage(30);
                enemy2.setShotType(0);
                enemy2.setScoreOnDeath(300);
                addEnemy(enemy1);
                addEnemy(enemy2);
                addObstacle(obstacle1);
                addObstacle(obstacle2);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
        initialiseDoors();
    }
    public void initialiseDoors(){
        if(upDoor){
            northDoor = new Rectangle(640-doorWidth/2,768-doorWidth,doorWidth,doorWidth);
        }
        if(downDoor){
            southDoor = new Rectangle(640-doorWidth/2,0,doorWidth,doorWidth);
        }
        if(rightDoor){
            eastDoor = new Rectangle(1280-doorWidth,384-doorWidth/2,doorWidth,doorWidth);
        }
        if(leftDoor){
            westDoor = new Rectangle(0,384-doorWidth/2,doorWidth,doorWidth);
        }
    }
    public void addEnemy(Enemy enemy){
        enemyList.add(enemy);
    }
    public ArrayList<Enemy> getEnemyList(){
        return enemyList;
    }
    public void addObstacle(Obstacle obstacle){
        obstacleList.add(obstacle);
    }
    public ArrayList<Obstacle> getObstacleList(){
        return obstacleList;
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
    public Rectangle getProjectileWallBottom(){
        return projectileWalls[0];
    }
    public Rectangle getProjectileWallLeft(){
        return projectileWalls[1];
    }
    public Rectangle getProjectileWallRight(){
        return projectileWalls[2];
    }
    public Rectangle getProjectileWallTop(){
        return projectileWalls[3];
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public boolean isEnemiesDead() {
        if (enemyList.size() == 0){enemiesDead = true;}
        return enemiesDead;
    }

    public void setEnemiesDead(boolean enemiesDead) {
        this.enemiesDead = enemiesDead;
    }

    public Boolean getUpDoor() {
        return upDoor;
    }
    public void setUpDoor(Boolean upDoor) {
        this.upDoor = upDoor;
    }
    public Boolean getRightDoor() {
        return rightDoor;
    }
    public void setRightDoor(Boolean rightDoor) {
        this.rightDoor = rightDoor;
    }
    public Boolean getDownDoor() {
        return downDoor;
    }
    public void setDownDoor(Boolean downDoor) {
        this.downDoor = downDoor;
    }
    public Boolean getLeftDoor() {
        return leftDoor;
    }
    public void setLeftDoor(Boolean leftDoor) {
        this.leftDoor = leftDoor;
    }
    public Rectangle getNorthDoor() {
        return northDoor;
    }
    public void setNorthDoor(Rectangle northDoor) {
        this.northDoor = northDoor;
    }
    public Rectangle getEastDoor() {
        return eastDoor;
    }
    public void setEastDoor(Rectangle eastDoor) {
        this.eastDoor = eastDoor;
    }
    public Rectangle getSouthDoor() {
        return southDoor;
    }
    public void setSouthDoor(Rectangle southDoor) {
        this.southDoor = southDoor;
    }
    public Rectangle getWestDoor() {
        return westDoor;
    }
    public void setWestDoor(Rectangle westDoor) {
        this.westDoor = westDoor;
    }
    public void killEnemy(Enemy enemy){
        enemyList.remove(enemy);
        enemyList.trimToSize();
        if (enemyList.size() == 0){enemiesDead = true;}
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
