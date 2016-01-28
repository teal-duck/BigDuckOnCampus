package com.muscovy.game;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.muscovy.game.enums.LevelType;
import com.muscovy.game.enums.ObjectiveType;
import com.muscovy.game.enums.RoomType;


/**
 * Created by ewh502 on 04/12/2015.
 */
public class EntityManager {
	private ArrayList<OnscreenDrawable> renderList;
	private ArrayList<Obstacle> obstacleList;
	private ArrayList<Enemy> enemyList;
	private ArrayList<Projectile> projectileList;
	private DungeonRoom currentDungeonRoom;
	private LevelGenerator levelGenerator;
	private Level[] levels;
	private int currentLevelNumber;
	private int maxLevels = 8;
	private int roomX;
	private int roomY;
	private float roomTimer = 0;
	private BitmapFont list;// Testing purposes
	private PlayerCharacter playerCharacter;
	// TODO: Only load 1 door texture and rotate when renderering
	private Texture northDoorTextureOpen;
	private Texture southDoorTextureOpen;
	private Texture eastDoorTextureOpen;
	private Texture westDoorTextureOpen;
	private Texture northDoorTextureClosed;
	private Texture southDoorTextureClosed;
	private Texture eastDoorTextureClosed;
	private Texture westDoorTextureClosed;

	private TextureMap textureMap;

	private ShapeRenderer shapeRenderer;


	public EntityManager(TextureMap textureMap) {
		this.textureMap = textureMap;
		shapeRenderer = new ShapeRenderer();

		renderList = new ArrayList<OnscreenDrawable>();
		obstacleList = new ArrayList<Obstacle>();
		enemyList = new ArrayList<Enemy>();
		projectileList = new ArrayList<Projectile>();

		levelGenerator = new LevelGenerator();
		maxLevels = 8;
		levels = new Level[maxLevels];

		list = new BitmapFont();
		list.setColor(Color.WHITE);// Testing purposes

		currentDungeonRoom = new DungeonRoom(textureMap);

		northDoorTextureOpen = textureMap
				.getTextureOrLoadFile("accommodationAssets/doorOpen/PNGs/accommodationDoorUp.png");
		northDoorTextureClosed = textureMap.getTextureOrLoadFile(
				"accommodationAssets/doorClosed/PNGs/accommodationDoorUpClosed.png");
		eastDoorTextureOpen = textureMap
				.getTextureOrLoadFile("accommodationAssets/doorOpen/PNGs/accommodationDoorRight.png");
		eastDoorTextureClosed = textureMap.getTextureOrLoadFile(
				"accommodationAssets/doorClosed/PNGs/accommodationDoorRightClosed.png");
		westDoorTextureOpen = textureMap
				.getTextureOrLoadFile("accommodationAssets/doorOpen/PNGs/accommodationDoorLeft.png");
		westDoorTextureClosed = textureMap.getTextureOrLoadFile(
				"accommodationAssets/doorClosed/PNGs/accommodationDoorLeftClosed.png");
		southDoorTextureOpen = textureMap
				.getTextureOrLoadFile("accommodationAssets/doorOpen/PNGs/accommodationDoorDown.png");
		southDoorTextureClosed = textureMap.getTextureOrLoadFile(
				"accommodationAssets/doorClosed/PNGs/accommodationDoorDownClosed.png");

	}


	public void generateLevels() {
		// TODO: Only generate level when player wants to play it?
		// Game is slow to load atm

		String templateFilename = "room_templates.csv";
		DungeonRoomTemplateLoader templateLoader = new DungeonRoomTemplateLoader(templateFilename);
		for (int i = 0; i < levels.length; i += 1) {
			LevelType levelType = LevelType.fromInt(i);
			LevelParameters levelParameters = LevelType.getParametersForLevel(levelType);

			DungeonRoom[][] rooms = levelGenerator.generateBuilding(textureMap, templateLoader, levelType,
					levelParameters);
			Level level = new Level(rooms, levelType, levelParameters);

			levels[i] = level;

		}
		templateLoader.dispose();
	}


	public int getLevelNo() {
		return currentLevelNumber;
	}


	public void setLevel(int levelNumber) {
		currentLevelNumber = levelNumber;
	}


	public Level getCurrentLevel() {
		return levels[currentLevelNumber];
	}


	public Level getLevel(int levelNumber) {
		return levels[levelNumber];
	}


