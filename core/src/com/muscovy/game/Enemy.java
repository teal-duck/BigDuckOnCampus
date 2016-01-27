package com.muscovy.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.enums.AttackType;
import com.muscovy.game.enums.EnemyShotType;
import com.muscovy.game.enums.MovementType;
import com.muscovy.game.enums.ProjectileDamager;


/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class Enemy extends Collidable {
	public static final float TOUCH_DAMAGE = 10f;
	public static final float ATTACK_INTERVAL = 1.5f;
	public static final float SPEED = 200;

	public static final float PROJECTILE_RANGE = 400;
	public static final float PROJECTILE_VELOCITY = 150;
	public static final float ATTACK_RANGE = 480;
	public static final float VIEW_DISTANCE = Enemy.ATTACK_RANGE;

	private MovementType movementType = MovementType.STATIC;
	private Vector2 velocity;
	private float currentSpeed = 0;
	private float maxSpeed = Enemy.SPEED;
	private float directionCounter = 0;

	private boolean collidingWithSomething;

	private AttackType attackType = AttackType.TOUCH;
	private EnemyShotType shotType = EnemyShotType.SINGLE_TOWARDS_PLAYER;
	private float touchDamage = Enemy.TOUCH_DAMAGE;

	// MuscovyGame.java checks these and does an attack if attack timer is greater than attack interval.
	private float attackTimer;
	private float attackInterval = Enemy.ATTACK_INTERVAL;

	private float projectileRange = Enemy.PROJECTILE_RANGE;
	private float projectileVelocity = Enemy.PROJECTILE_VELOCITY;
	private float projectileLife = projectileRange / projectileVelocity;
	private float attackRange = Enemy.ATTACK_RANGE;
	private float viewDistance = Enemy.VIEW_DISTANCE;

	private float currentHealth = 40;
	private int scoreOnDeath = 0;

	private boolean dead = false;

	private Random random;
	private TextureMap textureMap;


	public Enemy(Sprite sprite, Vector2 position, TextureMap textureMap, Random random) {
		super(sprite, position);
		this.textureMap = textureMap;
		this.random = random;

		velocity = new Vector2(1, 0).setLength(maxSpeed);
		rotateRandomDirection();

		setSprite(sprite);
	}


	public void update(float deltaTime, PlayerCharacter player) {
		attackTimer += deltaTime;
		movementLogic(deltaTime, player);
		moveEntity(deltaTime);
	}


	private void rotateRandomDirection() {
		float minRotation = 10;
		float maxRotation = 80;

		int direction = 0;
		if (random.nextBoolean()) {
			direction = 1;
		} else {
			direction = -1;
		}
		velocity.rotate(direction * (((maxRotation - minRotation) * random.nextFloat()) + minRotation));
	}


	public void movementLogic(float deltaTime, PlayerCharacter player) {
		switch (movementType) {
		case STATIC:
			currentSpeed = 0;
			maxSpeed = 0;
			break;

		case RANDOM:
			currentSpeed = maxSpeed;
			float timeToStayInSameDirection = 0.4f;

			if (directionCounter > timeToStayInSameDirection) {
				directionCounter = 0;
				rotateRandomDirection();
			} else {
				directionCounter += deltaTime;
			}
			break;

		case FOLLOW:
			if (getDistanceTo(player) < viewDistance) {
				currentSpeed = maxSpeed;
				pointTo(player);
			} else {
				currentSpeed = 0;
			}
			break;
		}

	}


	public void setVelocityLengthToCurrentSpeed() {
		velocity.setLength(currentSpeed);
	}


	public void moveEntity(float deltaTime) {
		setVelocityLengthToCurrentSpeed();
		getPosition().mulAdd(velocity, deltaTime);
		updateBoxesPosition();
	}


	public void pointTo(Collidable collidable) {
		float length = velocity.len();
		// Set to other position
		velocity.set(collidable.getPosition());
		// Subtract current position (to get vector between objects)
		velocity.sub(getPosition());
		// Reset the length of the velocity
		velocity.setLength(length);
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
			shootDirection.set(velocity).nor();
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
			return Projectile.shootProjectiles(bulletsToShoot, position, shootDirection, projectileRange,
					projectileVelocity, ProjectileDamager.PLAYER, textureMap);
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


	public void takeDamage(float damage) {
		currentHealth -= damage;
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


	public boolean lifeOver() {
		return dead;
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
		projectileLife = projectileRange / projectileVelocity;
	}


	public float getProjectileLife() {
		return projectileLife;
	}


	public void setProjectileLife(float projectileLife) {
		this.projectileLife = projectileLife;
		projectileRange = projectileVelocity * projectileLife;
	}


	public float getProjectileVelocity() {
		return projectileVelocity;
	}


	public void setProjectileVelocity(float projectileVelocity) {
		this.projectileVelocity = projectileVelocity;
		projectileLife = projectileRange / projectileVelocity;
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


	public float getVelocityX() {
		return velocity.x;
	}


	public void setVelocityX(float velocityX) {
		velocity.x = velocityX;
	}


	public float getVelocityY() {
		return velocity.y;
	}


	public void setVelocityY(float velocityY) {
		velocity.y = velocityY;
	}


	public float getSpeed() {
		return maxSpeed;
	}


	public void setSpeed(float speed) {
		maxSpeed = speed;
	}


	public MovementType getMovementType() {
		return movementType;
	}


	public void setMovementType(MovementType movementType) {
		this.movementType = movementType;
		calculateScoreOnDeath();
	}
}
