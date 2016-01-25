package com.muscovy.game;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	private Level[] level;
	private int levelNo;
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


	public EntityManager() {
		renderList = new ArrayList<OnscreenDrawable>();
		obstacleList = new ArrayList<Obstacle>();
		enemyList = new ArrayList<Enemy>();
		projectileList = new ArrayList<Projectile>();

		levelGenerator = new LevelGenerator();
		level = new Level[maxLevels];

		list = new BitmapFont();
		list.setColor(Color.WHITE);// Testing purposes

		currentDungeonRoom = new DungeonRoom();

		maxLevels = 8;

		northDoorTextureOpen = new Texture("accommodationAssets/doorOpen/PNGs/accommodationDoorUp.png");
		northDoorTextureClosed = new Texture(
				"accommodationAssets/doorClosed/PNGs/accommodationDoorUpClosed.png");
		eastDoorTextureOpen = new Texture("accommodationAssets/doorOpen/PNGs/accommodationDoorRight.png");
		eastDoorTextureClosed = new Texture(
				"accommodationAssets/doorClosed/PNGs/accommodationDoorRightClosed.png");
		westDoorTextureOpen = new Texture("accommodationAssets/doorOpen/PNGs/accommodationDoorLeft.png");
		westDoorTextureClosed = new Texture(
				"accommodationAssets/doorClosed/PNGs/accommodationDoorLeftClosed.png");
		southDoorTextureOpen = new Texture("accommodationAssets/doorOpen/PNGs/accommodationDoorDown.png");
		southDoorTextureClosed = new Texture(
				"accommodationAssets/doorClosed/PNGs/accommodationDoorDownClosed.png");

	}


	public void generateLevels() {
		// TODO: Only generate level when player wants to play it?
		// Game is slow to load atm
		level[0] = new Level(levelGenerator.generateBuilding(20, LevelType.CONSTANTINE), ObjectiveType.BOSS,
				LevelType.CONSTANTINE);
		level[1] = new Level(levelGenerator.generateBuilding(20, LevelType.LANGWITH), ObjectiveType.BOSS,
				LevelType.LANGWITH);
		level[2] = new Level(levelGenerator.generateBuilding(20, LevelType.GOODRICKE), ObjectiveType.BOSS,
				LevelType.GOODRICKE);
		level[3] = new Level(levelGenerator.generateBuilding(20, LevelType.LMB), ObjectiveType.BOSS,
				LevelType.LMB);
		level[4] = new Level(levelGenerator.generateBuilding(20, LevelType.CATALYST), ObjectiveType.BOSS,
				LevelType.CATALYST);
		level[5] = new Level(levelGenerator.generateBuilding(20, LevelType.TFTV), ObjectiveType.BOSS,
				LevelType.TFTV);
		level[6] = new Level(levelGenerator.generateBuilding(20, LevelType.COMP_SCI), ObjectiveType.BOSS,
				LevelType.COMP_SCI);
		level[7] = new Level(levelGenerator.generateBuilding(20, LevelType.RCH), ObjectiveType.BOSS,
				LevelType.RCH);
		/*
		 * while (steve<maxLevels-2){ level[steve] = new Level(levelGenerator.generateBuilding(20),0); steve++;
		 * }
		 */
	}


	public int getLevelNo() {
		return levelNo;
	}


	public void setLevel(int levelNo) {
		this.levelNo = levelNo;
	}


	public void startLevel(PlayerCharacter playerCharacter) {
		roomX = startRoomX;
		roomY = startRoomY;
		this.playerCharacter = playerCharacter;
		setCurrentDungeonRoom(level[levelNo].getRoom(roomX, roomY));
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
		if (level[levelNo].isCompleted()) {
			list.draw(batch, "LEVEL COMPLETED, PRESS P AND ESC TO CHOOSE ANOTHER", (windowWidth / 2) - 200,
					worldHeight - 69);
		}
		// list.draw(batch, "no of projectiles in controller = " + projectileList.size(), (float) 250, (float)
		// 450);//Testing purposes (shows number of projectiles)
	}


	public boolean levelCompleted(int level) {
		return this.level[level].isCompleted();
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


	public DungeonRoom getCurrentDungeonRoom() {
		return currentDungeonRoom;
	}


	public void moveNorth() {
		roomY--;
		setCurrentDungeonRoom(level[levelNo].getRoom(roomX, roomY));
		renderList.add(playerCharacter);
	}


	public void moveEast() {
		roomX++;
		setCurrentDungeonRoom(level[levelNo].getRoom(roomX, roomY));
		renderList.add(playerCharacter);
	}


	public void moveWest() {
		roomX--;
		setCurrentDungeonRoom(level[levelNo].getRoom(roomX, roomY));
		renderList.add(playerCharacter);
	}


	public void moveSouth() {
		roomY++;
		setCurrentDungeonRoom(level[levelNo].getRoom(roomX, roomY));
		renderList.add(playerCharacter);

	}


	public void checkLevelCompletion() {
		if (currentDungeonRoom.isEnemiesDead() && (currentDungeonRoom.getRoomType() == RoomType.BOSS)
				&& (level[levelNo].getObjective() == ObjectiveType.BOSS)) {
			level[levelNo].setCompleted(true);
		}
	}


	public float getRoomTimer() {
		return roomTimer;
	}
}
