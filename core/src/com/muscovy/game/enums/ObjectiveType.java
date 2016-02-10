package com.muscovy.game.enums;


/**
 */
public enum ObjectiveType {
	BOSS, FIND_ITEM, KILL_ENEMIES;

	/**
	 * @param objectiveType
	 * @return
	 */
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
