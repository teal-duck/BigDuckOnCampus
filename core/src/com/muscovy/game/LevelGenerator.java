package com.muscovy.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.enums.LevelType;
import com.muscovy.game.enums.RoomType;


/**
 * Created by Thomas on 04/12/2015.
 */

public class LevelGenerator {
	public static final int DUNGEON_ROOMS_WIDE = 7;
	public static final int DUNGEON_ROOMS_HIGH = 7;
	public static final int START_ROOM_X = 3;
	public static final int START_ROOM_Y = 3;


	/**
	 * As 2d arrays are indexed {{row1}{row2}{row3}...}, we want to refer to locations in this format:
	 * dungeonRoomArray[yPosition][xPosition]
	 */

	/* Count the number of rooms adjacent to the given location */
	public int checkAdjacent(int myY, int myX, DungeonRoom[][] dungeonRoomArray) {
		int numAdjacent = 0;
		/* check north */
		if ((myY > 0) && (dungeonRoomArray[myY - 1][myX] != null)) {
			numAdjacent++;
		}
		/* check east */
		if ((myX < (LevelGenerator.DUNGEON_ROOMS_WIDE - 1)) && (dungeonRoomArray[myY][myX + 1] != null)) {
			numAdjacent++;
		}
		/* check south */
		if ((myY < (LevelGenerator.DUNGEON_ROOMS_HIGH - 1)) && (dungeonRoomArray[myY + 1][myX] != null)) {
			numAdjacent++;
		}
		/* check west */
		if ((myX > 0) && (dungeonRoomArray[myY][myX - 1] != null)) {
			numAdjacent++;
		}

		return numAdjacent;
	}


	/* function is called to generate the array of rooms for our building */
	public DungeonRoom[][] generateBuilding(TextureMap textureMap, int maxRooms, LevelType level) {
		// Texture texture;
		boolean bossSet = false;
		boolean itemSet = false;
		boolean shopSet = false;

		DungeonRoom[][] dungeonRoomArray = new DungeonRoom[LevelGenerator.DUNGEON_ROOMS_HIGH][LevelGenerator.DUNGEON_ROOMS_WIDE];
		/* initialise starting room in the centre */
		dungeonRoomArray[LevelGenerator.START_ROOM_Y][LevelGenerator.START_ROOM_X] = new DungeonRoom(
				textureMap);
		dungeonRoomArray[LevelGenerator.START_ROOM_Y][LevelGenerator.START_ROOM_X].setRoomType(RoomType.START);

		/* initialise counter for current number of rooms */
		int currentRooms = 1;

		Random randomGenerator = new Random();
		int randomInteger = 0;

		/* continue until we have the required number of rooms +/- 1 */
		while (currentRooms < maxRooms) {
			for (int xPos = 0; xPos < LevelGenerator.DUNGEON_ROOMS_WIDE; xPos++) {
				for (int yPos = 0; yPos < LevelGenerator.DUNGEON_ROOMS_HIGH; yPos++) {
					/* current location has a room- randomly place new room(s) adjacent to it */
					if (dungeonRoomArray[yPos][xPos] != null) {
						if (yPos != 0) {
							randomInteger = randomGenerator.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos - 1][xPos] == null)
									&& (checkAdjacent(yPos - 1, xPos,
											dungeonRoomArray) <= 1)) {
								/* place room above current */
								dungeonRoomArray[yPos - 1][xPos] = new DungeonRoom(
										textureMap);
								dungeonRoomArray[yPos - 1][xPos].setDownDoor(true);
								dungeonRoomArray[yPos][xPos].setUpDoor(true);
								currentRooms += 1;
							}
						}

						if (xPos != (LevelGenerator.DUNGEON_ROOMS_WIDE - 1)) {
							randomInteger = randomGenerator.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos][xPos + 1] == null)
									&& (checkAdjacent(yPos, xPos + 1,
											dungeonRoomArray) <= 1)) {
								/* place room right of current */
								dungeonRoomArray[yPos][xPos + 1] = new DungeonRoom(
										textureMap);
								dungeonRoomArray[yPos][xPos + 1].setLeftDoor(true);
								dungeonRoomArray[yPos][xPos].setRightDoor(true);
								currentRooms += 1;
							}
						}

						if (yPos != (LevelGenerator.DUNGEON_ROOMS_HIGH - 1)) {
							randomInteger = randomGenerator.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos + 1][xPos] == null)
									&& (checkAdjacent(yPos + 1, xPos,
											dungeonRoomArray) <= 1)) {
								/* place room below current */
								dungeonRoomArray[yPos + 1][xPos] = new DungeonRoom(
										textureMap);
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
								dungeonRoomArray[yPos][xPos - 1] = new DungeonRoom(
										textureMap);
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

		/* place our boss room */
		ArrayList<Vector2> potentialBossRooms = new ArrayList<Vector2>();

		for (int xPos = 0; xPos < LevelGenerator.DUNGEON_ROOMS_WIDE; xPos++) {
			for (int yPos = 0; yPos < LevelGenerator.DUNGEON_ROOMS_HIGH; yPos++) {
				if (!bossSet) {
					if ((dungeonRoomArray[yPos][xPos] != null)
							&& (dungeonRoomArray[yPos][xPos]
									.getRoomType() == RoomType.NORMAL)
							&& (checkAdjacent(yPos, xPos, dungeonRoomArray) == 1)) {
						// dungeonRoomArray[yPos][xPos].setRoomType(RoomType.BOSS);
						// bossSet = true;
						// break;
						potentialBossRooms.add(new Vector2(xPos, yPos));
					}
				}
			}
		}

		if (potentialBossRooms.size() > 0) {
			int randomIndex = randomGenerator.nextInt(potentialBossRooms.size());
			Vector2 randomPosition = potentialBossRooms.get(randomIndex);
			int x = (int) randomPosition.x;
			int y = (int) randomPosition.y;
			dungeonRoomArray[y][x].setRoomType(RoomType.BOSS);
			// System.out.println("On level " + level.toString() + ", adding boss room at " +
			// randomPosition);
		} else {
			// System.out.println("No boss room");
		}

		/* place our item room */
		for (int xPos = 0; xPos < LevelGenerator.DUNGEON_ROOMS_WIDE; xPos++) {
			for (int yPos = 0; yPos < LevelGenerator.DUNGEON_ROOMS_HIGH; yPos++) {
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
		for (int xPos = 0; xPos < LevelGenerator.DUNGEON_ROOMS_WIDE; xPos++) {
			for (int yPos = 0; yPos < LevelGenerator.DUNGEON_ROOMS_HIGH; yPos++) {
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

		for (int xPos = 0; xPos < LevelGenerator.DUNGEON_ROOMS_WIDE; xPos++) {
			for (int yPos = 0; yPos < LevelGenerator.DUNGEON_ROOMS_HIGH; yPos++) {
				if (dungeonRoomArray[yPos][xPos] != null) {
					dungeonRoomArray[yPos][xPos].generateRoom(level);
				}
			}
		}

		return dungeonRoomArray;
	}
}