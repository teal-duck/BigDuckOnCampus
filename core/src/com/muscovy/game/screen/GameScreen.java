package com.muscovy.game.screen;


import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.EntityManager;
import com.muscovy.game.GUI;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.entity.Item;
import com.muscovy.game.entity.Obstacle;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.entity.Projectile;
import com.muscovy.game.enums.AttackType;
import com.muscovy.game.enums.ProjectileDamager;
import com.muscovy.game.input.Action;
import com.muscovy.game.level.Level;


public class GameScreen extends ScreenBase {
	private static final boolean PAUSE_ON_LOSE_FOCUS = true;
	private static final float ROOM_START_TIME = 1f; // 1.5f;

	private boolean paused = false;

	private EntityManager entityManager;
	private PlayerCharacter playerCharacter;

	private BitmapFont guiFont;
	private BitmapFont pauseFont;
	private GUI dungeonGUI;
	private GUI pauseGUI;

	private boolean pauseJustPressed = true;

	private float playerObstacleCollisionSpeed = PlayerCharacter.MAX_SPEED; // 100f;
	private float playerEnemyCollisionSpeed = PlayerCharacter.MAX_SPEED; // 100f;
	private float playerWallCollisionSpeed = PlayerCharacter.MAX_SPEED; // 200f;

	private boolean renderDebugGrid = false;


	public GameScreen(MuscovyGame game, Level level) {
		super(game);

		entityManager = new EntityManager(getGame(), level);
		initialisePlayerCharacter();
		initialiseGui();
		entityManager.startLevel(playerCharacter);
	}


	private void initialiseGui() {
		dungeonGUI = new GUI();
		// Sprite guiDungeonSprite = new Sprite(getTextureMap().getTextureOrLoadFile(AssetLocations.GUI_FRAME));
		// guiDungeonSprite.setX(0);
		// guiDungeonSprite.setY(0);
		// dungeonGUI.addElement(guiDungeonSprite);

		guiFont = AssetLocations.newFont();
		guiFont.setColor(Color.BLACK);

		int dungeonGuiX = 10;
		int dungeonGuiY = getWindowHeight();
		int dungeonGuiTop = 4;
		int dungeonGuiYSeparator = 30;

		dungeonGuiY -= dungeonGuiTop;
		dungeonGUI.addData("PlayerHealth", "Health: " + String.valueOf(playerCharacter.getHealth()), guiFont,
				dungeonGuiX, dungeonGuiY);
		dungeonGuiY -= dungeonGuiYSeparator;
		dungeonGUI.addData("PlayerScore", "Score: " + String.valueOf(playerCharacter.getScore()), guiFont,
				dungeonGuiX, dungeonGuiY);

		// int dungeonGuiY = getWindowHeight() - 16; // 900;
		// dungeonGUI.addData("PlayerHealth", "Health: " + String.valueOf(playerCharacter.getHealth()), guiFont,
		// 400, dungeonGuiY);
		// dungeonGUI.addData("PlayerScore", "Score: " + String.valueOf(playerCharacter.getScore()), guiFont,
		// 650,
		// dungeonGuiY);

		pauseFont = AssetLocations.newFont();
		pauseFont.setColor(Color.RED);
		pauseGUI = new GUI();
		pauseGUI.addData("Pause", "PAUSE", pauseFont, (getWindowWidth() / 2) - 50,
				(getWindowHeight() / 2) + 20);
	}


	public PlayerCharacter getPlayer() {
		return playerCharacter;
	}


	private void initialisePlayerCharacter() {
		// TODO: Player stats need to be passed to initialisePlayerCharacter
		Sprite playerSprite = new Sprite();
		playerSprite.setRegion(getTextureMap().getTextureOrLoadFile(AssetLocations.PLAYER));

		float playerStartX = getWindowWidth() / 2;
		float playerStartY = getWindowHeight() / 2;

		playerStartX -= playerSprite.getRegionWidth() / 2;
		playerStartY -= playerSprite.getRegionHeight() / 2;

		Vector2 playerStartPosition = new Vector2(playerStartX, playerStartY);
		playerCharacter = new PlayerCharacter(getGame(), playerSprite, playerStartPosition, getControlMap(),
				getController());
	}


	@Override
	public void updateScreen(float deltaTime) {
		if (getStateForAction(Action.PAUSE) > 0) {
			if (!pauseJustPressed) {
				paused = !paused;
			}
			pauseJustPressed = true;
		} else {
			pauseJustPressed = false;
		}

		if (paused) {
			if (getStateForAction(Action.ESCAPE) > 0) {
				setScreen(new LevelSelectScreen(getGame()));
			}

		} else {
			if (!entityManager.isTransitioning()) {
				playerUpdate(deltaTime);
				playerCharacter.update(deltaTime);
				projectilesUpdate(deltaTime);
				enemiesUpdate(deltaTime);

				if (playerCharacter.getHealth() <= 0) {
					setScreen(new GameOverScreen(getGame()));
					return;
				}

				playerCollision();
				cleanUpDeadThings();

				entityManager.checkLevelCompletion();
			}
		}
	}


	@Override
	public void renderScreen(float deltaTime, SpriteBatch batch) {
		clearScreen();
		updateAndSetCamera();

		dungeonGUI.editData("PlayerHealth", "Health: " + MathUtils.floor(playerCharacter.getHealth()));
		dungeonGUI.editData("PlayerScore", "Score: " + playerCharacter.getScore());

		batch.begin();
		entityManager.render(deltaTime, batch);
		dungeonGUI.render(batch);
		if (paused) {
			pauseGUI.render(batch);
		}
		batch.end();

		entityManager.renderMapOverlay();

		if (renderDebugGrid) {
			entityManager.renderGridOverlay();
		}
	}


