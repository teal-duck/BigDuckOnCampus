package com.muscovy.game.screen;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.EntityManager;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Bomb;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.entity.Explosion;
import com.muscovy.game.entity.Item;
import com.muscovy.game.entity.MoveableEntity;
import com.muscovy.game.entity.Obstacle;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.entity.Projectile;
import com.muscovy.game.enums.AttackType;
import com.muscovy.game.enums.ProjectileDamager;
import com.muscovy.game.gui.ButtonList;
import com.muscovy.game.gui.GUI;
import com.muscovy.game.input.Action;
import com.muscovy.game.level.Level;


/**
 *
 */
public class GameScreen extends ScreenBase {
	private static final boolean PAUSE_ON_LOSE_FOCUS = true;
	private static final float ROOM_START_TIME = 0.2f;

	private EntityManager entityManager;
	private PlayerCharacter playerCharacter;
	private Level level;

	private boolean renderDebugGrid = false;
	private boolean hasCompletedLevel = false;

	private static final String HEALTH_ID = "Health";
	private static final String SCORE_ID = "Score";
	private static final String LEVEL_NAME = "LevelName";
	private static final String OBJECTIVE_ID = "Objective";
	private static final String PAUSE_ID = "Pause";
	private static final String FLIGHT_ID = "Flight";
	private static final String BOMBS_ID = "Bombs";
	private static final int TEXT_EDGE_OFFSET = 10;

	private static final String FLIGHT_TEXT = "Flight: ";
	private float flightBarX = 0;
	private float flightBarY = 0;
	private float flightBarWidth = 130;
	private float flightBarHeight = 20;

	// GUI, GUI font
	private BitmapFont guiFont;
	private BitmapFont pauseFont;
	private GUI dungeonGui;
	private GUI pauseGui;

	// Item
	private Sprite bombSprite;

	// Pause status
	private boolean paused = false;
	private boolean pauseJustPressed = true;
	// When the player opens pause menu, only let them save once (i.e. set to true)
	// When they close it, reset this to false
	private boolean hasSavedThisPauseMenu = false;

	// Pause button options
	private static final int RESUME = 0;
	private static final int SAVE = 1;
	private static final int QUIT = 2;
	private static final String[] PAUSE_BUTTON_TEXTS = new String[] { "Resume", "Save", "Levels" };

	private ButtonList pauseMenuButtons;
	private BitmapFont pauseMenuFont;

	private ShapeRenderer shapeRenderer;


	public GameScreen(MuscovyGame game, Level level) {
		super(game);

		this.level = level;
		entityManager = new EntityManager(getGame(), level);
		playerCharacter = getPlayerCharacter();
		resetPlayer();
		initialiseGui();
		entityManager.startLevel(playerCharacter);
	}


