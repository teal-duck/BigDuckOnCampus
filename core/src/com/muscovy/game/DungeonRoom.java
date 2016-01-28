package com.muscovy.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.enums.AttackType;
import com.muscovy.game.enums.EnemyShotType;
import com.muscovy.game.enums.ItemType;
import com.muscovy.game.enums.LevelType;
import com.muscovy.game.enums.MovementType;
import com.muscovy.game.enums.RoomType;


/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class DungeonRoom {
	public static final int FLOOR_HEIGHT_IN_TILES = 10;
	public static final int FLOOR_WIDTH_IN_TILES = 18;

	/**
	 * Contains lists of obstacles and enemies in that room. They are passed to the entity manager when the room is
	 * entered. Room is generated using a 2d array (explained in more detail further down) There are 2 sets of
	 * walls, one for the enemies and player to collide with, and one for the projectile to collide with so it looks
	 * like they break halfway up the wall, and have some height associated with them. The room is made up of 32x32
	 * 'half tiles' (used in map gen). The number of tiles in each direction is stored in constants in
	 * {@link DungeonRoom.floorWidthInHalfTiles} & {@link DungeonRoom.floorHeightInHalfTiles}.
	 */
	// TODO: Should obstacles and enemy lists be the same, then use polymorphism?
	private ArrayList<Obstacle> obstacleList;
	private ArrayList<Enemy> enemyList;
	private ArrayList<Item> itemList;
	private Rectangle[] walls;
	private Rectangle[] projectileWalls;
	private RoomType roomType = RoomType.NORMAL;

	private boolean hasUpDoor = false;
	private boolean hasRightDoor = false;
	private boolean hasDownDoor = false;
	private boolean hasLeftDoor = false;
	private Rectangle upDoorRect;
	private Rectangle rightDoorRect;
	private Rectangle downDoorRect;
	private Rectangle leftDoorRect;
	private float doorSize = 65;

	private boolean allEnemiesDead;

	private Random random;
	private TextureMap textureMap;
	private Texture backgroundTexture;


	public DungeonRoom(Random random, TextureMap textureMap) {
		this.random = random;
		this.textureMap = textureMap;

		obstacleList = new ArrayList<Obstacle>();
		enemyList = new ArrayList<Enemy>();

		initialiseWalls();
	}


	private void initialiseWalls() {
		final float windowWidth = MuscovyGame.WINDOW_WIDTH;
		final float windowHeight = MuscovyGame.WINDOW_HEIGHT;
		final float tileSize = MuscovyGame.TILE_SIZE;
		final float halfTileSize = tileSize / 2;
		final float topGuiSize = MuscovyGame.TOP_GUI_SIZE;
		final float worldHeight = windowHeight - topGuiSize;

		walls = new Rectangle[4];
		walls[0] = new Rectangle(0, 0, windowWidth, tileSize);
		walls[1] = new Rectangle(0, 0, tileSize, windowHeight - topGuiSize);
		walls[2] = new Rectangle(windowWidth - tileSize, 0, tileSize, windowHeight - topGuiSize);
		walls[3] = new Rectangle(0, worldHeight - tileSize, windowWidth, tileSize);

		projectileWalls = new Rectangle[4];
		projectileWalls[0] = new Rectangle(0, 0, windowWidth, halfTileSize);
		projectileWalls[1] = new Rectangle(0, 0, halfTileSize, windowHeight - topGuiSize);
		projectileWalls[2] = new Rectangle(windowWidth - halfTileSize, 0, halfTileSize,
				windowHeight - topGuiSize);
		projectileWalls[3] = new Rectangle(0, worldHeight - halfTileSize, windowWidth, halfTileSize);

	}


	private Obstacle createNonDamagingObstacle(int x, int y) {
		Sprite rockSprite = new Sprite(textureMap.getTextureOrLoadFile(AssetLocations.RECYLING_BIN));
		Obstacle obstacle = new Obstacle(rockSprite, new Vector2(x, y));
		obstacle.setXTiles(x);
		obstacle.setYTiles(y);
		addObstacle(obstacle);
		return obstacle;
	}


	private Obstacle createDamagingObstacle(int x, int y) {
		Sprite spikeSprite = new Sprite(textureMap.getTextureOrLoadFile(AssetLocations.WASTE_BIN));
		Obstacle obstacle = new Obstacle(spikeSprite, new Vector2(x, y));
		obstacle.setXTiles(x);
		obstacle.setYTiles(y);

		obstacle.setDamaging(true);
		obstacle.setTouchDamage(Enemy.TOUCH_DAMAGE);

		addObstacle(obstacle);
		return obstacle;
	}


	private Enemy createRandomEnemy(EntityManager entityManager, int x, int y) {
		Sprite enemySprite;
		Enemy enemy;
		Vector2 position = new Vector2(x, y);

		if (random.nextBoolean()) {
			enemySprite = new Sprite(textureMap.getTextureOrLoadFile(AssetLocations.CLEANER));
			enemy = new Enemy(enemySprite, position, entityManager, textureMap, random);
			enemy.setAttackType(AttackType.TOUCH);
		} else {
			enemySprite = new Sprite(textureMap.getTextureOrLoadFile(AssetLocations.STUDENT));
			enemy = new Enemy(enemySprite, position, entityManager, textureMap, random);
			enemy.setAttackType(AttackType.RANGE);
		}

		switch (random.nextInt(3)) {
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

		enemy.setXTiles(x);
		enemy.setYTiles(y);
		enemy.calculateScoreOnDeath();

		addEnemy(enemy);
		return enemy;
	}
	
	private void createHealthPack(int x, int y) {
		Sprite sprite = new Sprite(textureMap.getTextureOrLoadFile("healthpack.png"));
		Vector2 position = new Vector2(x, y);
		Item healthPack = new Item(sprite, position, ItemType.HEALTH);
		
		addItem(healthPack);
	}


	public void generateRoom(DungeonRoomTemplateLoader templateLoader, LevelType levelType,
			EntityManager entityManager) {
		/**
		 * Currently chooses from 1 of 10 types of room, 5 with enemies, 5 without, most with some sort of
		 * obstacle. Tile array is based on a grid where each tile is 64x64. Represents the room. 0 = empty
		 * space, 1 = non-damaging obstacle, 2 = damaging obstacle, 3 = random obstacle, 4 = random enemy Easy
		 * to put in more, just extend the case statement below to have more specific stuff.
		 */
		Sprite enemySprite;
		Enemy enemy;

		switch (roomType) {
		case NORMAL:
			int[][] tileArray = templateLoader.getRandomTemplateOrLoad(random);
			boolean obstacleType3NonDamaging = random.nextBoolean();

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
						if (random.nextInt(5) < 3) {
							createRandomEnemy(entityManager, col, row);
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
			// TODO: Change boss parameters
			enemySprite = new Sprite(textureMap.getTextureOrLoadFile(AssetLocations.ACCOMODATION_BOSS));
			enemy = new Enemy(enemySprite, new Vector2(0, 0), entityManager, textureMap, random);
			enemy.setXTiles((int) ((DungeonRoom.FLOOR_WIDTH_IN_TILES / 2)
					- (enemy.getWidth() / MuscovyGame.TILE_SIZE / 2)));
			enemy.setYTiles((int) ((DungeonRoom.FLOOR_HEIGHT_IN_TILES / 2)
					- (enemy.getHeight() / MuscovyGame.TILE_SIZE / 2)));
			enemy.setAttackType(AttackType.BOTH);
			enemy.setMovementType(MovementType.FOLLOW);
			enemy.setMaxSpeed(enemy.getMaxSpeed() * 0.8f);
			enemy.setProjectileVelocity(enemy.getProjectileVelocity() * 2);
			enemy.setTouchDamage(20);
			enemy.setShotType(EnemyShotType.TRIPLE_TOWARDS_PLAYER);
			enemy.setScoreOnDeath(3000);
			enemy.setCurrentHealth(600);
			enemy.setHitboxRadius(80);
			enemy.setMovementRange(1000);
			addEnemy(enemy);
			break;

		case ITEM:
			// Item
			// TODO: Item room
			enemySprite = new Sprite(textureMap.getTextureOrLoadFile(AssetLocations.STUDENT));
			enemy = new Enemy(enemySprite, new Vector2(0, 0), entityManager, textureMap, random);
			enemy.setAttackType(AttackType.RANGE);
			enemy.setXTiles(100);
			enemy.setYTiles(100);
			addEnemy(enemy);
			break;

		case SHOP:
			// Shop
			enemySprite = new Sprite(textureMap.getTextureOrLoadFile(AssetLocations.STUDENT));
			enemy = new Enemy(enemySprite, new Vector2(0, 0), entityManager, textureMap, random);
			enemy.setAttackType(AttackType.RANGE);
			enemy.setX(0);
			enemy.setY(0);
			addEnemy(enemy);
			break;

		case START:
			// Start
			break;
		}

		Texture texture = textureMap.getTextureOrLoadFile(AssetLocations.getLevelBackground(levelType));
		setBackgroundTexture(texture);
		initialiseDoors();
	}


	public void initialiseDoors() {
		if (hasUpDoor) {
			upDoorRect = new Rectangle((MuscovyGame.WINDOW_WIDTH - doorSize) / 2,
					MuscovyGame.WORLD_HEIGHT - doorSize, doorSize, doorSize);
		}
		if (hasDownDoor) {
			downDoorRect = new Rectangle((MuscovyGame.WINDOW_WIDTH - doorSize) / 2, 0, doorSize, doorSize);
		}
		if (hasRightDoor) {
			rightDoorRect = new Rectangle(MuscovyGame.WINDOW_WIDTH - doorSize,
					(MuscovyGame.WORLD_HEIGHT - doorSize) / 2, doorSize, doorSize);
		}
		if (hasLeftDoor) {
			leftDoorRect = new Rectangle(0, (MuscovyGame.WORLD_HEIGHT - doorSize) / 2, doorSize, doorSize);
		}
	}


	public void addEnemy(Enemy enemy) {
		enemyList.add(enemy);
		// TODO: Add enemy should set allEnemiesDead = false
	}


	public void killEnemy(Enemy enemy) {
		enemyList.remove(enemy);
		// enemyList.trimToSize();
		areAllEnemiesDead();
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
	
	public void addItem(Item item) {
		itemList.add(item);
	}
	
	public ArrayList<Item> getItemList() {
		return itemList;
	}


	public Rectangle getBottomWall() {
		return walls[0];
	}


	public Rectangle getLeftWall() {
		return walls[1];
	}


	public Rectangle getRightWall() {
		return walls[2];
	}


	public Rectangle getTopWall() {
		return walls[3];
	}


	public Rectangle getBottomProjectileWall() {
		return projectileWalls[0];
	}


	public Rectangle getLeftProjectileWall() {
		return projectileWalls[1];
	}


	public Rectangle getRightProjectileWall() {
		return projectileWalls[2];
	}


	public Rectangle getTopProjectileWall() {
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
			allEnemiesDead = true;
		}
		return allEnemiesDead;
	}


	public void setEnemiesDead(boolean enemiesDead) {
		allEnemiesDead = enemiesDead;
	}


	public boolean hasUpDoor() {
		return hasUpDoor;
	}


	public boolean hasRightDoor() {
		return hasRightDoor;
	}


	public boolean hasDownDoor() {
		return hasDownDoor;
	}


	public boolean hasLeftDoor() {
		return hasLeftDoor;
	}


	public void setHasUpDoor(boolean upDoor) {
		hasUpDoor = upDoor;
	}


	public void setHasRightDoor(boolean rightDoor) {
		hasRightDoor = rightDoor;
	}


	public void setHasDownDoor(boolean downDoor) {
		hasDownDoor = downDoor;
	}


	public void setHasLeftDoor(boolean leftDoor) {
		hasLeftDoor = leftDoor;
	}


	public Rectangle getUpDoor() {
		return upDoorRect;
	}


	public Rectangle getRightDoor() {
		return rightDoorRect;
	}


	public Rectangle getDownDoor() {
		return downDoorRect;
	}


	public Rectangle getLeftDoor() {
		return leftDoorRect;
	}


	// public void setUpDoor(Rectangle northDoor) {
	// this.upDoorRect = northDoor;
	// }
	//
	//
	// public void setRightDoor(Rectangle eastDoor) {
	// this.rightDoorRect = eastDoor;
	// }
	//
	//
	// public void setDownDoor(Rectangle southDoor) {
	// this.downDoorRect = southDoor;
	// }
	//
	//
	// public void setLeftDoor(Rectangle westDoor) {
	// this.leftDoorRect = westDoor;
	// }

	public void setBackgroundTexture(Texture backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}


	public Texture getBackgroundTexture() {
		return backgroundTexture;
	}
}
