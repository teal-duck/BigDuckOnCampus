package com.muscovy.game;


import com.muscovy.game.enums.LevelType;
import com.muscovy.game.level.DungeonRoom;
import com.muscovy.game.level.DungeonRoomTemplateLoader;
import com.muscovy.game.level.Level;
import com.muscovy.game.level.LevelGenerator;
import com.muscovy.game.level.LevelParameters;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Groups all the levels together. Provides functions for generating the levels.
 */
public class Levels {
	private int maxLevels;
	private Level[] levels;


	public Levels() {
	}


	public Levels(Level[] levels) {
		this.levels = levels;
	}


	/**
	 * @return
	 */
	public Level[] getLevels() {
		return levels;
	}


	/**
	 * Creates the levels array and generates each level in the game.
	 *
	 * @param game
	 */
	public void generateLevels(MuscovyGame game) {
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
	 * For each level, calls generateRoomContents using the template file.
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


	/**
	 * @param level
	 * @return
	 */
	public Level getLevel(int level) {
		return levels[level];
	}


	/**
	 * @param levelType
	 * @return
	 */
	public Level getLevel(LevelType levelType) {
		return getLevel(levelType.ordinal());
	}


	/**
	 * @param level
	 * @return True if level has been completed.
	 */
	public boolean isLevelCompleted(int level) {
		return getLevel(level).isCompleted();
	}


	/**
	 * @param levelType
	 * @return True if level has been completed.
	 */
	public boolean isLevelCompleted(LevelType levelType) {
		return getLevel(levelType).isCompleted();
	}


	/**
	 * @return True if all levels have been completed.
	 */
	public boolean areAllLevelsCompleted() {
		for (Level level : levels) {
			if (!level.isCompleted()) {
				return false;
			}
		}
		return true;
	}
}