package com.muscovy.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.GUI;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.input.Action;

public class MainMenuScreen extends ScreenBase {
	private GUI gui;
	private boolean escapeJustPressed = true;
	private ButtonList buttons;
	private BitmapFont font;
	
	private static final int NEW_GAME = 0;
	private static final int LOAD_GAME = 1;
	private static final int SETTINGS = 2;
	private static final int QUIT = 3;
	private static final String[] BUTTON_TEXTS = new String[] { "New Game", "Load Game", "Settings", "Quit" };

	public MainMenuScreen(MuscovyGame game) {
		super(game);
		font = AssetLocations.newFont32();
		font.setColor(Color.WHITE);
		initialiseGuis();
		draw();
	}

	private void initialiseGuis() {
		Sprite background = new Sprite(getTextureMap().getTextureOrLoadFile(AssetLocations.MAIN_MENU));
		background.setX(0);
		background.setY(getWindowHeight() - background.getTexture().getHeight());
		
		gui = new GUI();
		gui.addElement(background);
		
		buttons = new ButtonList(MainMenuScreen.BUTTON_TEXTS, font, getCamera(), getControlMap(),
				getController());
		setButtonLocations();
	}

	private void setButtonLocations() {
		int x = (getWindowWidth() / 2) - (ButtonList.BUTTON_WIDTH / 2); 
				ButtonList.getHeightForDefaultButtonList(MainMenuScreen.BUTTON_TEXTS.length);
				
		int y =	(getWindowHeight() / 6) - ButtonList.WINDOW_EDGE_OFFSET + ButtonList
				.getHeightForDefaultButtonList(MainMenuScreen.BUTTON_TEXTS.length);
		buttons.setPositionDefaultSize(x, y);
	}


	@Override
	public void resize(int width, int height) {
		setButtonLocations();
	}


	/**
	 * @param selected
	 */
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
	}

	@Override
	public void renderScreen(float deltaTime, SpriteBatch batch) {
		clearScreen();
		updateAndSetCamera();

		batch.begin();
		gui.render(batch);
		batch.end();
		
		buttons.updateSelected();
		if (buttons.isSelectedSelected()) {
			int selected = buttons.getSelected();
			selectOption(selected);
		}
		draw();
	}
	

	private void draw() {
		clearScreen();
		SpriteBatch batch = getBatch();
		buttons.render(batch);
	}
	

	private void selectNewGame() {
		this.setScreen(new LevelSelectScreen(getGame()));
	}


	private void selectLoadGame() {
	}

	private void selectSettings() {
	}

	private void selectQuit() {
		Gdx.app.exit();
	}
}