	/**
	 *
	 */
	private void initialiseGui() {
		// DUNGEON GUI
		dungeonGui = new GUI();

		guiFont = AssetLocations.newFont20();
		guiFont.setColor(Color.WHITE);

		int dungeonGuiX = GameScreen.TEXT_EDGE_OFFSET;
		int dungeonGuiY = getWindowHeight();
		int dungeonGuiTop = 4;
		int dungeonGuiYSeparator = 30;

		// Health
		dungeonGuiY -= dungeonGuiTop;
		dungeonGui.addData(GameScreen.HEALTH_ID, "Health: " + playerCharacter.getHealth(), guiFont, dungeonGuiX,
				dungeonGuiY);

		// Score
		dungeonGuiY -= dungeonGuiYSeparator;
		dungeonGui.addData(GameScreen.SCORE_ID, "Score: " + playerCharacter.getScore(), guiFont, dungeonGuiX,
				dungeonGuiY);

		// Flight bar
		dungeonGuiY -= dungeonGuiYSeparator;
		dungeonGui.addData(GameScreen.FLIGHT_ID, GameScreen.FLIGHT_TEXT, guiFont, dungeonGuiX, dungeonGuiY);

		flightBarX = getTextWidth(guiFont, GameScreen.FLIGHT_TEXT);
		flightBarY = dungeonGuiY - (dungeonGuiYSeparator * 0.65f);

		// Number of bombs
		dungeonGuiY -= dungeonGuiYSeparator;
		dungeonGuiY -= dungeonGuiYSeparator * 2;
		bombSprite = new Sprite();
		bombSprite.setTexture(new Texture(Gdx.files.internal(AssetLocations.BOMB_GUI)));
		bombSprite.setX(dungeonGuiX);
		bombSprite.setY(dungeonGuiY + 3);
		dungeonGui.addElement(bombSprite);
		dungeonGuiY += dungeonGuiYSeparator;
		dungeonGui.addData(GameScreen.BOMBS_ID, "x" + playerCharacter.getBombCount(), guiFont, dungeonGuiX * 4,
				dungeonGuiY);

		// Level name
		dungeonGuiY = 26;
		dungeonGui.addData(GameScreen.LEVEL_NAME, level.getName(), guiFont, dungeonGuiX, dungeonGuiY);

		// Level objective
		String objectiveText = level.getObjectiveName();
		int objectiveX = getWindowWidth() - dungeonGuiX - getTextWidth(guiFont, objectiveText);
		dungeonGui.addData(GameScreen.OBJECTIVE_ID, objectiveText, guiFont, objectiveX, dungeonGuiY);

		// PAUSE MENU
		shapeRenderer = new ShapeRenderer();

		pauseFont = AssetLocations.newFont32();
		pauseFont.setColor(Color.WHITE);
		pauseGui = new GUI();
		pauseGui.addData(GameScreen.PAUSE_ID, "PAUSED", pauseFont, (getWindowWidth() / 2) - getWallWidth(),
				((3 * getWindowHeight()) / 4));

		pauseMenuFont = AssetLocations.newFont32();
		pauseMenuButtons = new ButtonList(GameScreen.PAUSE_BUTTON_TEXTS, pauseMenuFont, getTextureMap(),
				getControlMap(), getController());
		setPauseButtonLocations();
	}


	/**
	 *
	 */
	private void resetPlayer() {
		// Sprite playerSprite = playerCharacter.getSprite();

		float playerStartX = getWindowWidth() / 2;
		float playerStartY = getWindowHeight() / 2;
		playerStartX -= 32;
		playerStartY -= 32;
		// playerStartX -= playerSprite.getRegionWidth() / 2;
		// playerStartY -= playerSprite.getRegionHeight() / 2;

		playerCharacter.setX(playerStartX);
		playerCharacter.setY(playerStartY);

		playerCharacter.getAcceleration().setZero();
		playerCharacter.getVelocity().setZero();
		playerCharacter.setFullFlightBar();

		// playerCharacter.giveBombs(5);
	}


	/**
	 *
	 */
	private void setPauseButtonLocations() {
		int x = (getWindowWidth() / 2) - (ButtonList.BUTTON_WIDTH / 2);
		int y = ((getWindowHeight() / 6) + ButtonList.WINDOW_EDGE_OFFSET)
				+ ButtonList.getHeightForDefaultButtonList(GameScreen.PAUSE_BUTTON_TEXTS.length);
		pauseMenuButtons.setPositionDefaultSize(x, y + 100);
	}


	/**
	 * Pause menu options
	 *
	 * @param selected
	 */
	private void selectPauseOption(int selected) {
		switch (selected) {
		case RESUME:
			setPaused(false);
			break;
		case SAVE:
			selectSave();
			break;
		case QUIT:
			selectQuit();
			break;
		}
	}


	/**
	 *
	 */
	private void selectSave() {
		// Only allow the player to save once per pause
		if (hasSavedThisPauseMenu) {
			return;
		}
		pauseMenuButtons.changeTextOnButton(GameScreen.SAVE, "Saved!");
		hasSavedThisPauseMenu = true;

		getGame().saveCurrentGame();
	}


	/**
	 *
	 */
	private void selectQuit() {
		if (getGame().areAllLevelsCompleted()) {
			setScreen(new WinScreen(getGame()));
		} else {
			setScreen(new LevelSelectScreen(getGame(), level.getLevelType()));
		}
	}


	/**
	 * @param newPaused
	 */
	private void setPaused(boolean newPaused) {
		paused = newPaused;
		if (!paused) {
			// Reset the text on the save button
			pauseMenuButtons.changeTextOnButton(GameScreen.SAVE,
					GameScreen.PAUSE_BUTTON_TEXTS[GameScreen.SAVE]);
			hasSavedThisPauseMenu = false;
		}
	}


