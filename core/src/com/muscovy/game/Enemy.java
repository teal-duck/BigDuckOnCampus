package com.muscovy.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
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
	private Random random;

	private boolean collidingWithSomething;

	private AttackType attackType;
	private float touchDamage;
	private float currentHealth = 40;
	private boolean dead = false;
	private int scoreOnDeath = 0;

	private ArrayList<Projectile> rangedAttack;
	private EnemyShotType shotType = EnemyShotType.SINGLE_TOWARDS_PLAYER;

	// MuscovyGame.java checks these and does an attack if attack
	// timer is greater than attack interval.
	private float attackTimer;
	private float attackInterval = 1.5f;

	private float projectileRange = 400;
	private float projectileVelocity = 150;
	private float projectileLife = projectileRange / projectileVelocity;
	private float attackRange = 480;

	private MovementType movementType;
	private float viewDistance = 480;
	private Vector2 velocity;
	private float defaultVelocity = 200;
	private float directionCounter = 0;

	private TextureMap textureMap;


	public Enemy(TextureMap textureMap, Sprite sprite) {
		this.textureMap = textureMap;
		random = new Random();
		rangedAttack = new ArrayList<Projectile>();
		setSprite(sprite);
		touchDamage = 10.0f;
		movementType = MovementType.STATIC;
		attackType = AttackType.TOUCH;
		velocity = new Vector2(1, 0).setLength(defaultVelocity);
		initialiseX(0);
		initialiseY(0);
		setUpBoxes();

	}


	public boolean isCollidingWithSomething() {
		return collidingWithSomething;
	}


	public void setCollidingWithSomething(boolean collidingWithSomething) {
		this.collidingWithSomething = collidingWithSomething;
	}


	public void update(PlayerCharacter player) {
		rangedAttack.clear();
		movement(player);
		attackTimer += Gdx.graphics.getDeltaTime();
	}


	/**
	 * Attack & damage methods
	 */
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


	public ArrayList<Projectile> rangedAttack(PlayerCharacter playerCharacter) {
		float x = (getX() + (getWidth() / 2));
		float y = (getY() + (getHeight() / 2));
		Vector2 position = new Vector2(x, y);

		Vector2 directionToPlayer = new Vector2(playerCharacter.getCircleHitbox().x - getCircleHitbox().x,
				playerCharacter.getCircleHitbox().y - getCircleHitbox().y).nor();
		float dist = getDistanceTo(playerCharacter);

		int count = 0;
		Vector2 shootDirection = directionToPlayer;

		switch (shotType) {
		case SINGLE_MOVEMENT:
			count = 1;
			shootDirection.set(velocity).nor();
			break;
		case SINGLE_TOWARDS_PLAYER:
			if (dist < attackRange) {
				count = 1;
			}
			break;
		case DOUBLE_TOWARDS_PLAYER:
			if (dist < attackRange) {
				count = 2;
			}
			break;
		case TRIPLE_TOWARDS_PLAYER:
			if (dist < attackRange) {
				count = 3;
			}
			break;
		case RANDOM_DIRECTION:
			count = 4;
			shootDirection.setToRandomDirection();
			break;
		}

		return Projectile.shootProjectiles(textureMap, count, position, shootDirection, projectileRange,
				projectileVelocity, ProjectileDamager.PLAYER);
	}


	public boolean checkRangedAttack() {
		if (attackTimer > attackInterval) {
			attackTimer = 0;
			return true;
		} else {
			return false;
		}
	}


	public void setShotType(EnemyShotType shotType) {
		this.shotType = shotType;
	}


	public void damage(float damage) {
		currentHealth -= damage;
		if (currentHealth <= 0) {
			kill();
		}
	}


	public void kill() {
		dead = true;
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


	public void calculateScoreOnDeath() {
		scoreOnDeath = (attackType.getScoreMultiplier() * 10) + (movementType.getScoreMultiplier() * 10);
	}


	/**
	 * Movement methods
	 */
	public float getXVelocity() {
		return velocity.x;
	}


	public void setXVelocity(float xVelocity) {
		velocity.x = xVelocity;
	}


	public float getYVelocity() {
		return velocity.y;
	}


	public void setYVelocity(float yVelocity) {
		velocity.y = yVelocity;
	}


	public float getDefaultVelocity() {
		return defaultVelocity;
	}


	public void setDefaultVelocity(float defaultVelocity) {
		this.defaultVelocity = defaultVelocity;
	}


	public void setMovementType(MovementType movementType) {
		this.movementType = movementType;
		calculateScoreOnDeath();
	}


	public MovementType getMovementType() {
		return movementType;
	}


	public void movement(PlayerCharacter player) {
		switch (movementType) {
		case STATIC:
			setXVelocity(0);
			setYVelocity(0);
			break;

		case RANDOM:
			if (directionCounter > 0.3) {
				directionCounter = 0;

				if (random.nextBoolean()) {
					velocity.rotateRad(random.nextFloat());
				} else {
					velocity.rotateRad(-random.nextFloat());
				}
			} else {
				directionCounter += Gdx.graphics.getDeltaTime();
			}
			updateVelocities();
			break;

		case FOLLOW:
			if (getDistanceTo(player) < viewDistance) {
				pointTo(player);
				updateVelocities();
			} else {
				setXVelocity(0);
				setYVelocity(0);
			}
			break;
		}

		getPosition().mulAdd(velocity, Gdx.graphics.getDeltaTime());
		updateBoxesPosition();
	}


	public void pointTo(Collidable collidable) {
		float len2 = velocity.len2();
		velocity.set(collidable.getPosition()).sub(getPosition()).setLength2(len2);
	}


	public float getDistanceTo(Collidable collidable) {
		float xDist = (collidable.getX() - getX());
		float yDist = (collidable.getY() - getY());
		return (float) Math.sqrt((xDist * xDist) + (yDist * yDist));
	}


	public void updateVelocities() {
		velocity.setLength(defaultVelocity);
	}


	@Override
	public Sprite getSprite() {
		return super.getSprite();
	}


	@Override
	public void setSprite(Sprite sprite) {
		super.setSprite(sprite);
	}


	@Override
	public float getX() {
		return super.getX();
	}


	@Override
	public void setX(float x) {
		super.setX(x);
	}


	@Override
	public float getY() {
		return super.getY();
	}


	@Override
	public void setY(float y) {
		super.setY(y);
	}
}
