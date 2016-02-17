package com.muscovy.game.screen;


import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.Levels;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.TextureMap;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.input.Action;
import com.muscovy.game.input.ControlMap;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Base class for screens to inherit. Implements LibGDX screen and provides helper functions.
 */
public abstract class ScreenBase implements Screen {
	private final MuscovyGame game;


	public ScreenBase(MuscovyGame game) {
		this.game = game;
	}


	public abstract void updateScreen(float deltaTime);


	public abstract void renderScreen(float deltaTime, SpriteBatch batch);


	@Override
	public void render(float deltaTime) {
		updateScreen(deltaTime);
		renderScreen(deltaTime, getBatch());
	}


	/**
	 * @param font
	 * @param text
	 * @return
	 */
	public int getTextWidth(BitmapFont font, String text) {
		GlyphLayout glyphLayout = new GlyphLayout(font, text);
		return (int) glyphLayout.width;
	}


	/**
	 *
	 */
	public void clearScreen() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}


	/**
	 *
	 */
	public void updateAndSetCamera() {
		getCamera().update();
		getBatch().setProjectionMatrix(getCamera().combined);
	}


	/**
	 * @param screen
	 */
	public void setScreen(Screen screen) {
		game.setScreen(screen);
	}


	/**
	 * @param action
	 * @return
	 */
	public float getStateForAction(Action action) {
		return getControlMap().getStateForAction(action, getController());
	}


	/**
	 * @param actions
	 * @return
	 */
	public float getStateForActions(Action... actions) {
		return getControlMap().getStateForActions(getController(), actions);
	}


	/**
	 * @param action
	 * @return
	 */
	public boolean isStateForAction(Action action) {
		return (getStateForAction(action) > 0);
	}


	/**
	 * @param actions
	 * @return
	 */
	public boolean isStateForActions(Action... actions) {
		return (getStateForActions(actions) > 0);
	}


	@Override
	public void show() {
	}


	@Override
	public void resize(int width, int height) {
	}


	@Override
	public void pause() {
	}


	@Override
	public void resume() {
	}


	@Override
	public void hide() {
	}


	@Override
	public void dispose() {
	}


	/**
	 * @return
	 */
	public MuscovyGame getGame() {
		return game;
	}


	/**
	 * @return
	 */
	public SpriteBatch getBatch() {
		return game.getBatch();
	}


	/**
	 * @return
	 */
	public OrthographicCamera getCamera() {
		return game.getCamera();
	}


	/**
	 * @return
	 */
	public TextureMap getTextureMap() {
		return game.getTextureMap();
	}


	/**
	 * @return
	 */
	public Levels getLevels() {
		return game.getLevels();
	}


	/**
	 * @return
	 */
	public Random getRandom() {
		return game.getRandom();
	}


	/**
	 * @return
	 */
	public PlayerCharacter getPlayerCharacter() {
		return game.getPlayerCharacter();
	}


	/**
	 * @return
	 */
	public ControlMap getControlMap() {
		return game.getControlMap();
	}


	/**
	 * @return
	 */
	public Controller getController() {
		return game.getController();
	}


	/**
	 * @return
	 */
	public int getWindowWidth() {
		return game.getWindowWidth();
	}


	/**
	 * @return
	 */
	public int getWindowHeight() {
		return game.getWindowHeight();
	}


	/**
	 * @return
	 */
	public int getTileSize() {
		return game.getTileSize();
	}


	/**
	 * @return
	 */
	public int getWallWidth() {
		return game.getWallWidth();
	}


	/**
	 * @return
	 */
	public int getWallEdge() {
		return game.getWallEdge();
	}
}
