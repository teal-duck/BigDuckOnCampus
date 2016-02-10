package com.muscovy.game.level;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;
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

	/**
	 * Count the number of rooms adjacent to the given location
	 *
	 * @param dungeonRoomArray
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public static int numberOfAdjacentRooms(DungeonRoom[][] dungeonRoomArray, int roomX, int roomY) {
		int numAdjacent = 0;

		if ((roomY > 0) && (dungeonRoomArray[roomY - 1][roomX] != null)) {
			numAdjacent++;
		}

		if ((roomX < (dungeonRoomArray.length - 1)) && (dungeonRoomArray[roomY][roomX + 1] != null)) {
			numAdjacent++;
		}

		if ((roomY < (dungeonRoomArray[0].length - 1)) && (dungeonRoomArray[roomY + 1][roomX] != null)) {
			numAdjacent++;
		}

		if ((roomX > 0) && (dungeonRoomArray[roomY][roomX - 1] != null)) {
			numAdjacent++;
		}

		return numAdjacent;
	}


	/**
	 * Picks a random room that has less neighbours than maxNeighbours, then sets its type to roomType.
	 *
	 * @param dungeonRoomArray
	 * @param roomsWide
	 * @param roomsHigh
	 * @param roomType
	 * @param maxNeighbours
	 * @param random
	 * @param levelType
	 * @return
	 */
	private static DungeonRoom[][] setRandomRoomTo(DungeonRoom[][] dungeonRoomArray, int roomsWide, int roomsHigh,
			RoomType roomType, int maxNeighbours, Random random, LevelType levelType) {
		ArrayList<Vector2> potentialRooms = new ArrayList<Vector2>();

		for (int xPos = 0; xPos < roomsWide; xPos++) {
			for (int yPos = 0; yPos < roomsHigh; yPos++) {
				if ((dungeonRoomArray[yPos][xPos] != null)
						&& (dungeonRoomArray[yPos][xPos].getRoomType() == RoomType.NORMAL)
						&& (LevelGenerator.numberOfAdjacentRooms(dungeonRoomArray, xPos,
								yPos) <= maxNeighbours)) {

					potentialRooms.add(new Vector2(xPos, yPos));
				}

			}
		}

		if (potentialRooms.size() > 0) {
			int randomIndex = random.nextInt(potentialRooms.size());
			Vector2 randomPosition = potentialRooms.get(randomIndex);
			int x = (int) randomPosition.x;
			int y = (int) randomPosition.y;
			dungeonRoomArray[y][x].setRoomType(roomType);
		} else {
			System.out.println("No space to place room of type " + roomType + " in level " + levelType);
		}

		return dungeonRoomArray;
	}


	/**
	 * Function is called to generate the array of rooms for our building
	 *
	 * @param game
	 * @param levelType
	 * @param levelParameters
	 * @return
	 */
	public static DungeonRoom[][] generateBuilding(MuscovyGame game, LevelType levelType,
			LevelParameters levelParameters) {
		int maxRooms = levelParameters.getMaxRooms();
		int roomsWide = levelParameters.getRoomsWide();
		int roomsHigh = levelParameters.getRoomsHigh();
		int startX = levelParameters.getStartX();
		int startY = levelParameters.getStartY();

		DungeonRoom[][] dungeonRoomArray = new DungeonRoom[roomsHigh][roomsWide];
		DungeonRoom startRoom = new DungeonRoom(game);
		startRoom.setRoomType(RoomType.START);
		dungeonRoomArray[startY][startX] = startRoom;

		int currentRooms = 1;
		int randomInteger = 0;
		Random random = game.getRandom();

		/* continue until we have the required number of rooms +/- 1 */
		while (currentRooms < maxRooms) {
			for (int xPos = 0; xPos < roomsWide; xPos++) {
				for (int yPos = 0; yPos < roomsHigh; yPos++) {
					/* current location has a room- randomly place new room(s) adjacent to it */
					if (dungeonRoomArray[yPos][xPos] != null) {
						if (yPos != 0) {
							randomInteger = random.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos - 1][xPos] == null)
									&& (LevelGenerator.numberOfAdjacentRooms(
											dungeonRoomArray, xPos,
											yPos - 1) <= 1)) {
								/* place room above current */
								dungeonRoomArray[yPos - 1][xPos] = new DungeonRoom(
										game);
								dungeonRoomArray[yPos - 1][xPos].setHasDownDoor(true);
								dungeonRoomArray[yPos][xPos].setHasUpDoor(true);
								currentRooms += 1;
							}
						}

						if (xPos != (roomsWide - 1)) {
							randomInteger = random.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos][xPos + 1] == null)
									&& (LevelGenerator.numberOfAdjacentRooms(
											dungeonRoomArray, xPos + 1,
											yPos) <= 1)) {
								/* place room right of current */
								dungeonRoomArray[yPos][xPos + 1] = new DungeonRoom(
										game);
								dungeonRoomArray[yPos][xPos + 1].setHasLeftDoor(true);
								dungeonRoomArray[yPos][xPos].setHasRightDoor(true);
								currentRooms += 1;
							}
						}

						if (yPos != (roomsHigh - 1)) {
							randomInteger = random.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos + 1][xPos] == null)
									&& (LevelGenerator.numberOfAdjacentRooms(
											dungeonRoomArray, xPos,
											yPos + 1) <= 1)) {
								/* place room below current */
								dungeonRoomArray[yPos + 1][xPos] = new DungeonRoom(
										game);
								dungeonRoomArray[yPos + 1][xPos].setHasUpDoor(true);
								dungeonRoomArray[yPos][xPos].setHasDownDoor(true);
								currentRooms += 1;
							}
						}

						if (xPos != 0) {
							randomInteger = random.nextInt(2);
							if ((randomInteger == 1)
									&& (dungeonRoomArray[yPos][xPos - 1] == null)
									&& (LevelGenerator.numberOfAdjacentRooms(
											dungeonRoomArray, xPos - 1,
											yPos) <= 1)) {
								/* place room left of current */
								dungeonRoomArray[yPos][xPos - 1] = new DungeonRoom(
										game);
								dungeonRoomArray[yPos][xPos - 1].setHasRightDoor(true);
								dungeonRoomArray[yPos][xPos].setHasLeftDoor(true);
								currentRooms += 1;
							}
						}
					}
				}
			}
		}

		// Place the boss room if the objective wants it
		if (levelParameters.getObjectiveType() == ObjectiveType.BOSS) {
			LevelGenerator.setRandomRoomTo(dungeonRoomArray, roomsWide, roomsHigh, RoomType.BOSS, 1, random,
					levelType);
		}

		// LevelGenerator.setRandomRoomTo(dungeonRoomArray, roomsWide, roomsHigh, RoomType.ITEM, 1, random,
		// levelType);
		// LevelGenerator.setRandomRoomTo(dungeonRoomArray, roomsWide, roomsHigh, RoomType.SHOP, 1, random,
		// levelType);

		levelParameters.setRoomCount(currentRooms);
		return dungeonRoomArray;
	}


	/**
	 * @param dungeonRoomArray
	 * @param levelType
	 * @param templateLoader
	 */
	public static void generateRoomContents(DungeonRoom[][] dungeonRoomArray, LevelType levelType,
			DungeonRoomTemplateLoader templateLoader) {
		int roomsHigh = dungeonRoomArray.length;
		int roomsWide = dungeonRoomArray[0].length;
		for (int yPos = 0; yPos < roomsHigh; yPos++) {
			for (int xPos = 0; xPos < roomsWide; xPos++) {
				if (dungeonRoomArray[yPos][xPos] != null) {
					dungeonRoomArray[yPos][xPos].generateRoom(levelType, templateLoader);
				}
			}
		}
	}


	private LevelGenerator() {
	}
}