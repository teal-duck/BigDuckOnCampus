package com.muscovy.game;

import java.util.Random;

/**
 * Created by Thomas on 04/12/2015.
 */

public class Building {
    /* As 2d arrays are indexed {{row1}{row2}{row3}...},
     * we want to refer to locations in this format:
     * roomArray[yPosition][xPosition] */
    Room[][] roomArray = new Room[7][7];

    /* Count the number of rooms adjacent to the given location */
    public int checkAdjacent(int myY, int myX) {

        int numAdjacent = 0;

        /* check north */
        if (roomArray[myY - 1][myX] != null) {
            numAdjacent++;
        }

        /* check  east */
        if (roomArray[myY][myX + 1] != null) {
            numAdjacent++;
        }

        /* check south */
        if (roomArray[myY + 1][myX] != null) {
            numAdjacent++;
        }

        /* check west */
        if (roomArray[myY][myX - 1] != null) {
            numAdjacent++;
        }

        /* int numAdjacent = roomArray[myY][myX].upDoor + roomArray[myY][myX].rightDoor +
                roomArray[myY][myX].downDoor + roomArray[myY][myX].leftDoor; */

        return numAdjacent;
    }

    /* function is called to generate the array of rooms for our building */
    public void generateBuilding (int maxRooms) {
        /* initialise starting room in the centre */
        roomArray[3][3] = new Room();
        roomArray[3][3].roomType = "start";

        /* initialise counter for current number of rooms */
        int currentRooms = 1;

        Random randomGenerator = new Random();
        int randomInteger = 0;

        /* continue until we have the required number of rooms +/- 1 */
        while (currentRooms < maxRooms) {
            for (int xPos = 0; xPos < 7; xPos++) {
                for (int yPos = 0; yPos < 7; yPos++) {
                    /* current location has a room- randomly place new room(s) adjacent to it */
                    if (roomArray[yPos][xPos] != null) {
                        if (yPos != 0) {
                            randomInteger = randomGenerator.nextInt(2);
                            if ((randomInteger == 1) && (roomArray[yPos - 1][xPos] == null) && (checkAdjacent(yPos - 1, xPos) <= 1)) {
                                /* place room above current */
                                roomArray[yPos - 1][xPos] = new Room();
                                roomArray[yPos - 1][xPos].downDoor = true;
                                roomArray[yPos][xPos].upDoor = true;
                                currentRooms += 1;
                            }
                        }
                        if (xPos != 6) {
                            randomInteger = randomGenerator.nextInt(2);
                            if ((randomInteger == 1) && (roomArray[yPos][xPos + 1] == null) && (checkAdjacent(yPos, xPos + 1) <= 1)) {
                                /* place room right of current */
                                roomArray[yPos][xPos + 1] = new Room();
                                roomArray[yPos][xPos + 1].leftDoor = true;
                                roomArray[yPos][xPos].rightDoor = true;
                                currentRooms += 1;
                            }
                        }
                        if (yPos != 6) {
                            randomInteger = randomGenerator.nextInt(2);
                            if ((randomInteger == 1) && (roomArray[yPos + 1][xPos] == null) && (checkAdjacent(yPos + 1, xPos) <= 1)) {
                                /* place room below current */
                                roomArray[yPos + 1][xPos] = new Room();
                                roomArray[yPos + 1][xPos].upDoor = true;
                                roomArray[yPos][xPos].downDoor = true;
                                currentRooms += 1;
                            }
                        }
                        if (xPos != 0) {
                            randomInteger = randomGenerator.nextInt(2);
                            if ((randomInteger == 1) && (roomArray[yPos][xPos - 1] == null) && (checkAdjacent(yPos, xPos - 1) <= 1)) {
                                /* place room left of current */
                                roomArray[yPos][xPos - 1] = new Room();
                                roomArray[yPos][xPos - 1].rightDoor = true;
                                roomArray[yPos][xPos].leftDoor = true;
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
                if ((roomArray[yPos][xPos] != null) && (roomArray[yPos][xPos].roomType == "") && (checkAdjacent(yPos, xPos) == 1)) {
                    roomArray[yPos][xPos].roomType = "boss";
                    break;
                }
            }
        }
        /* place our item room */
        for (int xPos = 0; xPos < 7; xPos++) {
            for (int yPos = 0; yPos < 7; yPos++) {
                if ((roomArray[yPos][xPos] != null) && (roomArray[yPos][xPos].roomType == "") && (checkAdjacent(yPos, xPos) == 1)) {
                    roomArray[yPos][xPos].roomType = "item";
                    break;
                }
            }
        }
        /* place our shop room */
        for (int xPos = 0; xPos < 7; xPos++) {
            for (int yPos = 0; yPos < 7; yPos++) {
                if ((roomArray[yPos][xPos] != null) && (roomArray[yPos][xPos].roomType == "") && (checkAdjacent(yPos, xPos) == 1)) {
                    roomArray[yPos][xPos].roomType = "shop";
                    break;
                }
            }
        }
    }
}