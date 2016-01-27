package com.muscovy.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.enums.LevelType;
import com.muscovy.game.enums.ObjectiveType;
import com.muscovy.game.enums.RoomType;


/**
 * Created by Thomas on 04/12/2015.
 */

public class LevelGenerator {

	/**
	 * As 2d arrays are indexed {{row1}{row2}{row3}...}, we want to refer to locations in this format:
	 * dungeonRoomArray[yPosition][xPosition]
	 */

	/* Count the number of rooms adjacent to the given location */
	public int checkAdjacent(DungeonRoom[][] dungeonRoomArray, int roomX, int roomY) {
		int numAdjacent = 0;
		/* check north */
		if ((roomY > 0) && (dungeonRoomArray[roomY - 1][roomX] != null)) {
			numAdjacent++;
		}
		/* check east */
		if ((roomX < (dungeonRoomArray.length - 1)) && (dungeonRoomArray[roomY][roomX + 1] != null)) {
			numAdjacent++;
		}
		/* check south */
		if ((roomY < (dungeonRoomArray[0].length - 1)) && (dungeonRoomArray[roomY + 1][roomX] != null)) {
			numAdjacent++;
		}
		/* check west */
		if ((roomX > 0) && (dungeonRoomArray[roomY][roomX - 1] != null)) {
			numAdjacent++;
		}

		return numAdjacent;
	}


	/* function is called to generate the array of rooms for our building */
	public DungeonRoom[][] generateBuilding(TextureMap textureMap, DungeonRoomTemplateLoader templateLoader,
			LevelType levelType, LevelParameters levelParameters) {
		int maxRooms = levelParameters.getMaxRooms();
		int roomsWide = levelParameters.getRoomsWide();
		int roomsHigh = levelParameters.getRoomsHigh();
		int startX = levelParameters.getStartX();
		int startY = levelParameters.getStartY();

		boolean bossSet = false;
		boolean itemSet = false;
		boolean shopSet = false;

		DungeonRoom[][] dungeonRoomArray = new DungeonRoom[roomsHigh][roomsWide];
		/* initialise starting room in the centre */
		DungeonRoom startRoom = new DungeonRoom(textureMap);
		startRoom.setRoomType(RoomType.START);
		dungeonRoomArray[startY][startX] = startRoom;

		/* initialise counter for current number of rooms */
		int currentRooms = 1;

		Random randomGenerator = new Random();
		int randomInteger = 0;

		/* continue until we have the required number of rooms +/- 1 */
		while (currentRooms < maxRooms) {
			for (int xPos = 0; xPos < roomsWide; xPos++) {
				for (int yPos = 0; yPos < roomsHigh; yPos++) {
					/* current location has a room- randomly place new room(s) adjacent to it */
					if (dungeonRoomArray[yPos][xPos] != null) {
						if (yPos != 0) {
							randomInteger = randomGenerator.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos - 1][xPos] == null)
									&& (checkAdjacent(dungeonRoomArray, xPos,
											yPos - 1) <= 1)) {
								/* place room above current */
								dungeonRoomArray[yPos - 1][xPos] = new DungeonRoom(
										textureMap);
								dungeonRoomArray[yPos - 1][xPos].setDownDoor(true);
								dungeonRoomArray[yPos][xPos].setUpDoor(true);
								currentRooms += 1;
							}
						}

						if (xPos != (roomsWide - 1)) {
							randomInteger = randomGenerator.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos][xPos + 1] == null)
									&& (checkAdjacent(dungeonRoomArray, xPos + 1,
											yPos) <= 1)) {
								/* place room right of current */
								dungeonRoomArray[yPos][xPos + 1] = new DungeonRoom(
										textureMap);
								dungeonRoomArray[yPos][xPos + 1].setLeftDoor(true);
								dungeonRoomArray[yPos][xPos].setRightDoor(true);
								currentRooms += 1;
							}
						}

						if (yPos != (roomsHigh - 1)) {
							randomInteger = randomGenerator.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos + 1][xPos] == null)
									&& (checkAdjacent(dungeonRoomArray, xPos,
											yPos + 1) <= 1)) {
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
									&& (checkAdjacent(dungeonRoomArray, xPos - 1,
											yPos) <= 1)) {
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
		if (levelParameters.getObjectiveType() == ObjectiveType.BOSS) {
			ArrayList<Vector2> potentialBossRooms = new ArrayList<Vector2>();
			for (int xPos = 0; xPos < roomsWide; xPos++) {
				for (int yPos = 0; yPos < roomsHigh; yPos++) {
					if (!bossSet) {
						if ((dungeonRoomArray[yPos][xPos] != null)
								&& (dungeonRoomArray[yPos][xPos]
										.getRoomType() == RoomType.NORMAL)
								&& (checkAdjacent(dungeonRoomArray, xPos, yPos) == 1)) {

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
			} else {
				System.out.println("No boss room");
			}
		}

		/* place our item room */
		for (int xPos = 0; xPos < roomsWide; xPos++) {
			for (int yPos = 0; yPos < roomsHigh; yPos++) {
				if (!itemSet) {
					if ((dungeonRoomArray[yPos][xPos] != null)
							&& (dungeonRoomArray[yPos][xPos]
									.getRoomType() == RoomType.NORMAL)
							&& (checkAdjacent(dungeonRoomArray, xPos, yPos) == 1)) {
						dungeonRoomArray[yPos][xPos].setRoomType(RoomType.ITEM);
						itemSet = true;
						break;
					}
				}
			}
		}
		/* place our shop room */
		for (int xPos = 0; xPos < roomsWide; xPos++) {
			for (int yPos = 0; yPos < roomsHigh; yPos++) {
				if (!shopSet) {
					if ((dungeonRoomArray[yPos][xPos] != null)
							&& (dungeonRoomArray[yPos][xPos]
									.getRoomType() == RoomType.NORMAL)
							&& (checkAdjacent(dungeonRoomArray, xPos, xPos) == 1)) {
						dungeonRoomArray[yPos][xPos].setRoomType(RoomType.SHOP);
						shopSet = true;
						break;
					}
				}
			}
		}

		// Generate the contents inside each room
		for (int xPos = 0; xPos < roomsWide; xPos++) {
			for (int yPos = 0; yPos < roomsHigh; yPos++) {
				if (dungeonRoomArray[yPos][xPos] != null) {
					dungeonRoomArray[yPos][xPos].generateRoom(templateLoader, levelType);
				}
			}
		}

		return dungeonRoomArray;
	}
}