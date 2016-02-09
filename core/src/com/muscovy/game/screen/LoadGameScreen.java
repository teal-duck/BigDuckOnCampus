package com.muscovy.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.SaveHandler;
import com.muscovy.game.gui.Button;
import com.muscovy.game.gui.ButtonList;
import com.muscovy.game.gui.GUI;
import com.muscovy.game.input.Action;


public class LoadGameScreen extends ScreenBase {
	private final String[] BUTTON_TEXTS;

	private GUI gui;
	private boolean escapeJustPressed = true;
	private ButtonList buttons;
	private BitmapFont font;


	public LoadGameScreen(MuscovyGame game) {
		super(game);
		font = AssetLocations.newFont32();
		font.setColor(Color.WHITE);

		int saveCount = 0;
		while (SaveHandler.doesSaveFileExist(saveCount)) {
			saveCount += 1;
		}

		BUTTON_TEXTS = new String[saveCount + 1];
		for (int i = 0; i < saveCount; i += 1) {
			BUTTON_TEXTS[i] = "Save #" + (i + 1);
		}
		BUTTON_TEXTS[saveCount] = "Back";

		initialiseGuis();
	}


	private void initialiseGuis() {
		Sprite background = new Sprite(getTextureMap().getTextureOrLoadFile(AssetLocations.MAIN_MENU));
		background.setX(0);
		background.setY(getWindowHeight() - background.getTexture().getHeight());

		gui = new GUI();
		gui.addElement(background);

		buttons = new ButtonList(BUTTON_TEXTS, font, getTextureMap(), getControlMap(), getController());
		setButtonLocations();
	}


	private void setButtonLocations() {
		int x = (getWindowWidth() / 2) - (ButtonList.BUTTON_WIDTH / 2);
		ButtonList.getHeightForDefaultButtonList(BUTTON_TEXTS.length);

		int y = (getWindowHeight() / 6) + ButtonList.getHeightForDefaultButtonList(SaveHandler.MAX_SAVE_COUNT);
		buttons.setPositionDefaultSize(x, y);

		Button backButton = buttons.getButtonAtIndex(buttons.getButtonCount() - 1);
		backButton.setX(ButtonList.WINDOW_EDGE_OFFSET);
		backButton.setY(ButtonList.WINDOW_EDGE_OFFSET);
		backButton.setWidth(ButtonList.BUTTON_WIDTH * 0.6f);
	}


	@Override
	public void resize(int width, int height) {
		setButtonLocations();
	}


	@Override
	public void updateScreen(float deltaTime) {
		if (isStateForAction(Action.ESCAPE)) {
			if (!escapeJustPressed) {
				selectBack();
				return;
			}
			escapeJustPressed = true;
		} else {
			escapeJustPressed = false;
		}

		buttons.updateSelected(getCamera());
		if (buttons.isSelectedSelected(getCamera())) {
			int selected = buttons.getSelected();
			selectOption(selected);
		}
	}


	private void selectBack() {
		setScreen(new MainMenuScreen(getGame()));
	}


	private void selectOption(int selected) {
		if (selected == (buttons.getButtonCount() - 1)) {
			selectBack();
			return;
		} else {
			loadGame(selected);
		}
	}


	private void loadGame(int saveNumber) {
		if (SaveHandler.doesSaveFileExist(saveNumber)) {
			Gdx.app.log("LoadGame", "Loading game from save slot " + saveNumber);
			setScreen(new LoadingScreen(getGame(), saveNumber));
		} else {
			Gdx.app.log("LoadGame", "Error - save doesn't exist!");
		}
	}


	@Override
	public void renderScreen(float deltaTime, SpriteBatch batch) {
		clearScreen();
		updateAndSetCamera();

		batch.begin();
		gui.render(batch);
		buttons.render(batch);
		batch.end();
	}
}