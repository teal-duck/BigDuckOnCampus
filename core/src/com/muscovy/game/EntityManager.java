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
	private Texture northDoorTextureOpen;
	private Texture southDoorTextureOpen;
	private Texture eastDoorTextureOpen;
	private Texture westDoorTextureOpen;
	private Texture northDoorTextureClosed;
	private Texture southDoorTextureClosed;
	private Texture eastDoorTextureClosed;
	private Texture westDoorTextureClosed;

	private int startRoomX = 3;
	private int startRoomY = 3;

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

		for (int i = 0; i < maxLevels; i += 1) {
			int roomsWide = LevelGenerator.DUNGEON_ROOMS_WIDE;
			int roomsHigh = LevelGenerator.DUNGEON_ROOMS_HIGH;
			int maxRooms = LevelGenerator.MAX_ROOMS;
			int startX = LevelGenerator.START_ROOM_X;
			int startY = LevelGenerator.START_ROOM_Y;
			LevelType levelType = LevelType.fromInt(i);
			ObjectiveType objectiveType = ObjectiveType.BOSS;

			DungeonRoom[][] rooms = levelGenerator.generateBuilding(textureMap, maxRooms, levelType,
					roomsWide, roomsHigh, startX, startY);
			Level level = new Level(rooms, objectiveType, levelType);

			levels[i] = level;

		}
		// levels[0] = new Level(levelGenerator.generateBuilding(textureMap, 20, LevelType.CONSTANTINE),
		// ObjectiveType.BOSS, LevelType.CONSTANTINE);
		// levels[1] = new Level(levelGenerator.generateBuilding(textureMap, 20, LevelType.LANGWITH),
		// ObjectiveType.BOSS, LevelType.LANGWITH);
		// levels[2] = new Level(levelGenerator.generateBuilding(textureMap, 20, LevelType.GOODRICKE),
		// ObjectiveType.BOSS, LevelType.GOODRICKE);
		// levels[3] = new Level(levelGenerator.generateBuilding(textureMap, 20, LevelType.LMB),
		// ObjectiveType.BOSS, LevelType.LMB);
		// levels[4] = new Level(levelGenerator.generateBuilding(textureMap, 20, LevelType.CATALYST),
		// ObjectiveType.BOSS, LevelType.CATALYST);
		// levels[5] = new Level(levelGenerator.generateBuilding(textureMap, 20, LevelType.TFTV),
		// ObjectiveType.BOSS, LevelType.TFTV);
		// levels[6] = new Level(levelGenerator.generateBuilding(textureMap, 20, LevelType.COMP_SCI),
		// ObjectiveType.BOSS, LevelType.COMP_SCI);
		// levels[7] = new Level(levelGenerator.generateBuilding(textureMap, 20, LevelType.RCH),
		// ObjectiveType.BOSS, LevelType.RCH);
		/*
		 * while (steve<maxLevels-2){ level[steve] = new Level(levelGenerator.generateBuilding(20),0); steve++;
		 * }
		 */
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
		roomX = startRoomX;
		roomY = startRoomY;
		this.playerCharacter = playerCharacter;
		setCurrentDungeonRoom(roomX, roomY);
		// setCurrentDungeonRoom(levels[currentLevelNumber].getRoom(roomX, roomY));
		getCurrentLevel().markRoomVisited(roomX, roomY);
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

		batch.draw(currentDungeonRoom.getSprite().getTexture(), 0, 0);

		float windowWidth = MuscovyGame.WINDOW_WIDTH;
		float windowHeight = MuscovyGame.WINDOW_HEIGHT;
		float tileSize = MuscovyGame.TILE_SIZE;
		float topGuiSize = MuscovyGame.TOP_GUI_SIZE;
		float worldHeight = windowHeight - topGuiSize;

		Texture doorTexture;
		if (currentDungeonRoom.getUpDoor()) {
			doorTexture = (currentDungeonRoom.isEnemiesDead() ? northDoorTextureOpen
					: northDoorTextureClosed);
			batch.draw(doorTexture, (windowWidth - doorTexture.getWidth()) / 2,
					currentDungeonRoom.getNorthDoor().getY()
							+ (currentDungeonRoom.getNorthDoor().getWidth() - tileSize));

		}
		if (currentDungeonRoom.getDownDoor()) {
			doorTexture = (currentDungeonRoom.isEnemiesDead() ? southDoorTextureOpen
					: southDoorTextureClosed);
			batch.draw(doorTexture, (windowWidth - southDoorTextureOpen.getWidth()) / 2,
					currentDungeonRoom.getSouthDoor().getY() + 4);
		}
		if (currentDungeonRoom.getRightDoor()) {
			doorTexture = (currentDungeonRoom.isEnemiesDead() ? eastDoorTextureOpen
					: eastDoorTextureClosed);
			batch.draw(doorTexture,
					currentDungeonRoom.getEastDoor().getX()
							+ (currentDungeonRoom.getEastDoor().getWidth() - tileSize),
					(worldHeight - eastDoorTextureOpen.getWidth()) / 2);
		}
		if (currentDungeonRoom.getLeftDoor()) {
			doorTexture = (currentDungeonRoom.isEnemiesDead() ? westDoorTextureOpen
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
		float xOffset = MuscovyGame.WINDOW_WIDTH - (renderWidth * level.getRoomsWide());
		float yOffset = MuscovyGame.WINDOW_HEIGHT - renderHeight;

		float windowEdgeOffset = 0;
		xOffset -= windowEdgeOffset;
		yOffset -= windowEdgeOffset;

		for (int row = 0; row < level.getRoomsHigh(); row += 1) {
			for (int col = 0; col < level.getRoomsWide(); col += 1) {
				DungeonRoom room = level.getRoom(col, row);

				Color colour = Color.BLACK;
				if (room != null) {
					boolean isCurrentRoom = ((roomX == col) && (roomY == row));
					boolean isBossRoom = (room.getRoomType() == RoomType.BOSS);

					if (isCurrentRoom) {
						colour = Color.RED;
					} else {
						if (level.isRoomVisited(col, row)) {
							if (isBossRoom) {
								colour = Color.BLUE;
							} else {
								colour = Color.WHITE;
							}
						} else if (level.isRoomNeighbourVisited(col, row)) {
							if (isBossRoom) {
								colour = Color.BROWN;
							} else {
								colour = Color.DARK_GRAY;
							}
						}
					}
				}
				shapeRenderer.setColor(colour);

				float x = xOffset + (col * renderWidth);
				float y = yOffset - (row * renderHeight);
				float w = renderWidth;
				float h = renderHeight;

				shapeRenderer.rect(x, y, w, h);
			}
		}

		shapeRenderer.end();

	}


	public boolean levelCompleted(int levelNumber) {
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
		checkLevelCompletion();
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


	public void checkLevelCompletion() {
		if (currentDungeonRoom.isEnemiesDead() && (currentDungeonRoom.getRoomType() == RoomType.BOSS)
				&& (levels[currentLevelNumber].getObjective() == ObjectiveType.BOSS)) {
			levels[currentLevelNumber].setCompleted(true);
		}
	}


	public float getRoomTimer() {
		return roomTimer;
	}
}