	public void startLevel(PlayerCharacter playerCharacter) {
		this.playerCharacter = playerCharacter;
		Level level = getCurrentLevel();
		roomX = level.getStartX();
		roomY = level.getStartY();
		setCurrentDungeonRoom(roomX, roomY);
		level.markRoomVisited(roomX, roomY);
		renderList.add(this.playerCharacter);
	}


	public void render(SpriteBatch batch) {
		/**
		 * Renders sprites in the controller so those further back are rendered first, giving a perspective
		 * illusion
		 */
		roomTimer += Gdx.graphics.getDeltaTime(); // timer used to give the player a few seconds to look at a
								// room before attacking
		renderList.trimToSize();
		obstacleList.trimToSize();
		enemyList.trimToSize();
		projectileList.trimToSize();

		sortDrawables();

		batch.draw(currentDungeonRoom.getBackgroundTexture(), 0, 0);

		float windowWidth = MuscovyGame.WINDOW_WIDTH;
		float windowHeight = MuscovyGame.WINDOW_HEIGHT;
		float tileSize = MuscovyGame.TILE_SIZE;
		float topGuiSize = MuscovyGame.TOP_GUI_SIZE;
		float worldHeight = windowHeight - topGuiSize;

		Texture doorTexture;
		if (currentDungeonRoom.getUpDoor()) {
			doorTexture = (currentDungeonRoom.areAllEnemiesDead() ? northDoorTextureOpen
					: northDoorTextureClosed);
			batch.draw(doorTexture, (windowWidth - doorTexture.getWidth()) / 2,
					currentDungeonRoom.getNorthDoor().getY()
							+ (currentDungeonRoom.getNorthDoor().getWidth() - tileSize));

		}
		if (currentDungeonRoom.getDownDoor()) {
			doorTexture = (currentDungeonRoom.areAllEnemiesDead() ? southDoorTextureOpen
					: southDoorTextureClosed);
			batch.draw(doorTexture, (windowWidth - southDoorTextureOpen.getWidth()) / 2,
					currentDungeonRoom.getSouthDoor().getY() + 4);
		}
		if (currentDungeonRoom.getRightDoor()) {
			doorTexture = (currentDungeonRoom.areAllEnemiesDead() ? eastDoorTextureOpen
					: eastDoorTextureClosed);
			batch.draw(doorTexture,
					currentDungeonRoom.getEastDoor().getX()
							+ (currentDungeonRoom.getEastDoor().getWidth() - tileSize),
					(worldHeight - eastDoorTextureOpen.getWidth()) / 2);
		}
		if (currentDungeonRoom.getLeftDoor()) {
			doorTexture = (currentDungeonRoom.areAllEnemiesDead() ? westDoorTextureOpen
					: westDoorTextureClosed);
			batch.draw(doorTexture, currentDungeonRoom.getWestDoor().getX() + 4,
					(worldHeight - westDoorTextureOpen.getWidth()) / 2);
		}

		for (OnscreenDrawable drawable : renderList) {
			if (drawable instanceof PlayerCharacter) {
				if (((PlayerCharacter) drawable).isInvincible()) {
					if (!(((((PlayerCharacter) drawable).getInvincibilityCounter() * 10)
							% 2) < 0.75)) {
						batch.draw(drawable.getSprite().getTexture(), drawable.getX(),
								drawable.getY());
					}
				} else {
					batch.draw(drawable.getSprite().getTexture(), drawable.getX(), drawable.getY());
				}
			} else {
				batch.draw(drawable.getSprite().getTexture(), drawable.getX(), drawable.getY());
			}
		}

		for (Projectile projectile : projectileList) {
			batch.draw(projectile.getSprite().getTexture(), projectile.getX(), projectile.getY());
		}
		if (levels[currentLevelNumber].isCompleted()) {
			list.draw(batch, "LEVEL COMPLETED, PRESS P AND ESC TO CHOOSE ANOTHER", (windowWidth / 2) - 200,
					worldHeight - 69);
		}
		// list.draw(batch, "no of projectiles in controller = " + projectileList.size(), (float) 250, (float)
		// 450);//Testing purposes (shows number of projectiles)
	}


	public void renderMapOverlay(OrthographicCamera camera) {
		Level level = levels[currentLevelNumber];

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);

		float renderWidth = 25;
		float renderHeight = 15;

		// Size of items inside room (e.g. player location)
		float innerRoomSize = 10;

		// The distance between the rooms
		float doorLength = 5;
		// How thick the doors are
		float doorWidth = 5;

