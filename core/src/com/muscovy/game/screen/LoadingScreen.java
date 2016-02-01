package com.muscovy.game.screen;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.Levels;
import com.muscovy.game.MuscovyGame;


public class LoadingScreen extends ScreenBase {
	private BitmapFont font;
	private float time = 0;


	public LoadingScreen(MuscovyGame game) {
		super(game);
		font = new BitmapFont();
		font.setColor(Color.WHITE);
	}


	@Override
	public void resume() {
	}


	@Override
	public void updateScreen(float deltaTime) {
		time += deltaTime;
		if (time > 0.5f) {
			AssetLocations.loadAllTexturesIntoMap(getTextureMap());

			Levels levels = new Levels();
			levels.generateLevels(getGame());
			levels.generateLevelContents();
			getGame().setLevels(levels);
			setScreen(new MainMenuScreen(getGame()));
		}
	}


	@Override
	public void renderScreen(float deltaTime, SpriteBatch batch) {
		clearScreen();
		updateAndSetCamera();

		batch.begin();
		font.draw(batch, "RANDOMLY GENERATING LEVELS", (getWindowWidth() / 2) - 150, getWindowHeight() / 2);
		batch.end();
	}


	@Override
	public void dispose() {
		super.dispose();
		font.dispose();
	}
}
