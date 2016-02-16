package com.muscovy.game.entity;


import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.enums.AttackType;
import com.muscovy.game.enums.EnemyShotType;
import com.muscovy.game.enums.MovementType;
import com.muscovy.game.enums.ProjectileDamager;
import com.muscovy.game.enums.ProjectileType;


/**
 * Project URL : http://teal-duck.github.io/teal-duck
 */
public class Enemy extends MoveableEntity {

	private static final int INITIAL_MAX_ROTATION = 360;

	private static final int INITIAL_MIN_ROTATION = 0;

	// Attack
	public static final float TOUCH_DAMAGE = 10f;

	public static final float ATTACK_INTERVAL = 1.5f;
	public static final float ATTACK_RANDOMNESS = 0.5f;

	public static final float PROJECTILE_RANGE = 400;
	public static final float PROJECTILE_SPEED = 150;
	public static final float BOSS_PROJECTILE_SPEED = Enemy.PROJECTILE_SPEED * 2;

	// Used in the random movement type logic
	public static final float MIN_ROTATION = 10;
	public static final float MAX_ROTATION = 15;
	public static final float TIME_TO_STAY_IN_RANDOM_DIRECTION = 0.05f;

	public static final float VIEW_DISTANCE = 1000;

	public static final float JUST_DAMAGED_TIME = 0.6f;

	public static final int BOSS_WIDTH = 256;
	public static final int BOSS_HEIGHT = 256;

	// Movement
	private MovementType movementType = MovementType.STATIC;
	private float directionCounter = 0;

	// Collision detection
	private boolean collidingWithSomething;

	// Attack
	private AttackType attackType = AttackType.TOUCH;
	private EnemyShotType shotType = EnemyShotType.SINGLE_TOWARDS_PLAYER;
	private float touchDamage = Enemy.TOUCH_DAMAGE;

	private float attackTimer;
	private float maxAttackInterval = Enemy.ATTACK_INTERVAL;
	private float attackInterval = maxAttackInterval;
	private float attackRandomness = Enemy.ATTACK_RANDOMNESS;

	private float projectileRange = Enemy.PROJECTILE_RANGE;
	private float projectileSpeed = Enemy.PROJECTILE_SPEED;
	private float projectileLife = projectileRange / projectileSpeed;
	private ProjectileType projectileType = ProjectileType.STANDARD;

	private float viewDistance = Enemy.VIEW_DISTANCE;

	private float currentHealth = 40;
	private int scoreOnDeath = 0;

	private boolean dead = false;

	private float justDamagedTime = 0f;

	private boolean isBoss = false;


	public Enemy(MuscovyGame game, String textureName, Vector2 position) {
		this(game, textureName, position, OnscreenDrawable.DEFAULT_ENTITY_WIDTH,
				OnscreenDrawable.DEFAULT_ENTITY_HEIGHT);
	}


	public Enemy(MuscovyGame game, String textureName, Vector2 position, int width, int height) {
		super(game, textureName, position, new Vector2(1, 0), width, height);

		setAccelerationSpeed(MoveableEntity.ENEMY_ACCELERATION_SPEED);
		rotateRandomDirection(getVelocity(), Enemy.INITIAL_MIN_ROTATION, Enemy.INITIAL_MAX_ROTATION);
		chooseNewAttackInterval();
	}


	@Override
	public void selfUpdate(float deltaTime) {
		attackTimer += deltaTime;

		justDamagedTime -= deltaTime;
		if (justDamagedTime < 0) {
			justDamagedTime = 0;
		}
	}


	/**
	 * Rotates a vector by a random direction within (minRotation, maxRotation) (in degrees).
	 *
	 * @param vecToRotate
	 */
	private void rotateRandomDirection(Vector2 vecToRotate, float minRotation, float maxRotation) {
		int direction = 0;
		if (game.getRandom().nextBoolean()) {
			direction = 1;
		} else {
			direction = -1;
		}

		vecToRotate.rotate(direction
				* (((maxRotation - minRotation) * game.getRandom().nextFloat()) + minRotation));
	}


