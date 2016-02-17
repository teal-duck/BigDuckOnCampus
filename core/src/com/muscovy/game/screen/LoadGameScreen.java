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


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Presents a list of save files to the user for them to choose one to load/delete.
 */
public class LoadGameScreen extends ScreenBase {
	private String[] savedGameTexts;
	private static final String[] optionsTexts = new String[] { "Load", "Delete", "Back" };
	private static final int LOAD = 0;
	private static final int DELETE = 1;
	private static final int BACK = 2;

	// The button indices don't line up with the actual save numbers
	// E.g. if only saves 1 and 3 exist, the buttons would have indices 0 and 1 anyway
	// And show text "2" and "4"
	private int[] buttonToSaveNumberMap;

	private boolean escapeJustPressed = true;

	private GUI backgroundGui;
	private GUI selectedGameGui;
	private static final String SELECTED_GAME_NAME = "selectedGame";

	private ButtonList savedGameButtons;
	private ButtonList optionsButtons;
	private BitmapFont font;

	// -1 means no game selected
	private int selectedGameNumber = -1;


	public LoadGameScreen(MuscovyGame game) {
		super(game);
		font = AssetLocations.newFont32();
		font.setColor(Color.WHITE);

		initialiseGuis();
		createSaveGameButtons();
	}


	/**
	 *
	 */
	private void createSaveGameButtons() {
		int existingSaveCount = 0;
		for (int i = 0; i < SaveHandler.MAX_SAVE_COUNT; i += 1) {
			if (SaveHandler.doesSaveFileExist(i)) {
				existingSaveCount += 1;
			}
		}

		// Only show buttons for saves that exist
		// But their IDs aren't the same indices as in the savedGameTexts array
		savedGameTexts = new String[existingSaveCount + 1];
		buttonToSaveNumberMap = new int[existingSaveCount];
		int insert = 0;
		for (int i = 0; i < SaveHandler.MAX_SAVE_COUNT; i += 1) {
			if (SaveHandler.doesSaveFileExist(i)) {
				savedGameTexts[insert] = "Save #" + (i + 1);
				buttonToSaveNumberMap[insert] = i;
				insert += 1;
			}
		}
		savedGameTexts[savedGameTexts.length - 1] = "Back";

		savedGameButtons = new ButtonList(savedGameTexts, font, getTextureMap(), getControlMap(),
				getController());
		setButtonLocations();
	}


	/**
	 *
	 */
	private void initialiseGuis() {
		Sprite background = new Sprite(getTextureMap().getTextureOrLoadFile(AssetLocations.MAIN_MENU));
		background.setX(0);
		background.setY(getWindowHeight() - background.getTexture().getHeight());

		backgroundGui = new GUI();
		backgroundGui.addElement(background);

		selectedGameGui = new GUI();
		selectedGameGui.addData(LoadGameScreen.SELECTED_GAME_NAME, "-", font, 0, (getWindowHeight() / 6)
				+ ButtonList.getHeightForDefaultButtonList(SaveHandler.MAX_SAVE_COUNT));
		optionsButtons = new ButtonList(LoadGameScreen.optionsTexts, font, getTextureMap(), getControlMap(),
				getController());
	}


	/**
	 *
	 */
	private void setButtonLocations() {
		int x = (getWindowWidth() / 2) - (ButtonList.BUTTON_WIDTH / 2);
		ButtonList.getHeightForDefaultButtonList(savedGameTexts.length);

		int y = (getWindowHeight() / 6) + ButtonList.getHeightForDefaultButtonList(SaveHandler.MAX_SAVE_COUNT);
		savedGameButtons.setPositionDefaultSize(x, y);
		// Push the options down to make soace for the text saying the selected game
		optionsButtons.setPositionDefaultSize(x, y - 50);

		setBackButtonDimensions(savedGameButtons.getButtonAtIndex(savedGameButtons.getButtonCount() - 1));
		setBackButtonDimensions(optionsButtons.getButtonAtIndex(optionsButtons.getButtonCount() - 1));
	}


