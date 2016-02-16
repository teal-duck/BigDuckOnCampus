package com.muscovy.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.gui.ButtonList;
import com.muscovy.game.gui.GUI;
import com.muscovy.game.input.Action;


//TODO: Add different settings to setting screen
public class SettingsScreen extends ScreenBase {
	private static final String[] BUTTON_TEXTS = new String[] { "Back" };
	private GUI gui;
	private BitmapFont settingsFont;
	private BitmapFont font;
	private boolean escapeJustPressed = true;
	private ButtonList buttons;


	public SettingsScreen(MuscovyGame game) {
		super(game);
		font = AssetLocations.newFont32();
		font.setColor(Color.WHITE);
		settingsFont = AssetLocations.newFont20();
		settingsFont.setColor(Color.RED);
		initialiseGui();
	}


	/**
	 *
	 */
	private void initialiseGui() {

		gui = new GUI();
		gui.addData("Settings", "Settings", font, (getWindowWidth() / 2) - 130, getWindowWidth() / 2);
		gui.addData("Settings", "Currently there are no Settings", settingsFont, (getWindowWidth() / 2) - 300,
				(getWindowWidth() / 2) - 100);
		// gui.addData("Settings", "Deal with it", settingsFont, (getWindowWidth() / 2) - 130,
		// (getWindowWidth() / 2) - 200);

		buttons = new ButtonList(SettingsScreen.BUTTON_TEXTS, font, getTextureMap(), getControlMap(),
				getController());
		setButtonLocations();
	}


	private void setButtonLocations() {
		int x = (getWindowWidth() / 2) - (ButtonList.BUTTON_WIDTH / 2);
		ButtonList.getHeightForDefaultButtonList(SettingsScreen.BUTTON_TEXTS.length);

		int y = (getWindowHeight() / 6)
				+ ButtonList.getHeightForDefaultButtonList(SettingsScreen.BUTTON_TEXTS.length);
		buttons.setPositionDefaultSize(x, y);
	}


	@Override
	public void updateScreen(float deltaTime) {

		if (isStateForAction(Action.ESCAPE)) {
			if (!escapeJustPressed) {
				Gdx.app.exit();
				return;
			}
			escapeJustPressed = true;
		} else {
			escapeJustPressed = false;
		}

		buttons.updateSelected(getCamera());
		if (buttons.isSelectedSelected(getCamera())) {
			setScreen(new MainMenuScreen(getGame()));
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


	@Override
	public void dispose() {
		super.dispose();
		settingsFont.dispose();
	}
}