	@Override
	public void movementLogic(float deltaTime) {
		Vector2 direction = getVelocity().cpy().nor();

		switch (movementType) {
		// Static enemies shouldn't move, so have no movement logic.
		case STATIC:
			break;

		// Rotate the enemy a random direction after a set amount of time.
		case RANDOM:
			directionCounter += deltaTime;
			if (directionCounter > Enemy.TIME_TO_STAY_IN_RANDOM_DIRECTION) {
				directionCounter = 0;
				rotateRandomDirection(direction, Enemy.MIN_ROTATION, Enemy.MAX_ROTATION);
			}
			break;

		// Get the direction to the player and follow that vector.
		case FOLLOW:
			PlayerCharacter player = getPlayer();

			if ((player != null) && (getDistanceTo(player) < viewDistance)) {
				direction.set(player.getCenter()).sub(getCenter()).nor();
			} else {
				direction.setZero();
			}

			break;
		}

		// addMovementAcceleration take a normalised vector.
		direction.nor();
		addMovementAcceleration(direction);
	}


	/**
	 * @return
	 */
	public PlayerCharacter getPlayer() {
		return game.getPlayerCharacter();
	}


	/**
	 * @param collidable
	 * @return Distance between enemy and given Collidable.
	 */
	public float getDistanceTo(Collidable collidable) {
		return getPosition().dst(collidable.getPosition());
	}


	/**
	 * Shoots projectiles depending on this enemy's shot type.
	 *
	 * @param playerCharacter
	 * @return
	 */
	public ArrayList<Projectile> rangedAttack(PlayerCharacter playerCharacter) {
		Vector2 position = getCenter();

		// Vector2 directionToPlayer = new Vector2(playerCharacter.getCircleHitbox().x - getCircleHitbox().x,
		// playerCharacter.getCircleHitbox().y - getCircleHitbox().y).nor();
		Vector2 directionToPlayer = playerCharacter.getCenter().cpy().sub(getCenter()).nor();
		float distanceToPlayer = getDistanceTo(playerCharacter);

		int bulletsToShoot = 0; // Number of bullets in the attack
		Vector2 shootDirection = directionToPlayer;

		switch (shotType) {
		case SINGLE_MOVEMENT:
			bulletsToShoot = 1;
			shootDirection.set(getVelocity()).nor();
			break;
		case SINGLE_TOWARDS_PLAYER:
			if (distanceToPlayer < viewDistance) {
				bulletsToShoot = 1;
			}
			break;
		case DOUBLE_TOWARDS_PLAYER:
			if (distanceToPlayer < viewDistance) {
				bulletsToShoot = 2;
			}
			break;
		case TRIPLE_TOWARDS_PLAYER:
			if (distanceToPlayer < viewDistance) {
				bulletsToShoot = 3;
			}
			break;
		case RANDOM_DIRECTION:
			bulletsToShoot = 1;
			shootDirection.setToRandomDirection();
			break;
		case FOUR_DIRECTIONS:
			bulletsToShoot = 4;
			break;
		case SIX_DIRECTIONS:
			bulletsToShoot = 6;
			break;
		case EIGHT_DIRECTIONS:
			bulletsToShoot = 8;
			break;
		default:
			break;
		}

		if (bulletsToShoot > 0) {
			return Projectile.shootProjectiles(game, bulletsToShoot, position, shootDirection,
					projectileLife, projectileSpeed, ProjectileDamager.PLAYER, projectileType);

		} else {
			return new ArrayList<Projectile>();
		}
	}