		float xOffset = (MuscovyGame.WINDOW_WIDTH - ((renderWidth + doorLength) * level.getRoomsWide()))
				+ doorLength;
		float yOffset = MuscovyGame.WINDOW_HEIGHT - renderHeight;

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
				if ((roomX == col) && (roomY == row)) {
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


	public boolean isLevelCompleted(int levelNumber) {
		return getLevel(levelNumber).isCompleted();
	}


	private void sortDrawables() {
		/**
		 * Quicksorts the list of drawable objects in the controller by Y coordinate so it renders the things in
		 * the background first.
		 */
		ArrayList<OnscreenDrawable> newList = new ArrayList<OnscreenDrawable>();
		newList.addAll(quicksort(renderList));
		renderList.clear();
		renderList.addAll(newList);
	}


	/** Quicksort Helper Methods */
	private ArrayList<OnscreenDrawable> quicksort(ArrayList<OnscreenDrawable> input) {
		if (input.size() <= 1) {
			return input;
		}
		int middle = (int) Math.ceil((double) input.size() / 2);
		OnscreenDrawable pivot = input.get(middle);
		ArrayList<OnscreenDrawable> less = new ArrayList<OnscreenDrawable>();
		ArrayList<OnscreenDrawable> greater = new ArrayList<OnscreenDrawable>();
		for (int i = 0; i < input.size(); i++) {
			if (input.get(i).getY() >= pivot.getY()) {
				if (i == middle) {
					continue;
				}
				less.add(input.get(i));
			} else {
				greater.add(input.get(i));
			}
		}
		return concatenate(quicksort(less), pivot, quicksort(greater));
	}


	private ArrayList<OnscreenDrawable> concatenate(ArrayList<OnscreenDrawable> less, OnscreenDrawable pivot,
			ArrayList<OnscreenDrawable> greater) {
		ArrayList<OnscreenDrawable> list = new ArrayList<OnscreenDrawable>();
		for (int i = 0; i < less.size(); i++) {
			list.add(less.get(i));
		}
		list.add(pivot);
		for (int i = 0; i < greater.size(); i++) {
			list.add(greater.get(i));
		}
		return list;
	}


	public void killProjectiles() {
		ArrayList<Projectile> deadProjectiles = new ArrayList<Projectile>();
		for (Projectile projectile : projectileList) {
			if (projectile.lifeOver()) {
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
			if (enemy.lifeOver()) {
				deadEnemies.add(enemy);
			}
		}
		for (Enemy enemy : deadEnemies) {
			playerCharacter.increaseScore(enemy.getScoreOnDeath());
			renderList.remove(enemy);
			enemyList.remove(enemy);
			currentDungeonRoom.killEnemy(enemy);
		}
		checkCurrentLevelCompletion();
	}


	public void addNewDrawable(OnscreenDrawable drawable) {
		renderList.add(drawable);
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


	public void setCurrentDungeonRoom(DungeonRoom dungeonRoom) {
		roomTimer = 0;
		currentDungeonRoom = dungeonRoom;
		renderList.clear();
		obstacleList.clear();
		projectileList.clear();
		addNewObstacles(dungeonRoom.getObstacleList());
		enemyList.clear();
		addNewEnemies(dungeonRoom.getEnemyList());
	}


	public void setCurrentDungeonRoom(int roomX, int roomY) {
		Level currentLevel = getCurrentLevel();
		DungeonRoom room = currentLevel.getRoom(roomX, roomY);
		setCurrentDungeonRoom(room);
		currentLevel.markRoomVisited(roomX, roomY);
	}


	public DungeonRoom getCurrentDungeonRoom() {
		return currentDungeonRoom;
	}


	public void moveNorth() {
		roomY--;
		setCurrentDungeonRoom(roomX, roomY);
		renderList.add(playerCharacter);
	}


	public void moveEast() {
		roomX++;
		setCurrentDungeonRoom(roomX, roomY);
		renderList.add(playerCharacter);
	}


	public void moveWest() {
		roomX--;
		setCurrentDungeonRoom(roomX, roomY);
		renderList.add(playerCharacter);
	}


	public void moveSouth() {
		roomY++;
		setCurrentDungeonRoom(roomX, roomY);
		renderList.add(playerCharacter);

	}


	public boolean checkCurrentLevelCompletion() {
		return checkLevelCompletion(currentLevelNumber);
	}


	public boolean checkLevelCompletion(int levelNumber) {
		Level level = getLevel(levelNumber);
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


	public float getRoomTimer() {
		return roomTimer;
	}
}
