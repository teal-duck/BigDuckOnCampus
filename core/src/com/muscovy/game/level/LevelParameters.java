package com.muscovy.game.level;


import com.muscovy.game.enums.ObjectiveType;


public class LevelParameters {
	private int roomsWide;
	private int roomsHigh;
	private int maxRooms;
	private int startX;
	private int startY;
	private int roomCount;
	private ObjectiveType objectiveType;


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


	public int getRoomsWide() {
		return roomsWide;
	}


	public int getRoomsHigh() {
		return roomsHigh;
	}


	public int getMaxRooms() {
		return maxRooms;
	}


	public int getStartX() {
		return startX;
	}


	public int getStartY() {
		return startY;
	}


	public ObjectiveType getObjectiveType() {
		return objectiveType;
	}


	public int getRoomCount() {
		return roomCount;
	}


	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}


	@Override
	public String toString() {
		return "LevelParameters(" + roomsWide + ", " + roomsHigh + ", " + maxRooms + ", " + startX + ", "
				+ startY + ", " + objectiveType.toString() + ")";
	}


	public String toReadString() {
		return "LevelParameters(roomsWide=" + roomsWide + ", roomsHigh=" + roomsHigh + ", maxRooms=" + maxRooms
				+ ", startX=" + startX + ", startY=" + startY + ", objectiveType="
				+ objectiveType.toString() + ")";
	}
}
