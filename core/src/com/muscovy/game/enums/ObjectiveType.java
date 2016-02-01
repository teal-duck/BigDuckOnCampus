package com.muscovy.game.enums;


public enum ObjectiveType {
	// 0 = Boss fight, 1 = Find specific item, 3 = Kill certain number of enemies
	BOSS, FIND_ITEM, KILL_ENEMIES;

	public static String getName(ObjectiveType objectiveType) {
		switch (objectiveType) {
		case BOSS:
			return "Kill the boss!";
		case FIND_ITEM:
			return "Find the item!";
		case KILL_ENEMIES:
			return "Kill all enemies!";
		}
		return "";
	}
}