	/**
	 * Positions the back button in the bottom left corner and shrinks the width.
	 *
	 * @param backButton
	 */
	private void setBackButtonDimensions(Button backButton) {
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
				escapeJustPressed = true;
				return;
			}
			escapeJustPressed = true;
		} else {
			escapeJustPressed = false;
		}

		// Only update the visible list
		if (selectedGameNumber == -1) {
			savedGameButtons.updateSelected(getCamera());
			if (savedGameButtons.isSelectedSelected(getCamera())) {
				int selected = savedGameButtons.getSelected();
				selectGame(selected);
			}
		} else {
			optionsButtons.updateSelected(getCamera());
			if (optionsButtons.isSelectedSelected(getCamera())) {
				int selected = optionsButtons.getSelected();
				selectOption(selected);
			}
		}
	}


	/**
	 * @param newSelectedGame
	 */
	private void changeSelectedGame(int newSelectedGame) {
		selectedGameNumber = newSelectedGame;
		optionsButtons.setSelected(0);

		// This stops the player holding enter and skipping the menus
		savedGameButtons.setEnterJustPushedTrue();
		optionsButtons.setEnterJustPushedTrue();
	}


	/**
	 *
	 */
	private void selectBack() {
		// If player selected back on the saved game selected, go to main menu
		// Else (they selected on the options), go back to pick a game
		if (selectedGameNumber == -1) {
			setScreen(new MainMenuScreen(getGame()));
		} else {
			changeSelectedGame(-1);
		}
	}


	/**
	 * @param selected
	 */
	private void selectGame(int selected) {
		if (selected == (savedGameButtons.getButtonCount() - 1)) {
			selectBack();
		} else {
			changeSelectedGame(buttonToSaveNumberMap[selected]);

			// Copy the text from the button (easiest way to get the # of the save)
			// Position the X so the text is centre justified
			String newText = savedGameButtons.getButtonAtIndex(selected).getText();
			int width = getTextWidth(font, newText);
			int newX = (getWindowWidth() / 2) - (width / 2);
			int y = selectedGameGui.getDataY(LoadGameScreen.SELECTED_GAME_NAME);

			selectedGameGui.editData(LoadGameScreen.SELECTED_GAME_NAME, newText);
			selectedGameGui.moveData(LoadGameScreen.SELECTED_GAME_NAME, newX, y);
		}
	}


	/**
	 * @param selected
	 */
	private void selectOption(int selected) {
		switch (selected) {
		case LOAD:
			loadGame(selectedGameNumber);
			break;
		case DELETE:
			deleteGame(selectedGameNumber);
			break;
		case BACK:
			selectBack();
			break;
		}
	}


	/**
	 * @param saveNumber
	 */
	private void loadGame(int saveNumber) {
		if (SaveHandler.doesSaveFileExist(saveNumber)) {
			Gdx.app.log("LoadGame", "Loading game from save slot " + saveNumber);
			setScreen(new LoadingScreen(getGame(), saveNumber));
		} else {
			Gdx.app.log("LoadGame", "Error - save doesn't exist!");
		}
	}


	/**
	 * @param saveNumber
	 */
	private void deleteGame(int saveNumber) {
		SaveHandler.deleteSaveFile(saveNumber);
		// Recreate the buttons as this save no longer exists
		// So it shouldn't have a button
		createSaveGameButtons();
		// Automatically return to the select game page
		changeSelectedGame(-1);
	}


	@Override
	public void renderScreen(float deltaTime, SpriteBatch batch) {
		clearScreen();
		updateAndSetCamera();

		batch.begin();
		backgroundGui.render(batch);
		if (selectedGameNumber == -1) {
			savedGameButtons.render(batch);
		} else {
			font.setColor(Color.WHITE);
			selectedGameGui.render(batch);
			optionsButtons.render(batch);
		}
		batch.end();
	}


	@Override
	public void dispose() {
		super.dispose();
		font.dispose();
	}
}