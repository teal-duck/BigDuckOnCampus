package com.muscovy.game.enums;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Enumerates
 */
public enum ObjectiveType {
	BOSS, KILL_ENEMIES;

	/**
	 * @param objectiveType
	 * @return
	 */
	public static String getName(ObjectiveType objectiveType) {
		switch (objectiveType) {
		case BOSS:
			return "Kill the boss!";
		case KILL_ENEMIES:
			return "Kill all enemies!";
		}
		return "";
	}
}
