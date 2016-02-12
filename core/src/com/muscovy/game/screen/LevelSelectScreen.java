package com.muscovy.game.screen;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.enums.LevelType;
import com.muscovy.game.gui.GUI;
import com.muscovy.game.input.Action;


/**
 *
 */
public class LevelSelectScreen extends ScreenBase {
	private LevelType mapSelected;

	private GUI gui;
	private Sprite guiMapSprite;
	private Sprite guiSelector;
	private Texture availableLevel;
	private Texture unavailableLevel;

	// Allows the player to move selection 1 at a time
	// They have to let go of the button to move again
	private int previousDirection = 0;

	// As enter would have been pressed when they entered this screen from main menu
	// It'd still be pressed by the time update gets called
	// This requires player to let go of enter first
	private boolean enterJustPressed = true;
	private boolean escapeJustPressed = true;


	public LevelSelectScreen(MuscovyGame game) {
		this(game, LevelType.CONSTANTINE);
	}


	public LevelSelectScreen(MuscovyGame game, LevelType selectedLevel) {
		super(game);

		mapSelected = selectedLevel;
		initialiseGui();
		cursorUpdate();

		getGame().saveCurrentGame();
	}


	/**
	 *
	 */
	private void initialiseGui() {
		availableLevel = getTextureMap().getTextureOrLoadFile(AssetLocations.SELECTOR);
		unavailableLevel = getTextureMap().getTextureOrLoadFile(AssetLocations.BAD_SELECTOR);

		guiSelector = new Sprite(availableLevel);

		guiMapSprite = new Sprite(getTextureMap().getTextureOrLoadFile(AssetLocations.HES_EAST_MAP));
		guiMapSprite.setX(0);
		guiMapSprite.setY((getWindowHeight() - guiMapSprite.getTexture().getHeight()) / 2);

		gui = new GUI();
		gui.addElement(guiMapSprite);
		gui.addElement(guiSelector);
	}


	@Override
	public void updateScreen(float deltaTime) {
		int moveDirection = 0;
		if (isStateForActions(Action.WALK_DOWN, Action.SHOOT_DOWN, Action.DPAD_DOWN)
				&& (mapSelected.ordinal() < (LevelType.LEVEL_COUNT - 1))) {
			moveDirection = 1;
		}

		if (isStateForActions(Action.WALK_UP, Action.SHOOT_UP, Action.DPAD_UP) && (mapSelected.ordinal() > 0)) {
			moveDirection = -1;
		}

		final boolean allowSelectAnyLevel = false;

		boolean shouldAdvance = false;
		if ((moveDirection != 0) && (moveDirection != previousDirection)) {
			if (allowSelectAnyLevel) {
				shouldAdvance = true;
			} else if (moveDirection < 0) {
				shouldAdvance = true;
			} else if (getLevels().isLevelCompleted(mapSelected)) {
				shouldAdvance = true;
			}
		}
		if (shouldAdvance) {
			mapSelected = LevelType.advanceLevel(mapSelected, moveDirection);
		}
		previousDirection = moveDirection;

		if (isStateForAction(Action.ENTER)) {
			if (!enterJustPressed) {
				setScreen(new GameScreen(getGame(), getLevels().getLevel(mapSelected.ordinal())));
				return;
			}
			enterJustPressed = true;
		} else {
			enterJustPressed = false;
		}

		if (isStateForAction(Action.ESCAPE)) {
			if (!escapeJustPressed) {
				setScreen(new MainMenuScreen(getGame()));
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

		cursorUpdate();

		batch.begin();
		gui.render(batch);
		batch.end();
	}


	/**
	 *
	 */
	public void cursorUpdate() {
		if (!getLevels().isLevelCompleted(mapSelected.ordinal())) {
			guiSelector.setTexture(availableLevel);
		} else {
			guiSelector.setTexture(unavailableLevel);
		}

		switch (mapSelected) {
		case CONSTANTINE:
			guiSelector.setX(950);
			guiSelector.setY(680);
			break;

		case LANGWITH:
			guiSelector.setX(650);
			guiSelector.setY(600);
			break;

		case GOODRICKE:
			guiSelector.setX(300);
			guiSelector.setY(600);
			break;

		case LMB:
			guiSelector.setX(230);
			guiSelector.setY(420);
			break;

		case CATALYST:
			guiSelector.setX(110);
			guiSelector.setY(360);
			break;

		case TFTV:
			guiSelector.setX(160);
			guiSelector.setY(230);
			break;

		case COMP_SCI:
			guiSelector.setX(330);
			guiSelector.setY(270);
			break;

		case RCH:
			guiSelector.setX(440);
			guiSelector.setY(340);
			break;
		}
	}
}
