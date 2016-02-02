package com.muscovy.game.screen;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.Levels;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.save.SaveData;
import com.muscovy.game.save.SaveGame;


public class LoadingScreen extends ScreenBase {
	private BitmapFont font;
	private float time = 0;


	public LoadingScreen(MuscovyGame game) {
		super(game);
		font = AssetLocations.newFont();
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

			Levels levels = new Levels();
			levels.generateLevels(getGame());
			levels.generateLevelContents();
			getGame().setLevels(levels);

			doSaveLoadTest();

			setScreen(new MainMenuScreen(getGame()));
		}
	}


	private void doSaveLoadTest() {
		MuscovyGame game = getGame();
		SaveGame saveGame = new SaveGame(game);
		SaveData saveData = new SaveData(game.getPlayerCharacter(), game.getLevels());
		String json = saveGame.getSaveString(saveData);
		System.out.println(json);

		System.out.println("");

		SaveData loadedData = saveGame.loadFromSaveString(json);
		System.out.println(loadedData.getPlayer().getPosition());
		System.out.println(loadedData.getPlayer().getScore());
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
