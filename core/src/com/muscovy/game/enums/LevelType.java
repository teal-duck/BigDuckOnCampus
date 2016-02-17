package com.muscovy.game.enums;


import com.badlogic.gdx.Gdx;
import com.muscovy.game.entity.BossParameters;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.entity.MoveableEntity;
import com.muscovy.game.level.LevelParameters;


/**
 * Project URL : http://teal-duck.github.io/teal-duck
 * <br>
 * New class: Enumerates all level names.
 */
public enum LevelType {
	CONSTANTINE, LANGWITH, GOODRICKE, LMB, CATALYST, TFTV, COMP_SCI, RCH,;

	private static final LevelType[] LEVEL_VALUES = LevelType.values();
	public static final int LEVEL_COUNT = LevelType.LEVEL_VALUES.length;
	public static final LevelType FIRST_LEVEL_TO_GIVE_BOMBS = GOODRICKE;
	public static final LevelType FIRST_LEVEL_TO_GIVE_HEALTH = CONSTANTINE;


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


	/**
	 * @param levelType
	 * @return
	 */
	public static LevelParameters getParametersForLevel(LevelType levelType) {
		switch (levelType) {
		case CONSTANTINE:
			return new LevelParameters(7, 7, 6, 4, 4, ObjectiveType.BOSS);
		case LANGWITH:
			return new LevelParameters(7, 7, 5, 4, 4, ObjectiveType.KILL_ENEMIES);
		case GOODRICKE:
			return new LevelParameters(7, 7, 10, 4, 4, ObjectiveType.BOSS, MoveableEntity.WATER_FRICTION);
		case LMB:
			return new LevelParameters(7, 7, 10, 4, 4, ObjectiveType.KILL_ENEMIES);
		case CATALYST:
			return new LevelParameters(7, 7, 14, 4, 4, ObjectiveType.BOSS);
		case TFTV:
			return new LevelParameters(7, 7, 14, 4, 4, ObjectiveType.KILL_ENEMIES);
		case COMP_SCI:
			return new LevelParameters(7, 7, 20, 4, 4, ObjectiveType.KILL_ENEMIES);
		case RCH:
			return new LevelParameters(7, 7, 20, 4, 4, ObjectiveType.BOSS);
		default:
			System.out.println("Creating level " + levelType.toString() + " with default parameters.");
			return new LevelParameters(7, 7, 20, 4, 4, ObjectiveType.BOSS);
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
			return "Ron Cooke Hub";
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

		switch (levelType) {
		case CONSTANTINE:
			return ItemType.HEALTH_UNLOCK;
		case LANGWITH:
			return ItemType.FLIGHT;
		case GOODRICKE:
			return ItemType.BOMB;
		case LMB:
			return ItemType.RAPID_FIRE;
		case CATALYST:
			return ItemType.EXTRA_HEALTH;
		case TFTV:
			return ItemType.TRIPLE_SHOT;
		case COMP_SCI:
			return ItemType.FLAME_THROWER;
		case RCH:
			return ItemType.SUNGLASSES;
		default:
			return null;
		}

		// Below is what it should look like
		// switch (levelType) {
		// case CONSTANTINE:
		// return ItemType.FLIGHT;
		// case LANGWITH:
		// return ItemType.RAPID_FIRE;
		// case GOODRICKE:
		// return ItemType.FIRST_BOMB;
		// case LMB:
		// return ItemType.EXTRA_HEALTH;
		// case CATALYST:
		// return ItemType.HOMING_BULLET;
		// case TFTV:
		// return ItemType.TRIPLE_SHOT;
		// case COMP_SCI:
		// return ItemType.FLAME_THROWER;
		// case RCH:
		// return ItemType.MOUSTACHE;
		// default:
		// return null;
		// }
	}


	private static final BossParameters defaultBossParameters = new BossParameters(ProjectileType.STANDARD,
			EnemyShotType.TRIPLE_TOWARDS_PLAYER, MovementType.FOLLOW, Enemy.ATTACK_INTERVAL,
			Enemy.BOSS_PROJECTILE_SPEED, 2, MoveableEntity.BOSS_ACCELERATION_SPEED, 20, 600);


	/**
	 * @param levelType
	 * @return
	 */
	public static BossParameters getBossParameters(LevelType levelType) {
		switch (levelType) {
		case CONSTANTINE:
			return new BossParameters(ProjectileType.STANDARD, EnemyShotType.TRIPLE_TOWARDS_PLAYER,
					MovementType.FOLLOW, Enemy.ATTACK_INTERVAL, Enemy.BOSS_PROJECTILE_SPEED, 2,
					MoveableEntity.BOSS_ACCELERATION_SPEED, 20, 600);
		case GOODRICKE:
			return new BossParameters(ProjectileType.HOMING, EnemyShotType.SINGLE_TOWARDS_PLAYER,
					MovementType.FOLLOW, Enemy.ATTACK_INTERVAL, Enemy.BOSS_PROJECTILE_SPEED * 1.2f,
					5, MoveableEntity.BOSS_ACCELERATION_SPEED * 0.8f, 20, 600);
		case LMB:
			return new BossParameters(ProjectileType.CURVED, EnemyShotType.TRIPLE_TOWARDS_PLAYER,
					MovementType.FOLLOW, Enemy.ATTACK_INTERVAL, Enemy.BOSS_PROJECTILE_SPEED, 2,
					MoveableEntity.BOSS_ACCELERATION_SPEED, 20, 600);
		default:
			Gdx.app.log("Boss", "Using default boss parameters for level " + levelType.toString());
			return LevelType.defaultBossParameters;
		}
	}
	
	
	/**
	 * 
	 * @param levelType
	 * @return True if level is an "underwater level"
	 */
	public static boolean isUnderwater(LevelType levelType) {
		return levelType.equals(GOODRICKE);
	}
}
