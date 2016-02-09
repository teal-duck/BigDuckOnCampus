package com.muscovy.game.screen;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.EntityManager;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Enemy;
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


public class GameScreen extends ScreenBase {
	private static final boolean PAUSE_ON_LOSE_FOCUS = true;
	private static final float ROOM_START_TIME = 1f;

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
	private static final int TEXT_EDGE_OFFSET = 10;
	
	// GUI, GUI font
	private BitmapFont guiFont;
	private BitmapFont pauseFont;
	private GUI dungeonGui;
	private GUI pauseGui;

	// Pause status
	private boolean paused = false;
	private boolean pauseJustPressed = true;

	// Pause button options
	private static final int RESUME = 0;
	private static final int SAVE = 1;
	private static final int QUIT = 2;
	private static final String[] PAUSE_BUTTON_TEXTS = new String[] { "Resume", "Save", "Quit" };

	private ButtonList pauseMenuButtons;
	private BitmapFont pauseMenuFont;

	private ShapeRenderer shapeRenderer;


	public GameScreen(MuscovyGame game, Level level) {
		super(game);

		this.level = level;
		entityManager = new EntityManager(getGame(), level);
		// initialisePlayerCharacter();
		playerCharacter = getPlayerCharacter();
		resetPlayer();
		initialiseGui();
		entityManager.startLevel(playerCharacter);
	}


	private void initialiseGui() {

		// DUNGEON
		dungeonGui = new GUI();

		guiFont = AssetLocations.newFont20();
		guiFont.setColor(Color.WHITE);

		int dungeonGuiX = GameScreen.TEXT_EDGE_OFFSET;
		int dungeonGuiY = getWindowHeight();
		int dungeonGuiTop = 4;
		int dungeonGuiYSeparator = 30;

		dungeonGuiY -= dungeonGuiTop;
		dungeonGui.addData(GameScreen.HEALTH_ID, "Health: " + String.valueOf(playerCharacter.getHealth()),
				guiFont, dungeonGuiX, dungeonGuiY);
		dungeonGuiY -= dungeonGuiYSeparator;
		dungeonGui.addData(GameScreen.SCORE_ID, "Score: " + String.valueOf(playerCharacter.getScore()), guiFont,
				dungeonGuiX, dungeonGuiY);

		dungeonGuiY = 26;
		dungeonGui.addData(GameScreen.LEVEL_NAME, level.getName(), guiFont, dungeonGuiX, dungeonGuiY);

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
		pauseMenuButtons = new ButtonList(GameScreen.PAUSE_BUTTON_TEXTS, pauseMenuFont, getCamera(),
				getTextureMap(), getControlMap(), getController());
		setPauseButtonLocations();

	}


	public void resetPlayer() {
		Sprite playerSprite = playerCharacter.getSprite();

		float playerStartX = getWindowWidth() / 2;
		float playerStartY = getWindowHeight() / 2;
		playerStartX -= playerSprite.getRegionWidth() / 2;
		playerStartY -= playerSprite.getRegionHeight() / 2;

		playerCharacter.setX(playerStartX);
		playerCharacter.setY(playerStartY);
	}


	private int getTextWidth(BitmapFont font, String text) {
		GlyphLayout glyphLayout = new GlyphLayout(font, text);
		return (int) glyphLayout.width;
	}


	public PlayerCharacter getPlayer() {
		return playerCharacter;
	}


	private void setPauseButtonLocations() {
		int x = (getWindowWidth() / 2) - (ButtonList.BUTTON_WIDTH / 2);
		ButtonList.getHeightForDefaultButtonList(GameScreen.PAUSE_BUTTON_TEXTS.length);

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
			paused = false;
			break;
		case SAVE:
			selectSave();
			break;
		case QUIT:
			selectQuit();
			break;
		}
	}


	private void selectSave() {
		// TODO: Save Game
		Gdx.app.log("TODO", "Save game");
		getGame().saveCurrentGame();
	}


	private void selectQuit() {
		setScreen(new LevelSelectScreen(getGame()));
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
		pauseMenuButtons.updateSelected();
		if (pauseMenuButtons.isSelectedSelected()) {
			int selected = pauseMenuButtons.getSelected();
			selectPauseOption(selected);
		}

		if (paused) {
			if (getStateForAction(Action.ESCAPE) > 0) {
				setScreen(new LevelSelectScreen(getGame(), level.getLevelType()));
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

				boolean completed = entityManager.checkLevelCompletion();
				if (completed && !hasCompletedLevel) {
					hasCompletedLevel = true;
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

		batch.begin();
		entityManager.render(deltaTime, batch);
		dungeonGui.render(batch);
		batch.end();

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
	 * If player not on game window will pause screen i.e. click off window
	 */
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
		pauseMenuFont.dispose();
		// entityManager.dispose();
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
			enemy.setJustDamagedTime(Enemy.JUST_DAMAGED_TIME);
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
			enemy.setCollidingWithSomething(true);

			// TODO: Should enemies be damaged by obstacles?
			if (obstacle.isDamaging()) {
				enemy.takeDamage(obstacle.getTouchDamage());
			}
		}
	}


	public void enemyWallCollision(Enemy enemy) {
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
	public void playerEnemyCollision(Enemy enemy) {
		if (playerCharacter.collides(enemy)) {
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


	public void entityWallCollision(MoveableEntity entity) {

	}


	public void playerDoorCollision() {
		float doorOffset = 50; // 70
		// TODO: Replace "player walking towards door" check with dot product
		boolean collidedWithDoor = false;

		if (!collidedWithDoor && entityManager.getCurrentDungeonRoom().hasUpDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getUpDoor()))
					&& (getStateForAction(Action.WALK_UP) > 0)) {
				collidedWithDoor = true;
				playerCharacter.setVelocityToZero();
				entityManager.moveToUpRoom();
				playerCharacter.setY(doorOffset);
			}
		}

		if (!collidedWithDoor && entityManager.getCurrentDungeonRoom().hasDownDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getDownDoor()))
					&& (getStateForAction(Action.WALK_DOWN) > 0)) {
				collidedWithDoor = true;
				playerCharacter.setVelocityToZero();
				entityManager.moveToDownRoom();
				playerCharacter.setY(getWindowHeight() - playerCharacter.getHeight() - doorOffset);
			}
		}

		if (!collidedWithDoor && entityManager.getCurrentDungeonRoom().hasRightDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getRightDoor()))
					&& (getStateForAction(Action.WALK_RIGHT) > 0)) {
				collidedWithDoor = true;
				playerCharacter.setVelocityToZero();
				entityManager.moveToRightRoom();
				playerCharacter.setX(doorOffset);
			}
		}

		if (!collidedWithDoor && entityManager.getCurrentDungeonRoom().hasLeftDoor()) {
			if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),
					entityManager.getCurrentDungeonRoom().getLeftDoor()))
					&& (getStateForAction(Action.WALK_LEFT) > 0)) {
				collidedWithDoor = true;
				playerCharacter.setVelocityToZero();
				entityManager.moveToLeftRoom();
				playerCharacter.setX(getWindowWidth() - playerCharacter.getWidth() - doorOffset);
			}
		}
	}
}
