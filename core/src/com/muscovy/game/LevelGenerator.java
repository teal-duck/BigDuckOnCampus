package com.muscovy.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

/**
 * Created by Thomas on 04/12/2015.
 */

public class LevelGenerator {
    /** As 2d arrays are indexed {{row1}{row2}{row3}...},
     * we want to refer to locations in this format:
     * dungeonRoomArray[yPosition][xPosition] */


    /* Count the number of rooms adjacent to the given location */
    public int checkAdjacent(int myY, int myX, DungeonRoom[][] dungeonRoomArray) {

        int numAdjacent = 0;
            /* check north */
        if (myY>0) {
            if (dungeonRoomArray[myY - 1][myX] != null) {
                numAdjacent++;
            }
        }
            /* check  east */
        if (myX<6) {
            if (dungeonRoomArray[myY][myX + 1] != null) {
                numAdjacent++;
            }
        }
        if (myY<6) {
            /* check south */
            if (dungeonRoomArray[myY + 1][myX] != null) {
                numAdjacent++;
            }
        }
        if (myX>0) {
            /* check west */
            if (dungeonRoomArray[myY][myX - 1] != null) {
                numAdjacent++;
            }
        }

        /* int numAdjacent = dungeonRoomArray[myY][myX].upDoor + dungeonRoomArray[myY][myX].rightDoor +
                dungeonRoomArray[myY][myX].downDoor + dungeonRoomArray[myY][myX].leftDoor; */

        return numAdjacent;
    }

    /* function is called to generate the array of rooms for our building */
    public DungeonRoom[][] generateBuilding (int maxRooms, int level) {
        Texture texture;
        boolean bossSet = false, itemSet = false, shopSet = false;
        DungeonRoom[][] dungeonRoomArray = new DungeonRoom[7][7];
        /* initialise starting room in the centre */
        dungeonRoomArray[3][3] = new DungeonRoom();
        dungeonRoomArray[3][3].setRoomType(4);

        /* initialise counter for current number of rooms */
        int currentRooms = 1;

        Random randomGenerator = new Random();
        int randomInteger = 0;

        /* continue until we have the required number of rooms +/- 1 */
        while (currentRooms < maxRooms) {
            for (int xPos = 0; xPos < 7; xPos++) {
                for (int yPos = 0; yPos < 7; yPos++) {
                    /* current location has a room- randomly place new room(s) adjacent to it */
                    if (dungeonRoomArray[yPos][xPos] != null) {
                        if (yPos != 0) {
                            randomInteger = randomGenerator.nextInt(2);
                            if ((randomInteger == 1) && (dungeonRoomArray[yPos - 1][xPos] == null) && (checkAdjacent(yPos - 1, xPos, dungeonRoomArray) <= 1)) {
                                /* place room above current */
                                dungeonRoomArray[yPos - 1][xPos] = new DungeonRoom();
                                dungeonRoomArray[yPos - 1][xPos].setDownDoor(true);
                                dungeonRoomArray[yPos][xPos].setUpDoor(true);
                                currentRooms += 1;
                            }
                        }
                        if (xPos != 6) {
                            randomInteger = randomGenerator.nextInt(2);
                            if ((randomInteger == 1) && (dungeonRoomArray[yPos][xPos + 1] == null) && (checkAdjacent(yPos, xPos + 1, dungeonRoomArray) <= 1)) {
                                /* place room right of current */
                                dungeonRoomArray[yPos][xPos + 1] = new DungeonRoom();
                                dungeonRoomArray[yPos][xPos + 1].setLeftDoor(true);
                                dungeonRoomArray[yPos][xPos].setRightDoor(true);
                                currentRooms += 1;
                            }
                        }
                        if (yPos != 6) {
                            randomInteger = randomGenerator.nextInt(2);
                            if ((randomInteger == 1) && (dungeonRoomArray[yPos + 1][xPos] == null) && (checkAdjacent(yPos + 1, xPos, dungeonRoomArray) <= 1)) {
                                /* place room below current */
                                dungeonRoomArray[yPos + 1][xPos] = new DungeonRoom();
                                dungeonRoomArray[yPos + 1][xPos].setUpDoor(true);
                                dungeonRoomArray[yPos][xPos].setDownDoor(true);
                                currentRooms += 1;
                            }
                        }
                        if (xPos != 0) {
                            randomInteger = randomGenerator.nextInt(2);
                            if ((randomInteger == 1) && (dungeonRoomArray[yPos][xPos - 1] == null) && (checkAdjacent(yPos, xPos - 1, dungeonRoomArray) <= 1)) {
                                /* place room left of current */
                                dungeonRoomArray[yPos][xPos - 1] = new DungeonRoom();
                                dungeonRoomArray[yPos][xPos - 1].setRightDoor(true);
                                dungeonRoomArray[yPos][xPos].setLeftDoor(true);
                                currentRooms += 1;
                            }
                        }
                    }
                }
            }
        }
/*These are quite naive approaches atm, but they work!*/

        /* place our boss room */
        for (int xPos = 0; xPos < 7; xPos++) {
            for (int yPos = 0; yPos < 7; yPos++) {
                if (!bossSet){
                    if ((dungeonRoomArray[yPos][xPos] != null) && (dungeonRoomArray[yPos][xPos].getRoomType() == 0) && (checkAdjacent(yPos, xPos, dungeonRoomArray) == 1)) {
                        dungeonRoomArray[yPos][xPos].setRoomType(1);
                        bossSet = true;
                        break;
                    }
                }
            }
        }
        /* place our item room */
        for (int xPos = 0; xPos < 7; xPos++) {
            for (int yPos = 0; yPos < 7; yPos++) {
                if (!itemSet){
                    if ((dungeonRoomArray[yPos][xPos] != null) && (dungeonRoomArray[yPos][xPos].getRoomType() == 0) && (checkAdjacent(yPos, xPos, dungeonRoomArray) == 1)) {
                        dungeonRoomArray[yPos][xPos].setRoomType(2);
                        itemSet = true;
                        break;
                    }
                }
            }
        }
        /* place our shop room */
        for (int xPos = 0; xPos < 7; xPos++) {
            for (int yPos = 0; yPos < 7; yPos++) {
                if (!shopSet) {
                    if ((dungeonRoomArray[yPos][xPos] != null) && (dungeonRoomArray[yPos][xPos].getRoomType() == 0) && (checkAdjacent(yPos, xPos, dungeonRoomArray) == 1)) {
                        dungeonRoomArray[yPos][xPos].setRoomType(3);
                        shopSet = true;
                        break;
                    }
                }
            }
        }
        for (int xPos = 0; xPos < 7; xPos++) {
            for (int yPos = 0; yPos < 7; yPos++) {
                if (dungeonRoomArray[yPos][xPos] != null){
                    dungeonRoomArray[yPos][xPos].generateRoom(level);
                }
            }
        }
        return dungeonRoomArray;
    }
}