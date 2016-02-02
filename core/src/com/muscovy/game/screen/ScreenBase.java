package com.muscovy.game.screen;


import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.Levels;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.TextureMap;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.input.Action;
import com.muscovy.game.input.ControlMap;


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


	public void clearScreen() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}


	public void updateAndSetCamera() {
		getCamera().update();
		getBatch().setProjectionMatrix(getCamera().combined);
	}


	public void setScreen(Screen screen) {
		game.setScreen(screen);
	}


	public float getStateForAction(Action action) {
		return getControlMap().getStateForAction(action, getController());
	}


	public boolean isStateForAction(Action action) {
		return (getStateForAction(action) > 0);
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


	public MuscovyGame getGame() {
		return game;
	}


	public SpriteBatch getBatch() {
		return game.getBatch();
	}


	public OrthographicCamera getCamera() {
		return game.getCamera();
	}


	public TextureMap getTextureMap() {
		return game.getTextureMap();
	}


	public Levels getLevels() {
		return game.getLevels();
	}


	public Random getRandom() {
		return game.getRandom();
	}


	public PlayerCharacter getPlayerCharacter() {
		return game.getPlayerCharacter();
	}


	public ControlMap getControlMap() {
		return game.getControlMap();
	}


	public Controller getController() {
		return game.getController();
	}


	public int getWindowWidth() {
		return game.getWindowWidth();
	}


	public int getWindowHeight() {
		return game.getWindowHeight();
	}


	public int getTileSize() {
		return game.getTileSize();
	}


	public int getWallWidth() {
		return game.getWallWidth();
	}


	public int getWallEdge() {
		return game.getWallEdge();
	}
}