	@Override
	public void pause() {
		super.pause();
		if (GameScreen.PAUSE_ON_LOSE_FOCUS) {
			paused = true;
		}
	}


	@Override
	public void dispose() {
		super.dispose();
		guiFont.dispose();
		pauseFont.dispose();
		entityManager.dispose();
	}


	public void enemiesUpdate(float deltaTime) {
		if (entityManager.getRoomTimer() < GameScreen.ROOM_START_TIME) {
			return;
		}

		for (Enemy enemy : entityManager.getEnemies()) {
			enemy.update(deltaTime);

			if ((enemy.getAttackType() != AttackType.TOUCH) && enemy.checkRangedAttack()) {
				entityManager.addNewProjectiles(enemy.rangedAttack(playerCharacter));
			}

			playerEnemyCollision(enemy);
			enemyWallCollision(enemy);
			playerEnemyCollision(enemy);
			enemyWallCollision(enemy);
		}
	}


	/**
	 * Checks projectile collision with walls, and player, and updates projectile
	 *
	 * @param deltaTime
	 */
	public void projectilesUpdate(float deltaTime) {
		ArrayList<Projectile> projectileList = entityManager.getProjectiles();
		for (Projectile projectile : projectileList) {
			projectile.update(deltaTime);
			projectileWallCollision(projectile);
			projectilePlayerCollision(projectile);
		}
	}


	public void cleanUpDeadThings() {
		entityManager.killEnemies();
		entityManager.killProjectiles();
		entityManager.killItems();
	}


	public void playerUpdate(float deltaTime) {
		playerAttack(deltaTime);
	}


	public void playerAttack(float deltaTime) {
		if (playerCharacter.isFiring()) {
			if (playerCharacter.checkRangedAttack(deltaTime)) {
				entityManager.addNewProjectiles(playerCharacter.rangedAttack());
			}
		} else {
			if (playerCharacter.getTimeSinceLastAttack() < 0.5) {
				playerCharacter.incrementTimeSinceLastAttack(deltaTime);
			}
		}
	}


	/**
	 * Iterates through all enemies and obstacles, and calculates collisions with each other and player character
	 * Could be optimised, but at the moment, doesn't chug, even with many enemies, obstacles and projectiles
	 */
	public void playerCollision() {
		ArrayList<Obstacle> obstacleList = entityManager.getObstacles();
		ArrayList<Enemy> enemyList = entityManager.getEnemies();
		ArrayList<Projectile> projectileList = entityManager.getProjectiles();
		ArrayList<Item> itemList = entityManager.getItems();

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
			playerItemCollision(item);
		}

		if (entityManager.getCurrentDungeonRoom().areAllEnemiesDead()) {
			playerDoorCollision();
		}

		for (Enemy enemy : enemyList) {
			for (Projectile projectile : projectileList) {
				projectileEnemyCollision(projectile, enemy);
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

			if (obstacle.isDamaging()) {
				playerCharacter.takeDamage(obstacle.getTouchDamage());
			}
		}
	}


	public boolean playerItemCollision(Item item) {
		boolean applied = false;

		if (Intersector.overlaps(playerCharacter.getCircleHitbox(), item.getRectangleHitbox())) {
			applied = item.applyToPlayer(playerCharacter);
		}

		if (applied) {
			item.setLifeOver();
		}

		return applied;
	}


	public void enemyObstacleCollision(Enemy enemy, Obstacle obstacle) {
		if (enemy.collides(obstacle)) {
			enemy.moveToNearestEdgeRectangle(obstacle);

			// TODO: Should enemies be damaged by obstacles?
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
			playerCharacter.setHitboxCentre(getWallWidth() + playerCharacter.getCircleHitbox().radius,
					playerCharacter.getCircleHitbox().y);
		}

		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getBottomWall())) {
			playerCharacter.setVelocityY(0);
			playerCharacter.setMaxSpeed(playerWallCollisionSpeed);
			playerCharacter.setHitboxCentre(playerCharacter.getCircleHitbox().x,
					getWallWidth() + playerCharacter.getCircleHitbox().radius);
		}
	}


	public void playerDoorCollision() {
		float doorOffset = 70;
		// TODO: Replace "player walking towards door" check with dot product

		if (entityManager.getCurrentDungeonRoom().hasUpDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getUpDoor()))
					&& (getStateForAction(Action.WALK_UP) > 0)) {
				playerCharacter.setVelocity(0, 0);
				entityManager.moveToUpRoom();
				playerCharacter.setY(doorOffset);
			}
		}

		if (entityManager.getCurrentDungeonRoom().hasDownDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getDownDoor()))
					&& (getStateForAction(Action.WALK_DOWN) > 0)) {
				playerCharacter.setVelocity(0, 0);
				entityManager.moveToDownRoom();
				playerCharacter.setY(getWindowHeight() - playerCharacter.getHeight() - doorOffset);
			}
		}

		if (entityManager.getCurrentDungeonRoom().hasRightDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getRightDoor()))
					&& (getStateForAction(Action.WALK_RIGHT) > 0)) {
				playerCharacter.setVelocity(0, 0);
				entityManager.moveToRightRoom();
				playerCharacter.setX(doorOffset);
			}
		}

		if (entityManager.getCurrentDungeonRoom().hasLeftDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getLeftDoor()))
					&& (getStateForAction(Action.WALK_LEFT) > 0)) {
				playerCharacter.setVelocity(0, 0);
				entityManager.moveToLeftRoom();
				playerCharacter.setX(getWindowWidth() - playerCharacter.getWidth() - doorOffset);
			}
		}
	}
}
