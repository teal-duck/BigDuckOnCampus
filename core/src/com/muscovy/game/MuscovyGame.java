package com.muscovy.game;


import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.input.ControlMap;
import com.muscovy.game.input.ControlMapCreator;
import com.muscovy.game.input.controller.ControllerHelper;
import com.muscovy.game.level.Level;
import com.muscovy.game.save.control.SaveControls;
import com.muscovy.game.save.game.SaveData;
import com.muscovy.game.save.game.SaveGame;
import com.muscovy.game.screen.MainMenuScreen;


/**
 * If you need a hand, email me on ewh502
 */
/**
 * @author ben
 *
 */
public class MuscovyGame extends Game {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private TextureMap textureMap;

	private Levels levels;
	private Random random;

	private PlayerCharacter playerCharacter;
	private ControlMap controlMap;
	private Controller controller;

	private int currentSaveNumber = -1;

	private static final int WINDOW_WIDTH = 1344; // 1280;
	private static final int WINDOW_HEIGHT = 832; // 816; // 960;
	private static final int TILE_SIZE = 64;
	private static final int WALL_EDGE = 32; // WALL_WIDTH - TILE_SIZE;
	private static final int WALL_WIDTH = MuscovyGame.TILE_SIZE + MuscovyGame.WALL_EDGE; // 96;

	private float time = 0;
	private int frames = 0;
	private boolean logFPS = false;


	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, getWindowWidth(), getWindowHeight());
		textureMap = new TextureMap();

		initialiseInput();
		initialisePlayerCharacter();

		SaveControls saveControls = new SaveControls(this);
		saveControls.saveToFile(controlMap, AssetLocations.CONTROLS_FILE);
		ControlMap loadedControls = saveControls.loadFromFile(AssetLocations.CONTROLS_FILE);
		System.out.println(loadedControls);

		resetGame();
	}


	/**
	 *
	 */
	public void resetGame() {
		levels = null;
		random = MathUtils.random;

		setScreen(new MainMenuScreen(this));
	}


	/**
	 * Sets up controllers and the control map.
	 */
	public void initialiseInput() {
		ControllerHelper.setupControllers();
		controller = ControllerHelper.getFirstControllerOrNull();
		String controllerName = ControllerHelper.getControllerName(controller);

		// TODO: Load control map from file
		controlMap = ControlMapCreator.newDefaultControlMap(controllerName);
		if (!ControlMapCreator.isControllerKnown(controllerName)) {
			controller = null;
			Gdx.app.log("Controller", "Controller not known");
		}
	}


	/**
	 *
	 */
	private void initialisePlayerCharacter() {
		// TODO: Player stats need to be passed to initialisePlayerCharacter
		Sprite playerSprite = new Sprite();
		playerSprite.setRegion(getTextureMap().getTextureOrLoadFile(AssetLocations.PLAYER));

		float playerStartX = getWindowWidth() / 2;
		float playerStartY = getWindowHeight() / 2;

		playerStartX -= playerSprite.getRegionWidth() / 2;
		playerStartY -= playerSprite.getRegionHeight() / 2;

		Vector2 playerStartPosition = new Vector2(playerStartX, playerStartY);
		playerCharacter = new PlayerCharacter(this, AssetLocations.PLAYER, playerStartPosition, getControlMap(),
				getController());
		playerSprite.setRegion(getTextureMap().getTextureOrLoadFile(AssetLocations.PLAYER));
	}


	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		calculateFPS(deltaTime);
		super.render();
	}


	/**
	 * @param deltaTime
	 */
	private void calculateFPS(float deltaTime) {
		time += deltaTime;
		frames += 1;

		while (time >= 1) {
			String fpsText = "Calculated FPS: " + frames + "; Libgdx FPS: "
					+ Gdx.graphics.getFramesPerSecond();
			if (logFPS) {
				Gdx.app.log("FPS", fpsText);
			}
			frames = 0;
			time -= 1;
		}
	}


	@Override
	public void dispose() {
		textureMap.disposeAllTextures();
		super.dispose();
	}


	/**
	 * @param saveData
	 */
	public void initialiseFromSaveData(SaveData saveData) {
		playerCharacter = saveData.getPlayer();
		levels = saveData.getLevels();
	}


	/**
	 * @param saveNumber
	 * @param data
	 */
	public void saveDataToFile(int saveNumber, SaveData data) {
		Gdx.app.log("Load", "Saving game");
		SaveGame saveGame = new SaveGame(this);
		saveGame.saveToFile(data, SaveHandler.getFileForSaveNumber(saveNumber));
	}


	/**
	 *
	 */
	public void saveCurrentGame() {
		saveDataToFile(getCurrentSaveNumber(), getSaveData());
	}


	/**
	 * @return
	 */
	public SaveData getSaveData() {
		return new SaveData(playerCharacter, levels);
	}


	/**
	 * @param saveNumber
	 */
	public void setCurrentSaveNumber(int saveNumber) {
		currentSaveNumber = saveNumber;
	}


	/**
	 * @return
	 */
	public int getCurrentSaveNumber() {
		return currentSaveNumber;
	}


	/**
	 * @return
	 */
	public SpriteBatch getBatch() {
		return batch;
	}


	/**
	 * @return
	 */
	public OrthographicCamera getCamera() {
		return camera;
	}


	/**
	 * @return
	 */
	public TextureMap getTextureMap() {
		return textureMap;
	}


	/**
	 * @return
	 */
	public Levels getLevels() {
		return levels;
	}


	/**
	 * @param levels
	 */
	public void setLevels(Levels levels) {
		this.levels = levels;
	}
	
	/**
	 * 
	 * @return True if all levels have been completed.
	 */
	public boolean areAllLevelsCompleted() {
		return levels.areAllLevelsCompleted();
	}


	/**
	 * @return
	 */
	public Random getRandom() {
		return random;
	}


	/**
	 * @return
	 */
	public PlayerCharacter getPlayerCharacter() {
		return playerCharacter;
	}


	/**
	 * @return
	 */
	public ControlMap getControlMap() {
		return controlMap;
	}


	/**
	 * @return
	 */
	public Controller getController() {
		return controller;
	}


	/**
	 * @return
	 */
	public int getWindowWidth() {
		return MuscovyGame.WINDOW_WIDTH;
	}


	/**
	 * @return
	 */
	public int getWindowHeight() {
		return MuscovyGame.WINDOW_HEIGHT;
	}


	/**
	 * @return
	 */
	public int getTileSize() {
		return MuscovyGame.TILE_SIZE;
	}


	/**
	 * @return
	 */
	public int getWallWidth() {
		return MuscovyGame.WALL_WIDTH;
	}


	/**
	 * @return
	 */
	public int getWallEdge() {
		return MuscovyGame.WALL_EDGE;
	}
}