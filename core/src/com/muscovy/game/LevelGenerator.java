package com.muscovy.game;


import java.util.Random;

import com.muscovy.game.enums.RoomType;


/**
 * Created by Thomas on 04/12/2015.
 */

public class LevelGenerator {
	/**
	 * As 2d arrays are indexed {{row1}{row2}{row3}...}, we want to refer to locations in this format:
	 * dungeonRoomArray[yPosition][xPosition]
	 */

	// TODO: LevelGenerator magic numbers
	// I think the levels are 7x7
	// private final int levelWidth = 7;

	/* Count the number of rooms adjacent to the given location */
	public int checkAdjacent(int myY, int myX, DungeonRoom[][] dungeonRoomArray) {
		int numAdjacent = 0;
		/* check north */
		if ((myY > 0) && (dungeonRoomArray[myY - 1][myX] != null)) {
			numAdjacent++;
		}
		/* check east */
		if ((myX < 6) && (dungeonRoomArray[myY][myX + 1] != null)) {
			numAdjacent++;
		}
		/* check south */
		if ((myY < 6) && (dungeonRoomArray[myY + 1][myX] != null)) {
			numAdjacent++;
		}
		/* check west */
		if ((myX > 0) && (dungeonRoomArray[myY][myX - 1] != null)) {
			numAdjacent++;
		}

		/*
		 * int numAdjacent = dungeonRoomArray[myY][myX].upDoor + dungeonRoomArray[myY][myX].rightDoor +
		 * dungeonRoomArray[myY][myX].downDoor + dungeonRoomArray[myY][myX].leftDoor;
		 */

		return numAdjacent;
	}


	/* function is called to generate the array of rooms for our building */
	public DungeonRoom[][] generateBuilding(int maxRooms, int level) {
		// Texture texture;
		boolean bossSet = false, itemSet = false, shopSet = false;
		DungeonRoom[][] dungeonRoomArray = new DungeonRoom[7][7];
		/* initialise starting room in the centre */
		dungeonRoomArray[3][3] = new DungeonRoom();
		dungeonRoomArray[3][3].setRoomType(RoomType.START);

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
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos - 1][xPos] == null)
									&& (checkAdjacent(yPos - 1, xPos,
											dungeonRoomArray) <= 1)) {
								/* place room above current */
								dungeonRoomArray[yPos - 1][xPos] = new DungeonRoom();
								dungeonRoomArray[yPos - 1][xPos].setDownDoor(true);
								dungeonRoomArray[yPos][xPos].setUpDoor(true);
								currentRooms += 1;
							}
						}
						if (xPos != 6) {
							randomInteger = randomGenerator.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos][xPos + 1] == null)
									&& (checkAdjacent(yPos, xPos + 1,
											dungeonRoomArray) <= 1)) {
								/* place room right of current */
								dungeonRoomArray[yPos][xPos + 1] = new DungeonRoom();
								dungeonRoomArray[yPos][xPos + 1].setLeftDoor(true);
								dungeonRoomArray[yPos][xPos].setRightDoor(true);
								currentRooms += 1;
							}
						}
						if (yPos != 6) {
							randomInteger = randomGenerator.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos + 1][xPos] == null)
									&& (checkAdjacent(yPos + 1, xPos,
											dungeonRoomArray) <= 1)) {
								/* place room below current */
								dungeonRoomArray[yPos + 1][xPos] = new DungeonRoom();
								dungeonRoomArray[yPos + 1][xPos].setUpDoor(true);
								dungeonRoomArray[yPos][xPos].setDownDoor(true);
								currentRooms += 1;
							}
						}
						if (xPos != 0) {
							randomInteger = randomGenerator.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos][xPos - 1] == null)
									&& (checkAdjacent(yPos, xPos - 1,
											dungeonRoomArray) <= 1)) {
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
		/* These are quite naive approaches atm, but they work! */

		// TODO: Move break in room placement loops

		/* place our boss room */
		for (int xPos = 0; xPos < 7; xPos++) {
			for (int yPos = 0; yPos < 7; yPos++) {
				if (!bossSet) {
					if ((dungeonRoomArray[yPos][xPos] != null)
							&& (dungeonRoomArray[yPos][xPos]
									.getRoomType() == RoomType.NORMAL)
							&& (checkAdjacent(yPos, xPos, dungeonRoomArray) == 1)) {
						dungeonRoomArray[yPos][xPos].setRoomType(RoomType.BOSS);
						bossSet = true;
						break;
					}
				}
			}
		}

		/* place our item room */
		for (int xPos = 0; xPos < 7; xPos++) {
			for (int yPos = 0; yPos < 7; yPos++) {
				if (!itemSet) {
					if ((dungeonRoomArray[yPos][xPos] != null)
							&& (dungeonRoomArray[yPos][xPos]
									.getRoomType() == RoomType.NORMAL)
							&& (checkAdjacent(yPos, xPos, dungeonRoomArray) == 1)) {
						dungeonRoomArray[yPos][xPos].setRoomType(RoomType.ITEM);
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
					if ((dungeonRoomArray[yPos][xPos] != null)
							&& (dungeonRoomArray[yPos][xPos]
									.getRoomType() == RoomType.NORMAL)
							&& (checkAdjacent(yPos, xPos, dungeonRoomArray) == 1)) {
						dungeonRoomArray[yPos][xPos].setRoomType(RoomType.SHOP);
						shopSet = true;
						break;
					}
				}
			}
		}

		for (int xPos = 0; xPos < 7; xPos++) {
			for (int yPos = 0; yPos < 7; yPos++) {
				if (dungeonRoomArray[yPos][xPos] != null) {
					dungeonRoomArray[yPos][xPos].generateRoom(level);
				}
			}
		}

		return dungeonRoomArray;
	}
}