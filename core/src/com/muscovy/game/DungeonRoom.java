package com.muscovy.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.enums.AttackType;
import com.muscovy.game.enums.EnemyShotType;
import com.muscovy.game.enums.LevelType;
import com.muscovy.game.enums.MovementType;
import com.muscovy.game.enums.RoomType;


/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class DungeonRoom {
	/**
	 * Contains lists of obstacles and enemies in that room. They are passed to the entity manager when the room is
	 * entered. Room is generated using a 2d array (explained in more detail further down) There are 2 sets of
	 * walls, one for the enemies and player to collide with, and one for the projectile to collide with so it looks
	 * like they break halfway up the wall, and have some height associated with them. The room is made up of 32x32
	 * 'half tiles' (used in map gen). The number of tiles in each direction is stored in constants in
	 * {@link DungeonRoom.floorWidthInHalfTiles} & {@link DungeonRoom.floorHeightInHalfTiles}.
	 */
	private ArrayList<Obstacle> obstacleList;
	private ArrayList<Enemy> enemyList;
	private Rectangle[] walls;
	private Rectangle[] projectileWalls;
	private RoomType roomType = RoomType.NORMAL;

	// TODO: Maybe rename the URDL and NESW names for doors to hasUpDoor and upDoorRect etc?
	/* variables indicate if there is a door on that wall */
	private boolean upDoor = false;
	private boolean rightDoor = false;
	private boolean downDoor = false;
	private boolean leftDoor = false;
	private boolean enemiesDead;
	private Rectangle northDoor;
	private Rectangle eastDoor;
	private Rectangle southDoor;
	private Rectangle westDoor;
	private float doorSize = 65;

	private Random rand;

	private TextureMap textureMap;
	private Texture backgroundTexture;

	public static final int FLOOR_HEIGHT_IN_TILES = 10;
	public static final int FLOOR_WIDTH_IN_TILES = 18;


	public DungeonRoom(TextureMap textureMap) {
		this.textureMap = textureMap;
		rand = new Random();
		obstacleList = new ArrayList<Obstacle>();
		enemyList = new ArrayList<Enemy>();

		float windowWidth = MuscovyGame.WINDOW_WIDTH;
		float windowHeight = MuscovyGame.WINDOW_HEIGHT;
		float tileSize = MuscovyGame.TILE_SIZE;
		float halfTileSize = tileSize / 2;
		float topGuiSize = MuscovyGame.TOP_GUI_SIZE;
		float worldHeight = windowHeight - topGuiSize;

		walls = new Rectangle[4];
		walls[0] = new Rectangle(0, 0, windowWidth, tileSize); // bottom wall
		walls[1] = new Rectangle(0, 0, tileSize, windowHeight - topGuiSize); // left wall
		walls[2] = new Rectangle(windowWidth - tileSize, 0, tileSize, windowHeight - topGuiSize); // right wall
		walls[3] = new Rectangle(0, worldHeight - tileSize, windowWidth, tileSize); // top wall

		projectileWalls = new Rectangle[4];
		projectileWalls[0] = new Rectangle(0, 0, windowWidth, halfTileSize); // bottom wall
		projectileWalls[1] = new Rectangle(0, 0, halfTileSize, windowHeight - topGuiSize); // left wall
		projectileWalls[2] = new Rectangle(windowWidth - halfTileSize, 0, halfTileSize,
				windowHeight - topGuiSize); // right wall
		projectileWalls[3] = new Rectangle(0, worldHeight - halfTileSize, windowWidth, halfTileSize); // top
														// wall
	}


	private void createNonDamagingObstacle(int x, int y) {
		Sprite rockSprite;
		Obstacle obstacle5;
		rockSprite = new Sprite();
		rockSprite.setTexture(textureMap.getTextureOrLoadFile("accommodationAssets/obstacles/binRecycle.png"));
		obstacle5 = new Obstacle(rockSprite, new Vector2(x, y));
		obstacle5.setXTiles(x);
		obstacle5.setYTiles(y);
		addObstacle(obstacle5);
	}


	private void createDamagingObstacle(int x, int y) {
		Sprite spikeSprite;
		Obstacle obstacle6;
		spikeSprite = new Sprite();
		spikeSprite.setTexture(textureMap.getTextureOrLoadFile("accommodationAssets/obstacles/binWaste.png"));
		obstacle6 = new Obstacle(spikeSprite, new Vector2(x, y));
		obstacle6.setXTiles(x);
		obstacle6.setYTiles(y);
		addObstacle(obstacle6);
		obstacle6.setDamaging(true);
		obstacle6.setTouchDamage(10.0f);
	}


	private void createRandomEnemy(int x, int y) {
		Sprite enemySprite;
		Enemy enemy;
		Vector2 position = new Vector2(x, y);

		if (rand.nextBoolean()) {
			enemySprite = new Sprite(textureMap.getTextureOrLoadFile(
					"accommodationAssets/enemies/cleaner/rightCleanerWalk/PNGs/rightCleaner1.png"));
			enemy = new Enemy(enemySprite, position, textureMap, rand);
			enemy.setAttackType(AttackType.TOUCH);
		} else {
			enemySprite = new Sprite(textureMap.getTextureOrLoadFile(
					"accommodationAssets/enemies/student/rightStudentWalk/PNGs/rightStudent1.png"));
			enemy = new Enemy(enemySprite, position, textureMap, rand);
			enemy.setAttackType(AttackType.RANGE);
		}

		switch (rand.nextInt(3)) {
		case 0:
			enemy.setShotType(EnemyShotType.SINGLE_TOWARDS_PLAYER);
			enemy.setMovementType(MovementType.RANDOM);
			break;
		case 1:
			enemy.setShotType(EnemyShotType.SINGLE_TOWARDS_PLAYER);
			enemy.setMovementType(MovementType.RANDOM);
			break;
		case 2:
			enemy.setShotType(EnemyShotType.DOUBLE_TOWARDS_PLAYER);
			enemy.setMovementType(MovementType.FOLLOW);
			break;
		case 3:
			enemy.setShotType(EnemyShotType.RANDOM_DIRECTION);
			enemy.setMovementType(MovementType.FOLLOW);
			break;
		}

		enemy.setShotType(EnemyShotType.RANDOM_DIRECTION);

		enemy.setXTiles(x);
		enemy.setYTiles(y);
		enemy.calculateScoreOnDeath();
		addEnemy(enemy);
	}


	public void generateRoom(DungeonRoomTemplateLoader templateLoader, LevelType levelType) {
		/**
		 * Currently chooses from 1 of 10 types of room, 5 with enemies, 5 without, most with some sort of
		 * obstacle. Tile array is based on a grid where each tile is 64x64. Represents the room. 0 = empty
		 * space, 1 = non-damaging obstacle, 2 = damaging obstacle, 3 = random obstacle, 4 = random enemy Easy
		 * to put in more, just extend the case statement below to have more specific stuff.
		 */
		Sprite enemySprite;
		Enemy enemy;
		Texture texture;

		switch (roomType) {
		case NORMAL:
			// Normal

			int[][] tileArray = templateLoader.getRandomTemplateOrLoad(rand);

			boolean obstacleType3NonDamaging = rand.nextBoolean();

			for (int row = 0; row < tileArray.length; row++) {
				for (int col = 0; col < tileArray[row].length; col++) {
					int cell = tileArray[row][col];
					switch (cell) {
					case DungeonRoomTemplateLoader.EMPTY_TILE:
						break;
					case DungeonRoomTemplateLoader.NON_DAMAGING_OBSTACLE:
						createNonDamagingObstacle(col, row);
						break;
					case DungeonRoomTemplateLoader.DAMAGING_OBSTACLE:
						createDamagingObstacle(col, row);
						break;
					case DungeonRoomTemplateLoader.MAYBE_DAMAGING_OBSTACLE:
						if (obstacleType3NonDamaging) {
							createNonDamagingObstacle(col, row);
						} else if (!obstacleType3NonDamaging) {
							createDamagingObstacle(col, row);
						}
						break;
					case DungeonRoomTemplateLoader.MAYBE_ENEMY:
						if (rand.nextInt(5) < 3) {
							createRandomEnemy(col, row);
						}
						break;
					default:
						System.out.println("Unrecognised cell " + cell + "; location (" + col
								+ ", " + row + ")");
						break;
					}
				}
			}
			break;

		case BOSS:
			// Boss
			Sprite bossSprite;
			Enemy bossEnemy;

			bossSprite = new Sprite(
					textureMap.getTextureOrLoadFile("accommodationAssets/accommodationBoss.png"));
			bossEnemy = new Enemy(bossSprite, new Vector2(0, 0), textureMap, rand);
			bossEnemy.setXTiles((int) ((DungeonRoom.FLOOR_WIDTH_IN_TILES / 2)
					- (bossEnemy.getWidth() / MuscovyGame.TILE_SIZE / 2)));
			bossEnemy.setYTiles((int) ((DungeonRoom.FLOOR_HEIGHT_IN_TILES / 2)
					- (bossEnemy.getHeight() / MuscovyGame.TILE_SIZE / 2)));
			bossEnemy.setAttackType(AttackType.BOTH);
			bossEnemy.setMovementType(MovementType.FOLLOW);
			// bossEnemy.setMaxVelocity(100);
			// TODO: Change boss parameters
			bossEnemy.setSpeed(bossEnemy.getSpeed() * 0.8f);
			bossEnemy.setProjectileVelocity(bossEnemy.getProjectileVelocity() * 2);
			bossEnemy.setTouchDamage(20);
			bossEnemy.setShotType(EnemyShotType.TRIPLE_TOWARDS_PLAYER);
			bossEnemy.setScoreOnDeath(3000);
			bossEnemy.setCurrentHealth(600);
			bossEnemy.setHitboxRadius(80);
			bossEnemy.setMovementRange(1000);
			addEnemy(bossEnemy);
			break;

		case ITEM:
			// Item
			enemySprite = new Sprite(textureMap.getTextureOrLoadFile(
					"accommodationAssets/enemies/student/rightStudentWalk/PNGs/rightStudent1.png"));
			enemy = new Enemy(enemySprite, new Vector2(0, 0), textureMap, rand);
			enemy.setAttackType(AttackType.RANGE);
			enemy.setXTiles(100);
			enemy.setYTiles(100);
			addEnemy(enemy);
			break;

		case SHOP:
			// Shop
			enemySprite = new Sprite(textureMap.getTextureOrLoadFile(
					"accommodationAssets/enemies/student/rightStudentWalk/PNGs/rightStudent1.png"));
			enemy = new Enemy(enemySprite, new Vector2(0, 0), textureMap, rand);
			enemy.setAttackType(AttackType.RANGE);
			enemy.setX(0);
			enemy.setY(0);
			addEnemy(enemy);
			break;

		case START:
			// Start
			break;
		}

		// TODO: Make level backgrounds an array
		switch (levelType) {
		case CONSTANTINE:
			texture = textureMap.getTextureOrLoadFile("accommodationAssets/constantineBackground.png");
			break;
		case LANGWITH:
			texture = textureMap.getTextureOrLoadFile("accommodationAssets/langwithBackground.png");
			break;
		case GOODRICKE:
			texture = textureMap.getTextureOrLoadFile("accommodationAssets/goodrickeBackground.png");
			break;
		case LMB:
			texture = textureMap.getTextureOrLoadFile("accommodationAssets/lmbBackground.png");
			break;
		case CATALYST:
			texture = textureMap.getTextureOrLoadFile("accommodationAssets/catalystBackground.png");
			break;
		case TFTV:
			texture = textureMap.getTextureOrLoadFile("accommodationAssets/tftvBackground.png");
			break;
		case COMP_SCI:
			texture = textureMap.getTextureOrLoadFile("accommodationAssets/csBackground.png");
			break;
		case RCH:
			texture = textureMap.getTextureOrLoadFile("accommodationAssets/rchBackground.png");
			break;
		default:
			texture = null;
			break;
		}

		setBackgroundTexture(texture);
		initialiseDoors();
	}


	public void initialiseDoors() {
		if (upDoor) {
			northDoor = new Rectangle((MuscovyGame.WINDOW_WIDTH - doorSize) / 2,
					MuscovyGame.WORLD_HEIGHT - doorSize, doorSize, doorSize);
		}
		if (downDoor) {
			southDoor = new Rectangle((MuscovyGame.WINDOW_WIDTH - doorSize) / 2, 0, doorSize, doorSize);
		}
		if (rightDoor) {
			eastDoor = new Rectangle(MuscovyGame.WINDOW_WIDTH - doorSize,
					(MuscovyGame.WORLD_HEIGHT - doorSize) / 2, doorSize, doorSize);
		}
		if (leftDoor) {
			westDoor = new Rectangle(0, (MuscovyGame.WORLD_HEIGHT - doorSize) / 2, doorSize, doorSize);
		}
	}


	/**
	 * Getters and Setters
	 */
	public void addEnemy(Enemy enemy) {
		enemyList.add(enemy);
	}


	public void killEnemy(Enemy enemy) {
		enemyList.remove(enemy);
		enemyList.trimToSize();
		if (enemyList.size() == 0) {
			enemiesDead = true;
		}
	}


	public ArrayList<Enemy> getEnemyList() {
		return enemyList;
	}


	public void addObstacle(Obstacle obstacle) {
		obstacleList.add(obstacle);
	}


	public ArrayList<Obstacle> getObstacleList() {
		return obstacleList;
	}


	public Rectangle getBottomRectangle() {
		return walls[0];
	}


	public Rectangle getLeftRectangle() {
		return walls[1];
	}


	public Rectangle getRightRectangle() {
		return walls[2];
	}


	public Rectangle getTopRectangle() {
		return walls[3];
	}


	public Rectangle getProjectileWallBottom() {
		return projectileWalls[0];
	}


	public Rectangle getProjectileWallLeft() {
		return projectileWalls[1];
	}


	public Rectangle getProjectileWallRight() {
		return projectileWalls[2];
	}


	public Rectangle getProjectileWallTop() {
		return projectileWalls[3];
	}


	public RoomType getRoomType() {
		return roomType;
	}


	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}


	public boolean areAllEnemiesDead() {
		if (enemyList.size() == 0) {
			enemiesDead = true;
		}
		return enemiesDead;
	}


	public void setEnemiesDead(boolean enemiesDead) {
		this.enemiesDead = enemiesDead;
	}


	public boolean getUpDoor() {
		return upDoor;
	}


	public void setUpDoor(boolean upDoor) {
		this.upDoor = upDoor;
	}


	public boolean getRightDoor() {
		return rightDoor;
	}


	public void setRightDoor(boolean rightDoor) {
		this.rightDoor = rightDoor;
	}


	public boolean getDownDoor() {
		return downDoor;
	}


	public void setDownDoor(boolean downDoor) {
		this.downDoor = downDoor;
	}


	public boolean getLeftDoor() {
		return leftDoor;
	}


	public void setLeftDoor(boolean leftDoor) {
		this.leftDoor = leftDoor;
	}


	public Rectangle getNorthDoor() {
		return northDoor;
	}


	public void setNorthDoor(Rectangle northDoor) {
		this.northDoor = northDoor;
	}


	public Rectangle getEastDoor() {
		return eastDoor;
	}


	public void setEastDoor(Rectangle eastDoor) {
		this.eastDoor = eastDoor;
	}


	public Rectangle getSouthDoor() {
		return southDoor;
	}


	public void setSouthDoor(Rectangle southDoor) {
		this.southDoor = southDoor;
	}


	public Rectangle getWestDoor() {
		return westDoor;
	}


	public void setWestDoor(Rectangle westDoor) {
		this.westDoor = westDoor;
	}


	public void setBackgroundTexture(Texture backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}


	public Texture getBackgroundTexture() {
		return backgroundTexture;
	}
}
