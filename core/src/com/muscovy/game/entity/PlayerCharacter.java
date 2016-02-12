package com.muscovy.game.entity;


import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.enums.ItemType;
import com.muscovy.game.enums.PlayerShotType;
import com.muscovy.game.enums.ProjectileDamager;
import com.muscovy.game.enums.ProjectileType;
import com.muscovy.game.input.Action;
import com.muscovy.game.input.ControlMap;


/**
 * Created by ewh502 on 04/12/2015. Good luck
 */
public class PlayerCharacter extends MoveableEntity {
	public static final float MAX_SPEED = 350f;
	// public static final float ACCELERATION = PlayerCharacter.MAX_SPEED * 6;
	// public static final float DECELERATION = PlayerCharacter.MAX_SPEED * 5.5f;

	public static final float FLIGHT_SPEED_MULTIPLIER = 2f; // 3f;
	public static final float MAX_FLIGHT_TIME = 1f; // 0.25f;

	public static final float BASE_ATTACK_INTERVAL = 0.1f; // interval between attacks
	public static final float PROJECTILE_SPEED = 450;
	public static final float PROJECTILE_RANGE = 600;

	public static final float RAPID_FIRE_ATTACK_INTERVAL = PlayerCharacter.BASE_ATTACK_INTERVAL * 0.5f;
	public static final float FLAME_THROWER_ATTACK_INTERVAL = 0.03f;

	public static final int MAX_HEALTH = 100;
	public static final float INVINCIBILITY_DURATION = 2;

	public static final float HITBOX_Y_OFFSET = -6;
	public static final float HITBOX_RADIUS = (74 / 2) - 8; // (74 / 2) - 2;

	public static final float MAX_BOMB_DROP_TIME = 0.5f;

	// Position bomb below the player slightly
	// Means bomb will be rendered on top of player
	public static final float BOMB_Y_OFFSET = 34;

	private Vector2 shotDirection;
	private PlayerShotType shotType = PlayerShotType.SINGLE;
	private ProjectileType projectileType = ProjectileType.STANDARD;

	private float attackInterval = PlayerCharacter.BASE_ATTACK_INTERVAL;

	private float timeSinceLastAttack = attackInterval;

	private float projectileSpeed = PlayerCharacter.PROJECTILE_SPEED;
	private float projectileRange = PlayerCharacter.PROJECTILE_RANGE;
	private float projectileLife = projectileRange / projectileSpeed;
	private boolean firing = false;

	private int maxHealth = PlayerCharacter.MAX_HEALTH;
	private int currentHealth = maxHealth;
	private boolean invincible = false;
	private float invincibilityCounter = 0;
	private float invincibilityDuration = PlayerCharacter.INVINCIBILITY_DURATION;

	private HashSet<ItemType> obtainedItems = new HashSet<ItemType>();

	private int score = 0;

	private int animationCycle;
	private int animationCounter;
	// private ArrayList<Texture> downWalkCycle;
	// private ArrayList<Texture> leftWalkCycle;
	// private ArrayList<Texture> rightWalkCycle;
	// private ArrayList<Texture> upWalkCycle;

	private ControlMap controlMap;
	private Controller controller;

	private int bombCount = 0;
	private float maxBombDropTime = PlayerCharacter.MAX_BOMB_DROP_TIME;
	private float bombDropTime = 0;

	private boolean flying = false;
	private boolean usedAllFlight = false;
	private float maxFlightTime = PlayerCharacter.MAX_FLIGHT_TIME;
	private float flightTime = maxFlightTime;
	private float flightSpeedScale = PlayerCharacter.FLIGHT_SPEED_MULTIPLIER;


	public PlayerCharacter(MuscovyGame game, String textureName, Vector2 position, ControlMap controlMap,
			Controller controller) {
		super(game, textureName, position);
		this.controlMap = controlMap;
		this.controller = controller;

		setAccelerationSpeed(MoveableEntity.PLAYER_ACCELERATION_SPEED);

		animationCycle = 0;
		shotDirection = new Vector2(0, 1);

		// setShotType(PlayerShotType.FLAME_THROWER);

		// Sprite playerSprite;
		// downWalkCycle = new ArrayList<Texture>();
		// upWalkCycle = new ArrayList<Texture>();
		// rightWalkCycle = new ArrayList<Texture>();
		// leftWalkCycle = new ArrayList<Texture>();
		// Commented out due to animation not actually existing yet

		// TODO: Change animations to use spritesheets
		// Texture tempTexture;
		// for (int i = 1; i < 8; i++) {
		// tempTexture = textureMap.getTextureOrLoadFile(String.format("BasicDuckWalkCycle/downduck%d.png", i));
		// downWalkCycle.add(tempTexture);
		// }
		// for (int i = 1; i < 8; i++) {
		// tempTexture = textureMap.getTextureOrLoadFile(String.format("BasicDuckWalkCycle/upduck%d.png", i));
		// upWalkCycle.add(tempTexture);
		// }
		// for (int i = 1; i < 12; i++) {
		// tempTexture = textureMap.getTextureOrLoadFile(String.format("BasicDuckWalkCycle/leftduck%d.png", i));
		// leftWalkCycle.add(tempTexture);
		// }
		// for (int i = 1; i < 12; i++) {
		// tempTexture = textureMap.getTextureOrLoadFile(String.format("BasicDuckWalkCycle/rightduck%d.png",
		// i));
		// rightWalkCycle.add(tempTexture);
		// }

		setHitboxYOffset(PlayerCharacter.HITBOX_Y_OFFSET); // Just to get the hitbox in line with that fat fuck
									// of a duck's body
		setHitboxRadius(PlayerCharacter.HITBOX_RADIUS);
		setFullFlightBar();
	}


