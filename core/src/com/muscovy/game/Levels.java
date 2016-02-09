package com.muscovy.game;


import com.muscovy.game.enums.LevelType;
import com.muscovy.game.level.DungeonRoom;
import com.muscovy.game.level.DungeonRoomTemplateLoader;
import com.muscovy.game.level.Level;
import com.muscovy.game.level.LevelGenerator;
import com.muscovy.game.level.LevelParameters;


public class Levels {
	private int maxLevels;
	private Level[] levels;


	public Levels() {
	}


	/**
	 * @param levels
	 */
	public void setLevels(Level[] levels) {
		this.levels = levels;
	}


	/**
	 * @return
	 */
	public Level[] getLevels() {
		return levels;
	}


	/**
	 * @param game
	 */
	public void generateLevels(MuscovyGame game) {
		// TODO: Only generate level when player wants to play it?
		maxLevels = LevelType.LEVEL_COUNT;
		levels = new Level[maxLevels];
		for (int i = 0; i < levels.length; i += 1) {
			LevelType levelType = LevelType.fromInt(i);
			LevelParameters levelParameters = LevelType.getParametersForLevel(levelType);

			DungeonRoom[][] rooms = LevelGenerator.generateBuilding(game, levelType, levelParameters);
			Level level = new Level(rooms, levelType, levelParameters);

			levels[i] = level;

		}
	}


	/**
	 *
	 */
	public void generateLevelContents() {
		String templateFilename = AssetLocations.ROOM_TEMPLATE_FILE;
		DungeonRoomTemplateLoader templateLoader = new DungeonRoomTemplateLoader(templateFilename);
		for (int i = 0; i < levels.length; i += 1) {
			Level level = levels[i];
			DungeonRoom[][] rooms = level.getLevelArray();
			LevelType levelType = level.getLevelType();
			LevelGenerator.generateRoomContents(rooms, levelType, templateLoader);
		}
		templateLoader.dispose();

	}


	// TODO: Check for ArrayIndexOutOfBoundsException
	/**
	 * @param level
	 * @return
	 */
	public Level getLevel(int level) {
		return levels[level];
	}


	/**
	 * @param level
	 * @return
	 */
	public boolean isLevelCompleted(int level) {
		return getLevel(level).isCompleted();
	}
}
