package com.muscovy.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.GUI;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.input.Action;


public class MainMenuScreen extends ScreenBase {
	private GUI gui;
	private boolean escapeJustPressed = true;


	public MainMenuScreen(MuscovyGame game) {
		super(game);
		initialiseGuis();
	}


	private void initialiseGuis() {
		Sprite background = new Sprite(getTextureMap().getTextureOrLoadFile(AssetLocations.MAIN_MENU));
		background.setX(0);
		background.setY(getWindowHeight() - background.getTexture().getHeight());

		Sprite startButton = new Sprite(getTextureMap().getTextureOrLoadFile(AssetLocations.START_GAME_BUTTON));
		startButton.setCenter(getWindowWidth(), getWindowHeight());
		startButton.setX((getWindowWidth() - 392) / 2);
		startButton.setY(getWindowHeight() / 2);

		/*
		 * Sprite loadButton = new
		 * Sprite(getTextureMap().getTextureOrLoadFile(AssetLocations.LOAD_GAME_BUTTON));
		 * loadButton.setCenter(getWindowWidth(), getWindowHeight()); loadButton.setX((getWindowWidth() - 392) /
		 * 2); loadButton.setY((getWindowHeight() / 2) - 120);
		 *
		 * Sprite settingsButton = new
		 * Sprite(getTextureMap().getTextureOrLoadFile(AssetLocations.SETTINGS_BUTTON));
		 * settingsButton.setCenter(getWindowWidth(), getWindowHeight()); settingsButton.setX((getWindowWidth()
		 * - 392) / 2); settingsButton.setY((getWindowHeight() / 2) - 240);
		 */

		gui = new GUI();
		gui.addElement(background);
		gui.addElement(startButton);
		/*
		 * gui.addElement(loadButton); gui.addElement(settingsButton);
		 */
	}


	@Override
	public void updateScreen(float deltaTime) {
		if (isStateForAction(Action.ENTER)) {
			setScreen(new LevelSelectScreen(getGame()));
			return;
		}

		if (isStateForAction(Action.ESCAPE)) {
			if (!escapeJustPressed) {
				Gdx.app.exit();
				return;
			}
			escapeJustPressed = true;
		} else {
			escapeJustPressed = false;
		}
	}


	@Override
	public void renderScreen(float deltaTime, SpriteBatch batch) {
		clearScreen();
		updateAndSetCamera();

		batch.begin();
		gui.render(batch);
		batch.end();
	}
}
