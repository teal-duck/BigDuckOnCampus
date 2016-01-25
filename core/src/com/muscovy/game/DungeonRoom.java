package com.muscovy.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.muscovy.game.enums.AttackType;
import com.muscovy.game.enums.EnemyShotType;
import com.muscovy.game.enums.MovementType;
import com.muscovy.game.enums.RoomType;


/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class DungeonRoom extends OnscreenDrawable {
	/**
	 * Contains lists of obstacles and enemies in that room. They are passed to the entity manager when the room is
	 * entered. Room is generated using a 2d array (explained in more detail further down) There are 2 sets of
	 * walls, one for the enemies and player to collide with, and one for the projectile to collide with so it looks
	 * like they break halfway up the wall, and have some height associated with them.
	 */
	private ArrayList<Obstacle> obstacleList;
	private ArrayList<Enemy> enemyList;
	private Rectangle[] walls;
	private Rectangle[] projectileWalls;
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
	/*
	 * roomType indicates the type of room options: "" (default), "start", "boss", "item", "shop"
	 */
	private RoomType roomType = RoomType.NORMAL; // 0 = normal room, 1 = boss room, 2 = item room, 3 = shop room, 4
							// = start room
	private Random rand;


	public DungeonRoom() {
		rand = new Random();
		obstacleList = new ArrayList<Obstacle>();
		enemyList = new ArrayList<Enemy>();
		// TODO: Use constants for sizes of objects
		walls = new Rectangle[4];
		walls[0] = new Rectangle(0, 0, 1280, 64);// bottom wall
		walls[1] = new Rectangle(0, 0, 64, 960 - 192);// left wall
		walls[2] = new Rectangle(1280 - 64, 0, 64, 960 - 192);// right wall
		walls[3] = new Rectangle(0, 960 - 192 - 64, 1280, 64);// top wall
		projectileWalls = new Rectangle[4];
		projectileWalls[0] = new Rectangle(0, 0, 1280, 32);// bottom wall
		projectileWalls[1] = new Rectangle(0, 0, 32, 960 - 192);// left wall
		projectileWalls[2] = new Rectangle(1280 - 32, 0, 32, 960 - 192);// right wall
		projectileWalls[3] = new Rectangle(0, 768 - 32, 1280, 32);// top wall
	}


	private void createNonDamagingObstacle(int x, int y) {
		Sprite rockSprite;
		Obstacle obstacle5;
		rockSprite = new Sprite();
		rockSprite.setTexture(new Texture("accommodationAssets/obstacles/binRecycle.png"));
		obstacle5 = new Obstacle(rockSprite);
		obstacle5.setXTiles(x);
		obstacle5.setYTiles(y);
		addObstacle(obstacle5);
	}


	private void createDamagingObstacle(int x, int y) {
		Sprite spikeSprite;
		Obstacle obstacle6;
		spikeSprite = new Sprite();
		spikeSprite.setTexture(new Texture("accommodationAssets/obstacles/binWaste.png"));
		obstacle6 = new Obstacle(spikeSprite);
		obstacle6.setXTiles(x);
		obstacle6.setYTiles(y);
		addObstacle(obstacle6);
		obstacle6.setDamaging(true);
		obstacle6.setTouchDamage(10.0f);
	}


	private void createRandomEnemy(int x, int y) {
		Sprite enemySprite;
		Enemy enemy;
		if (rand.nextBoolean()) {
			enemySprite = new Sprite(new Texture(
					"accommodationAssets/enemies/cleaner/rightCleanerWalk/PNGs/rightCleaner1.png"));
			enemy = new Enemy(enemySprite);
			enemy.setAttackType(AttackType.TOUCH);
		} else {
			enemySprite = new Sprite(new Texture(
					"accommodationAssets/enemies/student/rightStudentWalk/PNGs/rightStudent1.png"));
			enemy = new Enemy(enemySprite);
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
		enemy.setXTiles(x);
		enemy.setYTiles(y);
		enemy.calculateScoreOnDeath();
		addEnemy(enemy);
	}


	public void generateRoom(int level) {
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
			int[][] tileArray = new int[10][18];
			int chosenLayout = rand.nextInt(10) + 1;
			// TODO: Load maps from file
			// TODO: What do 0, 1, 2, 3 and 4 mean in tileArray
			switch (chosenLayout) {
			case 1:
				tileArray = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
				break;
			case 2:
				tileArray = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 2, 0, 0, 0, 2, 2, 0, 0, 0, 2, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 2, 0, 0, 0, 2, 2, 0, 0, 0, 2, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
				break;
			case 3:
				tileArray = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 0, 4, 4, 0, 0, 0, 1, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 0, 4, 4, 0, 0, 0, 1, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
				break;
			case 4:
				tileArray = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
				break;
			case 5:
				tileArray = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 4, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 4, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 4, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 4, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
				break;
			case 6:
				tileArray = new int[][] { { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
						{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
						{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 } };
				break;
			case 7:
				tileArray = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
				break;
			case 8:
				tileArray = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
				break;
			case 9:
				tileArray = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
				break;
			case 10:
				tileArray = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 2, 0, 2, 2, 0, 2, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 2, 0, 2, 2, 0, 2, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
				break;
			}
			boolean obstacleType3NonDamaging = rand.nextBoolean();
			for (int i = 0; i < tileArray.length; i++) {
				for (int j = 0; j < tileArray[i].length; j++) {
					switch (tileArray[i][j]) {
					case 0:
						break;
					case 1:
						createNonDamagingObstacle(2 * j, 2 * i);
						break;
					case 2:
						createDamagingObstacle(2 * j, 2 * i);
						break;
					case 3:
						if (obstacleType3NonDamaging) {
							createNonDamagingObstacle(2 * j, 2 * i);
						} else if (!obstacleType3NonDamaging) {
							createDamagingObstacle(2 * j, 2 * i);
						}
						break;
					case 4:
						if (rand.nextInt(5) < 3) {
							createRandomEnemy(2 * j, 2 * i);
						}
						break;
					}
				}
			}
			break;
		case BOSS:
			// Boss
			Sprite bossSprite;
			Enemy bossEnemy;
			bossSprite = new Sprite(new Texture("accommodationAssets/accommodationBoss.png"));
			bossEnemy = new Enemy(bossSprite);
			bossEnemy.setXTiles((int) ((36 / 2) - (bossEnemy.getWidth() / 64)));
			bossEnemy.setYTiles((int) ((18 / 2) - (bossEnemy.getHeight() / 64)));
			bossEnemy.setAttackType(AttackType.BOTH);
			bossEnemy.setMovementType(MovementType.FOLLOW);
			// bossEnemy.setMaxVelocity(100);
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
			enemySprite = new Sprite(new Texture(
					"accommodationAssets/enemies/student/rightStudentWalk/PNGs/rightStudent1.png"));
			enemy = new Enemy(enemySprite);
			enemy.setAttackType(AttackType.RANGE);
			enemy.setXTiles(100);
			enemy.setYTiles(100);
			addEnemy(enemy);
			break;
		case SHOP:
			// Shop
			enemySprite = new Sprite(new Texture(
					"accommodationAssets/enemies/student/rightStudentWalk/PNGs/rightStudent1.png"));
			enemy = new Enemy(enemySprite);
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
		switch (level) {
		case 0:
			texture = new Texture("accommodationAssets/constantineBackground.png");
			break;
		case 1:
			texture = new Texture("accommodationAssets/langwithBackground.png");
			break;
		case 2:
			texture = new Texture("accommodationAssets/goodrickeBackground.png");
			break;
		case 3:
			texture = new Texture("accommodationAssets/lmbBackground.png");
			break;
		case 4:
			texture = new Texture("accommodationAssets/catalystBackground.png");
			break;
		case 5:
			texture = new Texture("accommodationAssets/tftvBackground.png");
			break;
		case 6:
			texture = new Texture("accommodationAssets/csBackground.png");
			break;
		default:
			texture = new Texture("accommodationAssets/rchBackground.png");
			break;
		}

		setSprite(new Sprite(texture));
		initialiseDoors();
	}


	public void initialiseDoors() {
		if (upDoor) {
			northDoor = new Rectangle((1280 - doorSize) / 2, 768 - doorSize, doorSize, doorSize);
		}
		if (downDoor) {
			southDoor = new Rectangle((1280 - doorSize) / 2, 0, doorSize, doorSize);
		}
		if (rightDoor) {
			eastDoor = new Rectangle(1280 - doorSize, (768 - doorSize) / 2, doorSize, doorSize);
		}
		if (leftDoor) {
			westDoor = new Rectangle(0, (768 - doorSize) / 2, doorSize, doorSize);
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


	public boolean isEnemiesDead() {
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


	@Override
	public void setSprite(Sprite sprite) {
		sprite.setX(0);
		sprite.setY(0);
		super.setSprite(sprite);
	}
}
