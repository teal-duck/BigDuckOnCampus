package com.muscovy.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.Levels;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.SaveHandler;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.save.game.SaveData;
import com.muscovy.game.save.game.SaveGame;


/**
 
 */
public class LoadingScreen extends ScreenBase {
	private final BitmapFont font;
	private final int saveNumber;

	private float time = 0;

	// For testing purposes
	// The loading code isn't finished so parts are still null
	private final boolean loadData = true;


	public LoadingScreen(MuscovyGame game, int saveNumber) {
		super(game);
		this.saveNumber = saveNumber;
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
			getGame().initialisePlayerCharacter();
			AssetLocations.loadAllTexturesIntoMap(getTextureMap());

			// Try to load the data for the save
			SaveData loadedData = loadSave(saveNumber);

			// If the save file doesn't exist
			// Load a new world and save it
			if (loadedData == null) {
				loadedData = generateNewGame(saveNumber);
				// getGame().saveData(saveNumber, loadedData);
			}

			// Set the game to use this loaded world
			getGame().initialiseFromSaveData(loadedData);
			getGame().setCurrentSaveNumber(saveNumber);

			if (getGame().areAllLevelsCompleted()) {
				setScreen(new WinScreen(getGame()));
			} else {
				setScreen(new LevelSelectScreen(getGame()));
			}
		}
	}


	/**
	 * Loads the save data for a given save number. Returns null if there is no data.
	 *
	 * @param saveNumber
	 * @return
	 */
	private SaveData loadSave(int saveNumber) {
		if (loadData) {
			Gdx.app.log("Load", "Loading save");
			SaveGame saveGame = new SaveGame(getGame());
			String saveLocation = SaveHandler.getFileForSaveNumber(saveNumber);
			SaveData loadedData = saveGame.loadFromFileOrNull(saveLocation);
			return loadedData;
		} else {
			Gdx.app.log("Load", "Loading disabled, returning null");
			return null;
		}
	}


	/**
	 * Randomly generates a new world.
	 *
	 * @param saveNumber
	 * @return
	 */
	private SaveData generateNewGame(int saveNumber) {
		Gdx.app.log("Load", "Generating new game");
		Levels levels = new Levels();
		levels.generateLevels(getGame());
		levels.generateLevelContents();
		PlayerCharacter player = getGame().getPlayerCharacter();
		return new SaveData(player, levels);
	}


	@Override
	public void renderScreen(float deltaTime, SpriteBatch batch) {
		clearScreen();
		updateAndSetCamera();

		batch.begin();
		font.draw(batch, "Loading Game...", (getWindowWidth() / 2) - 250, getWindowHeight() / 2);
		batch.end();
	}


	@Override
	public void dispose() {
		super.dispose();
		font.dispose();
	}
}
