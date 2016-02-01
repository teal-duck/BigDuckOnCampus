package com.muscovy.game;


import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.entity.Item;
import com.muscovy.game.entity.Obstacle;
import com.muscovy.game.entity.OnscreenDrawable;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.entity.Projectile;
import com.muscovy.game.enums.ObjectiveType;
import com.muscovy.game.enums.RoomType;
import com.muscovy.game.level.DungeonRoom;
import com.muscovy.game.level.Level;


/**
 * Created by ewh502 on 04/12/2015.
 */
public class EntityManager {
	private MuscovyGame game;
	private Level level;
	private PlayerCharacter playerCharacter;

	private ArrayList<OnscreenDrawable> renderList;
	private ArrayList<Obstacle> obstacleList;
	private ArrayList<Enemy> enemyList;
	private ArrayList<Projectile> projectileList;
	private ArrayList<Item> itemList;

	private DungeonRoom currentDungeonRoom;
	private int currentRoomX;
	private int currentRoomY;
	private float roomTimer = 0;

	// TODO: Only load 1 door texture and rotate when rendering
	private Texture upDoorTextureOpen;
	private Texture downDoorTextureOpen;
	private Texture rightDoorTextureOpen;
	private Texture leftDoorTextureOpen;
	private Texture upDoorTextureClosed;
	private Texture downDoorTextureClosed;
	private Texture rightDoorTextureClosed;
	private Texture leftDoorTextureClosed;

	private ShapeRenderer shapeRenderer;
	private BitmapFont levelCompleteFont;


