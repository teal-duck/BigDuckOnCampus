package com.muscovy.game;

/**
 * Created by ewh502 on 11/01/2016.
 */
public class Level {
    Room[][] levelArray;
    public Level(Room[][] levelArray) {
        this.levelArray = levelArray;
    }
    public Room getRoom(int roomX, int roomY){
        return levelArray[roomY][roomX];
    }
}
