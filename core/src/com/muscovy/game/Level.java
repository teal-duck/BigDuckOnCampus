package com.muscovy.game;


import com.muscovy.game.enums.LevelType;
import com.muscovy.game.enums.ObjectiveType;


/**
 * Created by ewh502 on 11/01/2016.
 */
public class Level {
	/**
	 * Really simple container class for the room array and objective. Only 1 objective implemented atm, so it's not
	 * used, but yeah
	 */
	private final ObjectiveType objectiveType;
	private final LevelType levelType;
	public DungeonRoom[][] levelArray;
	public boolean[][] visitedRooms;
	private boolean completed = false;

	private final int roomsWide;
	private final int roomsHigh;
	private final int startX;
	private final int startY;
	private final int roomCount;


	public Level(DungeonRoom[][] levelArray, LevelType levelType, LevelParameters levelParameters) {
		this.levelArray = levelArray;
		this.levelType = levelType;

		roomsHigh = levelParameters.getRoomsHigh();
		roomsWide = levelParameters.getRoomsWide();
		startX = levelParameters.getStartX();
		startY = levelParameters.getStartY();
		objectiveType = levelParameters.getObjectiveType();
		roomCount = levelParameters.getRoomCount();

		visitedRooms = new boolean[roomsHigh][roomsWide];
		for (int y = 0; y < visitedRooms.length; y += 1) {
			for (int x = 0; x < visitedRooms[y].length; x += 1) {
				visitedRooms[y][x] = false;
			}
		}
	}


	public boolean areAllEnemiesDead() {
		for (int y = 0; y < levelArray.length; y++) {
			for (int x = 0; x < levelArray[0].length; x++) {
				if ((getRoom(x, y) != null) && !getRoom(x, y).areAllEnemiesDead()) {
					return false;
				}
			}
		}
		return true;
	}


	public boolean areAllRoomsVisited() {
		for (int y = 0; y < levelArray.length; y++) {
			for (int x = 0; x < levelArray[0].length; x++) {
				if ((getRoom(x, y) != null) && !isRoomVisited(x, y)) {
					return false;
				}
			}
		}
		return true;
	}


	public void markRoomVisited(int roomX, int roomY) {
		if (locationInBounds(roomX, roomY)) {
			visitedRooms[roomY][roomX] = true;
		}
	}


	public boolean isRoomVisited(int roomX, int roomY) {
		if (locationInBounds(roomX, roomY)) {
			return visitedRooms[roomY][roomX];
		} else {
			return false;
		}
	}


	public boolean isRoomNeighbourVisited(int roomX, int roomY) {
		return (isLeftNeighbourVisited(roomX, roomY) //
				|| isRightNeighbourVisited(roomX, roomY) //
				|| isUpNeighbourVisited(roomX, roomY) //
				|| isDownNeighbourVisited(roomX, roomY));
	}


	public boolean isLeftNeighbourVisited(int roomX, int roomY) {
		return ((roomX > 0) && isRoomVisited(roomX - 1, roomY));
	}


	public boolean isRightNeighbourVisited(int roomX, int roomY) {
		return ((roomX < (roomsWide - 1)) && isRoomVisited(roomX + 1, roomY));
	}


	public boolean isUpNeighbourVisited(int roomX, int roomY) {
		return ((roomY > 0) && isRoomVisited(roomX, roomY - 1));
	}


	public boolean isDownNeighbourVisited(int roomX, int roomY) {
		return ((roomY < (roomsHigh - 1)) && isRoomVisited(roomX, roomY + 1));
	}


	public DungeonRoom getLeftRoom(int roomX, int roomY) {
		return ((roomX > 0) ? getRoom(roomX - 1, roomY) : null);
	}


	public DungeonRoom getRightRoom(int roomX, int roomY) {
		return ((roomX < (roomsWide - 1)) ? getRoom(roomX + 1, roomY) : null);
	}


	public DungeonRoom getUpRoom(int roomX, int roomY) {
		return ((roomY > 0) ? getRoom(roomX, roomY - 1) : null);
	}


	public DungeonRoom getDownRoom(int roomX, int roomY) {
		return ((roomY < (roomsHigh - 1)) ? getRoom(roomX, roomY + 1) : null);
	}


	public boolean locationInBounds(int roomX, int roomsY) {
		return ((roomX >= 0) && (roomX < roomsWide) && (roomsY >= 0) && (roomsY < roomsHigh));
	}


	public DungeonRoom getRoom(int roomX, int roomY) {
		if (locationInBounds(roomX, roomY)) {
			return levelArray[roomY][roomX];
		} else {
			return null;
		}
	}


	public DungeonRoom[][] getLevelArray() {
		return levelArray;
	}


	public boolean isCompleted() {
		return completed;
	}


	public void setCompleted(boolean completed) {
		this.completed = completed;
	}


	public LevelType getLevel() {
		return levelType;
	}


	public ObjectiveType getObjectiveType() {
		return objectiveType;
	}


	public int getRoomsWide() {
		return roomsWide;
	}


	public int getRoomsHigh() {
		return roomsHigh;
	}


	public int getStartX() {
		return startX;
	}


	public int getStartY() {
		return startY;
	}


	public int getRoomCount() {
		return roomCount;
	}
}
