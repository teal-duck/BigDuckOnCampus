package com.muscovy.game;


import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.entity.Item;
import com.muscovy.game.entity.Obstacle;
import com.muscovy.game.entity.OnscreenDrawable;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.entity.Projectile;
import com.muscovy.game.enums.ItemType;
import com.muscovy.game.enums.LevelType;
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

	private DungeonRoom previousDungeonRoom;
	private float transitionTime = 0f;
	private float MAX_TRANSITION_TIME = 0.3f;
	private ArrayList<OnscreenDrawable> previousRenderList;
	private Vector2 transitionDirection;
	private int previousRoomX;
	private int previousRoomY;

	// TODO: Only load 1 door texture and rotate when rendering
	private Texture upDoorTextureOpen;
	private Texture downDoorTextureOpen;
	private Texture rightDoorTextureOpen;
	private Texture leftDoorTextureOpen;
	private Texture upDoorTextureClosed;
	private Texture downDoorTextureClosed;
	private Texture rightDoorTextureClosed;
	private Texture leftDoorTextureClosed;
	private Texture upDoorTextureBoss;
	private Texture downDoorTextureBoss;
	private Texture rightDoorTextureBoss;
	private Texture leftDoorTextureBoss;

	private ShapeRenderer shapeRenderer;


	public EntityManager(MuscovyGame game, Level level) {
		this.game = game;
		this.level = level;

		renderList = new ArrayList<OnscreenDrawable>();
		obstacleList = new ArrayList<Obstacle>();
		enemyList = new ArrayList<Enemy>();
		projectileList = new ArrayList<Projectile>();
		itemList = new ArrayList<Item>();

		currentDungeonRoom = null;
		previousDungeonRoom = null;
		transitionDirection = new Vector2(0, 0);
		previousRenderList = new ArrayList<OnscreenDrawable>();

		TextureMap textureMap = game.getTextureMap();
		upDoorTextureOpen = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_UP_OPEN);
		upDoorTextureClosed = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_UP_CLOSED);
		upDoorTextureBoss = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_UP_BOSS);
		rightDoorTextureOpen = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_RIGHT_OPEN);
		rightDoorTextureClosed = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_RIGHT_CLOSED);
		rightDoorTextureBoss = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_RIGHT_BOSS);
		leftDoorTextureOpen = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_LEFT_OPEN);
		leftDoorTextureClosed = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_LEFT_CLOSED);
		leftDoorTextureBoss = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_LEFT_BOSS);
		downDoorTextureOpen = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_DOWN_OPEN);
		downDoorTextureClosed = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_DOWN_CLOSED);
		downDoorTextureBoss = textureMap.getTextureOrLoadFile(AssetLocations.DOOR_DOWN_BOSS);

		shapeRenderer = new ShapeRenderer();
	}


	/**
	 * @return
	 */
	public boolean isTransitioning() {
		return (transitionTime > 0);
	}


	/**
	 * @param playerCharacter
	 */
	public void startLevel(PlayerCharacter playerCharacter) {
		this.playerCharacter = playerCharacter;
		currentRoomX = level.getStartX();
		currentRoomY = level.getStartY();
		setCurrentDungeonRoom(currentRoomX, currentRoomY);
		level.markRoomVisited(currentRoomX, currentRoomY);
		renderList.add(this.playerCharacter);
	}


	/**
	 * @param dungeonRoom
	 * @param transitionDirection
	 */
	private void setCurrentDungeonRoom(DungeonRoom dungeonRoom, Vector2 transitionDirection) {
		roomTimer = 0;

		if (transitionDirection != null) {
			previousRenderList.clear();
			previousRenderList.addAll(renderList);
			previousRenderList.remove(playerCharacter);
			transitionTime = MAX_TRANSITION_TIME;
			this.transitionDirection.set(transitionDirection);
			previousDungeonRoom = currentDungeonRoom;
			previousRoomX = currentRoomX;
			previousRoomY = currentRoomY;
		}

		renderList.clear();
		obstacleList.clear();
		projectileList.clear();
		enemyList.clear();
		itemList.clear();

		currentDungeonRoom = dungeonRoom;
		addNewObstacles(dungeonRoom.getObstacleList());
		addNewEnemies(dungeonRoom.getEnemyList());
		addNewItems(dungeonRoom.getItemList());
	}


	/**
	 * @param roomX
	 * @param roomY
	 */
	public void setCurrentDungeonRoom(int roomX, int roomY) {
		setCurrentDungeonRoom(roomX, roomY, null);
	}


	/**
	 * @param roomX
	 * @param roomY
	 * @param transitionDirection
	 */
	public void setCurrentDungeonRoom(int roomX, int roomY, Vector2 transitionDirection) {
		DungeonRoom room = level.getRoom(roomX, roomY);
		setCurrentDungeonRoom(room, transitionDirection);
		level.markRoomVisited(roomX, roomY);
	}


	/**
	 *
	 */
	public void moveToUpRoom() {
		int newY = currentRoomY - 1;
		setCurrentDungeonRoom(currentRoomX, newY, new Vector2(0, 1));
		currentRoomY = newY;
		renderList.add(playerCharacter);
	}


	/**
	 *
	 */
	public void moveToRightRoom() {
		int newX = currentRoomX + 1;
		setCurrentDungeonRoom(newX, currentRoomY, new Vector2(1, 0));
		currentRoomX = newX;
		renderList.add(playerCharacter);
	}


	/**
	 *
	 */
	public void moveToLeftRoom() {
		int newX = currentRoomX - 1;
		setCurrentDungeonRoom(newX, currentRoomY, new Vector2(-1, 0));
		currentRoomX = newX;
		renderList.add(playerCharacter);
	}


	public void moveToDownRoom() {
		int newY = currentRoomY + 1;
		setCurrentDungeonRoom(currentRoomX, newY, new Vector2(0, -1));
		currentRoomY = newY;
		renderList.add(playerCharacter);
	}


	/**
	 * Renders sprites in the controller so those further back are rendered first, giving a perspective illusion
	 *
	 * @param deltaTime
	 * @param batch
	 */
	public void render(float deltaTime, SpriteBatch batch) {
		if (isTransitioning()) {
			transitionTime -= deltaTime;

			if (transitionTime < 0f) {
				transitionTime = 0f;
				previousDungeonRoom = null;
				previousRenderList.clear();
			} else {

				float transitionAmount = transitionTime / MAX_TRANSITION_TIME;
				float translateX = game.getWindowWidth() * transitionAmount * transitionDirection.x;
				float translateY = game.getWindowHeight() * transitionAmount * transitionDirection.y;

				renderDungeonRoom(batch, currentDungeonRoom, currentRoomX, currentRoomY, renderList,
						translateX, translateY);
				translateX -= transitionDirection.x * game.getWindowWidth();
				translateY -= transitionDirection.y * game.getWindowHeight();
				renderDungeonRoom(batch, previousDungeonRoom, previousRoomX, previousRoomY,
						previousRenderList, translateX, translateY);
			}
		}

		if (!isTransitioning()) {
			// Timer used to give the player a few seconds to look at a room before attacking
			roomTimer += deltaTime;
			renderDungeonRoom(batch, currentDungeonRoom, currentRoomX, currentRoomY, renderList, 0, 0);

			for (Projectile projectile : projectileList) {
				batch.draw(projectile.getSprite().getTexture(), projectile.getX(), projectile.getY());
			}
		}
	}


	/**
	 * @param batch
	 * @param dungeonRoom
	 * @param roomX
	 * @param roomY
	 * @param renderList
	 * @param translateX
	 * @param translateY
	 */
	private void renderDungeonRoom(SpriteBatch batch, DungeonRoom dungeonRoom, int roomX, int roomY,
			ArrayList<OnscreenDrawable> renderList, float translateX, float translateY) {
		renderList = sortDrawables(renderList);

		batch.draw(dungeonRoom.getBackgroundTexture(), translateX, translateY);

		Texture doorTexture;
		Rectangle doorRectangle;
		final float doorScale = 1.8f;

		if (dungeonRoom.hasUpDoor()) {
			if (dungeonRoom.areAllEnemiesDead()
					&& level.getUpRoom(roomX, roomY).getRoomType().equals(RoomType.BOSS)
					&& !level.getUpRoom(roomX, roomY).areAllEnemiesDead()) {
				doorTexture = upDoorTextureBoss;
			} else if (dungeonRoom.areAllEnemiesDead()) {
				doorTexture = upDoorTextureOpen;
			} else {
				doorTexture = upDoorTextureClosed;
			}

			doorRectangle = dungeonRoom.getUpDoor();
			renderDoor(batch, doorTexture, doorRectangle, translateX, translateY + 1, doorScale, 1f);
		}

		if (dungeonRoom.hasDownDoor()) {
			if (dungeonRoom.areAllEnemiesDead()
					&& level.getDownRoom(roomX, roomY).getRoomType().equals(RoomType.BOSS)
					&& !level.getDownRoom(roomX, roomY).areAllEnemiesDead()) {
				doorTexture = downDoorTextureBoss;
			} else if (dungeonRoom.areAllEnemiesDead()) {
				doorTexture = downDoorTextureOpen;
			} else {
				doorTexture = downDoorTextureClosed;
			}

			doorRectangle = dungeonRoom.getDownDoor();
			renderDoor(batch, doorTexture, doorRectangle, translateX, translateY - 1, doorScale, 1f);
		}

		if (dungeonRoom.hasRightDoor()) {
			if (dungeonRoom.areAllEnemiesDead()
					&& level.getRightRoom(roomX, roomY).getRoomType().equals(RoomType.BOSS)
					&& !level.getRightRoom(roomX, roomY).areAllEnemiesDead()) {
				doorTexture = rightDoorTextureBoss;
			} else if (dungeonRoom.areAllEnemiesDead()) {
				doorTexture = rightDoorTextureOpen;
			} else {
				doorTexture = rightDoorTextureClosed;
			}

			doorRectangle = dungeonRoom.getRightDoor();
			renderDoor(batch, doorTexture, doorRectangle, translateX + 1, translateY, 1f, doorScale);
		}

		if (dungeonRoom.hasLeftDoor()) {
			if (dungeonRoom.areAllEnemiesDead()
					&& level.getLeftRoom(roomX, roomY).getRoomType().equals(RoomType.BOSS)
					&& !level.getLeftRoom(roomX, roomY).areAllEnemiesDead()) {
				doorTexture = leftDoorTextureBoss;
			} else if (dungeonRoom.areAllEnemiesDead()) {
				doorTexture = leftDoorTextureOpen;
			} else {
				doorTexture = leftDoorTextureClosed;
			}

			doorRectangle = dungeonRoom.getLeftDoor();
			renderDoor(batch, doorTexture, doorRectangle, translateX - 1, translateY, 1f, doorScale);
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
				batch.draw(drawable.getSprite().getTexture(), translateX + drawable.getX(),
						translateY + drawable.getY());
			}
		}
	}


	/**
	 * @param batch
	 * @param doorTexture
	 * @param doorRectangle
	 * @param translateX
	 * @param translateY
	 * @param scaleX
	 * @param scaleY
	 */
	private void renderDoor(SpriteBatch batch, Texture doorTexture, Rectangle doorRectangle, float translateX,
			float translateY, float scaleX, float scaleY) {
		float width = doorRectangle.width;
		float height = doorRectangle.height;
		float scaledWidth = width * scaleX;
		float scaledHeight = height * scaleY;

		batch.draw(doorTexture, (translateX + doorRectangle.x) - ((scaledWidth - width) / 2),
				(translateY + doorRectangle.y) - ((scaledHeight - height) / 2), scaledWidth,
				scaledHeight);
	}


	/**
	 *
	 */
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
	 *
	 */
	public void renderGridOverlay() {
		shapeRenderer.setProjectionMatrix(game.getCamera().combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);

		int gridSize = game.getTileSize();
		int startX = game.getWallEdge();
		int startY = game.getWallEdge();
		int endX = game.getWindowWidth() - game.getWallEdge();
		int endY = game.getWindowHeight() - game.getWallEdge();

		int renderWidth = endX - startX;
		int renderHeight = endY - startY;

		int columns = (renderWidth / gridSize) + 1;
		int rows = (renderHeight / gridSize) + 1;

		int gridX = 0;
		int gy = 0;

		for (int x = 0; x < columns; x += 1) {
			gridX = startX + (x * gridSize);
			gy = startY + (0 * gridSize);
			shapeRenderer.line(gridX, gy, gridX, gy + renderHeight);

		}
		for (int y = 0; y < rows; y += 1) {
			gridX = startX + (0 * gridSize);
			gy = startY + (y * gridSize);
			shapeRenderer.line(gridX, gy, gridX + renderWidth, gy);
		}

		shapeRenderer.end();
	}


	/**
	 * Quicksorts the list of drawable objects in the controller by Y coordinate so it renders the things in the
	 * background first.
	 *
	 */
	private ArrayList<OnscreenDrawable> sortDrawables(ArrayList<OnscreenDrawable> renderList) {
		// TODO: Optimise sortDrawables
		return quicksort(renderList);
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


	/**
	 * @param less
	 * @param pivot
	 * @param greater
	 * @return
	 */
	private ArrayList<OnscreenDrawable> concatenate(ArrayList<OnscreenDrawable> less, OnscreenDrawable pivot,
			ArrayList<OnscreenDrawable> greater) {
		less.add(pivot);
		less.addAll(greater);
		return less;
	}


	/**
	 *
	 */
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


	/**
	 *
	 */
	public void killItems() {
		ArrayList<Item> deadItems = new ArrayList<Item>();

		for (Item item : itemList) {
			if (item.isLifeOver()) {
				deadItems.add(item);
			}
		}

		for (Item item : deadItems) {
			renderList.remove(item);
			itemList.remove(item);
		}
	}


	/**
	 *
	 */
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


	/**
	 * @return
	 */
	public boolean checkLevelCompletion() {
		ObjectiveType objectiveType = level.getObjectiveType();
		LevelType levelType = level.getLevelType();

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
				
				ItemType itemType = LevelType.getItemType(levelType);
				
				if (itemType != null) {
					Item item = new Item(game, ItemType.getItemTextureName(itemType), new Vector2(0, 0), itemType);
					item.setXTiles(5);
					item.setYTiles(5);
					currentDungeonRoom.addItem(item);
					addNewItem(item);
				}
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


	/**
	 * @return
	 */
	public DungeonRoom getCurrentDungeonRoom() {
		return currentDungeonRoom;
	}


	/**
	 * @return
	 */
	public float getRoomTimer() {
		return roomTimer;
	}


	/**
	 * @param drawable
	 */
	public void addNewDrawable(OnscreenDrawable drawable) {
		renderList.add(drawable);
	}


	/**
	 * @return
	 */
	public ArrayList<Item> getItems() {
		return itemList;
	}


	/**
	 * @param drawables
	 */
	public void addNewDrawables(ArrayList<OnscreenDrawable> drawables) {
		renderList.addAll(drawables);
	}


	/**
	 * @param obstacle
	 */
	public void addNewObstacle(Obstacle obstacle) {
		renderList.add(obstacle);
		obstacleList.add(obstacle);
	}


	/**
	 * @param obstacles
	 */
	public void addNewObstacles(ArrayList<Obstacle> obstacles) {
		renderList.addAll(obstacles);
		obstacleList.addAll(obstacles);
	}


	/**
	 * @param enemy
	 */
	public void addNewEnemy(Enemy enemy) {
		renderList.add(enemy);
		enemyList.add(enemy);
	}


	/**
	 * @param enemies
	 */
	public void addNewEnemies(ArrayList<Enemy> enemies) {
		renderList.addAll(enemies);
		enemyList.addAll(enemies);
	}
	
	
	public void addNewItem(Item item) {
		renderList.add(item);
		itemList.add(item);
	}


	/**
	 * @param items
	 */
	public void addNewItems(ArrayList<Item> items) {
		renderList.addAll(items);
		itemList.addAll(items);
	}


	/**
	 * @param projectile
	 */
	public void addNewProjectile(Projectile projectile) {
		projectileList.add(projectile);
	}


	/**
	 * @param projectiles
	 */
	public void addNewProjectiles(ArrayList<Projectile> projectiles) {
		projectileList.addAll(projectiles);
	}


	/**
	 * @return
	 */
	public ArrayList<Obstacle> getObstacles() {
		return obstacleList;
	}


	/**
	 * @return
	 */
	public ArrayList<Enemy> getEnemies() {
		return enemyList;
	}


	/**
	 * @return
	 */
	public ArrayList<Projectile> getProjectiles() {
		return projectileList;
	}


	/**
	 * @return
	 */
	public PlayerCharacter getPlayer() {
		return playerCharacter;
	}
}
