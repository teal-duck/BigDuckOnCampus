package com.muscovy.game.screen;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.Levels;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.save.game.SaveData;
import com.muscovy.game.save.game.SaveGame;


public class LoadingScreen extends ScreenBase {
	private BitmapFont font;
	private float time = 0;


	public LoadingScreen(MuscovyGame game) {
		super(game);
		font = AssetLocations.newFont20();
		font.setColor(Color.WHITE);
	}


	@Override
	public void resume() {
	}


	private final float startTime = 0.1f; // 0.5f;


	@Override
	public void updateScreen(float deltaTime) {
		time += deltaTime;
		if (time > startTime) {
			AssetLocations.loadAllTexturesIntoMap(getTextureMap());

			int saveNumber = 0;

			// Try to load the data for the save
			SaveData loadedData = loadSave(saveNumber);

			// For testing purposes
			// The loading code isn't finished so parts are still null
			loadedData = null;

			// If the save file doesn't exist
			// Load a new world and save it
			if (loadedData == null) {
				loadedData = generateNewGame(saveNumber);
				saveData(saveNumber, loadedData);
			}

			// Set the game to use this loaded world
			getGame().initialiseFromSaveData(loadedData);

			setScreen(new MainMenuScreen(getGame()));
		}
	}


	/**
	 * Loads the save data for a given save number. Returns null if there is no data.
	 *
	 * @param saveNumber
	 * @return
	 */
	private SaveData loadSave(int saveNumber) {
		SaveGame saveGame = new SaveGame(getGame());
		String saveLocation = AssetLocations.getFileForSaveNumber(saveNumber);
		SaveData loadedData = saveGame.loadFromFileOrNull(saveLocation);
		return loadedData;
	}


	private void saveData(int saveNumber, SaveData data) {
		new SaveGame(getGame()).saveToFile(data, AssetLocations.getFileForSaveNumber(saveNumber));
	}


	/**
	 * Randomly generates a new world.
	 *
	 * @param saveNumber
	 * @return
	 */
	private SaveData generateNewGame(int saveNumber) {
		Levels levels = new Levels();
		levels.generateLevels(getGame());
		levels.generateLevelContents();
		PlayerCharacter player = getGame().getPlayerCharacter();
		return new SaveData(player, levels);
	}


	@SuppressWarnings("unused")
	private void doSaveLoadTest() {
		MuscovyGame game = getGame();
		SaveGame saveGame = new SaveGame(game);
		SaveData saveData = new SaveData(game.getPlayerCharacter(), game.getLevels());

		String saveLocation = AssetLocations.getFileForSaveNumber(0);
		saveGame.saveToFile(saveData, saveLocation);

		SaveData loadedData = saveGame.loadFromFileOrNull(saveLocation);
		if (loadedData == null) {
			System.out.println("File doesn't exist");
		} else {
			System.out.println(loadedData);
		}
	}


	@Override
	public void renderScreen(float deltaTime, SpriteBatch batch) {
		clearScreen();
		updateAndSetCamera();

		batch.begin();
		font.draw(batch, "RANDOMLY GENERATING LEVELS", (getWindowWidth() / 2) - 250, getWindowHeight() / 2);
		batch.end();
	}


	@Override
	public void dispose() {
		super.dispose();
		font.dispose();
	}
}
