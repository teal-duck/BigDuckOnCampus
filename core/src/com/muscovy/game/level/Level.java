package com.muscovy.game.level;


import com.muscovy.game.enums.LevelType;
import com.muscovy.game.enums.ObjectiveType;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * Modified class: Helper functions and keeping track of visited rooms.
 */
public class Level {
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
	private float groundFriction;


	public Level(DungeonRoom[][] levelArray, LevelType levelType, LevelParameters levelParameters) {
		this.levelArray = levelArray;
		this.levelType = levelType;

		roomsHigh = levelParameters.getRoomsHigh();
		roomsWide = levelParameters.getRoomsWide();
		startX = levelParameters.getStartX();
		startY = levelParameters.getStartY();
		objectiveType = levelParameters.getObjectiveType();
		roomCount = levelParameters.getRoomCount();
		groundFriction = levelParameters.getGroundFriction();

		visitedRooms = new boolean[roomsHigh][roomsWide];
		for (int y = 0; y < visitedRooms.length; y += 1) {
			for (int x = 0; x < visitedRooms[y].length; x += 1) {
				visitedRooms[y][x] = false;
			}
		}
	}


	/**
	 * @return
	 */
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


	/**
	 * @return
	 */
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


	/**
	 * @param roomX
	 * @param roomY
	 */
	public void markRoomVisited(int roomX, int roomY) {
		if (locationInBounds(roomX, roomY)) {
			visitedRooms[roomY][roomX] = true;
		}
	}


	/**
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public boolean isRoomVisited(int roomX, int roomY) {
		if (locationInBounds(roomX, roomY)) {
			return visitedRooms[roomY][roomX];
		} else {
			return false;
		}
	}


	/**
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public boolean isRoomNeighbourVisited(int roomX, int roomY) {
		return (isLeftNeighbourVisited(roomX, roomY) //
				|| isRightNeighbourVisited(roomX, roomY) //
				|| isUpNeighbourVisited(roomX, roomY) //
				|| isDownNeighbourVisited(roomX, roomY));
	}


	/**
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public boolean isLeftNeighbourVisited(int roomX, int roomY) {
		return ((roomX > 0) && isRoomVisited(roomX - 1, roomY));
	}


	/**
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public boolean isRightNeighbourVisited(int roomX, int roomY) {
		return ((roomX < (roomsWide - 1)) && isRoomVisited(roomX + 1, roomY));
	}


	/**
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public boolean isUpNeighbourVisited(int roomX, int roomY) {
		return ((roomY > 0) && isRoomVisited(roomX, roomY - 1));
	}


	/**
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public boolean isDownNeighbourVisited(int roomX, int roomY) {
		return ((roomY < (roomsHigh - 1)) && isRoomVisited(roomX, roomY + 1));
	}


	/**
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public DungeonRoom getLeftRoom(int roomX, int roomY) {
		return ((roomX > 0) ? getRoom(roomX - 1, roomY) : null);
	}


	/**
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public DungeonRoom getRightRoom(int roomX, int roomY) {
		return ((roomX < (roomsWide - 1)) ? getRoom(roomX + 1, roomY) : null);
	}


	/**
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public DungeonRoom getUpRoom(int roomX, int roomY) {
		return ((roomY > 0) ? getRoom(roomX, roomY - 1) : null);
	}


	/**
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public DungeonRoom getDownRoom(int roomX, int roomY) {
		return ((roomY < (roomsHigh - 1)) ? getRoom(roomX, roomY + 1) : null);
	}


	/**
	 * @param roomX
	 * @param roomsY
	 * @return
	 */
	public boolean locationInBounds(int roomX, int roomsY) {
		return ((roomX >= 0) && (roomX < roomsWide) && (roomsY >= 0) && (roomsY < roomsHigh));
	}


	/**
	 * Returns null if the room is null or out of bounds.
	 *
	 * @param roomX
	 * @param roomY
	 * @return
	 */
	public DungeonRoom getRoom(int roomX, int roomY) {
		if (locationInBounds(roomX, roomY)) {
			return levelArray[roomY][roomX];
		} else {
			return null;
		}
	}


	/**
	 * @return
	 */
	public DungeonRoom[][] getLevelArray() {
		return levelArray;
	}


	/**
	 * @return
	 */
	public boolean isCompleted() {
		return completed;
	}


	/**
	 * @param completed
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}


	/**
	 * @return
	 */
	public LevelType getLevelType() {
		return levelType;
	}


	/**
	 * @return
	 */
	public ObjectiveType getObjectiveType() {
		return objectiveType;
	}


	/**
	 * @return
	 */
	public int getRoomsWide() {
		return roomsWide;
	}


	/**
	 * @return
	 */
	public int getRoomsHigh() {
		return roomsHigh;
	}


	/**
	 * @return
	 */
	public int getStartX() {
		return startX;
	}


	/**
	 * @return
	 */
	public int getStartY() {
		return startY;
	}


	/**
	 * @return
	 */
	public int getRoomCount() {
		return roomCount;
	}


	/**
	 * @return
	 */
	public float getGroundFriction() {
		return groundFriction;
	}


	/**
	 * @return
	 */
	public String getName() {
		return LevelType.getName(levelType);
	}


	/**
	 * @return
	 */
	public String getObjectiveName() {
		return ObjectiveType.getName(objectiveType);
	}


	/**
	 * @return True if level is underwater (Goodricke)
	 */
	public boolean isUnderwater() {
		if (levelType.equals(LevelType.GOODRICKE)) {
			return true;
		} else {
			return false;
		}
	}
}