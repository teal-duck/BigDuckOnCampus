package com.muscovy.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.SaveHandler;
import com.muscovy.game.gui.ButtonList;
import com.muscovy.game.gui.GUI;
import com.muscovy.game.input.Action;


/**
 *
 */
public class MainMenuScreen extends ScreenBase {
	private static final int NEW_GAME = 0;
	private static final int LOAD_GAME = 1;
	private static final int SETTINGS = 2;
	private static final int QUIT = 3;
	private static final String[] BUTTON_TEXTS = new String[] { "New Game", "Load Game", "Settings", "Quit" };

	private GUI gui;
	private boolean escapeJustPressed = true;
	private ButtonList buttons;
	private BitmapFont font;


	public MainMenuScreen(MuscovyGame game) {
		super(game);
		font = AssetLocations.newFont32();
		font.setColor(Color.WHITE);
		initialiseGuis();
	}


	/**
	 *
	 */
	private void initialiseGuis() {
		Sprite background = new Sprite(getTextureMap().getTextureOrLoadFile(AssetLocations.MAIN_MENU));
		background.setX(0);
		background.setY(getWindowHeight() - background.getTexture().getHeight());

		gui = new GUI();
		gui.addElement(background);

		buttons = new ButtonList(MainMenuScreen.BUTTON_TEXTS, font, getTextureMap(), getControlMap(),
				getController());
		setButtonLocations();
	}


	/**
	 *
	 */
	private void setButtonLocations() {
		int x = (getWindowWidth() / 2) - (ButtonList.BUTTON_WIDTH / 2);
		ButtonList.getHeightForDefaultButtonList(MainMenuScreen.BUTTON_TEXTS.length);

		int y = (getWindowHeight() / 6)
				+ ButtonList.getHeightForDefaultButtonList(MainMenuScreen.BUTTON_TEXTS.length);
		buttons.setPositionDefaultSize(x, y);
	}


	@Override
	public void resize(int width, int height) {
		setButtonLocations();
	}


	@Override
	public void updateScreen(float deltaTime) {
		if (isStateForAction(Action.ESCAPE)) {
			if (!escapeJustPressed) {
				selectQuit();
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


	@Override
	public void renderScreen(float deltaTime, SpriteBatch batch) {
		clearScreen();
		updateAndSetCamera();

		batch.begin();
		gui.render(batch);
		buttons.render(batch);
		batch.end();
	}


	private void selectOption(int selected) {
		switch (selected) {
		case NEW_GAME:
			selectNewGame();
			break;
		case LOAD_GAME:
			selectLoadGame();
			break;
		case SETTINGS:
			selectSettings();
			break;
		case QUIT:
			selectQuit();
			break;
		}
	}


	/**
	 *
	 */
	private void selectNewGame() {
		int saveNumber = getNextUnusedSaveNumber();
		Gdx.app.log("NewGame", "Starting a new game in save slot " + saveNumber);

		if (saveNumber < 0) {
			// TODO: Overwrite save
			Gdx.app.log("TODO", "Overwrite save");
			int overwriteSaveNumber = getSaveSlotToOverwrite();
			Gdx.app.log("OverwriteGame", "Overwiting game in slot " + overwriteSaveNumber);
			SaveHandler.deleteSaveFile(overwriteSaveNumber);
			saveNumber = overwriteSaveNumber;
		}

		setScreen(new LoadingScreen(getGame(), saveNumber));
	}


	/**
	 * @return
	 */
	private int getSaveSlotToOverwrite() {
		// TODO: getSaveSlotToOverwrite
		return 0;
	}


	/**
	 * Returns -1 if there are no free saves.
	 *
	 * @return
	 */
	private int getNextUnusedSaveNumber() {
		for (int saveNumber = 0; saveNumber < SaveHandler.MAX_SAVE_COUNT; saveNumber += 1) {
			if (!SaveHandler.doesSaveFileExist(saveNumber)) {
				return saveNumber;
			}
		}
		return -1;
	}


	/**
	 *
	 */
	private void selectLoadGame() {
		setScreen(new LoadGameScreen(getGame()));
	}


	/**
	 *
	 */
	private void selectSettings() {
		// TODO: Settings
		Gdx.app.log("TODO", "Settings");
	}


	/**
	 *
	 */
	private void selectQuit() {
		Gdx.app.exit();
	}


	@Override
	public void dispose() {
		super.dispose();
		font.dispose();
	}
}
