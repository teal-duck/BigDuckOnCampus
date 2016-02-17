package com.muscovy.game.level;


import java.util.Random;

import com.badlogic.gdx.Gdx;


/**
 * Project URL : http://teal-duck.github.io/teal-duck
 * <br>
 * New class: Loads .csv files describing item locations in each room.
 */
public class DungeonRoomTemplateLoader {
	// Constants for the values that can be in the template
	public static final int EMPTY_TILE = 0;
	public static final int NON_DAMAGING_OBSTACLE = 1;
	public static final int DAMAGING_OBSTACLE = 2;
	/**
	 * Randomly choose a damaging or non damaging obstacle
	 */
	public static final int MAYBE_DAMAGING_OBSTACLE = 3;
	/**
	 * Randomly choose whether to place an enemy here
	 */
	public static final int MAYBE_ENEMY = 4;
	public static final int UNUSED = 5;
	public static final int POWERUP = 6;

	private String filename;
	private int[][][] roomTemplates;
	private boolean isLoaded;


	public DungeonRoomTemplateLoader(String filename) {
		this.filename = filename;
		isLoaded = false;
	}


	/**
	 * @return
	 */
	private String readFileContents() {
		return Gdx.files.internal(filename).readString();
	}


	/**
	 * Turns a comma separated list of integers into an array of integers.
	 *
	 * @param templateString
	 * @return
	 */
	private int[] templateStringToIntegers(String templateString) {
		// Remove whitespace
		templateString = templateString.replaceAll("\\s", "");

		String[] splits = templateString.split(",");
		int[] integers = new int[splits.length];

		// Try to turn each value read into an integer
		// Defaults to 0 if invalid
		for (int index = 0; index < splits.length; index += 1) {
			String split = splits[index];
			try {
				int readInt = Integer.parseInt(split);
				integers[index] = readInt;
			} catch (NumberFormatException e) {
				System.out.println("Invalid number at location " + index);
				integers[index] = 0;
			}
		}

		return integers;
	}


	/**
	 * If the rooms haven't been loaded, read the file and load them into roomTemplates array. Returns the array.
	 *
	 * @return array of templates from file
	 */
	private int[][][] loadRoomTemplates() {
		if (isLoaded) {
			return roomTemplates;
		}

		String templateString = readFileContents();
		int[] integers = templateStringToIntegers(templateString);

		int roomCount = integers[0];
		int roomWidth = integers[1];
		int roomHeight = integers[2];

		roomTemplates = new int[roomCount][roomHeight][roomWidth];

		int readingIndex = 3;
		for (int room = 0; room < roomCount; room += 1) {
			for (int y = 0; y < roomHeight; y += 1) {
				for (int x = 0; x < roomWidth; x += 1) {
					int readInt = integers[readingIndex];
					roomTemplates[room][y][x] = readInt;
					readingIndex += 1;
				}
			}
		}
		isLoaded = true;
		return roomTemplates;
	}


	/**
	 * If the rooms have been loaded, return them. Else it calls loadRoomTemplates() to load them from the file.
	 *
	 * @return
	 */
	public int[][][] getRoomTemplatesOrLoad() {
		if (isLoaded) {
			return roomTemplates;
		} else {
			return loadRoomTemplates();
		}
	}


	/**
	 * Returns a random room template. Calls getRoomTemplatesOrLoad() so that if the templates haven't been loaded,
	 * they get loaded.
	 *
	 * @param random
	 * @return
	 */
	public int[][] getRandomTemplateOrLoad(Random random) {
		int[][][] rooms = getRoomTemplatesOrLoad();
		int template = random.nextInt(rooms.length);
		return rooms[template];
	}


	/**
	 * @return
	 */
	public String getFilename() {
		return filename;
	}


	/**
	 * @return
	 */
	public boolean isLoaded() {
		return isLoaded;
	}


	/**
	 *
	 */
	public void dispose() {
		roomTemplates = null;
		isLoaded = false;
	}
}