	public EntityManager(MuscovyGame game, Level level) {
		this.game = game;
		this.level = level;

		renderList = new ArrayList<OnscreenDrawable>();
		obstacleList = new ArrayList<Obstacle>();
		enemyList = new ArrayList<Enemy>();
		projectileList = new ArrayList<Projectile>();
		itemList = new ArrayList<Item>();

		levelCompleteFont = AssetLocations.newFont();
		levelCompleteFont.setColor(Color.WHITE);

		currentDungeonRoom = null;

		TextureMap textureMap = game.getTextureMap();
		upDoorTextureOpen = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_UP_OPEN);
		upDoorTextureClosed = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_UP_CLOSED);
		rightDoorTextureOpen = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_RIGHT_OPEN);
		rightDoorTextureClosed = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_RIGHT_CLOSED);
		leftDoorTextureOpen = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_LEFT_OPEN);
		leftDoorTextureClosed = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_LEFT_CLOSED);
		downDoorTextureOpen = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_DOWN_OPEN);
		downDoorTextureClosed = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_DOWN_CLOSED);

		shapeRenderer = new ShapeRenderer();
	}


	public void startLevel(PlayerCharacter playerCharacter) {
		this.playerCharacter = playerCharacter;
		currentRoomX = level.getStartX();
		currentRoomY = level.getStartY();
		setCurrentDungeonRoom(currentRoomX, currentRoomY);
		level.markRoomVisited(currentRoomX, currentRoomY);
		renderList.add(this.playerCharacter);
	}


	private void setCurrentDungeonRoom(DungeonRoom dungeonRoom) {
		roomTimer = 0;

		renderList.clear();
		obstacleList.clear();
		projectileList.clear();
		enemyList.clear();

		currentDungeonRoom = dungeonRoom;
		addNewObstacles(dungeonRoom.getObstacleList());
		addNewEnemies(dungeonRoom.getEnemyList());
	}


	public void setCurrentDungeonRoom(int roomX, int roomY) {
		DungeonRoom room = level.getRoom(roomX, roomY);
		setCurrentDungeonRoom(room);
		level.markRoomVisited(roomX, roomY);
	}


	public void moveToUpRoom() {
		currentRoomY -= 1;
		setCurrentDungeonRoom(currentRoomX, currentRoomY);
		renderList.add(playerCharacter);
	}


	public void moveToRightRoom() {
		currentRoomX += 1;
		setCurrentDungeonRoom(currentRoomX, currentRoomY);
		renderList.add(playerCharacter);
	}


	public void moveToLeftRoom() {
		currentRoomX -= 1;
		setCurrentDungeonRoom(currentRoomX, currentRoomY);
		renderList.add(playerCharacter);
	}


	public void moveToDownRoom() {
		currentRoomY += 1;
		setCurrentDungeonRoom(currentRoomX, currentRoomY);
		renderList.add(playerCharacter);
	}


	/**
	 * Renders sprites in the controller so those further back are rendered first, giving a perspective illusion
	 *
	 * @param deltaTime
	 * @param batch
	 */
	public void render(float deltaTime, SpriteBatch batch) {
		// Timer used to give the player a few seconds to look at a room before attacking
		roomTimer += deltaTime;

		sortDrawables();

		batch.draw(currentDungeonRoom.getBackgroundTexture(), 0, 0);

		final int windowWidth = game.getWindowWidth();
		final int windowHeight = game.getWindowHeight();
		final int tileSize = game.getTileSize();

		Texture doorTexture;
		if (currentDungeonRoom.hasUpDoor()) {
			doorTexture = (currentDungeonRoom.areAllEnemiesDead() ? upDoorTextureOpen
					: upDoorTextureClosed);
			batch.draw(doorTexture, (windowWidth - doorTexture.getWidth()) / 2,
					currentDungeonRoom.getUpDoor().getY()
							+ (currentDungeonRoom.getUpDoor().getWidth() - tileSize));
		}

		if (currentDungeonRoom.hasDownDoor()) {
			doorTexture = (currentDungeonRoom.areAllEnemiesDead() ? downDoorTextureOpen
					: downDoorTextureClosed);
			batch.draw(doorTexture, (windowWidth - downDoorTextureOpen.getWidth()) / 2,
					currentDungeonRoom.getDownDoor().getY() + 4);
		}

		if (currentDungeonRoom.hasRightDoor()) {
			doorTexture = (currentDungeonRoom.areAllEnemiesDead() ? rightDoorTextureOpen
					: rightDoorTextureClosed);
			batch.draw(doorTexture,
					currentDungeonRoom.getRightDoor().getX()
							+ (currentDungeonRoom.getRightDoor().getWidth() - tileSize),
					(windowHeight - rightDoorTextureOpen.getWidth()) / 2);
		}

		if (currentDungeonRoom.hasLeftDoor()) {
			doorTexture = (currentDungeonRoom.areAllEnemiesDead() ? leftDoorTextureOpen
					: leftDoorTextureClosed);
			batch.draw(doorTexture, currentDungeonRoom.getLeftDoor().getX() + 4,
					(windowHeight - leftDoorTextureOpen.getWidth()) / 2);
		}

		for (OnscreenDrawable drawable : renderList) {
			boolean shouldDraw = true;

			if (drawable instanceof PlayerCharacter) {
				PlayerCharacter p = (PlayerCharacter) drawable;
				if (p.isInvincible() && (((p.getInvincibilityCounter() * 10) % 2) < 0.75)) {
					shouldDraw = false;

				}
			}

			if (shouldDraw) {
				batch.draw(drawable.getSprite().getTexture(), drawable.getX(), drawable.getY());
			}
		}

		for (Projectile projectile : projectileList) {
			batch.draw(projectile.getSprite().getTexture(), projectile.getX(), projectile.getY());
		}

		if (level.isCompleted()) {
			levelCompleteFont.draw(batch, "LEVEL COMPLETED, PRESS P AND ESC TO CHOOSE ANOTHER",
					(windowWidth / 2) - 200, windowHeight - 69);
		}
	}


	public void renderMapOverlay() {
		shapeRenderer.setProjectionMatrix(game.getCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);

		float renderWidth = 25;
		float renderHeight = 15;

		// Size of items inside room (e.g. player location)
		float innerRoomSize = 10;

		// The distance between the rooms
		float doorLength = 5;
		// How thick the doors are
		float doorWidth = 5;

		float xOffset = (game.getWindowWidth() - ((renderWidth + doorLength) * level.getRoomsWide()))
				+ doorLength;
		float yOffset = game.getWindowHeight() - renderHeight;

		float windowEdgeOffset = 0;
		xOffset -= windowEdgeOffset;
		yOffset -= windowEdgeOffset;

		final Color roomColour = Color.WHITE;
		final Color bossColour = Color.BLUE;
		final Color shopColour = Color.BROWN;
		final Color itemColour = Color.YELLOW;
		final Color playerColour = Color.RED;
		final Color doorColour = Color.BLACK;
		final float darkerScale = 0.35f;

		for (int row = 0; row < level.getRoomsHigh(); row += 1) {
			for (int col = 0; col < level.getRoomsWide(); col += 1) {
				DungeonRoom room = level.getRoom(col, row);
				if (room == null) {
					continue;
				}

				// Only render visited or neighbours of visited rooms
				boolean isVisited = level.isRoomVisited(col, row);
				if (!isVisited && !level.isRoomNeighbourVisited(col, row)) {
					continue;
				}

				Color colour = roomColour;
				if (room.getRoomType() == RoomType.BOSS) {
					colour = bossColour;
				}
				if (room.getRoomType() == RoomType.SHOP) {
					colour = shopColour;
				}
				if (room.getRoomType() == RoomType.ITEM) {
					colour = itemColour;
				}

				// If player hasn't visited room, make the colour darker
				if (!isVisited) {
					colour = new Color(colour.r * darkerScale, colour.g * darkerScale,
							colour.b * darkerScale, 1f);
				}

				float x = xOffset + (col * (renderWidth + doorLength));
				float y = yOffset - (row * (renderHeight + doorLength));
				float w = renderWidth;
				float h = renderHeight;

				shapeRenderer.setColor(colour);
				shapeRenderer.rect(x, y, w, h);

				// Render the player in the current room
				if ((currentRoomX == col) && (currentRoomY == row)) {
					w = innerRoomSize;
					h = innerRoomSize;
					x += (renderWidth / 2) - (w / 2);
					y += (renderHeight / 2) - (h / 2);

					shapeRenderer.setColor(playerColour);
					shapeRenderer.rect(x, y, w, h);
				}
			}
		}

		shapeRenderer.setColor(doorColour);

		// TODO: When rendering doors in maps, check if room actually has a door
		// If we add bombs like binding of isaac, where you can get to secrets by blowing up a wall
		// We need to render this on the map so a secret room is only visible if they've been there

		// For rendering doors, only look at the right and down
		for (int col = 0; col < level.getRoomsWide(); col += 1) {
			for (int row = 0; row < level.getRoomsHigh(); row += 1) {
				DungeonRoom room = level.getRoom(col, row);
				if (room == null) {
					continue;
				}

				DungeonRoom rightRoom = level.getRoom(col + 1, row);
				if (rightRoom != null) {
					if (level.isRoomVisited(col, row) || level.isRoomVisited(col + 1, row)) {
						float x = xOffset + (col * (renderWidth + doorLength)) + renderWidth;
						float y = ((yOffset - (row * (renderHeight + doorLength)))
								+ (renderHeight / 2)) - (doorWidth / 2);
						float w = doorLength;
						float h = doorWidth;

						shapeRenderer.rect(x, y, w, h);
					}
				}

				DungeonRoom downRoom = level.getRoom(col, row + 1);
				if (downRoom != null) {
					if (level.isRoomVisited(col, row) || level.isRoomVisited(col, row + 1)) {
						float x = (xOffset + (col * (renderWidth + doorLength))
								+ (renderWidth / 2)) - (doorWidth / 2);
						float y = yOffset - (row * (renderHeight + doorLength)) - doorLength;
						float w = doorWidth;
						float h = doorLength;

						shapeRenderer.rect(x, y, w, h);
					}
				}
			}
		}

		shapeRenderer.end();
	}


	/**
	 * Quicksorts the list of drawable objects in the controller by Y coordinate so it renders the things in the
	 * background first.
	 *
	 */
	private void sortDrawables() {
		// TODO: Optimise sortDrawables
		renderList = quicksort(renderList);
	}


	/*
	 * Quicksort Helper Methods
	 *
	 * @param input
	 *
	 * @return
	 */
	private ArrayList<OnscreenDrawable> quicksort(ArrayList<OnscreenDrawable> input) {
		if (input.size() <= 1) {
			return input;
		}

		int middle = (int) Math.ceil((double) input.size() / 2);
		OnscreenDrawable pivot = input.get(middle);

		ArrayList<OnscreenDrawable> less = new ArrayList<OnscreenDrawable>();
		ArrayList<OnscreenDrawable> greater = new ArrayList<OnscreenDrawable>();

		for (int i = 0, l = input.size(); i < l; i += 1) {
			OnscreenDrawable node = input.get(i);
			if (node.getY() >= pivot.getY()) {
				if (i == middle) {
					continue;
				}
				less.add(node);
			} else {
				greater.add(node);
			}
		}

		return concatenate(quicksort(less), pivot, quicksort(greater));
	}


	private ArrayList<OnscreenDrawable> concatenate(ArrayList<OnscreenDrawable> less, OnscreenDrawable pivot,
			ArrayList<OnscreenDrawable> greater) {
		less.add(pivot);
		less.addAll(greater);
		return less;
	}


	public void killProjectiles() {
		ArrayList<Projectile> deadProjectiles = new ArrayList<Projectile>();

		for (Projectile projectile : projectileList) {
			if (projectile.isLifeOver()) {
				deadProjectiles.add(projectile);
			}
		}

		for (Projectile projectile : deadProjectiles) {
			projectileList.remove(projectile);
		}
	}


	public void killEnemies() {
		ArrayList<Enemy> deadEnemies = new ArrayList<Enemy>();

		for (Enemy enemy : enemyList) {
			if (enemy.isLifeOver()) {
				deadEnemies.add(enemy);
			}
		}

		for (Enemy enemy : deadEnemies) {
			playerCharacter.increaseScore(enemy.getScoreOnDeath());
			renderList.remove(enemy);
			enemyList.remove(enemy);
			currentDungeonRoom.killEnemy(enemy);
		}
	}


	public boolean checkLevelCompletion() {
		ObjectiveType objectiveType = level.getObjectiveType();

		// If the level has already been completed, don't check again
		boolean completed = level.isCompleted();
		if (completed) {
			return true;
		}

		switch (objectiveType) {
		case BOSS:
			// TODO: Implement level.getBossRoom()
			if (currentDungeonRoom.areAllEnemiesDead()
					&& (currentDungeonRoom.getRoomType() == RoomType.BOSS)) {
				completed = true;
			}
			break;
		case KILL_ENEMIES:
			if (level.areAllEnemiesDead() && level.areAllRoomsVisited()) {
				completed = true;
			}
			break;
		case FIND_ITEM:
			completed = false;
			break;
		}

		level.setCompleted(completed);
		return completed;
	}


	public void dispose() {
		levelCompleteFont.dispose();
	}


	public DungeonRoom getCurrentDungeonRoom() {
		return currentDungeonRoom;
	}


	public float getRoomTimer() {
		return roomTimer;
	}


	public void addNewDrawable(OnscreenDrawable drawable) {
		renderList.add(drawable);
	}


	public ArrayList<Item> getItems() {
		return itemList;
	}


	public void addNewDrawables(ArrayList<OnscreenDrawable> drawables) {
		renderList.addAll(drawables);
	}


	public void addNewObstacle(Obstacle obstacle) {
		renderList.add(obstacle);
		obstacleList.add(obstacle);
	}


	public void addNewObstacles(ArrayList<Obstacle> obstacles) {
		renderList.addAll(obstacles);
		obstacleList.addAll(obstacles);
	}


	public void addNewEnemy(Enemy enemy) {
		renderList.add(enemy);
		enemyList.add(enemy);
	}


	public void addNewEnemies(ArrayList<Enemy> enemies) {
		renderList.addAll(enemies);
		enemyList.addAll(enemies);
	}


	public void addNewProjectile(Projectile projectile) {
		projectileList.add(projectile);
	}


	public void addNewProjectiles(ArrayList<Projectile> projectiles) {
		projectileList.addAll(projectiles);
	}


	public ArrayList<Obstacle> getObstacles() {
		return obstacleList;
	}


	public ArrayList<Enemy> getEnemies() {
		return enemyList;
	}


	public ArrayList<Projectile> getProjectiles() {
		return projectileList;
	}


	public PlayerCharacter getPlayer() {
		return playerCharacter;
	}
}
