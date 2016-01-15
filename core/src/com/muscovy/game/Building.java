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
}

    /* function is called to generate the array of rooms for our building */
