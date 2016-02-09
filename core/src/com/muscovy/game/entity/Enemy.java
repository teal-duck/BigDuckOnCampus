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
 * Created by SeldomBucket on 05-Dec-15.
 */
public class Enemy extends MoveableEntity {
	public static final float TOUCH_DAMAGE = 10f;
	public static final float ATTACK_INTERVAL = 1.5f;
	public static final float ATTACK_RANDOMNESS = 0.5f;
	public static final float MAX_SPEED = 200;
	public static final float BOSS_MAX_SPEED = Enemy.MAX_SPEED * 0.8f;

	public static final float PROJECTILE_RANGE = 400;
	public static final float PROJECTILE_SPEED = 150;
	public static final float ATTACK_RANGE = 480;
	public static final float VIEW_DISTANCE = Enemy.ATTACK_RANGE;

	public static final float JUST_DAMAGED_TIME = 0.6f;

	private MovementType movementType = MovementType.STATIC;
	private float directionCounter = 0;

	private boolean collidingWithSomething;

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
	private float attackRange = Enemy.ATTACK_RANGE;
	private float viewDistance = Enemy.VIEW_DISTANCE;

	private float currentHealth = 40;
	private int scoreOnDeath = 0;

	private boolean dead = false;

	private float justDamagedTime = 0f;


	public Enemy(MuscovyGame game, String textureName, Vector2 position) {
		super(game, textureName, position, new Vector2(1, 0).setLength(Enemy.MAX_SPEED));

		setAccelerationSpeed(MoveableEntity.ENEMY_ACCELERATION_SPEED);
		rotateRandomDirection(getVelocity());
		chooseNewAttackInterval();
	}


	public void setJustDamagedTime(float justDamagedTime) {
		this.justDamagedTime = justDamagedTime;
	}


	public float getJustDamagedTime() {
		return justDamagedTime;
	}


	@Override
	public void selfUpdate(float deltaTime) {
		attackTimer += deltaTime;

		justDamagedTime -= deltaTime;
		if (justDamagedTime < 0) {
			justDamagedTime = 0;
		}
	}


	public void flipDirection() {
		getVelocity().scl(-1f);
	}


	private void rotateRandomDirection(Vector2 vecToRotate) {
		float minRotation = 10;
		float maxRotation = 15;

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
		case STATIC:
			setMaxSpeed(0);
			break;

		case RANDOM:
			float timeToStayInSameDirection = 0.05f;

			if (directionCounter > timeToStayInSameDirection) {
				directionCounter = 0;
				rotateRandomDirection(direction);
			} else {
				directionCounter += deltaTime;
			}
			break;

		case FOLLOW:
			PlayerCharacter player = getPlayer();

			if ((player != null) && (getDistanceTo(player) < viewDistance)) {
				direction.set(player.getCenter()).sub(getCenter()).nor();
			} else {
				direction.setZero();
			}

			break;
		}

