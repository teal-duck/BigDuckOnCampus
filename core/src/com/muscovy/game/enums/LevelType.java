package com.muscovy.game.enums;


import com.muscovy.game.level.LevelParameters;


public enum LevelType {
	// 0 = Constantine, 1 = Langwith, 2 = Goodricke, 3 = Law and Management, 4 = Catalyst, 5 =
	// TFTV, 6 = Computer Science, 7 = Ron Cooke Hub
	CONSTANTINE, LANGWITH, GOODRICKE, LMB, CATALYST, TFTV, COMP_SCI, RCH;

	private static final LevelType[] LEVEL_VALUES = LevelType.values();
	public static final int LEVEL_COUNT = LevelType.LEVEL_VALUES.length;


	public static LevelType fromInt(int i) {
		return LevelType.LEVEL_VALUES[i];
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

		return LevelType.LEVEL_VALUES[nextLevel];
	}


	// public static final int DUNGEON_ROOMS_WIDE = 7;
	// public static final int DUNGEON_ROOMS_HIGH = 7;
	// public static final int MAX_ROOMS = 20;
	// public static final int START_ROOM_X = 3;
	// public static final int START_ROOM_Y = 3;

	public static LevelParameters getParametersForLevel(LevelType levelType) {
		switch (levelType) {
		case CONSTANTINE:
			return new LevelParameters(5, 5, 6, 2, 2, ObjectiveType.BOSS);
		case LANGWITH:
			return new LevelParameters(6, 6, 5, 3, 3, ObjectiveType.KILL_ENEMIES);
		default:
			return new LevelParameters(7, 7, 20, 3, 3, ObjectiveType.BOSS);
		}
	}


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


	public static ItemType getItemType(LevelType levelType) {
		ItemType itemType;

		if (levelType == CONSTANTINE) {
			itemType = ItemType.TRIPLE_SHOT;
		} else if (levelType == GOODRICKE) {
			itemType = ItemType.RAPID_FIRE;
		} else {
			itemType = null;
		}

		return itemType;
	}
}
