package com.muscovy.game.level;


import com.muscovy.game.enums.ObjectiveType;


/**
 *
 */
public class LevelParameters {
	private final int roomsWide;
	private final int roomsHigh;
	private final int maxRooms;
	private final int startX;
	private final int startY;
	private int roomCount;
	private final ObjectiveType objectiveType;


	public LevelParameters(int roomsWide, int roomsHigh, int maxRooms, int startX, int startY,
			ObjectiveType objectiveType) {
		this.roomsWide = roomsWide;
		this.roomsHigh = roomsHigh;
		this.maxRooms = maxRooms;
		this.startX = startX;
		this.startY = startY;
		this.objectiveType = objectiveType;
		roomCount = 0;
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
	public int getMaxRooms() {
		return maxRooms;
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
	public ObjectiveType getObjectiveType() {
		return objectiveType;
	}


	/**
	 * @return
	 */
	public int getRoomCount() {
		return roomCount;
	}


	/**
	 * @param roomCount
	 */
	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}


	/**
	 * @return
	 */
	public String toReadString() {
		return "LevelParameters(roomsWide=" + roomsWide + ", roomsHigh=" + roomsHigh + ", maxRooms=" + maxRooms
				+ ", startX=" + startX + ", startY=" + startY + ", objectiveType="
				+ objectiveType.toString() + ")";
	}


	@Override
	public String toString() {
		return "LevelParameters(" + roomsWide + ", " + roomsHigh + ", " + maxRooms + ", " + startX + ", "
				+ startY + ", " + objectiveType.toString() + ")";
	}
}
