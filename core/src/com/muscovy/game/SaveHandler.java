package com.muscovy.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


/**
 * Project URL : http://teal-duck.github.io/teal-duck
 *
 * Helper class for dealing with save files.
 */
public class SaveHandler {
	public static final int MAX_SAVE_COUNT = 4;


	/**
	 * Returns just the name for the save file. E.g. save_0.json
	 *
	 * @param saveNumber
	 * @return
	 */
	public static String getFileNameForSaveNumber(int saveNumber) {
		return "save_" + saveNumber + ".json";
	}


	/**
	 * Returns the folder and file name concatentated. E.g. preferences/saves/save_0.json
	 *
	 * @param saveNumber
	 * @return
	 */
	public static String getFileForSaveNumber(int saveNumber) {
		return AssetLocations.SAVE_FOLDER + SaveHandler.getFileNameForSaveNumber(saveNumber);
	}


	/**
	 * @param saveFileName
	 * @return
	 */
	public static FileHandle getFileHandleForSaveName(String saveFileName) {
		return Gdx.files.local(saveFileName);
	}


	/**
	 * @param saveNumber
	 * @return
	 */
	public static FileHandle getFileHandleForSaveNumber(int saveNumber) {
		return SaveHandler.getFileHandleForSaveName(SaveHandler.getFileForSaveNumber(saveNumber));
	}


	/**
	 * @param saveNumber
	 * @return
	 */
	public static boolean doesSaveFileExist(int saveNumber) {
		return SaveHandler.getFileHandleForSaveNumber(saveNumber).exists();
	}


	/**
	 * @param saveNumber
	 * @return
	 */
	public static boolean deleteSaveFile(int saveNumber) {
		FileHandle fileHandle = SaveHandler.getFileHandleForSaveNumber(saveNumber);
		if (fileHandle.exists()) {
			return fileHandle.delete();
		}
		return false;
	}
}
