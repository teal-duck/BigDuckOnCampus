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

		Sprite button = new Sprite(getTextureMap().getTextureOrLoadFile(AssetLocations.START_GAME_BUTTON));
		button.setCenter(getWindowWidth(), getWindowHeight());
		button.setX((getWindowWidth() - 392) / 2);
		button.setY(getWindowHeight() / 2);

		gui = new GUI();
		gui.addElement(background);
		gui.addElement(button);
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
