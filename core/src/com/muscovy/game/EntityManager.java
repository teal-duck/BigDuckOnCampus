package com.muscovy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

/**
 * Created by ewh502 on 04/12/2015.
 */
public class EntityManager {
    private ArrayList<OnscreenDrawable> renderList ;
    private ArrayList<Obstacle> obstacleList;
    private ArrayList<Enemy> enemyList;
    private ArrayList<Projectile> projectileList;
    private DungeonRoom currentDungeonRoom;
    private LevelGenerator levelGenerator;
    private Level[] level;
    private int levelNo, maxLevels = 8;
    private int roomX, roomY;
    private BitmapFont list;//Testing purposes
    private PlayerCharacter playerCharacter;
    public EntityManager() {
        this.renderList = new ArrayList<OnscreenDrawable>();
        this.obstacleList = new ArrayList<Obstacle>();
        this.enemyList = new ArrayList<Enemy>();
        this.projectileList = new ArrayList<Projectile>();
        this.levelGenerator = new LevelGenerator();
        level = new Level[maxLevels];
        list = new BitmapFont();
        list.setColor(Color.WHITE);//Testing purposes
        this.currentDungeonRoom = new DungeonRoom();
        maxLevels = 8;
    }
    public void generateLevels(){
        level[0] = new Level(levelGenerator.generateBuilding(20),0);
        level[1] = new Level(levelGenerator.generateBuilding(20),0);
        level[2] = new Level(levelGenerator.generateBuilding(20),0);
        level[3] = new Level(levelGenerator.generateBuilding(20),0);
        level[4] = new Level(levelGenerator.generateBuilding(20),0);
        level[5] = new Level(levelGenerator.generateBuilding(20),0);
        level[6] = new Level(levelGenerator.generateBuilding(20),0);
        level[7] = new Level(levelGenerator.generateBuilding(20),0);
        /*while (steve<maxLevels-2){
            level[steve] = new Level(levelGenerator.generateBuilding(20),0);
            steve++;
        }*/
    }
    public int getLevelNo() {
        return levelNo;
    }
    public void setLevel(int levelNo) {
        this.levelNo = levelNo;
    }
    public void startLevel(PlayerCharacter playerCharacter){
        roomX = 3;
        roomY = 3;
        this.playerCharacter = playerCharacter;
        setCurrentDungeonRoom(level[levelNo].getRoom(roomX, roomY));
        this.renderList.add(this.playerCharacter);
    }
    public void render(SpriteBatch batch){
        /**
         * Renders sprites in the controller so those further back are rendered first, giving a perspective illusion
         */
        renderList.trimToSize();
        obstacleList.trimToSize();
        enemyList.trimToSize();
        projectileList.trimToSize();
        sortDrawables();
        batch.draw(currentDungeonRoom.getSprite().getTexture(),0,0);
        for (OnscreenDrawable drawable:renderList){
            if (drawable instanceof PlayerCharacter){
                if (((PlayerCharacter) drawable).isInvincible()){
                    if (!(((PlayerCharacter) drawable).getInvincibilityCounter()*10 % 2 < 0.75)){
                        batch.draw(drawable.getSprite().getTexture(), drawable.getX(), drawable.getY());
                    }
                }else{
                    batch.draw(drawable.getSprite().getTexture(), drawable.getX(), drawable.getY());
                }
            }else {
                batch.draw(drawable.getSprite().getTexture(), drawable.getX(), drawable.getY());
            }
        }
        for (Projectile projectile:projectileList){
            batch.draw(projectile.getSprite().getTexture(), projectile.getX(), projectile.getY());
        }
        list.draw(batch, "no of projectiles in controller = " + projectileList.size(), (float) 250, (float) 450);//Testing purposes
    }
    private void sortDrawables(){
        /**
        * Quicksorts the list of drawable objects in the controller by Y coordinate so
        * it renders the things in the background first.
        */
        ArrayList<OnscreenDrawable> newList = new ArrayList<OnscreenDrawable>();
        newList.addAll(quicksort(renderList));
        renderList.clear();
        renderList.addAll(newList);
    }
    /**Quicksort Helper Methods*/
    private ArrayList<OnscreenDrawable> quicksort(ArrayList<OnscreenDrawable> input){
        if(input.size() <= 1){
            return input;
        }
        int middle = (int) Math.ceil((double)input.size() / 2);
        OnscreenDrawable pivot = input.get(middle);
        ArrayList<OnscreenDrawable> less = new ArrayList<OnscreenDrawable>();
        ArrayList<OnscreenDrawable> greater = new ArrayList<OnscreenDrawable>();
        for (int i = 0; i < input.size(); i++) {
            if(input.get(i).getY() >= pivot.getY()){
                if(i == middle){
                    continue;
                }
                less.add(input.get(i));
            }
            else{
                greater.add(input.get(i));
            }
        }
        return concatenate(quicksort(less), pivot, quicksort(greater));
    }
    private ArrayList<OnscreenDrawable> concatenate(ArrayList<OnscreenDrawable> less, OnscreenDrawable pivot, ArrayList<OnscreenDrawable> greater){
        ArrayList<OnscreenDrawable> list = new ArrayList<OnscreenDrawable>();
        for (int i = 0; i < less.size(); i++) {
            list.add(less.get(i));
        }
        list.add(pivot);
        for (int i = 0; i < greater.size(); i++) {
            list.add(greater.get(i));
        }
        return list;
    }
    public void killProjectiles(){
        ArrayList<Projectile> deadProjectiles = new ArrayList<Projectile>();
        for (Projectile projectile:projectileList){
            if (projectile.lifeOver()){
                deadProjectiles.add(projectile);
            }
        }
        for (Projectile projectile:deadProjectiles){
            projectileList.remove(projectile);
        }
    }
    public void killEnemies(){
        ArrayList<Enemy> deadEnemies = new ArrayList<Enemy>();
        for (Enemy enemy:enemyList){
            if (enemy.lifeOver()){
                deadEnemies.add(enemy);
            }
        }
        for (Enemy enemy:deadEnemies){
            renderList.remove(enemy);
            enemyList.remove(enemy);
            this.currentDungeonRoom.killEnemy(enemy);
        }
    }
    public void addNewDrawable(OnscreenDrawable drawable){
        renderList.add(drawable);
    }
    public void addNewDrawables(ArrayList<OnscreenDrawable> drawables){
        renderList.addAll(drawables);
    }
    public void addNewObstacle(Obstacle obstacle){
        renderList.add(obstacle);
        obstacleList.add(obstacle);
    }
    public void addNewObstacles(ArrayList<Obstacle> obstacles){
        renderList.addAll(obstacles);
        obstacleList.addAll(obstacles);
    }
    public void addNewEnemy(Enemy enemy){
        renderList.add(enemy);
        enemyList.add(enemy);
    }
    public void addNewEnemies(ArrayList<Enemy> enemies){
        renderList.addAll(enemies);
        enemyList.addAll(enemies);
    }
    public void addNewProjectile(Projectile projectile){
        projectileList.add(projectile);
    }
    public void addNewProjectiles(ArrayList<Projectile> projectiles){
        projectileList.addAll(projectiles);
    }
    public ArrayList<Obstacle> getObstacles(){
        return obstacleList;
    }
    public ArrayList<Enemy> getEnemies(){
        return enemyList;
    }
    public ArrayList<Projectile> getProjectiles(){
        return projectileList;
    }
    public void setCurrentDungeonRoom(DungeonRoom dungeonRoom){
        this.currentDungeonRoom = dungeonRoom;
        this.renderList.clear();
        this.obstacleList.clear();
        this.obstacleList.addAll(dungeonRoom.getObstacleList());
        this.enemyList.clear();
        this.enemyList.addAll(dungeonRoom.getEnemyList());
    }
    public DungeonRoom getCurrentDungeonRoom(){
        return this.currentDungeonRoom;
    }

    public void moveNorth(){
        roomX++;
        setCurrentDungeonRoom(level[levelNo].getRoom(roomX,roomY));
        this.renderList.add(playerCharacter);
    }
    public void moveEast(){
        roomY++;
        setCurrentDungeonRoom(level[levelNo].getRoom(roomX,roomY));
        this.renderList.add(playerCharacter);
    }
    public void moveWest(){
        roomY--;
        setCurrentDungeonRoom(level[levelNo].getRoom(roomX,roomY));
        this.renderList.add(playerCharacter);
    }
    public void moveSouth(){
        roomX--;
        setCurrentDungeonRoom(level[levelNo].getRoom(roomX, roomY));
        this.renderList.add(playerCharacter);
    }
}