	@Override
	public void selfUpdate(float deltaTime) {
		if (invincible) {
			invincibilityUpdate(deltaTime);
		}

		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.muscovy.game.entity.MoveableEntity#movementLogic(float)
	 */
	@Override
	public void movementLogic(float deltaTime) {
		float rightState = controlMap.getStateForAction(Action.WALK_RIGHT, controller);
		float leftState = controlMap.getStateForAction(Action.WALK_LEFT, controller);
		float upState = controlMap.getStateForAction(Action.WALK_UP, controller);
		float downState = controlMap.getStateForAction(Action.WALK_DOWN, controller);

		float dx = rightState - leftState;
		float dy = upState - downState;

		Vector2 acceleration = new Vector2(dx, dy);
		// If both dx and dy are 1 (i.e. both W and D are pressed)
		// Limit the magnitude of the acceleration
		// Uses limit, not setLength, because controllers allow for movement between 0 and 1
		acceleration.limit(1);

		// When flying, the timer is decreasing to 0
		// When not flying, the timer is incrementing back to the max
		if (flying) {
			flightTime -= deltaTime;
		} else {
			flightTime += deltaTime;
		}

		// If the number reaches 0, then all the fly has been used
		if (flightTime <= 0) {
			flightTime = 0;
			flying = false;
			usedAllFlight = true;
		}

		// If the number reaches the max, then flying fully recharged
		if (flightTime >= maxFlightTime) {
			flightTime = maxFlightTime;
			usedAllFlight = false;
		}

		float flyScale = 1;
		if (controlMap.getStateForAction(Action.FLY, controller) > 0) {
			// Player already flying
			if (flying) {
				flyScale = flightSpeedScale;
			} else {
				// Only allow sprinting to start if the player didn't use the charge
				if ((flightTime >= 0) && !usedAllFlight) {
					flying = true;
					flyScale = flightSpeedScale;
				}
			}
		} else {
			flying = false;
		}

		setAccelerationSpeed(MoveableEntity.PLAYER_ACCELERATION_SPEED * flyScale);

		addMovementAcceleration(acceleration);

		float shootRightState = controlMap.getStateForAction(Action.SHOOT_RIGHT, controller);
		float shootLeftState = controlMap.getStateForAction(Action.SHOOT_LEFT, controller);
		float shootUpState = controlMap.getStateForAction(Action.SHOOT_UP, controller);
		float shootDownState = controlMap.getStateForAction(Action.SHOOT_DOWN, controller);

		float shootDX = shootRightState - shootLeftState;
		float shootDY = shootUpState - shootDownState;

		int sx = 0;
		int sy = 0;

		// Shoot in the larger of the 2 directions
		// If they're the same, shoots on X
		if (Math.abs(shootDY) > Math.abs(shootDX)) {
			sx = 0;
			sy = (int) Math.signum(shootDY);
		} else {
			sx = (int) Math.signum(shootDX);
			sy = 0;
		}

		if ((sx != 0) || (sy != 0)) {
			setFiring(true);
			setShotDirection(sx, sy);
		} else {
			setFiring(false);
		}
	}


	/**
	 * @param deltaTime
	 * @return
	 */
	public boolean checkBombDrop(float deltaTime) {
		bombDropTime -= deltaTime;

		if (bombDropTime <= 0) {
			bombDropTime = 0;
		}

		return ((bombCount > 0) && (bombDropTime <= 0));
	}


	/**
	 * Returns null if no bomb dropped.
	 *
	 * @return
	 */
	public Bomb attemptDropBomb() {
		if (bombDropTime > 0) {
			return null;
		}
		if (bombCount <= 0) {
			return null;
		}

		if (controlMap.getStateForAction(Action.DROP_BOMB, controller) > 0) {
			float bombTextureRadius = 32;
			Vector2 bombPosition = getCenter().cpy();
			bombPosition.sub(bombTextureRadius, bombTextureRadius);
			bombPosition.sub(0, PlayerCharacter.BOMB_Y_OFFSET);
			Bomb bomb = new Bomb(game, AssetLocations.BOMB, bombPosition);
			bombDropTime = maxBombDropTime;
			bombCount -= 1;
			return bomb;
		}

		return null;
	}


	/**
	 * @return
	 */
	public int getBombCount() {
		return bombCount;
	}


	/**
	 * @param bombCount
	 * @return
	 */
	public void giveBombs(int bombCount) {
		this.bombCount += bombCount;
	}


	/**
	 * Attack methods (only shots currently)
	 */
	public boolean checkRangedAttack(float deltaTime) {
		if (timeSinceLastAttack > attackInterval) {
			timeSinceLastAttack = 0;
			return true;
		} else {
			incrementTimeSinceLastAttack(deltaTime);
			return false;
		}
	}


	/**
	 * @param deltaTime
	 */
	public void incrementTimeSinceLastAttack(float deltaTime) {
		timeSinceLastAttack += deltaTime;
	}


	/**
	 *
	 */
	public void resetAttackTimer() {
		timeSinceLastAttack = attackInterval;
	}


	/**
	 * Returns a different projectile array list depending on the shot type, so that needs to be given directly to
	 * the entity manager
	 *
	 * @return
	 */
	public ArrayList<Projectile> rangedAttack() {
		float x = (getX() + (getWidth() / 2)) - 8;
		// TODO: This should get player's height, not tile size
		float y = getY() + (getHeight() / 2);

		// Player position
		Vector2 position = new Vector2(x, y);

		Vector2 direction = shotDirection.cpy();

		int count = 0;
		switch (shotType) {
		case SINGLE:
			count = 1;
			break;
		case DOUBLE:
			count = 2;
			break;
		case TRIPLE:
			count = 3;
			break;
		}

		return Projectile.shootProjectiles(game, count, position, direction, projectileLife, projectileSpeed,
				ProjectileDamager.ENEMY, projectileType);
	}


	/**
	 * @return
	 */
	public boolean isFiring() {
		return firing;
	}


	/**
	 * @param firing
	 */
	public void setFiring(boolean firing) {
		this.firing = firing;
	}


	/**
	 * @param score
	 */
	public void increaseScore(int score) {
		this.score += score;
	}


	/**
	 * @param damage
	 */
	public void takeDamage(float damage) {
		if (!invincible) {
			currentHealth -= damage;
			invincible = true;
		}
	}


	/**
	 * @param health
	 * @return
	 */
	public boolean gainHealth(int health) {
		if (currentHealth == maxHealth) {
			return false;
		} else {
			currentHealth = Math.min(currentHealth + health, maxHealth);
			return true;
		}
	}


	/**
	 * @param deltaTime
	 */
	private void invincibilityUpdate(float deltaTime) {
		invincibilityCounter += deltaTime;
		if (invincibilityCounter > invincibilityDuration) {
			invincible = false;
			invincibilityCounter = 0;
		}
	}


	/**
	 * @return
	 */
	public boolean isInvincible() {
		return invincible;
	}


	/**
	 * @return
	 */
	public int getHealth() {
		return currentHealth;
	}


	/**
	 * If the new health is bigger than max health, clamps it to max.
	 *
	 * @param newHealth
	 */
	public void setHealth(int newHealth) {
		currentHealth = newHealth;
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}


	/**
	 * @return
	 */
	public float getMaxHealth() {
		return maxHealth;
	}


	/**
	 * @param maxHealth
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}


	/**
	 * @return
	 */
	public float getInvincibilityCounter() {
		return invincibilityCounter;
	}


	/**
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
	}


	/**
	 * @return
	 */
	public int getScore() {
		return score;
	}


	/**
	 * @return
	 */
	public Vector2 getShotDirection() {
		return shotDirection;
	}


	/**
	 * @param shotDirection
	 */
	public void setShotDirection(Vector2 shotDirection) {
		this.shotDirection.set(shotDirection).nor();
	}


	/**
	 * @param x
	 * @param y
	 */
	public void setShotDirection(float x, float y) {
		shotDirection.set(x, y).nor();
	}


	/**
	 * @return
	 */
	public float getTimeSinceLastAttack() {
		return timeSinceLastAttack;
	}


	/**
	 * @param timeSinceLastAttack
	 */
	public void setTimeSinceLastAttack(float timeSinceLastAttack) {
		this.timeSinceLastAttack = timeSinceLastAttack;
	}


	public ProjectileType getProjectileType() {
		return projectileType;
	}


	public void setProjectileType(ProjectileType projectileType) {
		this.projectileType = projectileType;
	}


	public PlayerShotType getShotType() {
		return shotType;
	}


	/**
	 * @return
	 */
	public float getProjectileRange() {
		return projectileRange;
	}


	/**
	 * @param projectileRange
	 */
	public void setProjectileRange(float projectileRange) {
		this.projectileRange = projectileRange;
		projectileLife = projectileRange / projectileSpeed;
	}


	/**
	 * @return
	 */
	public float getProjectileLife() {
		return projectileLife;
	}


	/**
	 * @param projectileLife
	 */
	public void setProjectileLife(float projectileLife) {
		this.projectileLife = projectileLife;
		projectileRange = projectileSpeed * projectileLife;
	}


	/**
	 * @return
	 */
	public float getProjectileVelocity() {
		return projectileSpeed;
	}


	/**
	 * @param projectileVelocity
	 */
	public void setProjectileVelocity(float projectileVelocity) {
		projectileSpeed = projectileVelocity;
		projectileLife = projectileRange / projectileVelocity;
	}


	/**
	 * @param type
	 */
	public void setShotType(PlayerShotType type) {
		shotType = type;
	}


	/**
	 * @return
	 */
	public float getAttackInterval() {
		return attackInterval;
	}


	/**
	 * @param attackInterval
	 */
	public void setAttackInterval(float attackInterval) {
		this.attackInterval = attackInterval;
	}


	/**
	 * @param itemType
	 */
	public void addItemToObtainedItems(ItemType itemType) {
		obtainedItems.add(itemType);
	}


	/**
	 * @return
	 */
	public HashSet<ItemType> getObtainedItems() {
		return obtainedItems;
	}


	/**
	 * @return
	 */
	public float getMaxFlightTime() {
		return maxFlightTime;
	}


	/**
	 * @param maxFlightTime
	 */
	public void setMaxFlightTime(float maxFlightTime) {
		this.maxFlightTime = maxFlightTime;
	}


	/**
	 * @return
	 */
	public float getFlightSpeedScale() {
		return flightSpeedScale;
	}


	/**
	 * @param flightSpeedScale
	 */
	public void setFlightSpeedScale(float flightSpeedScale) {
		this.flightSpeedScale = flightSpeedScale;
	}


	/**
	 * @return
	 */
	public boolean isFlying() {
		return flying;
	}


	/**
	 * @return
	 */
	public boolean hasUsedAllFlight() {
		return usedAllFlight;
	}


	/**
	 * @return
	 */
	public float getFlightTime() {
		return flightTime;
	}


	public void setFullFlightBar() {
		flightTime = maxFlightTime;
	}


	// TODO: Animations

	/**
	 * @return
	 */
	public int getAnimationCounter() {
		return animationCounter;
	}


	/**
	 * @return
	 */
	public int getAnimationCycle() {
		return animationCycle;
	}


	/**
	 * Animation methods currently commented out, as we only have one sprite atm, but they should be easy to work
	 * out. Might be worth revamping later though
	 */
	public void walkCycleNext() {
		// float direction = velocity.angleRad();
		// int switcher = (int) (direction / (Math.PI / 2));
		//
		// // int switcher = 0;
		// // if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
		// // if (velocity.x > 0) {
		// // switcher = 1;
		// // } else {
		// // switcher = 3;
		// // }
		// // } else {
		// // if (velocity.y > 0) {
		// // switcher = 0;
		// // } else {
		// // switcher = 2;
		// // }
		// // }
		//
		// switch (switcher) {
		// case 0:
		// setTexture(upWalkCycle.get(animationCycle));
		// if (animationCycle == 6) {
		// animationCycle = 0;
		// } else {
		// animationCycle++;
		// }
		// break;
		// case 1:
		// setTexture(rightWalkCycle.get(animationCycle));
		// if (animationCycle == 10) {
		// animationCycle = 2;
		// } else {
		// animationCycle++;
		// }
		// break;
		// case 2:
		// setTexture(downWalkCycle.get(animationCycle));
		// if (animationCycle == 6) {
		// animationCycle = 0;
		// } else {
		// animationCycle++;
		// }
		// break;
		// case 3:
		// setTexture(leftWalkCycle.get(animationCycle));
		// if (animationCycle == 10) {
		// animationCycle = 2;
		// } else {
		// animationCycle++;
		// }
		// break;
		// }
	}


	public void movementAnimation() {
		// if (animationCounter == 5) {
		// animationCounter = 0;
		// walkCycleNext();
		// } else {
		// animationCounter++;
		// }
	}


	public void idleAnimation() {
		// velocity.len2() < 1
		// if ((velocity.x == 0) && (velocity.y == 0)) {
		// // this.setTexture(downWalkCycle.get(0));
		// animationCycle = 0;
		// }
	}
}