		direction.nor();
		addMovementAcceleration(direction);
	}


	public PlayerCharacter getPlayer() {
		// TODO: Make Enemy.getPlayer() nicer
		return game.getPlayerCharacter();
	}


	public float getDistanceTo(Collidable collidable) {
		return getPosition().dst(collidable.getPosition());
	}


	public ArrayList<Projectile> rangedAttack(PlayerCharacter playerCharacter) {
		Vector2 position = getCenter();

		Vector2 directionToPlayer = new Vector2(playerCharacter.getCircleHitbox().x - getCircleHitbox().x,
				playerCharacter.getCircleHitbox().y - getCircleHitbox().y).nor();
		float distanceToPlayer = getDistanceTo(playerCharacter);

		int bulletsToShoot = 0;
		Vector2 shootDirection = directionToPlayer;

		switch (shotType) {
		case SINGLE_MOVEMENT:
			bulletsToShoot = 1;
			shootDirection.set(getVelocity()).nor();
			break;
		case SINGLE_TOWARDS_PLAYER:
			if (distanceToPlayer < attackRange) {
				bulletsToShoot = 1;
			}
			break;
		case DOUBLE_TOWARDS_PLAYER:
			if (distanceToPlayer < attackRange) {
				bulletsToShoot = 2;
			}
			break;
		case TRIPLE_TOWARDS_PLAYER:
			if (distanceToPlayer < attackRange) {
				bulletsToShoot = 3;
			}
			break;
		case RANDOM_DIRECTION:
			bulletsToShoot = 1;
			shootDirection.setToRandomDirection();
			break;
		}

		if (bulletsToShoot > 0) {
			return Projectile.shootProjectiles(game, bulletsToShoot, position, shootDirection,
					projectileRange, projectileSpeed, ProjectileDamager.PLAYER, projectileType);
		} else {
			return new ArrayList<Projectile>();
		}
	}


	public boolean checkRangedAttack() {
		if (attackTimer > attackInterval) {
			attackTimer = 0;
			return true;
		} else {
			return false;
		}
	}


	public void chooseNewAttackInterval() {
		attackInterval = MathUtils.random(maxAttackInterval - attackRandomness,
				maxAttackInterval + attackRandomness);
	}


	public void takeDamage(float damage) {
		currentHealth -= damage;
		setJustDamagedTime(Enemy.JUST_DAMAGED_TIME);
		if (currentHealth <= 0) {
			killSelf();
		}
	}


	public void killSelf() {
		dead = true;
	}


	public void calculateScoreOnDeath() {
		scoreOnDeath = (attackType.getScoreMultiplier() * 10) + (movementType.getScoreMultiplier() * 10);
	}


	public boolean isCollidingWithSomething() {
		return collidingWithSomething;
	}


	public void setCollidingWithSomething(boolean collidingWithSomething) {
		this.collidingWithSomething = collidingWithSomething;
	}


	public float getTouchDamage() {
		return touchDamage;
	}


	public void setTouchDamage(float touchDamage) {
		this.touchDamage = touchDamage;
	}


	public AttackType getAttackType() {
		return attackType;
	}


	public void setAttackType(AttackType attackType) {
		this.attackType = attackType;
		calculateScoreOnDeath();
	}


	public void setShotType(EnemyShotType shotType) {
		this.shotType = shotType;
	}


	public EnemyShotType getShotType() {
		return shotType;
	}


	public boolean isLifeOver() {
		return dead;
	}


	public float getMaxAttackInterval() {
		return maxAttackInterval;
	}


	public void setMaxAttackInterval(float maxAttackInterval) {
		this.maxAttackInterval = maxAttackInterval;
	}


	public float getAttackRandomness() {
		return attackRandomness;
	}


	public void setAttackRandomness(float attackRandomness) {
		this.attackRandomness = attackRandomness;
	}


	public float getAttackRange() {
		return attackRange;
	}


	public void setAttackRange(float attackRange) {
		this.attackRange = attackRange;
	}


	public float getProjectileRange() {
		return projectileRange;
	}


	public void setProjectileRange(float projectileRange) {
		this.projectileRange = projectileRange;
		projectileLife = projectileRange / projectileSpeed;
	}


	public float getProjectileLife() {
		return projectileLife;
	}


	public void setProjectileLife(float projectileLife) {
		this.projectileLife = projectileLife;
		projectileRange = projectileSpeed * projectileLife;
	}


	public float getProjectileVelocity() {
		return projectileSpeed;
	}


	public void setProjectileVelocity(float projectileVelocity) {
		projectileSpeed = projectileVelocity;
		projectileLife = projectileRange / projectileVelocity;
	}


	public ProjectileType getProjectileType() {
		return projectileType;
	}


	public void setProjectileType(ProjectileType projectileType) {
		this.projectileType = projectileType;
	}


	public float getMovementRange() {
		return viewDistance;
	}


	public void setMovementRange(float movementRange) {
		viewDistance = movementRange;
	}


	public float getCurrentHealth() {
		return currentHealth;
	}


	public void setCurrentHealth(float currentHealth) {
		this.currentHealth = currentHealth;
	}


	public int getScoreOnDeath() {
		return scoreOnDeath;
	}


	public void setScoreOnDeath(int scoreOnDeath) {
		this.scoreOnDeath = scoreOnDeath;
	}


	public MovementType getMovementType() {
		return movementType;
	}


	public void setMovementType(MovementType movementType) {
		this.movementType = movementType;
		calculateScoreOnDeath();
	}
}
