package com.muscovy.game;


import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.muscovy.game.input.ControlMap;
import com.muscovy.game.input.ControlMapCreator;
import com.muscovy.game.input.controller.ControllerHelper;
import com.muscovy.game.screen.LoadingScreen;


/**
 * If you need a hand, email me on ewh502
 */
public class MuscovyGame extends Game {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private TextureMap textureMap;
	private Levels levels;
	private Random random;

	private ControlMap controlMap;
	private Controller controller;

	private static final int WINDOW_WIDTH = 1280;
	private static final int WINDOW_HEIGHT = 816; // 960;
	private static final int TILE_SIZE = 64;
	private static final int WORLD_HEIGHT = 768; // WINDOW_HEIGHT - TOP_GUI_SIZE; // 768
	private static final int TOP_GUI_SIZE = MuscovyGame.WINDOW_HEIGHT - MuscovyGame.WORLD_HEIGHT; // 192;


	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, getWindowWidth(), getWindowHeight());
		textureMap = new TextureMap();

		initialiseInput();

		resetGame();
	}


	public void initialiseInput() {
		ControllerHelper.setupControllers();
		controller = ControllerHelper.getFirstControllerOrNull();
		String controllerName = ControllerHelper.getControllerName(controller);
		controlMap = ControlMapCreator.newDefaultControlMap(controllerName);
		if (!ControlMapCreator.isControllerKnown(controllerName)) {
			controller = null;
			Gdx.app.log("Controller", "Controller not known");
		}
	}


	public void resetGame() {
		levels = null;
		random = new Random();
		setScreen(new LoadingScreen(this));
	}


	@Override
	public void render() {
		super.render();
	}


	@Override
	public void dispose() {
		textureMap.disposeAllTextures();
		super.dispose();
	}


	public SpriteBatch getBatch() {
		return batch;
	}


	public OrthographicCamera getCamera() {
		return camera;
	}


	public TextureMap getTextureMap() {
		return textureMap;
	}


	public Levels getLevels() {
		return levels;
	}


	public void setLevels(Levels levels) {
		this.levels = levels;
	}


	public Random getRandom() {
		return random;
	}


	public ControlMap getControlMap() {
		return controlMap;
	}


	public Controller getController() {
		return controller;
	}


	public int getWindowWidth() {
		return MuscovyGame.WINDOW_WIDTH;
	}


	public int getWindowHeight() {
		return MuscovyGame.WINDOW_HEIGHT;
	}


	public int getTileSize() {
		return MuscovyGame.TILE_SIZE;
	}


	public int getWorldHeight() {
		return MuscovyGame.WORLD_HEIGHT;
	}


	public int getTopGuiSize() {
		return MuscovyGame.TOP_GUI_SIZE;
	}
}