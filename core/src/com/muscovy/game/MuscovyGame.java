package com.muscovy.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.enums.AttackType;
import com.muscovy.game.enums.GameState;
import com.muscovy.game.enums.LevelType;
import com.muscovy.game.enums.ProjectileDamager;


/**
 * If you need a hand, email me on ewh502
 */
public class MuscovyGame extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	private OrthographicCamera camera;
	private float timer = 0;
	private SpriteBatch batch;
	private PlayerCharacter playerCharacter;
	private GUI mainMenuGUI;
	private GUI dungeonGUI;
	private GUI overworldGUI;
	private GUI pauseGUI;
	private GUI gameOverGUI;
	private Texture availableLevel;
	private Texture unavailableLevel;
	private EntityManager entityManager;

	private boolean keyflagW;
	private boolean keyflagD;
	private boolean keyflagA;
	private boolean keyflagS;
	private boolean keyflagUp;
	private boolean keyflagRight;
	private boolean keyflagLeft;
	private boolean keyflagDown;
	private boolean firing = false;

	private Sprite guiMapSprite;
	private Sprite guiSelector;
	private BitmapFont font;
	private BitmapFont gameOverFont;
	private BitmapFont loading;

	public static final float WINDOW_WIDTH = 1344;
	public static final float WINDOW_HEIGHT = 832; 
	public static final int TILE_SIZE = 64;
	public  static final int WALL_WIDTH = 96;
	public  static final int WALL_EDGE = WALL_WIDTH - TILE_SIZE;


	private GameState gameState;
	private LevelType mapSelected;

	private final int playerStartX = 300;
	private final int playerStartY = 300;

	private TextureMap textureMap;

	private Random random;

	public static final int KEY_UP = Keys.W;
	public static final int KEY_DOWN = Keys.S;
	public static final int KEY_LEFT = Keys.A;
	public static final int KEY_RIGHT = Keys.D;
	public static final int KEY_SHOOT_UP = Keys.UP;
	public static final int KEY_SHOOT_DOWN = Keys.DOWN;
	public static final int KEY_SHOOT_LEFT = Keys.LEFT;
	public static final int KEY_SHOOT_RIGHT = Keys.RIGHT;

	float playerObstacleCollisionSpeed = PlayerCharacter.MAX_SPEED; // 100f;
	float playerEnemyCollisionSpeed = PlayerCharacter.MAX_SPEED; // 100f;
	float playerWallCollisionSpeed = PlayerCharacter.MAX_SPEED; // 200f;


	@Override
	public void create() {
		gameState = GameState.STARTUP;
		textureMap = new TextureMap();

		random = new Random();

		loading = new BitmapFont();
		loading.setColor(Color.WHITE);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MuscovyGame.WINDOW_WIDTH, MuscovyGame.WINDOW_HEIGHT);
		Gdx.input.setInputProcessor(this);
	}


	@Override
	public void dispose() {
		textureMap.disposeAllTextures();
		super.dispose();
	}


	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!(gameState == GameState.STARTUP)) {
			update(deltaTime); // Waiting for 1 render cycle before initialising anything
		}
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		boolean batchEnded = false;

		switch (gameState) {
		case MAIN_MENU:
			mainMenuGUI.render(batch);
			break;

		case OVERWORLD:
			overworldGUI.render(batch);
			break;

		case DUNGEON:
			// entityManager.render(deltaTime, batch);
			// These work by referencing the data you want to change with the first string you pass it
			dungeonGUI.editData("PlayerHealth", "Health: " + MathUtils.floor(playerCharacter.getHealth()));
			dungeonGUI.editData("PlayerScore", "Score: " + playerCharacter.getScore());
			entityManager.render(deltaTime, batch);
			dungeonGUI.render(batch);
			batchEnded = true;
			batch.end();
			entityManager.renderMapOverlay(camera);
			break;

		case PAUSE:
			dungeonGUI.render(batch);
			entityManager.render(deltaTime, batch);
			batch.draw(playerCharacter.getSprite().getTexture(), playerCharacter.getX(),
					playerCharacter.getY());
			pauseGUI.render(batch);
			dungeonGUI.render(batch);
			batchEnded = true;
			batch.end();
			entityManager.renderMapOverlay(camera);
			break;

		case GAME_OVER:
			gameOverGUI.render(batch);
			batch.draw(playerCharacter.getSprite().getTexture(), playerCharacter.getX(),
					playerCharacter.getY());
			break;

		default:
			loading.draw(batch, "RANDOMLY GENERATING LEVELS", (MuscovyGame.WINDOW_WIDTH / 2) - 150,
					MuscovyGame.WINDOW_HEIGHT / 2);
			gameState = GameState.LOADING; // Indicates that the render cycle has finished
			break;
		}

		if (!batchEnded) {
			batch.end();
		}
	}


	/**
	 * INITIALISATION METHODS
	 */
	private void initialiseLevels() {
		entityManager = new EntityManager(random, textureMap);
		entityManager.generateLevels();
	}


	private void initialiseGUIs() {
		/**
		 * Setting up GUI stuff, with buttons, and text.
		 */
		Sprite mainMenuSprite = new Sprite();
		Sprite mainMenuStartButton = new Sprite();
		// Sprite mainMenuLoadButton = new Sprite();
		// Sprite mainMenuSettingsButton = new Sprite();
		// Sprite guiMapSprite = new Sprite();
		Sprite guiDungeonSprite = new Sprite();

		// Main menu
		mainMenuGUI = new GUI();
		mainMenuSprite.setTexture(textureMap.getTextureOrLoadFile(AssetLocations.MAIN_MENU));
		mainMenuSprite.setX(0);
		mainMenuSprite.setY(MuscovyGame.WINDOW_HEIGHT - mainMenuSprite.getTexture().getHeight());

		mainMenuStartButton.setTexture(textureMap.getTextureOrLoadFile(AssetLocations.START_GAME_BUTTON));
		mainMenuStartButton.setCenter(MuscovyGame.WINDOW_WIDTH, MuscovyGame.WINDOW_HEIGHT);
		mainMenuStartButton.setX((MuscovyGame.WINDOW_WIDTH - 392) / 2);
		mainMenuStartButton.setY(MuscovyGame.WINDOW_HEIGHT / 2);
		mainMenuGUI.addElement(mainMenuSprite);
		mainMenuGUI.addElement(mainMenuStartButton);

		// Dungeon
		dungeonGUI = new GUI();
		//guiDungeonSprite.setTexture(textureMap.getTextureOrLoadFile(AssetLocations.GUI_FRAME));
		//guiDungeonSprite.setX(0);
		//guiDungeonSprite.setY(0);
		//dungeonGUI.addElement(guiDungeonSprite);
		//font = new BitmapFont();
		font = new BitmapFont(Gdx.files.internal(AssetLocations.FONT_FNT), Gdx.files.internal(AssetLocations.FONT_PNG), false);
		//font.setColor(Color.BLACK);

		int dungeonGuiY = (int) (MuscovyGame.WINDOW_HEIGHT ); // 900;
		dungeonGUI.addData("PlayerHealth", "Health: " + String.valueOf(playerCharacter.getHealth()), font, 20,
				dungeonGuiY - 20);
		dungeonGUI.addData("PlayerScore", "Score: " + String.valueOf(playerCharacter.getScore()), font, 20,
				dungeonGuiY - 50);

		// GameOver
		gameOverFont = new BitmapFont();
		gameOverFont.setColor(Color.RED);
		gameOverGUI = new GUI();
		gameOverGUI.addData("Gameover", "Game Over", gameOverFont, 640, 150);
		gameOverGUI.addData("Gameover", "Press ENTER to restart and ESC to quit", gameOverFont, 640, 130);

		// Pause
		pauseGUI = new GUI();
		pauseGUI.addData("Pause", "PAUSE", gameOverFont, (int) (MuscovyGame.WINDOW_WIDTH / 2), 720 / 2);
	}


	private void initialisePlayerCharacter() {
		Sprite playerSprite = new Sprite();
		playerSprite.setRegion(textureMap.getTextureOrLoadFile(AssetLocations.PLAYER));
		Vector2 playerStartPosition = new Vector2(playerStartX, playerStartY);
		playerCharacter = new PlayerCharacter(playerSprite, playerStartPosition, textureMap);
		// playerCharacter.setX(playerStartX);
		// playerCharacter.setY(playerStartY);
	}


	public void initaliseOverworld() {
		mapSelected = LevelType.CONSTANTINE;

		guiMapSprite = new Sprite();
		guiSelector = new Sprite();
		overworldGUI = new GUI();

		availableLevel = textureMap.getTextureOrLoadFile(AssetLocations.SELECTOR);
		unavailableLevel = textureMap.getTextureOrLoadFile(AssetLocations.BAD_SELECTOR);

		guiSelector.setTexture(availableLevel);

		cursorUpdate();

		guiMapSprite.setTexture(textureMap.getTextureOrLoadFile(AssetLocations.HES_EAST_MAP));
		guiMapSprite.setX(0);
		guiMapSprite.setY((MuscovyGame.WINDOW_HEIGHT - guiMapSprite.getTexture().getHeight()) / 2);
		overworldGUI.addElement(guiMapSprite);
		overworldGUI.addElement(guiSelector);
	}


	/**
	 * UPDATE METHODS
	 */
	public void update(float deltaTime) {
		/**
		 * Uses gameState to update game as needed for each state. Waits for 1 render cycle before initialising
		 * everything (case 102), so the loading screen can be displayed
		 */
		switch (gameState) {
		case OVERWORLD:
			cursorUpdate();
			break;

		case DUNGEON:
			playerUpdate(deltaTime);
			playerCharacter.update(deltaTime);
			projectilesUpdate(deltaTime);
			enemiesUpdate(deltaTime);

			if (playerCharacter.getHealth() <= 0) {
				gameState = GameState.GAME_OVER;
			}

			collision();
			cleanUpDeadThings();

			entityManager.checkCurrentLevelCompletion();

			if (timer > 10) {
				timer = 0; // Useful for debugging. Put a break point here if you need to see the
						// variables after 10 seconds
			}
			timer += deltaTime;
			break;

		case LOADING:
			initialisePlayerCharacter();
			initialiseLevels();
			initialiseGUIs();
			initaliseOverworld();
			gameState = GameState.MAIN_MENU;
			break;
		default:
			break;
		}

	}


	public void enemiesUpdate(float deltaTime) {
		if (entityManager.getRoomTimer() > 2) {
			for (Enemy enemy : entityManager.getEnemies()) {
				enemy.update(deltaTime); // , playerCharacter);
				if ((enemy.getAttackType() != AttackType.TOUCH) && (enemy.checkRangedAttack())) {
					entityManager.addNewProjectiles(enemy.rangedAttack(playerCharacter));
				}

				playerEnemyCollision(enemy);
				enemyWallCollision(enemy);
				playerEnemyCollision(enemy);
				enemyWallCollision(enemy);
			}
		}
	}


	/**
	 * Checks projectile collision with walls, and player, and updates projectile
	 *
	 * @param deltaTime
	 */
	public void projectilesUpdate(float deltaTime) {

		ArrayList<Projectile> projectileList = new ArrayList<Projectile>(entityManager.getProjectiles());
		for (Projectile projectile : projectileList) {
			projectile.update(deltaTime);
			projectileWallCollision(projectile);
			projectilePlayerCollision(projectile);
		}
	}


	public void cleanUpDeadThings() {
		entityManager.killEnemies();
		entityManager.killProjectiles();
	}


	public void playerUpdate(float deltaTime) {
		playerMovement(deltaTime);
		playerAttack(deltaTime);
	}


	public void playerAttack(float deltaTime) {
		if (firing) {
			if (playerCharacter.checkRangedAttack(deltaTime)) {
				if (keyflagRight) {
					playerCharacter.setShotDirection(1, 0);
				}
				if (keyflagLeft) {
					playerCharacter.setShotDirection(-1, 0);
				}
				if (keyflagUp) {
					playerCharacter.setShotDirection(0, 1);
				}
				if (keyflagDown) {
					playerCharacter.setShotDirection(0, -1);
				}
				entityManager.addNewProjectiles(playerCharacter.rangedAttack());
			}
		} else {
			if (playerCharacter.getTimeSinceLastAttack() < 0.5) {
				playerCharacter.incrementTimeSinceLastAttack(deltaTime);
			}
		}
	}


	public void playerMovement(float deltaTime) {
		// if (keyflagW) {
		// playerCharacter.goUp(deltaTime);
		// playerCharacter.movementAnimation();
		// }
		// if (!keyflagW && keyflagS) {
		// playerCharacter.goDown(deltaTime);
		// playerCharacter.movementAnimation();
		// }
		// if (keyflagD) {
		// playerCharacter.goRight(deltaTime);
		// playerCharacter.movementAnimation();
		// }
		// if (!keyflagD && keyflagA) {
		// playerCharacter.goLeft(deltaTime);
		// playerCharacter.movementAnimation();
		// }
		//
		// if (!keyflagD && !keyflagA) {
		// playerCharacter.decelXToStop(deltaTime);
		// }
		// if (!keyflagW && !keyflagS) {
		// playerCharacter.decelYToStop(deltaTime);
		// }
	}


	public void cursorUpdate() {
		if (!entityManager.isLevelCompleted(mapSelected.ordinal())) {
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


	/**
	 * Iterates through all enemies and obstacles, and calculates collisions with each other and player character
	 * Could be optimised, but at the moment, doesn't chug, even with many enemies, obstacles and projectiles
	 */
	public void collision() {
		ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>(entityManager.getObstacles());
		ArrayList<Enemy> enemyList = new ArrayList<Enemy>(entityManager.getEnemies());
		ArrayList<Projectile> projectileList = new ArrayList<Projectile>(entityManager.getProjectiles());
		ArrayList<Item> itemList = new ArrayList<Item>(entityManager.getItems());

		playerCharacter.setMaxSpeed(PlayerCharacter.MAX_SPEED);
		playerCharacter.setSpeedToMax();

		for (Obstacle obstacle : obstacleList) {
			playerObstacleCollision(obstacle);
			for (Enemy enemy : enemyList) {
				enemyObstacleCollision(enemy, obstacle);
			}
		}

		playerWallCollision();

		for (Item item : itemList) {
			playerItemCollection(item);
		}

		if (entityManager.getCurrentDungeonRoom().areAllEnemiesDead()) {
			playerDoorCollision();
		} else {
			for (Enemy enemy : enemyList) {
				for (Projectile projectile : projectileList) {
					projectileEnemyCollision(projectile, enemy);
				}
			}
		}

		for (Obstacle obstacle : obstacleList) {
			for (Projectile projectile : projectileList) {
				projectileObstacleCollision(projectile, obstacle);
			}
		}

	}


	public void projectileEnemyCollision(Projectile projectile, Enemy enemy) {
		if ((Intersector.overlaps(enemy.getCircleHitbox(), projectile.getCollisionBox()))
				&& !(projectile.getDamagesWho() == ProjectileDamager.PLAYER)) {
			projectile.killSelf();
			enemy.takeDamage(projectile.getDamage());
		}
	}


	public void projectileObstacleCollision(Projectile projectile, Obstacle obstacle) {
		if (Intersector.overlaps(projectile.getCollisionBox(), obstacle.getRectangleHitbox())) {
			projectile.killSelf();
		}
	}


	public void projectilePlayerCollision(Projectile projectile) {
		if ((Intersector.overlaps(playerCharacter.getCircleHitbox(), projectile.getCollisionBox()))
				&& !(projectile.getDamagesWho() == ProjectileDamager.ENEMY)) {
			projectile.killSelf();
			playerCharacter.takeDamage(projectile.getDamage());
		}
	}


	public void projectileWallCollision(Projectile projectile) {
		if (Intersector.overlaps(projectile.getCollisionBox(),
				entityManager.getCurrentDungeonRoom().getBottomProjectileWall())) {
			projectile.killSelf();
		}
		if (Intersector.overlaps(projectile.getCollisionBox(),
				entityManager.getCurrentDungeonRoom().getTopProjectileWall())) {
			projectile.killSelf();
		}
		if (Intersector.overlaps(projectile.getCollisionBox(),
				entityManager.getCurrentDungeonRoom().getLeftProjectileWall())) {
			projectile.killSelf();
		}
		if (Intersector.overlaps(projectile.getCollisionBox(),
				entityManager.getCurrentDungeonRoom().getRightProjectileWall())) {
			projectile.killSelf();
		}
	}


	public void playerObstacleCollision(Obstacle obstacle) {
		if (Intersector.overlaps(playerCharacter.getCircleHitbox(), obstacle.getRectangleHitbox())) {
			playerCharacter.moveToNearestEdgeRectangle(obstacle);
			playerCharacter.setMaxSpeed(playerObstacleCollisionSpeed);
			dungeonGUI.editData("Collides", "Collided");
			if (obstacle.isDamaging()) {
				playerCharacter.takeDamage(obstacle.getTouchDamage());
			}
		}
	}


	public boolean playerItemCollection(Item item) {
		boolean applied = false;
		if (Intersector.overlaps(playerCharacter.getCircleHitbox(), item.getRectangleHitbox())) {
			applied = item.applyToPlayer(playerCharacter);
		}
		return applied;
	}


	public void enemyObstacleCollision(Enemy enemy, Obstacle obstacle) {
		if (enemy.collides(obstacle)) {
			enemy.moveToNearestEdgeRectangle(obstacle);
			if (obstacle.isDamaging()) {
				enemy.takeDamage(obstacle.getTouchDamage());
				enemy.setCollidingWithSomething(true);
			}
		}
	}


	public void enemyWallCollision(Enemy enemy) {
		enemy.setCollidingWithSomething(false);

		if (Intersector.overlaps(enemy.getCircleHitbox(), entityManager.getCurrentDungeonRoom().getTopWall())) {
			enemy.setVelocityY(0);
			enemy.setHitboxCentre(enemy.getCircleHitbox().x,
					entityManager.getCurrentDungeonRoom().getTopWall().getY()
							- enemy.getCircleHitbox().radius);
			enemy.setCollidingWithSomething(true);
		}

		if (Intersector.overlaps(enemy.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getRightWall())) {
			enemy.setVelocityX(0);
			enemy.setHitboxCentre(entityManager.getCurrentDungeonRoom().getRightWall().getX()
					- enemy.getCircleHitbox().radius, enemy.getCircleHitbox().y);
			enemy.setCollidingWithSomething(true);
		}

		if (Intersector.overlaps(enemy.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getLeftWall())) {
			enemy.setVelocityX(0);
			enemy.setHitboxCentre(entityManager.getCurrentDungeonRoom().getLeftWall().getX()
					+ entityManager.getCurrentDungeonRoom().getLeftWall().getWidth()
					+ enemy.getCircleHitbox().radius, enemy.getCircleHitbox().y);
			enemy.setCollidingWithSomething(true);
		}

		if (Intersector.overlaps(enemy.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getBottomWall())) {
			enemy.setVelocityY(0);
			enemy.setHitboxCentre(enemy.getCircleHitbox().x,
					entityManager.getCurrentDungeonRoom().getBottomWall().getY() + entityManager
							.getCurrentDungeonRoom().getBottomWall().getHeight()
					+ enemy.getCircleHitbox().radius);
			enemy.setCollidingWithSomething(true);
		}
	}


	/**
	 * Doesn't damage twice if called more than once in a cycle because of invincibility frames
	 *
	 * @param enemy
	 */
	public void playerEnemyCollision(Enemy enemy) {
		if (playerCharacter.collides(enemy)) {
			playerCharacter.setMaxSpeed(playerEnemyCollisionSpeed);
			if (enemy.isCollidingWithSomething()) {
				playerCharacter.moveToNearestEdgeCircle(enemy);
			} else {
				enemy.moveToNearestEdgeCircle(playerCharacter);
			}

			if (enemy.getAttackType() != AttackType.RANGE) {
				playerCharacter.takeDamage(enemy.getTouchDamage());
			}
		}
	}


	public void playerWallCollision() {
		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getTopWall())) {
			playerCharacter.setVelocityY(0);
			playerCharacter.setMaxSpeed(playerWallCollisionSpeed);
			playerCharacter.setHitboxCentre(playerCharacter.getCircleHitbox().x,
					entityManager.getCurrentDungeonRoom().getTopWall().getY()
							- playerCharacter.getCircleHitbox().radius);
		}

		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getRightWall())) {
			playerCharacter.setVelocityX(0);
			playerCharacter.setMaxSpeed(playerWallCollisionSpeed);
			playerCharacter.setHitboxCentre(
					entityManager.getCurrentDungeonRoom().getRightWall().getX()
							- playerCharacter.getCircleHitbox().radius,
					playerCharacter.getCircleHitbox().y);
		}

		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getLeftWall())) {
			playerCharacter.setVelocityX(0);
			playerCharacter.setMaxSpeed(playerWallCollisionSpeed);
			playerCharacter.setHitboxCentre(WALL_WIDTH + playerCharacter.getCircleHitbox().radius,
					playerCharacter.getCircleHitbox().y);
		}

		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getBottomWall())) {
			playerCharacter.setVelocityY(0);
			playerCharacter.setMaxSpeed(playerWallCollisionSpeed);
			playerCharacter.setHitboxCentre(playerCharacter.getCircleHitbox().x,
					WALL_WIDTH + playerCharacter.getCircleHitbox().radius);
		}
	}


	public void playerDoorCollision() {
		float doorOffset = 70;
		if (entityManager.getCurrentDungeonRoom().hasUpDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getUpDoor())) && keyflagW) {
				playerCharacter.setVelocity(0, 0);
				entityManager.moveToUpRoom();
				playerCharacter.setY(doorOffset);
			}
		}

		if (entityManager.getCurrentDungeonRoom().hasDownDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getDownDoor())) && keyflagS) {
				playerCharacter.setVelocity(0, 0);
				entityManager.moveToDownRoom();
				playerCharacter.setY(
						MuscovyGame.WINDOW_HEIGHT - playerCharacter.getHeight() - doorOffset);
			}
		}

		if (entityManager.getCurrentDungeonRoom().hasRightDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getRightDoor())) && keyflagD) {
				playerCharacter.setVelocity(0, 0);
				entityManager.moveToRightRoom();
				playerCharacter.setX(doorOffset);
			}
		}

		if (entityManager.getCurrentDungeonRoom().hasLeftDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getLeftDoor())) && keyflagA) {
				playerCharacter.setVelocity(0, 0);
				entityManager.moveToLeftRoom();
				playerCharacter.setX(
						MuscovyGame.WINDOW_WIDTH - playerCharacter.getWidth() - doorOffset);
			}
		}
	}


	@Override
	public boolean keyDown(int keycode) {
		switch (gameState) {
		case MAIN_MENU:
			if (keycode == Input.Keys.ENTER) {
				gameState = GameState.OVERWORLD;
			}
			if (keycode == Input.Keys.ESCAPE) {
				Gdx.app.exit();
			}
			break;

		case OVERWORLD:
			if (((keycode == MuscovyGame.KEY_DOWN) || (keycode == MuscovyGame.KEY_SHOOT_DOWN))
					&& (mapSelected.ordinal() < LevelType.LEVEL_COUNT)) {
				mapSelected = LevelType.advanceLevel(mapSelected, 1);
			}

			if (((keycode == MuscovyGame.KEY_UP) || (keycode == MuscovyGame.KEY_SHOOT_UP))
					&& (mapSelected.ordinal() > 0)) {
				mapSelected = LevelType.advanceLevel(mapSelected, -1);
			}

			if (keycode == Input.Keys.ENTER) {
				if (!entityManager.isLevelCompleted(mapSelected.ordinal())) {
					entityManager.setLevel(mapSelected.ordinal());
					entityManager.startLevel(playerCharacter);
					gameState = GameState.DUNGEON;
				}
			}

			if (keycode == Input.Keys.ESCAPE) {
				gameState = GameState.MAIN_MENU;
			}

			break;

		case DUNGEON:
			if (keycode == MuscovyGame.KEY_UP) {
				keyflagW = true;
			}
			if (keycode == MuscovyGame.KEY_DOWN) {
				keyflagS = true;
			}
			if (keycode == MuscovyGame.KEY_RIGHT) {
				keyflagD = true;
			}
			if (keycode == MuscovyGame.KEY_LEFT) {
				keyflagA = true;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_RIGHT) {
				keyflagRight = true;
				firing = true;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_UP) {
				keyflagUp = true;
				firing = true;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_LEFT) {
				keyflagLeft = true;
				firing = true;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_DOWN) {
				keyflagDown = true;
				firing = true;
			}
			break;

		case PAUSE:
			if (keycode == MuscovyGame.KEY_UP) {
				keyflagW = true;
			}
			if (keycode == MuscovyGame.KEY_DOWN) {
				keyflagS = true;
			}
			if (keycode == MuscovyGame.KEY_RIGHT) {
				keyflagD = true;
			}
			if (keycode == MuscovyGame.KEY_LEFT) {
				keyflagA = true;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_RIGHT) {
				keyflagRight = true;
				firing = true;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_UP) {
				keyflagUp = true;
				firing = true;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_LEFT) {
				keyflagLeft = true;
				firing = true;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_DOWN) {
				keyflagDown = true;
				firing = true;
			}
			if (keycode == Input.Keys.ESCAPE) {
				gameState = GameState.OVERWORLD;
			}
			break;

		case GAME_OVER:
			if (keycode == Input.Keys.ENTER) {
				gameState = GameState.STARTUP;
			}
			if (keycode == Input.Keys.ESCAPE) {
				Gdx.app.exit();
			}
			break;

		default:
			break;
		}
		return true;
	}


	@Override
	public boolean keyUp(int keycode) {
		switch (gameState) {
		case DUNGEON:
			if (keycode == MuscovyGame.KEY_UP) {
				keyflagW = false;
			}
			if (keycode == MuscovyGame.KEY_DOWN) {
				keyflagS = false;
			}
			if (keycode == MuscovyGame.KEY_RIGHT) {
				keyflagD = false;
			}
			if (keycode == MuscovyGame.KEY_LEFT) {
				keyflagA = false;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_RIGHT) {
				keyflagRight = false;
				firing = false;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_UP) {
				keyflagUp = false;
				firing = false;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_LEFT) {
				keyflagLeft = false;
				firing = false;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_DOWN) {
				keyflagDown = false;
				firing = false;
			}
			if (keyflagDown || keyflagLeft || keyflagUp || keyflagRight) {
				firing = true;
			}
			if (keycode == Input.Keys.P) {
				gameState = GameState.PAUSE;
			}
			// if(keycode == Input.Keys.T){playerCharacter.setHitboxCentre(300,300);}
			break;
		case PAUSE:
			if (keycode == MuscovyGame.KEY_UP) {
				keyflagW = false;
			}
			if (keycode == MuscovyGame.KEY_DOWN) {
				keyflagS = false;
			}
			if (keycode == MuscovyGame.KEY_RIGHT) {
				keyflagD = false;
			}
			if (keycode == MuscovyGame.KEY_LEFT) {
				keyflagA = false;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_RIGHT) {
				keyflagRight = false;
				firing = false;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_UP) {
				keyflagUp = false;
				firing = false;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_LEFT) {
				keyflagLeft = false;
				firing = false;
			}
			if (keycode == MuscovyGame.KEY_SHOOT_DOWN) {
				keyflagDown = false;
				firing = false;
			}
			if (keyflagDown || keyflagLeft || keyflagUp || keyflagRight) {
				firing = true;
			}
			if (keycode == Input.Keys.P) {
				gameState = GameState.DUNGEON;
			}
			break;
		default:
			break;
		}
		return true;
	}


	@Override
	public boolean keyTyped(char character) {
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}


	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
