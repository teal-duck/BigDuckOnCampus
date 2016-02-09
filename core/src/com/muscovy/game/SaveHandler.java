package com.muscovy.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


public class SaveHandler {
	public static final int MAX_SAVE_COUNT = 4;


	/**
	 * @param saveNumber
	 * @return
	 */
	public static String getFileNameForSaveNumber(int saveNumber) {
		return "save_" + saveNumber + ".json";
	}


	/**
	 * @param saveNumber
	 * @return
	 */
	public static String getFileForSaveNumber(int saveNumber) {
		return AssetLocations.SAVE_FOLDER + SaveHandler.getFileNameForSaveNumber(saveNumber);
	}


	public static FileHandle getFileHandleForSaveName(String saveFileName) {
		return Gdx.files.local(saveFileName);
	}


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
	public static boolean deleteSave(int saveNumber) {
		FileHandle fileHandle = SaveHandler.getFileHandleForSaveNumber(saveNumber);
		if (fileHandle.exists()) {
			return fileHandle.delete();
		}
		return false;
	}
}
