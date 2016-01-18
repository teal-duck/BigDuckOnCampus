package com.muscovy.game;

/**
 * Created by ewh502 on 11/01/2016.
 */
public class Level {
    DungeonRoom[][] levelArray;
    public Level(DungeonRoom[][] levelArray) {
        this.levelArray = levelArray;
    }
    public DungeonRoom getRoom(int roomX, int roomY){
        return levelArray[roomY][roomX];
    }
}