	@Override
	public void updateScreen(float deltaTime) {
		// if (getStateForAction(Action.PAUSE) > 0) {
		if (isStateForAction(Action.PAUSE) || isStateForAction(Action.ESCAPE)) {
			if (!pauseJustPressed) {
				setPaused(!paused);
			}
			pauseJustPressed = true;
		} else {
			pauseJustPressed = false;
		}

		if (paused) {
			pauseMenuButtons.updateSelected(getCamera());
			if (pauseMenuButtons.isSelectedSelected(getCamera())) {
				int selected = pauseMenuButtons.getSelected();
				selectPauseOption(selected);
			}

		} else {
			if (!entityManager.isTransitioning()) {
				explosionsUpdate(deltaTime);
				bombsUpdate(deltaTime);
				projectilesUpdate(deltaTime);
				enemiesUpdate(deltaTime);
				playerUpdate(deltaTime);

				if (playerCharacter.getHealth() <= 0) {
					setScreen(new GameOverScreen(getGame()));
					return;
				}

				handleCollisions();
				cleanUpDeadThings();

				boolean completed = entityManager.checkLevelCompletion();
				if (completed && !hasCompletedLevel) {
					hasCompletedLevel = true;
					// Update the text and location of the objective gui
					String levelCompleteText = "Level Complete!";
					int x = getWindowWidth() - GameScreen.TEXT_EDGE_OFFSET
							- getTextWidth(guiFont, levelCompleteText);
					dungeonGui.editData(GameScreen.OBJECTIVE_ID, levelCompleteText);
					dungeonGui.moveData(GameScreen.OBJECTIVE_ID, x,
							dungeonGui.getDataY(GameScreen.OBJECTIVE_ID));

				}

			}
		}
	}


	@Override
	public void renderScreen(float deltaTime, SpriteBatch batch) {
		clearScreen();
		updateAndSetCamera();

		dungeonGui.editData(GameScreen.HEALTH_ID, "Health: " + MathUtils.floor(playerCharacter.getHealth()));
		dungeonGui.editData(GameScreen.SCORE_ID, "Score: " + playerCharacter.getScore());
		dungeonGui.editData(GameScreen.BOMBS_ID, "x" + playerCharacter.getBombCount());

		batch.begin();
		entityManager.render(deltaTime, batch);
		dungeonGui.render(batch);
		batch.end();

		renderFlightBar(getPlayerCharacter());
		if (paused) {
			renderPauseOverlay();
		}

		entityManager.renderMapOverlay();

		if (renderDebugGrid) {
			entityManager.renderGridOverlay();
		}

	}


	/**
	 * Render Pause Menu shape overlay over screen, pause GUI and buttons.
	 */
	private void renderPauseOverlay() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(getCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);

		float windowWidth = getWindowWidth();
		float windowHeight = getWindowHeight();

		// Overlay entire window
		float colour = 0.2f;
		float alpha = 0.5f;
		shapeRenderer.setColor(colour, colour, colour, alpha);
		shapeRenderer.rect(0, 0, windowWidth, windowHeight);

		// Pause menu box size
		float width = 500;
		float height = 500;

