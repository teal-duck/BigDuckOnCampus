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
	private ObjectiveType objective; // 0 = Boss fight, 1 = Find specific item, 3 = Kill certain number of enemies
	private LevelType level; // 0 = Constantine, 1 = Langwith, 2 = Goodricke, 3 = Law and Management, 4 = Catalyst,
					// 5 =
					// TFTV, 6 = Computer Science, 7 = Ron Cooke Hub
	private boolean completed = false;


	public Level(DungeonRoom[][] levelArray, ObjectiveType objective, LevelType level) {
		this.levelArray = new DungeonRoom[7][7];
		this.levelArray = levelArray;
		this.objective = objective;
		this.level = level;
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
