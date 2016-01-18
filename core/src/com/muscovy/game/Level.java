package com.muscovy.game;

/**
 * Created by ewh502 on 11/01/2016.
 */
public class Level {
    public DungeonRoom[][] levelArray;
    private int objective;
    private int level; //level, going to be used for
    private boolean completed = false;
    public Level(DungeonRoom[][] levelArray, int objective) {
        this.levelArray = new DungeonRoom[7][7];
        this.levelArray = levelArray;
        this.objective = objective;
    }
    public DungeonRoom getRoom(int roomX, int roomY){
        return levelArray[roomY][roomX];
    }

    public DungeonRoom[][] getLevelArray() {
        return levelArray;
    }

    public void setLevelArray(DungeonRoom[][] levelArray) {
        this.levelArray = levelArray;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
