package com.muscovy.game;

import java.util.Random;

/**
 * Created by Thomas on 04/12/2015.
 */

public class Building {
    /* As 2d arrays are indexed {{row1}{row2}{row3}...},
     * we want to refer to locations in this format:
     * dungeonRoomArray[yPosition][xPosition] */
    DungeonRoom[][] dungeonRoomArray = new DungeonRoom[7][7];

    /* Count the number of rooms adjacent to the given location */
    public int checkAdjacent(int myY, int myX) {

        int numAdjacent = 0;

        /* check north */
        if (dungeonRoomArray[myY - 1][myX] != null) {
            numAdjacent++;
        }

        /* check  east */
        if (dungeonRoomArray[myY][myX + 1] != null) {
            numAdjacent++;
        }

        /* check south */
        if (dungeonRoomArray[myY + 1][myX] != null) {
            numAdjacent++;
        }

        /* check west */
        if (dungeonRoomArray[myY][myX - 1] != null) {
            numAdjacent++;
        }

        /* int numAdjacent = dungeonRoomArray[myY][myX].upDoor + dungeonRoomArray[myY][myX].rightDoor +
                dungeonRoomArray[myY][myX].downDoor + dungeonRoomArray[myY][myX].leftDoor; */

        return numAdjacent;
    }

    /* function is called to generate the array of rooms for our building */
    public void generateBuilding (int maxRooms) {
        /* initialise starting room in the centre */
        dungeonRoomArray[3][3] = new DungeonRoom();
        dungeonRoomArray[3][3].roomType = "start";

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
                            if ((randomInteger == 1) && (dungeonRoomArray[yPos - 1][xPos] == null) && (checkAdjacent(yPos - 1, xPos) <= 1)) {
                                /* place room above current */
                                dungeonRoomArray[yPos - 1][xPos] = new DungeonRoom();
                                dungeonRoomArray[yPos - 1][xPos].downDoor = true;
                                dungeonRoomArray[yPos][xPos].upDoor = true;
                                currentRooms += 1;
                            }
                        }
                        if (xPos != 6) {
                            randomInteger = randomGenerator.nextInt(2);
                            if ((randomInteger == 1) && (dungeonRoomArray[yPos][xPos + 1] == null) && (checkAdjacent(yPos, xPos + 1) <= 1)) {
                                /* place room right of current */
                                dungeonRoomArray[yPos][xPos + 1] = new DungeonRoom();
                                dungeonRoomArray[yPos][xPos + 1].leftDoor = true;
                                dungeonRoomArray[yPos][xPos].rightDoor = true;
                                currentRooms += 1;
                            }
                        }
                        if (yPos != 6) {
                            randomInteger = randomGenerator.nextInt(2);
                            if ((randomInteger == 1) && (dungeonRoomArray[yPos + 1][xPos] == null) && (checkAdjacent(yPos + 1, xPos) <= 1)) {
                                /* place room below current */
                                dungeonRoomArray[yPos + 1][xPos] = new DungeonRoom();
                                dungeonRoomArray[yPos + 1][xPos].upDoor = true;
                                dungeonRoomArray[yPos][xPos].downDoor = true;
                                currentRooms += 1;
                            }
                        }
                        if (xPos != 0) {
                            randomInteger = randomGenerator.nextInt(2);
                            if ((randomInteger == 1) && (dungeonRoomArray[yPos][xPos - 1] == null) && (checkAdjacent(yPos, xPos - 1) <= 1)) {
                                /* place room left of current */
                                dungeonRoomArray[yPos][xPos - 1] = new DungeonRoom();
                                dungeonRoomArray[yPos][xPos - 1].rightDoor = true;
                                dungeonRoomArray[yPos][xPos].leftDoor = true;
                                currentRooms += 1;
                            }
                        }
                    }
                }
            }
        }
        /* place our boss room */
        for (int xPos = 0; xPos < 7; xPos++) {
            for (int yPos = 0; yPos < 7; yPos++) {
                if ((dungeonRoomArray[yPos][xPos] != null) && (dungeonRoomArray[yPos][xPos].roomType == "") && (checkAdjacent(yPos, xPos) == 1)) {
                    dungeonRoomArray[yPos][xPos].roomType = "boss";
                    break;
                }
            }
        }
        /* place our item room */
        for (int xPos = 0; xPos < 7; xPos++) {
            for (int yPos = 0; yPos < 7; yPos++) {
                if ((dungeonRoomArray[yPos][xPos] != null) && (dungeonRoomArray[yPos][xPos].roomType == "") && (checkAdjacent(yPos, xPos) == 1)) {
                    dungeonRoomArray[yPos][xPos].roomType = "item";
                    break;
                }
            }
        }
        /* place our shop room */
        for (int xPos = 0; xPos < 7; xPos++) {
            for (int yPos = 0; yPos < 7; yPos++) {
                if ((dungeonRoomArray[yPos][xPos] != null) && (dungeonRoomArray[yPos][xPos].roomType == "") && (checkAdjacent(yPos, xPos) == 1)) {
                    dungeonRoomArray[yPos][xPos].roomType = "shop";
                    break;
                }
            }
        }
    }
}