		/// Pause menu box
		float x = (windowWidth / 2) - (width / 2);
		float y = (windowHeight / 2) - (height / 2);
		shapeRenderer.setColor(colour, colour, colour, 1f);
		shapeRenderer.rect(x, y, width, height);

		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);

		SpriteBatch batch = getBatch();

		batch.begin();
		pauseGui.render(batch);
		pauseMenuButtons.render(batch);
		batch.end();
	}


	/**
	 * @param playerCharacter
	 */
	private void renderFlightBar(PlayerCharacter playerCharacter) {
		float maxFlightTime = playerCharacter.getMaxFlightTime();
		float flightTime = playerCharacter.getFlightTime();

		float flightBarFillWidth = (flightBarWidth / maxFlightTime) * flightTime;

		shapeRenderer.setProjectionMatrix(getCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		float borderSize = 2f;
		shapeRenderer.rect(flightBarX - borderSize, flightBarY - borderSize, flightBarWidth + (borderSize * 2),
				flightBarHeight + (borderSize * 2));
		shapeRenderer.rect(flightBarX, flightBarY, flightBarWidth, flightBarHeight);

		if (playerCharacter.hasUsedAllFlight()) {
			shapeRenderer.setColor(Color.RED);
		} else if (flightTime >= maxFlightTime) {
			shapeRenderer.setColor(Color.GREEN);
		} else {
			shapeRenderer.setColor(Color.YELLOW);
		}
		shapeRenderer.rect(flightBarX, flightBarY, flightBarFillWidth, flightBarHeight);

		shapeRenderer.end();
	}


	/**
	 * If player not on game window will pause screen i.e. click off window
	 */
	@Override
	public void pause() {
		super.pause();
		if (GameScreen.PAUSE_ON_LOSE_FOCUS) {
			setPaused(true);
		}
	}


	@Override
	public void dispose() {
		super.dispose();
		guiFont.dispose();
		pauseFont.dispose();
		pauseMenuFont.dispose();
	}


	/**
	 * @param deltaTime
	 */
	private void bombsUpdate(float deltaTime) {
		ArrayList<Bomb> deadBombs = new ArrayList<Bomb>();

		for (Bomb bomb : entityManager.getBombs()) {
			bomb.update(deltaTime);

			if (bomb.getBlastTime() < 0) {
				bomb.goKaboom(entityManager);
				Explosion explosion = new Explosion(getGame(), AssetLocations.EXPLOSION,
						bomb.getBottomLeft(), bomb.getBlastRadius());
				entityManager.addExplosion(explosion);
				deadBombs.add(bomb);
			}
		}

		for (Bomb bomb : deadBombs) {
			entityManager.removeBomb(bomb);
		}
	}


	private void explosionsUpdate(float deltaTime) {
		ArrayList<Explosion> deadExplosions = new ArrayList<Explosion>();

		for (Explosion explosion : entityManager.getExplosionList()) {
			explosion.update(deltaTime);
			if (explosion.getViewTime() < 0) {
				deadExplosions.add(explosion);
			}
		}

		for (Explosion explosion : deadExplosions) {
			entityManager.removeExplosion(explosion);
		}
	}


	/**
	 * @param deltaTime
	 */
	private void enemiesUpdate(float deltaTime) {
		if (entityManager.getRoomTimer() < GameScreen.ROOM_START_TIME) {
			return;
		}

		for (Enemy enemy : entityManager.getEnemies()) {
			enemy.update(deltaTime);

			if ((enemy.getAttackType() != AttackType.TOUCH) && enemy.checkRangedAttack()) {
				entityManager.addNewProjectiles(enemy.rangedAttack(playerCharacter));
			}

		}
	}


	/**
	 * @param deltaTime
	 */
	private void projectilesUpdate(float deltaTime) {
		ArrayList<Projectile> projectileList = entityManager.getProjectiles();
		for (Projectile projectile : projectileList) {
			projectile.update(deltaTime);
		}
	}


	/**
	 * @param deltaTime
	 */
	private void playerUpdate(float deltaTime) {
		playerCharacter.update(deltaTime);

		if (playerCharacter.isFiring()) {
			if (playerCharacter.checkRangedAttack(deltaTime)) {
				entityManager.addNewProjectiles(playerCharacter.rangedAttack());
			}
		} else {
			if (playerCharacter.getTimeSinceLastAttack() < 0.5) {
				playerCharacter.incrementTimeSinceLastAttack(deltaTime);
			}
		}

		if (playerCharacter.checkBombDrop(deltaTime)) {
			if (!playerCharacter.isFlying()) {
				Bomb bomb = playerCharacter.attemptDropBomb();
				if (bomb != null) {
					entityManager.addBomb(bomb);
				}
			}
		}
	}


	/**
	 *
	 */
	private void cleanUpDeadThings() {
		entityManager.killEnemies();
		entityManager.killProjectiles();
		entityManager.killItems();
	}


	/**
	 * Iterates through all enemies and obstacles, and calculates collisions with each other and player character
	 * Could be optimised, but at the moment, doesn't chug, even with many enemies, obstacles and projectile
	 */
	private void handleCollisions() {
		ArrayList<Obstacle> obstacles = entityManager.getObstacles();
		ArrayList<Enemy> enemies = entityManager.getEnemies();
		ArrayList<Projectile> projectiles = entityManager.getProjectiles();
		ArrayList<Item> items = entityManager.getItems();
		ArrayList<Bomb> bombs = entityManager.getBombs();

		for (Projectile projectile : projectiles) {
			projectileWallCollision(projectile);
			projectilePlayerCollision(projectile);
		}

		for (Obstacle obstacle : obstacles) {
			playerObstacleCollision(obstacle);
		}

		for (Enemy enemy : enemies) {
			playerEnemyCollision(enemy);
			enemyWallCollision(enemy);

			boolean enemyCollidedWithObstacle = false;
			for (Obstacle obstacle : obstacles) {
				if (enemyObstacleCollision(enemy, obstacle)) {
					enemyCollidedWithObstacle = true;
				}
			}

			if (enemyCollidedWithObstacle) {
				enemy.flipDirection();
			}
		}

		for (Bomb bomb : bombs) {
			bombWallCollision(bomb);

			boolean bombCollidedWithObstacle = false;
			for (Obstacle obstacle : obstacles) {
				if (bombObstacleCollision(bomb, obstacle)) {
					bombCollidedWithObstacle = true;
				}
			}

			if (bombCollidedWithObstacle) {
				bomb.flipDirection();
			}
		}

		playerWallCollision();

		for (Item item : items) {
			playerItemCollision(item);
		}

		if (entityManager.getCurrentDungeonRoom().areAllEnemiesDead()) {
			playerDoorCollision();
		}

		for (Enemy enemy : enemies) {
			for (Projectile projectile : projectiles) {
				projectileEnemyCollision(projectile, enemy);
			}
		}

		for (Obstacle obstacle : obstacles) {
			for (Projectile projectile : projectiles) {
				projectileObstacleCollision(projectile, obstacle);
			}
		}

		// Push enemies out of each other
		for (int i = 0; i < (enemies.size() - 1); i += 1) {
			for (int j = i + 1; j < enemies.size(); j += 1) {
				Enemy e0 = enemies.get(i);
				Enemy e1 = enemies.get(j);
				enemyEnemyCollision(e0, e1);
			}
		}
	}


	/**
	 * Pushes e0 out of e1.
	 *
	 * @param e0
	 * @param e1
	 */
	private void enemyEnemyCollision(MoveableEntity e0, MoveableEntity e1) {
		if (Intersector.overlaps(e0.getCircleHitbox(), e1.getCircleHitbox())) {
			Vector2 c0 = e0.getCenter();
			Vector2 c1 = e1.getCenter();

			Vector2 c1ToC0 = c0.cpy().sub(c1);

			e0.getVelocity().add(c1ToC0.scl(1.5f));

		}
	}


	/**
	 * @param projectile
	 * @param enemy
	 */
	private void projectileEnemyCollision(Projectile projectile, Enemy enemy) {
		if ((Intersector.overlaps(enemy.getCircleHitbox(), projectile.getCollisionBox()))
				&& !(projectile.getDamagesWho() == ProjectileDamager.PLAYER)) {
			projectile.killSelf();
			enemy.takeDamage(projectile.getDamage());
		}
	}


	/**
	 * @param projectile
	 * @param obstacle
	 */
	private void projectileObstacleCollision(Projectile projectile, Obstacle obstacle) {
		if (Intersector.overlaps(projectile.getCollisionBox(), obstacle.getRectangleHitbox())) {
			projectile.killSelf();
		}
	}


	/**
	 * @param projectile
	 */
	private void projectilePlayerCollision(Projectile projectile) {
		if ((Intersector.overlaps(playerCharacter.getCircleHitbox(), projectile.getCollisionBox()))
				&& !(projectile.getDamagesWho() == ProjectileDamager.ENEMY)) {
			projectile.killSelf();
			playerCharacter.takeDamage(projectile.getDamage());
		}
	}


	/**
	 * @param projectile
	 */
	private void projectileWallCollision(Projectile projectile) {
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


	/**
	 * @param obstacle
	 */
	private void playerObstacleCollision(Obstacle obstacle) {
		if (Intersector.overlaps(playerCharacter.getCircleHitbox(), obstacle.getRectangleHitbox())) {
			playerCharacter.moveToNearestEdgeRectangle(obstacle);

			if (obstacle.isDamaging()) {
				playerCharacter.takeDamage(obstacle.getTouchDamage());
			}
		}
	}


	/**
	 * If the player has collided with an item, attempt to apply the item to the player. If it was applied, mark the
	 * item's life over and return true.
	 *
	 * @param item
	 * @return
	 */
	private boolean playerItemCollision(Item item) {
		boolean applied = false;

		if (Intersector.overlaps(playerCharacter.getCircleHitbox(), item.getRectangleHitbox())) {
			applied = item.applyToPlayer(playerCharacter);
		}

		if (applied) {
			item.setLifeOver();
		}

		return applied;
	}


	/**
	 * @param enemy
	 * @param obstacle
	 * @return
	 */
	private boolean bombObstacleCollision(Bomb bomb, Obstacle obstacle) {
		if (bomb.collides(obstacle)) {
			bomb.moveToNearestEdgeRectangle(obstacle);
			return true;
		}

		return false;
	}


	/**
	 * @param enemy
	 */
	private void enemyWallCollision(Enemy enemy) {
		enemy.setCollidingWithSomething(false);

		if (Intersector.overlaps(enemy.getCircleHitbox(), entityManager.getCurrentDungeonRoom().getTopWall())) {
			enemy.setVelocityY(-1 * Math.abs(enemy.getVelocityY()));
			enemy.setHitboxCentre(enemy.getCircleHitbox().x,
					entityManager.getCurrentDungeonRoom().getTopWall().getY()
							- enemy.getCircleHitbox().radius);
			enemy.setCollidingWithSomething(true);
		}

		if (Intersector.overlaps(enemy.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getRightWall())) {
			enemy.setVelocityX(-1 * Math.abs(enemy.getVelocityX()));
			enemy.setHitboxCentre(entityManager.getCurrentDungeonRoom().getRightWall().getX()
					- enemy.getCircleHitbox().radius, enemy.getCircleHitbox().y);
			enemy.setCollidingWithSomething(true);
		}

		if (Intersector.overlaps(enemy.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getLeftWall())) {
			enemy.setVelocityX(1 * Math.abs(enemy.getVelocityX()));
			enemy.setHitboxCentre(entityManager.getCurrentDungeonRoom().getLeftWall().getX()
					+ entityManager.getCurrentDungeonRoom().getLeftWall().getWidth()
					+ enemy.getCircleHitbox().radius, enemy.getCircleHitbox().y);
			enemy.setCollidingWithSomething(true);
		}

		if (Intersector.overlaps(enemy.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getBottomWall())) {
			enemy.setVelocityY(1 * Math.abs(enemy.getVelocityY()));
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
	private void playerEnemyCollision(Enemy enemy) {
		if (playerCharacter.collides(enemy)) {
			if (enemy.isCollidingWithSomething()) {
				playerCharacter.moveToNearestEdgeCircle(enemy);
			} else {
				enemy.moveToNearestEdgeCircle(playerCharacter);
			}

			playerCharacter.takeDamage(enemy.getTouchDamage());

		}
	}


	/**
	 *
	 */
	private void playerWallCollision() {
		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getTopWall())) {
			playerCharacter.setVelocityY(0);
			playerCharacter.setHitboxCentre(playerCharacter.getCircleHitbox().x,
					entityManager.getCurrentDungeonRoom().getTopWall().getY()
							- playerCharacter.getCircleHitbox().radius);
		}

		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getRightWall())) {
			playerCharacter.setVelocityX(0);
			playerCharacter.setHitboxCentre(
					entityManager.getCurrentDungeonRoom().getRightWall().getX()
							- playerCharacter.getCircleHitbox().radius,
					playerCharacter.getCircleHitbox().y);
		}

		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getLeftWall())) {
			playerCharacter.setVelocityX(0);
			playerCharacter.setHitboxCentre(getWallWidth() + playerCharacter.getCircleHitbox().radius,
					playerCharacter.getCircleHitbox().y);
		}

		if (Intersector.overlaps(playerCharacter.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getBottomWall())) {
			playerCharacter.setVelocityY(0);
			playerCharacter.setHitboxCentre(playerCharacter.getCircleHitbox().x,
					getWallWidth() + playerCharacter.getCircleHitbox().radius);
		}
	}


	@SuppressWarnings("unused")
	private void entityWallCollision(MoveableEntity entity) {
		// TODO: Entity wall collision
	}


	/**
	 *
	 */
	private void playerDoorCollision() {
		float doorVerticalOffset = 50; // 70
		float doorHorizontalOffset = 0;
		// TODO: Replace "player walking towards door" check with dot product
		boolean collidedWithDoor = false;

		if (!collidedWithDoor && entityManager.getCurrentDungeonRoom().hasUpDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getUpDoor()))
					&& (getStateForAction(Action.WALK_UP) > 0)) {
				collidedWithDoor = true;
				playerCharacter.setVelocityToZero();
				entityManager.moveToUpRoom();
				playerCharacter.setY(doorVerticalOffset);
			}
		}

		if (!collidedWithDoor && entityManager.getCurrentDungeonRoom().hasDownDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getDownDoor()))
					&& (getStateForAction(Action.WALK_DOWN) > 0)) {
				collidedWithDoor = true;
				playerCharacter.setVelocityToZero();
				entityManager.moveToDownRoom();
				playerCharacter.setY(
						getWindowHeight() - playerCharacter.getHeight() - doorVerticalOffset);
			}
		}

		if (!collidedWithDoor && entityManager.getCurrentDungeonRoom().hasRightDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getRightDoor()))
					&& (getStateForAction(Action.WALK_RIGHT) > 0)) {
				collidedWithDoor = true;
				playerCharacter.setVelocityToZero();
				entityManager.moveToRightRoom();
				playerCharacter.setX(doorHorizontalOffset);
			}
		}

		if (!collidedWithDoor && entityManager.getCurrentDungeonRoom().hasLeftDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getLeftDoor()))
					&& (getStateForAction(Action.WALK_LEFT) > 0)) {
				collidedWithDoor = true;
				playerCharacter.setVelocityToZero();
				entityManager.moveToLeftRoom();
				playerCharacter.setX(
						getWindowWidth() - playerCharacter.getWidth() - doorHorizontalOffset);
			}
		}
	}


	private boolean enemyObstacleCollision(Enemy enemy, Obstacle obstacle) {
		if (enemy.collides(obstacle)) {
			enemy.moveToNearestEdgeRectangle(obstacle);
			enemy.setCollidingWithSomething(true);
			return true;
		}

		return false;
	}


	// TODO: bombWallCollision is nearly same as enemyWallCollision
	private void bombWallCollision(Bomb bomb) {
		if (Intersector.overlaps(bomb.getCircleHitbox(), entityManager.getCurrentDungeonRoom().getTopWall())) {
			bomb.setVelocityY(-1 * Math.abs(bomb.getVelocityY()));
			bomb.setHitboxCentre(bomb.getCircleHitbox().x,
					entityManager.getCurrentDungeonRoom().getTopWall().getY()
							- bomb.getCircleHitbox().radius);
		}

		if (Intersector.overlaps(bomb.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getRightWall())) {
			bomb.setVelocityX(-1 * Math.abs(bomb.getVelocityX()));
			bomb.setHitboxCentre(entityManager.getCurrentDungeonRoom().getRightWall().getX()
					- bomb.getCircleHitbox().radius, bomb.getCircleHitbox().y);
		}

		if (Intersector.overlaps(bomb.getCircleHitbox(), entityManager.getCurrentDungeonRoom().getLeftWall())) {
			bomb.setVelocityX(1 * Math.abs(bomb.getVelocityX()));
			bomb.setHitboxCentre(entityManager.getCurrentDungeonRoom().getLeftWall().getX()
					+ entityManager.getCurrentDungeonRoom().getLeftWall().getWidth()
					+ bomb.getCircleHitbox().radius, bomb.getCircleHitbox().y);
		}

		if (Intersector.overlaps(bomb.getCircleHitbox(),
				entityManager.getCurrentDungeonRoom().getBottomWall())) {
			bomb.setVelocityY(1 * Math.abs(bomb.getVelocityY()));
			bomb.setHitboxCentre(bomb.getCircleHitbox().x,
					entityManager.getCurrentDungeonRoom().getBottomWall().getY() + entityManager
							.getCurrentDungeonRoom().getBottomWall().getHeight()
					+ bomb.getCircleHitbox().radius);
		}
	}
}
