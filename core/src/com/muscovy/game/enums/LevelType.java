package com.muscovy.game.enums;


import com.muscovy.game.LevelParameters;


public enum LevelType {
	// 0 = Constantine, 1 = Langwith, 2 = Goodricke, 3 = Law and Management, 4 = Catalyst, 5 =
	// TFTV, 6 = Computer Science, 7 = Ron Cooke Hub
	CONSTANTINE, LANGWITH, GOODRICKE, LMB, CATALYST, TFTV, COMP_SCI, RCH;

	public static final int LEVEL_COUNT = RCH.ordinal();

	private static final LevelType[] levelValues = LevelType.values();


	public static LevelType fromInt(int i) {
		return LevelType.levelValues[i];
	}


	public static LevelType advanceLevel(LevelType level, int amount) {
		int currentLevel = level.ordinal();
		int nextLevel = currentLevel + amount;

		while (nextLevel > LevelType.LEVEL_COUNT) {
			nextLevel -= LevelType.LEVEL_COUNT;
		}
		while (nextLevel < 0) {
			nextLevel += LevelType.LEVEL_COUNT;
		}

		return LevelType.levelValues[nextLevel];
	}


	// public static final int DUNGEON_ROOMS_WIDE = 7;
	// public static final int DUNGEON_ROOMS_HIGH = 7;
	// public static final int MAX_ROOMS = 20;
	// public static final int START_ROOM_X = 3;
	// public static final int START_ROOM_Y = 3;

	public static LevelParameters getParametersForLevel(LevelType level) {
		switch (level) {
		case CONSTANTINE:
			return new LevelParameters(4, 4, 4, 1, 1, ObjectiveType.BOSS);
		case LANGWITH:
			return new LevelParameters(5, 5, 3, 3, 3, ObjectiveType.KILL_ENEMIES);
		default:
			return new LevelParameters(7, 7, 20, 3, 3, ObjectiveType.BOSS);
		}
	}
}