	/**
	 * @return
	 */
	public boolean checkRangedAttack() {
		if (attackTimer > attackInterval) {
			attackTimer = 0;
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Random time between attacks
	 */
	public void chooseNewAttackInterval() {
		attackInterval = MathUtils.random(maxAttackInterval - attackRandomness,
				maxAttackInterval + attackRandomness);
	}


	/**
	 * @param damage
	 */
	public void takeDamage(float damage) {
		currentHealth -= damage;
		setJustDamagedTime(Enemy.JUST_DAMAGED_TIME);
		if (currentHealth <= 0) {
			killSelf();
		}
	}


	/**
	 * @param justDamagedTime
	 */
	public void setJustDamagedTime(float justDamagedTime) {
		this.justDamagedTime = justDamagedTime;
	}


	/**
	 * @return
	 */
	public float getJustDamagedTime() {
		return justDamagedTime;
	}


	/**
	 * Set this.dead to true, marking this enemy as okay to delete.
	 */
	public void killSelf() {
		dead = true;
	}


	/**
	 * Set score to add to player's total after this entity's death.
	 */
	public void calculateScoreOnDeath() {
		scoreOnDeath = (attackType.getScoreMultiplier() * 10) + (movementType.getScoreMultiplier() * 10);
	}


	/**
	 * @return
	 */
	public boolean isCollidingWithSomething() {
		return collidingWithSomething;
	}


	/**
	 * @param collidingWithSomething
	 */
	public void setCollidingWithSomething(boolean collidingWithSomething) {
		this.collidingWithSomething = collidingWithSomething;
	}


	/**
	 * @return
	 */
	public float getTouchDamage() {
		return touchDamage;
	}


	/**
	 * @param touchDamage
	 */
	public void setTouchDamage(float touchDamage) {
		this.touchDamage = touchDamage;
	}


	/**
	 * @return
	 */
	public AttackType getAttackType() {
		return attackType;
	}


	/**
	 * @param attackType
	 */
	public void setAttackType(AttackType attackType) {
		this.attackType = attackType;
		calculateScoreOnDeath();
	}


	/**
	 * @param shotType
	 */
	public void setShotType(EnemyShotType shotType) {
		this.shotType = shotType;
	}


	/**
	 * @return
	 */
	public EnemyShotType getShotType() {
		return shotType;
	}


	/**
	 * @return True if this enemy is ready to be deleted and should no longer be rendered.
	 */
	public boolean isLifeOver() {
		return dead;
	}


	/**
	 * @return Maximum time between firing projectiles.
	 */
	public float getMaxAttackInterval() {
		return maxAttackInterval;
	}


	/**
	 * @param maxAttackInterval
	 *                Maximum time between firing projectiles.
	 */
	public void setMaxAttackInterval(float maxAttackInterval) {
		this.maxAttackInterval = maxAttackInterval;
	}


	/**
	 * @return attackRandomness = 0.5 * maximum spread of attack intervals.
	 */
	public float getAttackRandomness() {
		return attackRandomness;
	}


	/**
	 * @param attackRandomness
	 *                0.5 * maximum spread of attack intervals.
	 */
	public void setAttackRandomness(float attackRandomness) {
		this.attackRandomness = attackRandomness;
	}


	/**
	 * @return Maximum distance that an enemy will begin following a player.
	 */
	public float getViewDistance() {
		return viewDistance;
	}


	/**
	 * @param viewDistance
	 *                Maximum distance that an enemy will begin following a player.
	 */
	public void setViewDistance(float viewDistance) {
		this.viewDistance = viewDistance;
	}


	/**
	 * @return Maximum distance that projectiles fired by this enemy will fly.
	 */
	public float getProjectileRange() {
		return projectileRange;
	}


	/**
	 * Also modifies projectileLife to correctly account for new projectile range.
	 *
	 * @param projectileRange
	 *                Maximum distance that projectiles fired by this enemy will fly.
	 */
	public void setProjectileRange(float projectileRange) {
		this.projectileRange = projectileRange;
		projectileLife = projectileRange / projectileSpeed;
	}


	/**
	 * @return Maximum distance that projectiles fired by this enemy will fly.
	 */
	public float getProjectileLife() {
		return projectileLife;
	}


	/**
	 * Also modifies projectileRange to correctly account for new projectile lifespan.
	 *
	 * @param projectileLife
	 *                Maximum time that a projectile will fly before disappearing.
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
	 * @return
	 */
	public ProjectileType getProjectileType() {
		return projectileType;
	}


	/**
	 * @param projectileType
	 */
	public void setProjectileType(ProjectileType projectileType) {
		this.projectileType = projectileType;
	}


	/**
	 * @return
	 */
	public float getCurrentHealth() {
		return currentHealth;
	}


	/**
	 * @param currentHealth
	 */
	public void setCurrentHealth(float currentHealth) {
		this.currentHealth = currentHealth;
	}


	/**
	 * @return
	 */
	public int getScoreOnDeath() {
		return scoreOnDeath;
	}


	/**
	 * @param scoreOnDeath
	 */
	public void setScoreOnDeath(int scoreOnDeath) {
		this.scoreOnDeath = scoreOnDeath;
	}


	/**
	 * @return
	 */
	public MovementType getMovementType() {
		return movementType;
	}


	/**
	 * @param movementType
	 */
	public void setMovementType(MovementType movementType) {
		this.movementType = movementType;
		calculateScoreOnDeath();
	}


	/**
	 * @return True if this enemy is a boss.
	 */
	public boolean isBoss() {
		return isBoss;
	}


	/**
	 * Sets this enemy to become a boss.
	 */
	public void setBoss() {
		isBoss = true;
	}
}
