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
    float doorWidth = 66;
    /* roomType indicates the type of room
     * options: "" (default), "start", "boss", "item", "shop" */
    private int roomType = 0; //0 = normal room, 1 = boss room,  2 = item room, 3 = shop room, 4 = start room
    private boolean enemiesDead;
    private Random rand;

    public DungeonRoom() {
        rand = new Random();
        this.setSprite(new Sprite(new Texture("core/assets/accommodationAssets/accommodationBackground.png")));
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
    private void createBoulder(int x, int y){
        Sprite RockSprite;
        Obstacle obstacle5;
        RockSprite = new Sprite();
        RockSprite.setTexture(new Texture("core/assets/accommodationAssets/obstacles/binRecycle.png"));
        obstacle5 = new Obstacle(RockSprite);
        obstacle5.setXTiles(x);
        obstacle5.setYTiles(y);
        addObstacle(obstacle5);
    }
    private void createSpikes(int x, int y){
        Sprite SpikeSprite;
        Obstacle obstacle6;
        SpikeSprite = new Sprite();
        SpikeSprite.setTexture(new Texture("core/assets/accommodationAssets/obstacles/binWaste.png"));
        obstacle6 = new Obstacle(SpikeSprite);
        obstacle6.setXTiles(x);
        obstacle6.setYTiles(y);
        addObstacle(obstacle6);
        obstacle6.setDamaging(true);
        obstacle6.setTouchDamage(10.0f);
    }
    private void createRandomEnemy(int x, int y){
        Sprite enemySprite;
        Enemy enemy;
        if (rand.nextBoolean()){
            enemySprite = new Sprite(new Texture("core/assets/accommodationAssets/enemies/cleaner/rightCleanerWalk/PNGs/rightCleaner1.png"));
            enemy = new Enemy(enemySprite);
            enemy.setAttackType(0);
        }else{
            enemySprite = new Sprite(new Texture("core/assets/accommodationAssets/enemies/student/rightStudentWalk/PNGs/rightStudent1.png"));
            enemy = new Enemy(enemySprite);
            enemy.setAttackType(1);
        }
        switch (rand.nextInt(3)){
            case 0:
                enemy.setShotType(0);
                enemy.setMovementType(1);
                enemy.setXTiles(x);
                enemy.setYTiles(y);
                break;
            case 1:
                enemy.setShotType(1);
                enemy.setMovementType(1);
                enemy.setXTiles(x);
                enemy.setYTiles(y);
                break;
            case 2:
                enemy.setShotType(2);
                enemy.setMovementType(2);
                break;
            case 3:
                enemy.setShotType(4);
                enemy.setMovementType(2);
                break;
        }
        enemy.setXTiles(x);
        enemy.setYTiles(y);
        enemy.calculateScoreOnDeath();
        addEnemy(enemy);
    }
    public void generateRoom(){
        switch (roomType){
            case 0:
                int[][] TileArray = new int[10][18];
                int ChosenValue = rand.nextInt(10)+1;
                boolean ChosenValue2 = rand.nextBoolean();
                switch (ChosenValue){
                    case 1:
                        TileArray = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                        break;
                    case 2:
                        TileArray = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 4, 0, 0, 0, 2, 2, 0, 0, 0, 4, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 4, 0, 0, 0, 2, 2, 0, 0, 0, 4, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                        break;
                    case 3:
                        TileArray = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                                                {0, 0, 0, 1, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 1, 0, 0, 0},
                                                {0, 0, 0, 1, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 1, 0, 0, 0},
                                                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                        break;
                    case 4:
                        TileArray = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                        break;
                    case 5:
                        TileArray = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 4, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 4, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 4, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 4, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                        break;
                    case 6:
                        TileArray = new int[][]{{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                                                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                                                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}};
                        break;
                    case 7:
                        TileArray = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                        break;
                    case 8:
                        TileArray = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                        break;
                    case 9:
                        TileArray = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                        break;
                    case 10:
                        TileArray = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 2, 0, 2, 2, 0, 2, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 2, 0, 2, 2, 0, 2, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                        break;
                }
                for (int i = 0; i<TileArray.length; i++){
                    for (int j = 0; j<TileArray[i].length; j++){
                        switch (TileArray[i][j]){
                            case 0:
                                break;
                            case 1:
                                createBoulder(2*j, 2*i);
                                break;
                            case 2:
                                createSpikes(2*j, 2*i);
                                break;
                            case 3:
                                if(ChosenValue2){
                                    createBoulder(2*j, 2*i);
                                }else if(!ChosenValue2){
                                    createSpikes(2*j, 2*i);
                                }
                                break;
                            case 4:
                                if(rand.nextInt(5)<3){
                                    createRandomEnemy(2*j,2*i);
                                }
                                break;
                        }
                    }
                }
                break;
            case 1:
                Sprite bossSprite;
                Enemy bossEnemy;
                bossSprite = new Sprite(new Texture("core/assets/accommodationAssets/accommodationBoss.png"));
                bossEnemy = new Enemy(bossSprite);
                bossEnemy.setXTiles((int)(36/2-(bossEnemy.getWidth()/64)));
                bossEnemy.setYTiles((int)(18/2-(bossEnemy.getHeight()/64)));
                bossEnemy.setAttackType(2);
                bossEnemy.setMovementType(2);
                bossEnemy.setMaxVelocity(100);
                bossEnemy.setTouchDamage(20);
                bossEnemy.setShotType(3);
                bossEnemy.setScoreOnDeath(3000);
                bossEnemy.setCurrentHealth(600);
                bossEnemy.setHitboxRadius(80);
                addEnemy(bossEnemy);
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
