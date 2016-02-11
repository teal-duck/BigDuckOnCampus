package com.muscovy.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.gui.GUI;
import com.muscovy.game.input.Action;


/**
 *
 */
public class WinScreen extends ScreenBase {
	private GUI gui;
	private BitmapFont gameOverFont;
	private boolean enterJustPressed = true;
	private boolean escapeJustPressed = true;


	public WinScreen(MuscovyGame game) {
		super(game);

		initialiseGui();
	}


	/**
	 *
	 */
	private void initialiseGui() {
		gameOverFont = AssetLocations.newFont20();
		gameOverFont.setColor(Color.FOREST);

		gui = new GUI();
		gui.addData("Gamewin", "You won!", gameOverFont, (getWindowWidth() / 2) - 80, 150);
		gui.addData("Gamewin", "Press " + Action.ENTER.toString() + " to go to main menu or " + Action.ESCAPE.toString() + " to quit", gameOverFont,
				(getWindowWidth() / 2) - 350, 120);
	}


	@Override
	public void updateScreen(float deltaTime) {
		if (isStateForAction(Action.ENTER)) {
			if (!enterJustPressed) {
				getGame().resetGame();
				return;
			}
			enterJustPressed = true;
		} else {
			enterJustPressed = false;
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


	@Override
	public void dispose() {
		super.dispose();
		gameOverFont.dispose();
	}
}
