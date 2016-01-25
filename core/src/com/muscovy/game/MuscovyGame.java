package com.muscovy.game;


import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;


/**
 * If you need a hand, email me on ewh502
 */
public class MuscovyGame extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	private OrthographicCamera camera;
	float timer = 0;
	SpriteBatch batch;
	PlayerCharacter playerCharacter;
	GUI mainMenuGUI, dungeonGUI, overworldGUI, pauseGUI, gameOverGUI;
	Texture availableLevel, unavailableLevel;
	EntityManager entityManager;
	boolean keyflagW, keyflagD, keyflagA, keyflagS, keyflagUP, keyflagRIGHT, keyflagLEFT, keyflagDOWN,
			firing = false;
	Sprite guiMapSprite, guiSelector;
	private BitmapFont xVal, yVal, gameOverFont, loading;
	float w = 1280, h = 960;
	int gameState; // 0 = Main Menu, 1 = Overworld/Map, 2 = Dungeon/LevelGenerator, 3 = Pause, 4 = Game Over, 101 =
			// Startup, 102 = Loading
	int MapSelected; // 1 = Constantine, 2 = Langwith, 3 = Goodricke, 4 = Law and Management, 5 = Catalyst, 6 =
				// TFTV, 7 = Computer Science, 8 = Ron Cooke Hub


	@Override
	public void create() {
		gameState = 101;
		loading = new BitmapFont();
		loading.setColor(Color.WHITE);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		Gdx.input.setInputProcessor(this);
	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!(gameState == 101)) {
			update(); // Waiting for 1 render cycle before initialising anything
		}
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		switch (gameState) {
		case 0:
			// MAIN MENU
			mainMenuGUI.render(batch);
			break;
		case 1:
			// OVERWORLD
			overworldGUI.render(batch);
			break;
		case 2:
			// IN DUNGEON/BUILDING
			entityManager.render(batch);
			//// These work by referencing the data you want to change with the first string you pass it
			dungeonGUI.editData("PlayerHealth", "Health: " + String.valueOf(playerCharacter.getHealth()));
			dungeonGUI.editData("PlayerScore", "Score: " + String.valueOf(playerCharacter.getScore()));
			////
			dungeonGUI.render(batch);
			entityManager.render(batch);
			break;
		case 3:
			// PAUSE
			dungeonGUI.render(batch);
			entityManager.render(batch);
			batch.draw(playerCharacter.getSprite().getTexture(), playerCharacter.getX(),
					playerCharacter.getY());
			pauseGUI.render(batch);
			break;
		case 4:
			// GAME OVER
			gameOverGUI.render(batch);
			batch.draw(playerCharacter.getSprite().getTexture(), playerCharacter.getX(),
					playerCharacter.getY());
			break;
		default:
			// GENERATING LEVELS
			loading.draw(batch, "RANDOMLY GENERATING LEVELS", (1280 / 2) - 150, 960 / 2);
			gameState = 102; // Indicates that the render cycle has finished
			break;
		}
		batch.end();
	}


	/**
	 * INITIALISATION METHODS
	 */
	private void initialiseLevels() {
		entityManager = new EntityManager();
		entityManager.generateLevels();
	}


	private void initialiseGUIs() {
		/**
		 * Setting up GUI stuff, with buttons, and text.
		 */
		Sprite mainMenuSprite = new Sprite();
		Sprite mainMenuStartButton = new Sprite();
		// Sprite guiMapSprite = new Sprite();
		Sprite guiDungeonSprite = new Sprite();
		// Main menu
		mainMenuGUI = new GUI();
		mainMenuSprite.setTexture(new Texture(Gdx.files.internal("mainMenu.png")));
		mainMenuSprite.setX(0);
		mainMenuSprite.setY(0);
		mainMenuStartButton.setTexture(new Texture(Gdx.files.internal("startGameButton.png")));
		mainMenuStartButton.setCenter(1280, 960);
		mainMenuStartButton.setX((1280 - 392) / 2);
		mainMenuStartButton.setY(960 / 2);
		mainMenuGUI.addElement(mainMenuSprite);
		mainMenuGUI.addElement(mainMenuStartButton);
		// Dungeon
		dungeonGUI = new GUI();
		guiDungeonSprite.setTexture(new Texture(Gdx.files.internal("guiFrame.png")));
		guiDungeonSprite.setX(0);
		guiDungeonSprite.setY(0);
		dungeonGUI.addElement(guiDungeonSprite);
		xVal = new BitmapFont();
		xVal.setColor(Color.BLACK);
		yVal = new BitmapFont();
		yVal.setColor(Color.BLACK);
		dungeonGUI.addData("PlayerHealth", "Health: " + String.valueOf(playerCharacter.getHealth()), xVal, 400,
				900);
		dungeonGUI.addData("PlayerScore", "Score: " + String.valueOf(playerCharacter.getScore()), xVal, 650,
				900);
		// GameOver
		gameOverFont = new BitmapFont();
		gameOverFont.setColor(Color.RED);
		gameOverGUI = new GUI();
		gameOverGUI.addData("Gameover", "Game Over", gameOverFont, 640, 150);
		pauseGUI = new GUI();
		pauseGUI.addData("Pause", "PAUSE", gameOverFont, 1280 / 2, 720 / 2);
	}


	private void initialisePlayerCharacter() {
		playerCharacter = new PlayerCharacter();
		playerCharacter.setY(300);
		playerCharacter.setX(300);
	}


	public void initaliseOverworld() {
		MapSelected = 1;
		guiMapSprite = new Sprite();
		guiSelector = new Sprite();
		overworldGUI = new GUI();
		availableLevel = new Texture(Gdx.files.internal("selector.png"));
		unavailableLevel = new Texture(Gdx.files.internal("badselector.png"));
		guiSelector.setTexture(availableLevel);
		cursorUpdate();
		guiMapSprite.setTexture(new Texture(Gdx.files.internal("hesEastMap.png")));
		guiMapSprite.setX(0);
		guiMapSprite.setY(0);
		overworldGUI.addElement(guiMapSprite);
		overworldGUI.addElement(guiSelector);
	}


	/**
	 * UPDATE METHODS
	 */
	public void update() {
		/**
		 * Uses gameState to update game as needed for each state. Waits for 1 render cycle before initialising
		 * everything (case 102), so the loading screen can be displayed
		 */
		switch (gameState) {
		case 0:
			break;
		case 1:
			cursorUpdate();
			break;
		case 2:
			playerUpdate();
			playerCharacter.update();
			projectilesUpdate();
			enemiesUpdate();
			if (playerCharacter.getHealth() <= 0) {
				gameState = 4;
			}
			collision();
			cleanupDeadThings();
			if (timer > 10) {
				timer = 0; // Useful for debugging. Put a break point here if you need to see the
						// variables after 10 seconds
			}
			timer += Gdx.graphics.getDeltaTime();
			break;
		case 3:
			break;
		case 4:
			break;
		case 102:
			initialisePlayerCharacter();
			initialiseLevels();
			initialiseGUIs();
			initaliseOverworld();
			gameState = 0;
			break;
		}

	}


	public void enemiesUpdate() {
		if (entityManager.getRoomTimer() > 2) {
			for (Enemy enemy : entityManager.getEnemies()) {
				enemy.update(playerCharacter);
				if ((enemy.getAttackType() != 0) && (enemy.checkRangedAttack())) {
					entityManager.addNewProjectiles(enemy.rangedAttack(playerCharacter));
				}
				playerEnemyCollision(enemy);
				enemyWallCollision(enemy);
				playerEnemyCollision(enemy);
				enemyWallCollision(enemy);
			}
		}
	}


	public void projectilesUpdate() {
		/**
		 * Checks projectile collision with walls, and player, adn updates projectile
		 */
		ArrayList<Projectile> projectileList = new ArrayList<Projectile>(entityManager.getProjectiles());
		for (Projectile projectile : projectileList) {
			projectile.update();
			projectileWallCollision(projectile);
			projectilePlayerCollision(projectile);
		}
	}


	public void cleanupDeadThings() {
		entityManager.killEnemies();
		entityManager.killProjectiles();
	}


	public void playerUpdate() {
		playerMovement();
		playerAttack();
	}


	public void playerAttack() {
		if (firing) {
			if (playerCharacter.checkRangedAttack()) {
				if (keyflagRIGHT) {
					playerCharacter.setShotDirection((float) (Math.PI / 2));
				}
				if (keyflagLEFT) {
					playerCharacter.setShotDirection((float) ((3 * Math.PI) / 2));
				}
				if (keyflagUP) {
					playerCharacter.setShotDirection(0);
				}
				if (keyflagDOWN) {
					playerCharacter.setShotDirection((float) Math.PI);
				}
				entityManager.addNewProjectiles(playerCharacter.rangedAttack());
			}
		} else {
			if (playerCharacter.getTimeSinceLastAttack() < 0.5) {
				playerCharacter.incrementTimeSinceLastAttack();
			}
		}
	}


	public void playerMovement() {
		if (keyflagW) {
			playerCharacter.Up();
			playerCharacter.movementAnimation();
		}
		if (!keyflagW && keyflagS) {
			playerCharacter.Down();
			playerCharacter.movementAnimation();
		}
		if (keyflagD) {
			playerCharacter.Right();
			playerCharacter.movementAnimation();
		}
		if (!keyflagD && keyflagA) {
			playerCharacter.Left();
			playerCharacter.movementAnimation();
		}
		if (!keyflagD && !keyflagA) {
			playerCharacter.decelXToStop();
		}
		if (!keyflagW && !keyflagS) {
			playerCharacter.decelYToStop();
		}
		/**
		 * Animation stuff
		 */
		if (keyflagD) {
			playerCharacter.setDirection((float) (Math.PI / 2));
		}
		if (keyflagA) {
			playerCharacter.setDirection((float) ((3 * Math.PI) / 2));
		}
		// if (keyflagA && keyflagD) playerCharacter.setDirection(2);
		if (keyflagW) {
			playerCharacter.setDirection(0);
		}
		if (keyflagS) {
			playerCharacter.setDirection((float) Math.PI);
		}
	}


	public void cursorUpdate() {
		switch (MapSelected) {
		case 1:
			if (!entityManager.levelCompleted(0)) {
				guiSelector.setTexture(availableLevel);
			} else {
				guiSelector.setTexture(unavailableLevel);
			}
			guiSelector.setX(950);
			guiSelector.setY(680);
			break;
		case 2:
			if (!entityManager.levelCompleted(1)) {
				guiSelector.setTexture(availableLevel);
			} else {
				guiSelector.setTexture(unavailableLevel);
			}
			guiSelector.setX(650);
			guiSelector.setY(600);
			break;
		case 3:
			if (!entityManager.levelCompleted(2)) {
				guiSelector.setTexture(availableLevel);
			} else {
				guiSelector.setTexture(unavailableLevel);
			}
			guiSelector.setX(300);
			guiSelector.setY(600);
			break;
		case 4:
			if (!entityManager.levelCompleted(3)) {
				guiSelector.setTexture(availableLevel);
			} else {
				guiSelector.setTexture(unavailableLevel);
			}
			guiSelector.setX(230);
			guiSelector.setY(420);
			break;
		case 5:
			if (!entityManager.levelCompleted(4)) {
				guiSelector.setTexture(availableLevel);
			} else {
				guiSelector.setTexture(unavailableLevel);
			}
			guiSelector.setX(110);
			guiSelector.setY(360);
			break;
		case 6:
			if (!entityManager.levelCompleted(5)) {
				guiSelector.setTexture(availableLevel);
			} else {
				guiSelector.setTexture(unavailableLevel);
			}
			guiSelector.setX(160);
			guiSelector.setY(230);
			break;
		case 7:
			if (!entityManager.levelCompleted(6)) {
				guiSelector.setTexture(availableLevel);
			} else {
				guiSelector.setTexture(unavailableLevel);
			}
			guiSelector.setX(330);
			guiSelector.setY(270);
			break;
		case 8:
			if (!entityManager.levelCompleted(7)) {
				guiSelector.setTexture(availableLevel);
			} else {
				guiSelector.setTexture(unavailableLevel);
			}
			guiSelector.setX(440);
			guiSelector.setY(340);
			break;
		}
	}


	/**
	 * Collision Methods
	 */
	public void collision() {
		/**
		 * Iterates through all enemies and obstacles, and calculates collisions with each other and player
		 * character Could be optimised, but at the moment, doesn't chug, even with many enemies, obstacles and
		 * projectiles
		 */
		ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>(entityManager.getObstacles());
		ArrayList<Enemy> enemyList = new ArrayList<Enemy>(entityManager.getEnemies());
		ArrayList<Projectile> projectileList = new ArrayList<Projectile>(entityManager.getProjectiles());
		playerCharacter.resetMaxVelocity();
		for (Obstacle obstacle : obstacleList) {
			playerObstacleCollision(obstacle);
			for (Enemy enemy : enemyList) {
				// enemy.resetMaxVelocity();
				enemyObstacleCollision(enemy, obstacle);
			}
		}
		playerWallCollision();
		if (entityManager.getCurrentDungeonRoom().isEnemiesDead()) {
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
				&& !(projectile.getDamagesWho() == 0)) {
			projectile.kill();
			enemy.damage(projectile.getDamage());
		}
	}


	public void projectileObstacleCollision(Projectile projectile, Obstacle obstacle) {
		if (Intersector.overlaps(projectile.getCollisionBox(), obstacle.getRectangleHitbox())) {
			projectile.kill();
		}
	}


	public void projectilePlayerCollision(Projectile projectile) {
		if ((Intersector.overlaps(playerCharacter.getCircleHitbox(), projectile.getCollisionBox()))
				&& !(projectile.getDamagesWho() == 1)) {
			projectile.kill();
			playerCharacter.damage(projectile.getDamage());
		}
	}


	public void projectileWallCollision(Projectile projectile) {
		if (Intersector.overlaps(projectile.getCollisionBox(),
				entityManager.getCurrentDungeonRoom().getProjectileWallBottom())) {
			projectile.kill();
		}
		if (Intersector.overlaps(projectile.getCollisionBox(),
				entityManager.getCurrentDungeonRoom().getProjectileWallTop())) {
			projectile.kill();
		}
		if (Intersector.overlaps(projectile.getCollisionBox(),
				entityManager.getCurrentDungeonRoom().getProjectileWallLeft())) {
			projectile.kill();
		}
		if (Intersector.overlaps(projectile.getCollisionBox(),
				entityManager.getCurrentDungeonRoom().getProjectileWallRight())) {
			projectile.kill();
		}
	}


	public void playerObstacleCollision(Obstacle obstacle) {
		if (Intersector.overlaps(playerCharacter.getCircleHitbox(), obstacle.getRectangleHitbox())) {
			playerCharacter.moveToNearestEdgeRectangle(obstacle);
			playerCharacter.setMaxVelocity(100);
			dungeonGUI.editData("Collides", "Collided");
			if (obstacle.isDamaging()) {
				playerCharacter.damage(obstacle.getTouchDamage());
			}
		}
	}


	public void enemyObstacleCollision(Enemy enemy, Obstacle obstacle) {
		if (enemy.collides(obstacle)) {
			enemy.moveToNearestEdgeRectangle(obstacle);
			if (obstacle.isDamaging()) {
				enemy.damage(obstacle.getTouchDamage());
				enemy.setCollidingWithSomething(true);
			}
		}
	}


	public void enemyWallCollision(Enemy enemy) {
		enemy.setCollidingWithSomething(false);
		if (Intersector.overlaps(enemy.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getTopRectangle())) {
			enemy.setYVelocity(0);
			// enemy.setMaxVelocity(50);
			enemy.setHitboxCentre(enemy.getCircleHitbox().x,
					entityManager.getCurrentDungeonRoom().getTopRectangle().getY()
							- enemy.getCircleHitbox().radius);
			enemy.setCollidingWithSomething(true);
		}
		if (Intersector.overlaps(enemy.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getRightRectangle())) {
			enemy.setXVelocity(0);
			// enemy.setMaxVelocity(50);
			enemy.setHitboxCentre(entityManager.getCurrentDungeonRoom().getRightRectangle().getX()
					- enemy.getCircleHitbox().radius, enemy.getCircleHitbox().y);
			enemy.setCollidingWithSomething(true);
		}
		if (Intersector.overlaps(enemy.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getLeftRectangle())) {
			enemy.setXVelocity(0);
			// enemy.setMaxVelocity(50);
			enemy.setHitboxCentre(entityManager.getCurrentDungeonRoom().getLeftRectangle().getX()
					+ entityManager.getCurrentDungeonRoom().getLeftRectangle().getWidth()
					+ enemy.getCircleHitbox().radius, enemy.getCircleHitbox().y);
			enemy.setCollidingWithSomething(true);
		}
		if (Intersector.overlaps(enemy.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getBottomRectangle())) {
			enemy.setYVelocity(0);
			// enemy.setMaxVelocity(50);
			enemy.setHitboxCentre(enemy.getCircleHitbox().x,
					entityManager.getCurrentDungeonRoom().getBottomRectangle().getY()
							+ entityManager.getCurrentDungeonRoom().getBottomRectangle()
									.getHeight()
							+ enemy.getCircleHitbox().radius);
			enemy.setCollidingWithSomething(true);
		}
	}


	public void playerEnemyCollision(Enemy enemy) {
		/**
		 * Doesn't damage twice if called more than once in a cycle because of invincibility frames
		 */
		if (playerCharacter.collides(enemy)) {
			playerCharacter.setMaxVelocity(100);
			if (enemy.isCollidingWithSomething()) {
				playerCharacter.moveToNearestEdgeCircle(enemy);
			} else {
				enemy.moveToNearestEdgeCircle(playerCharacter);
			}
			if (enemy.getAttackType() != 1) {
				playerCharacter.damage(enemy.getTouchDamage());
			}
		}
	}


	public void playerWallCollision() {
		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getTopRectangle())) {
			playerCharacter.setYVelocity(0);
			playerCharacter.setMaxVelocity(200);
			playerCharacter.setHitboxCentre(playerCharacter.getCircleHitbox().x,
					entityManager.getCurrentDungeonRoom().getTopRectangle().getY()
							- playerCharacter.getCircleHitbox().radius);
		}
		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getRightRectangle())) {
			playerCharacter.setXVelocity(0);
			playerCharacter.setMaxVelocity(200);
			playerCharacter.setHitboxCentre(
					entityManager.getCurrentDungeonRoom().getRightRectangle().getX()
							- playerCharacter.getCircleHitbox().radius,
					playerCharacter.getCircleHitbox().y);
		}
		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getLeftRectangle())) {
			playerCharacter.setXVelocity(0);
			playerCharacter.setMaxVelocity(200);
			playerCharacter.setHitboxCentre(64 + playerCharacter.getCircleHitbox().radius,
					playerCharacter.getCircleHitbox().y);
		}
		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getBottomRectangle())) {
			playerCharacter.setYVelocity(0);
			playerCharacter.setMaxVelocity(200);
			playerCharacter.setHitboxCentre(playerCharacter.getCircleHitbox().x,
					64 + playerCharacter.getCircleHitbox().radius);
		}
	}


	public void playerDoorCollision() {
		if (entityManager.getCurrentDungeonRoom().getUpDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getNorthDoor())) && keyflagW) {
				playerCharacter.setYVelocity(0);
				playerCharacter.setXVelocity(0);
				entityManager.moveNorth();
				playerCharacter.setY(70);
			}
		}
		if (entityManager.getCurrentDungeonRoom().getDownDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getSouthDoor())) && keyflagS) {
				playerCharacter.setYVelocity(0);
				playerCharacter.setXVelocity(0);
				entityManager.moveSouth();
				playerCharacter.setY(768 - playerCharacter.getHeight() - 70);
			}
		}
		if (entityManager.getCurrentDungeonRoom().getRightDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getEastDoor())) && keyflagD) {
				playerCharacter.setYVelocity(0);
				playerCharacter.setXVelocity(0);
				entityManager.moveEast();
				playerCharacter.setX(70);
			}
		}
		if (entityManager.getCurrentDungeonRoom().getLeftDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getWestDoor())) && keyflagA) {
				playerCharacter.setYVelocity(0);
				playerCharacter.setXVelocity(0);
				entityManager.moveWest();
				playerCharacter.setX(1280 - playerCharacter.getWidth() - 70);
			}
		}
	}


	@Override
	public boolean keyDown(int keycode) {
		switch (gameState) {
		case 0:
			if (keycode == Input.Keys.ENTER) {
				gameState = 1;
			}
			if (keycode == Input.Keys.ESCAPE) {
				Gdx.app.exit();
			}
			break;
		case 1:
			if ((keycode == Input.Keys.DOWN) && (MapSelected < 8)) {
				MapSelected += 1;
			}
			if ((keycode == Input.Keys.UP) && (MapSelected > 1)) {
				MapSelected -= 1;
			}
			if (keycode == Input.Keys.ENTER) {
				if (!entityManager.levelCompleted(MapSelected - 1)) {
					entityManager.setLevel(MapSelected - 1);
					entityManager.startLevel(playerCharacter);
					gameState = 2;
				}
			}
			if (keycode == Input.Keys.ESCAPE) {
				gameState = 0;
			}
			break;
		case 2:
			if (keycode == Input.Keys.W) {
				keyflagW = true;
			}
			if (keycode == Input.Keys.S) {
				keyflagS = true;
			}
			if (keycode == Input.Keys.D) {
				keyflagD = true;
			}
			if (keycode == Input.Keys.A) {
				keyflagA = true;
			}
			if (keycode == Input.Keys.RIGHT) {
				keyflagRIGHT = true;
				firing = true;
			}
			if (keycode == Input.Keys.UP) {
				keyflagUP = true;
				firing = true;
			}
			if (keycode == Input.Keys.LEFT) {
				keyflagLEFT = true;
				firing = true;
			}
			if (keycode == Input.Keys.DOWN) {
				keyflagDOWN = true;
				firing = true;
			}
			break;
		case 3:
			if (keycode == Input.Keys.W) {
				keyflagW = true;
			}
			if (keycode == Input.Keys.S) {
				keyflagS = true;
			}
			if (keycode == Input.Keys.D) {
				keyflagD = true;
			}
			if (keycode == Input.Keys.A) {
				keyflagA = true;
			}
			if (keycode == Input.Keys.RIGHT) {
				keyflagRIGHT = true;
				firing = true;
			}
			if (keycode == Input.Keys.UP) {
				keyflagUP = true;
				firing = true;
			}
			if (keycode == Input.Keys.LEFT) {
				keyflagLEFT = true;
				firing = true;
			}
			if (keycode == Input.Keys.DOWN) {
				keyflagDOWN = true;
				firing = true;
			}
			if (keycode == Input.Keys.ESCAPE) {
				gameState = 1;
			}
			break;
		case 4:
			break;
		}
		return true;
	}


	@Override
	public boolean keyUp(int keycode) {
		switch (gameState) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			if (keycode == Input.Keys.W) {
				keyflagW = false;
			}
			if (keycode == Input.Keys.S) {
				keyflagS = false;
			}
			if (keycode == Input.Keys.D) {
				keyflagD = false;
			}
			if (keycode == Input.Keys.A) {
				keyflagA = false;
			}
			if (keycode == Input.Keys.RIGHT) {
				keyflagRIGHT = false;
				firing = false;
			}
			if (keycode == Input.Keys.UP) {
				keyflagUP = false;
				firing = false;
			}
			if (keycode == Input.Keys.LEFT) {
				keyflagLEFT = false;
				firing = false;
			}
			if (keycode == Input.Keys.DOWN) {
				keyflagDOWN = false;
				firing = false;
			}
			if (keyflagDOWN || keyflagLEFT || keyflagUP || keyflagRIGHT) {
				firing = true;
			}
			if (keycode == Input.Keys.P) {
				gameState = 3;
			}
			// if(keycode == Input.Keys.T){playerCharacter.setHitboxCentre(300,300);}
			break;
		case 3:
			if (keycode == Input.Keys.W) {
				keyflagW = false;
			}
			if (keycode == Input.Keys.S) {
				keyflagS = false;
			}
			if (keycode == Input.Keys.D) {
				keyflagD = false;
			}
			if (keycode == Input.Keys.A) {
				keyflagA = false;
			}
			if (keycode == Input.Keys.RIGHT) {
				keyflagRIGHT = false;
				firing = false;
			}
			if (keycode == Input.Keys.UP) {
				keyflagUP = false;
				firing = false;
			}
			if (keycode == Input.Keys.LEFT) {
				keyflagLEFT = false;
				firing = false;
			}
			if (keycode == Input.Keys.DOWN) {
				keyflagDOWN = false;
				firing = false;
			}
			if (keyflagDOWN || keyflagLEFT || keyflagUP || keyflagRIGHT) {
				firing = true;
			}
			if (keycode == Input.Keys.P) {
				gameState = 2;
			}
			break;
		case 4:
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
