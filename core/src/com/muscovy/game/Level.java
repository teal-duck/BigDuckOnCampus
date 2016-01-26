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
	public DungeonRoom[][] levelArray;
	public boolean[][] visitedRooms;
	private ObjectiveType objective;
	private LevelType level;
	private boolean completed = false;

	private int roomsWide = LevelGenerator.DUNGEON_ROOMS_WIDE;
	private int roomsHigh = LevelGenerator.DUNGEON_ROOMS_HIGH;


	public Level(DungeonRoom[][] levelArray, ObjectiveType objective, LevelType level) {
		this.levelArray = new DungeonRoom[roomsHigh][roomsWide];
		visitedRooms = new boolean[roomsHigh][roomsWide];
		this.levelArray = levelArray;
		this.objective = objective;
		this.level = level;

		for (int y = 0; y < visitedRooms.length; y += 1) {
			for (int x = 0; x < visitedRooms[y].length; x += 1) {
				visitedRooms[y][x] = false;
			}
		}
	}


	public void markRoomVisited(int roomX, int roomY) {
		visitedRooms[roomY][roomX] = true;
	}


	public boolean isRoomVisited(int roomX, int roomY) {
		return visitedRooms[roomY][roomX];
	}


	public boolean isRoomNeighbourVisited(int roomX, int roomY) {
		if ((roomX > 0) && isRoomVisited(roomX - 1, roomY)) {
			return true;
		} else if ((roomX < (roomsWide - 1)) && isRoomVisited(roomX + 1, roomY)) {
			return true;
		} else if ((roomY > 0) && isRoomVisited(roomX, roomY - 1)) {
			return true;
		} else if ((roomY < (roomsHigh - 1)) && isRoomVisited(roomX, roomY + 1)) {
			return true;
		}

		return false;
	}


	public int getRoomsWide() {
		return roomsWide;
	}


	public int getRoomsHigh() {
		return roomsHigh;
	}


	public DungeonRoom getRoom(int roomX, int roomY) {
		return levelArray[roomY][roomX];
	}


	public DungeonRoom[][] getLevelArray() {
		return levelArray;
	}


	public void setLevelArray(DungeonRoom[][] levelArray) {
		this.levelArray = levelArray;
	}


	public boolean isCompleted() {
		return completed;
	}


	public void setCompleted(boolean completed) {
		this.completed = completed;
	}


	public LevelType getLevel() {
		return level;
	}


	public void setLevel(LevelType level) {
		this.level = level;
	}


	public ObjectiveType getObjective() {
		return objective;
	}


	public void setObjective(ObjectiveType objective) {
		this.objective = objective;
	}
}
