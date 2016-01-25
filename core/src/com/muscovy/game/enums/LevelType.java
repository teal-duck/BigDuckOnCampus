package com.muscovy.game.enums;


public enum LevelType {
	// 0 = Constantine, 1 = Langwith, 2 = Goodricke, 3 = Law and Management, 4 = Catalyst, 5 =
	// TFTV, 6 = Computer Science, 7 = Ron Cooke Hub
	CONSTANTINE, LANGWITH, GOODRICKE, LMB, CATALYST, TFTV, COMP_SCI, RCH;

	// public static final LevelType FIRST_LEVEL = CONSTANTINE;
	// public static final LevelType LAST_LEVEL = RCH;
	public static final int LEVEL_COUNT = RCH.ordinal();

	private static final LevelType[] levelValues = LevelType.values();


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
}
