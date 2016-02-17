package com.muscovy.game.level;


import com.muscovy.game.entity.MoveableEntity;
import com.muscovy.game.enums.ObjectiveType;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Grouping of parameters to make unique levels.
 */
public class LevelParameters {
	private final int roomsWide;
	private final int roomsHigh;
	private final int maxRooms;
	private final int startX;
	private final int startY;
	private int roomCount;
	private final ObjectiveType objectiveType;
	private final float groundFriction;


	public LevelParameters(int roomsWide, int roomsHigh, int maxRooms, int startX, int startY,
			ObjectiveType objectiveType) {
		this(roomsWide, roomsHigh, maxRooms, startX, startY, objectiveType, MoveableEntity.WORLD_FRICTION);
	}


	public LevelParameters(int roomsWide, int roomsHigh, int maxRooms, int startX, int startY,
			ObjectiveType objectiveType, float groundFriction) {
		this.roomsWide = roomsWide;
		this.roomsHigh = roomsHigh;
		this.maxRooms = maxRooms;
		this.startX = startX;
		this.startY = startY;
		this.objectiveType = objectiveType;
		this.groundFriction = groundFriction;
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
	public float getGroundFriction() {
		return groundFriction;
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
