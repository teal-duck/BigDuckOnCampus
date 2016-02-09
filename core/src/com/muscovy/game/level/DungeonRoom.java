package com.muscovy.game.level;


import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.entity.Item;
import com.muscovy.game.entity.Obstacle;
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
	private final float doorSize = 65;

	private boolean allEnemiesDead;

	private Texture backgroundTexture;

	private MuscovyGame game;


	public DungeonRoom(MuscovyGame game) {
		this.game = game;

		obstacleList = new ArrayList<Obstacle>();
		enemyList = new ArrayList<Enemy>();
		itemList = new ArrayList<Item>();

		initialiseWalls();
	}


	private void initialiseWalls() {
		final int windowWidth = game.getWindowWidth();
		final int windowHeight = game.getWindowHeight();
		final int wallWidth = game.getWallWidth();
		final int projectileWallWidth = (2 * wallWidth) / 3;

		walls = new Rectangle[4];
		walls[0] = new Rectangle(0, 0, windowWidth, wallWidth); // bottom
		walls[1] = new Rectangle(0, 0, wallWidth, windowHeight); // left
		walls[2] = new Rectangle(windowWidth - wallWidth, 0, wallWidth, windowHeight); // right
		walls[3] = new Rectangle(0, windowHeight - wallWidth, windowWidth, wallWidth); // top

		projectileWalls = new Rectangle[4];
		projectileWalls[0] = new Rectangle(0, 0, windowWidth, projectileWallWidth);
		projectileWalls[1] = new Rectangle(0, 0, projectileWallWidth, windowHeight);
		projectileWalls[2] = new Rectangle(windowWidth - projectileWallWidth, 0, projectileWallWidth,
				windowHeight);
		projectileWalls[3] = new Rectangle(0, windowHeight - projectileWallWidth, windowWidth,
				projectileWallWidth);
	}


	private Obstacle createNonDamagingObstacle(int x, int y) {
		Sprite rockSprite = new Sprite(game.getTextureMap().getTextureOrLoadFile(AssetLocations.RECYLING_BIN));
		Obstacle obstacle = new Obstacle(game, rockSprite, new Vector2(x, y));
		obstacle.setXTiles(x);
		obstacle.setYTiles(y);
		addObstacle(obstacle);
		return obstacle;
	}


	private Obstacle createDamagingObstacle(int x, int y) {
		Sprite spikeSprite = new Sprite(game.getTextureMap().getTextureOrLoadFile(AssetLocations.WASTE_BIN));
		Obstacle obstacle = new Obstacle(game, spikeSprite, new Vector2(x, y));
		obstacle.setXTiles(x);
		obstacle.setYTiles(y);

		obstacle.setDamaging(true);
		obstacle.setTouchDamage(Enemy.TOUCH_DAMAGE);

		addObstacle(obstacle);
		return obstacle;
	}


	private Enemy createRandomEnemy(int x, int y) {
		Sprite enemySprite;
		Enemy enemy;
		Vector2 position = new Vector2(x, y);

		if (game.getRandom().nextBoolean()) {
			enemySprite = new Sprite(game.getTextureMap().getTextureOrLoadFile(AssetLocations.CLEANER));
			enemy = new Enemy(game, enemySprite, position);
			enemy.setAttackType(AttackType.TOUCH);
		} else {
			enemySprite = new Sprite(game.getTextureMap().getTextureOrLoadFile(AssetLocations.STUDENT));
			enemy = new Enemy(game, enemySprite, position);
			enemy.setAttackType(AttackType.RANGE);
		}

		switch (game.getRandom().nextInt(3)) {
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
		Sprite sprite = new Sprite(game.getTextureMap().getTextureOrLoadFile("healthpack.png"));
		Vector2 position = new Vector2(x, y);
		Item healthPack = new Item(game, sprite, position, ItemType.HEALTH);

		healthPack.setXTiles(x);
		healthPack.setYTiles(y);
		addItem(healthPack);
	}


	public void generateRoom(LevelType levelType, DungeonRoomTemplateLoader templateLoader) {
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
			int[][] tileArray = templateLoader.getRandomTemplateOrLoad(game.getRandom());
			boolean obstacleType3NonDamaging = game.getRandom().nextBoolean();

			for (int row = 0; row < tileArray.length; row++) {
				for (int col = 0; col < tileArray[row].length; col++) {
					int cell = tileArray[row][col];
					int fixedY = tileArray.length - row - 1;

					switch (cell) {
					case DungeonRoomTemplateLoader.EMPTY_TILE:
						break;
					case DungeonRoomTemplateLoader.NON_DAMAGING_OBSTACLE:
						createNonDamagingObstacle(col, fixedY);
						break;
					case DungeonRoomTemplateLoader.DAMAGING_OBSTACLE:
						createDamagingObstacle(col, fixedY);
						break;
					case DungeonRoomTemplateLoader.MAYBE_DAMAGING_OBSTACLE:
						if (obstacleType3NonDamaging) {
							createNonDamagingObstacle(col, fixedY);
						} else if (!obstacleType3NonDamaging) {
							createDamagingObstacle(col, fixedY);
						}
						break;
					case DungeonRoomTemplateLoader.MAYBE_ENEMY:
						if (game.getRandom().nextInt(5) < 3) {
							createRandomEnemy(col, fixedY);
						}
						break;
					case DungeonRoomTemplateLoader.HEALTHPACK:
						createHealthPack(col, fixedY);
						break;
					default:
						System.out.println("Unrecognised cell " + cell + "; location (" + col
								+ ", " + cell + ") (fixed y: " + fixedY + ")");
						break;
					}
				}
			}
			break;

		case BOSS:
			// Boss
			// TODO: Change boss parameters
			enemySprite = new Sprite(
					game.getTextureMap().getTextureOrLoadFile(AssetLocations.ACCOMODATION_BOSS));
			enemy = new Enemy(game, enemySprite, new Vector2(0, 0));
			enemy.setXTiles((int) ((DungeonRoom.FLOOR_WIDTH_IN_TILES / 2)
					- (enemy.getWidth() / game.getTileSize() / 2)));
			enemy.setYTiles((int) ((DungeonRoom.FLOOR_HEIGHT_IN_TILES / 2)
					- (enemy.getHeight() / game.getTileSize() / 2)));
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
			enemySprite = new Sprite(game.getTextureMap().getTextureOrLoadFile(AssetLocations.STUDENT));
			enemy = new Enemy(game, enemySprite, new Vector2(0, 0));
			enemy.setAttackType(AttackType.RANGE);
			enemy.setXTiles(DungeonRoom.FLOOR_WIDTH_IN_TILES - 1);
			enemy.setYTiles(DungeonRoom.FLOOR_HEIGHT_IN_TILES - 1);
			addEnemy(enemy);
			
			ItemType itemType = ItemType.TRIPLE_SHOT;
			Sprite itemSprite = new Sprite(ItemType.getItemSprite(game, itemType));
			Item item = new Item(game, itemSprite, new Vector2(0, 0), itemType);
			item.setXTiles(5);
			item.setYTiles(5);
			addItem(item);
			break;

		case SHOP:
			// Shop
			// TODO: Shop room
			enemySprite = new Sprite(game.getTextureMap().getTextureOrLoadFile(AssetLocations.STUDENT));
			enemy = new Enemy(game, enemySprite, new Vector2(0, 0));
			enemy.setAttackType(AttackType.RANGE);
			enemy.setXTiles(0);
			enemy.setYTiles(0);
			addEnemy(enemy);
			break;

		case START:
			// Start
			break;
		}

		loadBackgroundTexture(levelType);
		initialiseDoors();
	}


	public void loadBackgroundTexture(LevelType levelType) {
		Texture backgroundTexture = game.getTextureMap()
				.getTextureOrLoadFile(AssetLocations.getLevelBackground(levelType));
		setBackgroundTexture(backgroundTexture);
	}


	public void initialiseDoors() {
		if (hasUpDoor) {
			createUpDoorRect();
		}
		if (hasDownDoor) {
			createDownDoorRect();
		}
		if (hasRightDoor) {
			createRightDoorRect();
		}
		if (hasLeftDoor) {
			createLeftDoorRect();
		}
	}


	private void createUpDoorRect() {
		upDoorRect = new Rectangle((game.getWindowWidth() - doorSize) / 2,
				game.getWindowHeight() - (doorSize + game.getWallEdge()), doorSize, doorSize);
	}


	private void createDownDoorRect() {
		downDoorRect = new Rectangle((game.getWindowWidth() - doorSize) / 2, game.getWallEdge(), doorSize,
				doorSize);
	}


	private void createRightDoorRect() {
		rightDoorRect = new Rectangle(game.getWindowWidth() - (doorSize + game.getWallEdge()),
				(game.getWindowHeight() - doorSize) / 2, doorSize, doorSize);
	}


	private void createLeftDoorRect() {
		leftDoorRect = new Rectangle(game.getWallEdge(), (game.getWindowHeight() - doorSize) / 2, doorSize,
				doorSize);
	}


	public void addEnemy(Enemy enemy) {
		// TODO: Add enemy should set allEnemiesDead = false
		enemyList.add(enemy);
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


	// TODO: Setting has a door should generate the door?
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


	public void setBackgroundTexture(Texture backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}


	public Texture getBackgroundTexture() {
		return backgroundTexture;
	}
}
