package com.muscovy.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.GUI;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.input.Action;


public class GameOverScreen extends ScreenBase {
	private GUI gui;
	private BitmapFont gameOverFont;
	private boolean enterJustPressed = true;
	private boolean escapeJustPressed = true;


	public GameOverScreen(MuscovyGame game) {
		super(game);

		initialiseGui();
	}


	private void initialiseGui() {
		gameOverFont = AssetLocations.newFont20();
		gameOverFont.setColor(Color.RED);

		gui = new GUI();
		gui.addData("Gameover", "Game Over", gameOverFont, (getWindowWidth() / 2) - 80, 150);
		gui.addData("Gameover", "Press ENTER to restart and ESC to quit", gameOverFont,
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
		// batch.draw(playerCharacter.getSprite().getTexture(), playerCharacter.getX(),
		// playerCharacter.getY());
		batch.end();
	}


	@Override
	public void dispose() {
		super.dispose();
		gameOverFont.dispose();
	}
}
