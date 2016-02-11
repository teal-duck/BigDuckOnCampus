package com.muscovy.game.enums;


import com.muscovy.game.entity.BossParameters;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.level.LevelParameters;


/**
 *
 */
public enum LevelType {
	CONSTANTINE, LANGWITH, GOODRICKE, LMB, CATALYST, TFTV, COMP_SCI, RCH;

	private static final LevelType[] LEVEL_VALUES = LevelType.values();
	public static final int LEVEL_COUNT = LevelType.LEVEL_VALUES.length;


	/**
	 * @param i
	 * @return
	 */
	public static LevelType fromInt(int i) {
		return LevelType.LEVEL_VALUES[i];
	}


	/**
	 * Increments the ordinal value for level by amount, wraps around and returns the new level.
	 *
	 * @param level
	 * @param amount
	 * @return
	 */
	public static LevelType advanceLevel(LevelType level, int amount) {
		int currentLevel = level.ordinal();
		int nextLevel = currentLevel + amount;

		while (nextLevel > LevelType.LEVEL_COUNT) {
			nextLevel -= LevelType.LEVEL_COUNT;
		}
		while (nextLevel < 0) {
			nextLevel += LevelType.LEVEL_COUNT;
		}

		return LevelType.LEVEL_VALUES[nextLevel];
	}


	// public static final int DUNGEON_ROOMS_WIDE = 7;
	// public static final int DUNGEON_ROOMS_HIGH = 7;
	// public static final int MAX_ROOMS = 20;
	// public static final int START_ROOM_X = 3;
	// public static final int START_ROOM_Y = 3;

	/**
	 * @param levelType
	 * @return
	 */
	public static LevelParameters getParametersForLevel(LevelType levelType) {
		switch (levelType) {
		case CONSTANTINE:
			return new LevelParameters(5, 5, 6, 2, 2, ObjectiveType.BOSS);
		case LANGWITH:
			return new LevelParameters(6, 6, 5, 3, 3, ObjectiveType.KILL_ENEMIES);
		case GOODRICKE:
			return new LevelParameters(6, 6, 10, 3, 3, ObjectiveType.BOSS);
		default:
			return new LevelParameters(7, 7, 20, 3, 3, ObjectiveType.BOSS);
		}
	}


	/**
	 * @param levelType
	 * @return
	 */
	public static String getName(LevelType levelType) {
		switch (levelType) {
		case CONSTANTINE:
			return "Constantine College";
		case LANGWITH:
			return "Langwith College";
		case GOODRICKE:
			return "Goodricke College";
		case LMB:
			return "Law and Management Building";
		case CATALYST:
			return "Catalyst";
		case TFTV:
			return "Theatre, Film and Television";
		case COMP_SCI:
			return "Computer Science";
		case RCH:
			return "Ron-Cooke Hub";
		}
		return "";
	}


	/**
	 * Item the player gets when they beat the level.
	 *
	 * @param levelType
	 * @return
	 */
	public static ItemType getItemType(LevelType levelType) {
		ItemType itemType;

		if (levelType == CONSTANTINE) {
			itemType = ItemType.TRIPLE_SHOT;
		} else if (levelType == LANGWITH) {
			itemType = ItemType.RAPID_FIRE;
		} else if (levelType == GOODRICKE) {
			itemType = ItemType.FLAME_THROWER;
		} else {
			itemType = null;
		}

		return itemType;
	}


	/**
	 * @param levelType
	 * @return
	 */
	public static BossParameters getBossParameters(LevelType levelType) {
		switch (levelType) {
		case CONSTANTINE:
			return new BossParameters(ProjectileType.STANDARD, EnemyShotType.TRIPLE_TOWARDS_PLAYER,
					MovementType.FOLLOW, Enemy.ATTACK_INTERVAL, Enemy.PROJECTILE_SPEED * 2, 2,
					Enemy.BOSS_MAX_SPEED, 20, 600);
		case GOODRICKE:
			return new BossParameters(ProjectileType.HOMING, EnemyShotType.SINGLE_TOWARDS_PLAYER,
					MovementType.FOLLOW, Enemy.ATTACK_INTERVAL, Enemy.PROJECTILE_SPEED * 2, 5,
					Enemy.BOSS_MAX_SPEED, 20, 600);
		case LMB:
			return new BossParameters(ProjectileType.CURVED, EnemyShotType.TRIPLE_TOWARDS_PLAYER,
					MovementType.FOLLOW, Enemy.ATTACK_INTERVAL, Enemy.PROJECTILE_SPEED * 2, 2,
					Enemy.BOSS_MAX_SPEED, 20, 600);
		default:
			return null;
		}
	}
}
