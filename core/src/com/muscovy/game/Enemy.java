package com.muscovy.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

	private AttackType attackType; // 0 = touch damage, 1 = ranged attack, 2 = both
	private float touchDamage;
	private float currentHealth = 40;
	private boolean dead = false;
	private int scoreOnDeath = 0;

	private ArrayList<Projectile> rangedAttack;
	private EnemyShotType shotType = EnemyShotType.SINGLE_TOWARDS_PLAYER; // 0 = single shot in direction of
										// movement, 1 = shoot
	// towards
	// player if in range, 2 =
	// double shot towards player if in range, 3 = triple shot towards player if in
	// range, 4 = random shot direction
	// MuscovyGame.java checks these and does an attack if attack
	// timer is greater than attack interval.
	private float attackTimer;
	private float attackInterval = 1.5f;

	private float projectileRange = 400;
	private float projectileVelocity = 150;
	private float projectileLife = projectileRange / projectileVelocity;
	private float attackRange = 480;

	private MovementType movementType; // 0 = static, 1 = random movement, 2 = following
	private float movementRange = 480;
	// TODO: Use vectors for positions, velocities and directions
	private float xVelocity = 0;
	private float yVelocity = 0;
	private float defaultVelocity = 200;
	// private float maxVelocity = 200;
	private float direction; // 0 to 2*pi, 0 being vertically upwards
	private float directionCounter = 0;

	private float upperXBounds = 1280 - 32;
	private float upperYBounds = 720 - 128;
	// private float lowerYBounds = 32;
	// private float lowerXBounds = 32;
	private float spriteWidth;
	private float spriteHeight;


	public Enemy(Sprite sprite) {
		random = new Random();
		spriteWidth = sprite.getRegionWidth();
		spriteHeight = sprite.getRegionHeight();
		rangedAttack = new ArrayList<Projectile>();
		setSprite(sprite);
		touchDamage = 10.0f;
		movementType = MovementType.STATIC;
		attackType = AttackType.TOUCH;
		upperXBounds = upperXBounds - spriteWidth;
		upperYBounds = upperYBounds - spriteHeight;
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
		// scoreOnDeath = (attackType.getScoreMultiplier() * 10) + (movementType.getScoreMultiplier() * 10);
		calculateScoreOnDeath();
	}


	public ArrayList<Projectile> rangedAttack(PlayerCharacter playerCharacter) {
		float x = (getX() + (getWidth() / 2));
		float y = (getY() + (getHeight() / 2));
		float dist = getDistanceTo(playerCharacter);
		switch (shotType) {
		case SINGLE_MOVEMENT:
			rangedAttack.add(new Projectile(x, y, direction, projectileRange, projectileVelocity, xVelocity,
					yVelocity, ProjectileDamager.PLAYER));
			break;
		case SINGLE_TOWARDS_PLAYER:
			if (dist < attackRange) {
				rangedAttack.add(new Projectile(x, y, getAngleTo(playerCharacter), projectileRange,
						projectileVelocity, xVelocity, yVelocity, ProjectileDamager.PLAYER));
			}
			break;
		case DOUBLE_TOWARDS_PLAYER:
			if (dist < attackRange) {
				rangedAttack.add(new Projectile(x, y,
						(float) (getAngleTo(playerCharacter) - (Math.PI / 24)), projectileRange,
						projectileVelocity, xVelocity, yVelocity, ProjectileDamager.PLAYER));
				rangedAttack.add(new Projectile(x, y,
						(float) (getAngleTo(playerCharacter) + (Math.PI / 24)), projectileRange,
						projectileVelocity, xVelocity, yVelocity, ProjectileDamager.PLAYER));
			}
			break;
		case TRIPLE_TOWARDS_PLAYER:
			if (dist < attackRange) {
				rangedAttack.add(new Projectile(x, y,
						(float) (getAngleTo(playerCharacter) - (Math.PI / 12)), projectileRange,
						projectileVelocity, xVelocity, yVelocity, ProjectileDamager.PLAYER));
				rangedAttack.add(new Projectile(x, y,
						(float) (getAngleTo(playerCharacter) + (Math.PI / 12)), projectileRange,
						projectileVelocity, xVelocity, yVelocity, ProjectileDamager.PLAYER));
				rangedAttack.add(new Projectile(x, y, getAngleTo(playerCharacter), projectileRange,
						projectileVelocity, xVelocity, yVelocity, ProjectileDamager.PLAYER));
			}
			break;
		case RANDOM_DIRECTION:
			rangedAttack.add(new Projectile(x, y, (float) (random.nextFloat() * Math.PI * 2),
					projectileRange, projectileVelocity, xVelocity, yVelocity,
					ProjectileDamager.PLAYER));
			break;
		}
		return rangedAttack;
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
		return movementRange;
	}


	public void setMovementRange(float movementRange) {
		this.movementRange = movementRange;
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
		return xVelocity;
	}


	public void setXVelocity(float xVelocity) {
		this.xVelocity = xVelocity;
	}


	public float getYVelocity() {
		return yVelocity;
	}


	public void setYVelocity(float yVelocity) {
		this.yVelocity = yVelocity;
	}


	public float getDefaultVelocity() {
		return defaultVelocity;
	}


	public void setDefaultVelocity(float defaultVelocity) {
		this.defaultVelocity = defaultVelocity;
	}


	public void setMovementType(MovementType movementType) {
		this.movementType = movementType;
		// TODO: Call calculate score on death when setting movement type?
		// calculateScoreOnDeath() ?
	}


	public MovementType getMovementType() {
		return movementType;
	}


	// public void setMaxVelocity(float maxVelocity) {
	// this.maxVelocity = maxVelocity;
	// }
	//
	//
	// public void resetMaxVelocity() {
	// maxVelocity = defaultVelocity;
	// }

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
					direction = (float) (((direction + random.nextFloat()) % Math.PI) * 2);
				} else {
					direction = (float) (((direction - random.nextFloat()) % Math.PI) * 2);
				}
			} else {
				directionCounter += Gdx.graphics.getDeltaTime();
			}
			updateVelocities();
			break;

		case FOLLOW:
			if (getDistanceTo(player) < movementRange) {
				pointTo(player);
				updateVelocities();
			} else {
				setXVelocity(0);
				setYVelocity(0);
			}
			break;
		}

		setX(getX() + (xVelocity * Gdx.graphics.getDeltaTime()));
		setY(getY() + (yVelocity * Gdx.graphics.getDeltaTime()));
	}


	// TODO: There's a lot of duplicate of angle code
	// Need to use atan2() and/or vectors
	public void pointTo(Collidable collidable) {
		float x = ((collidable.getX() + (collidable.getCircleHitbox().radius / 2))
				- (getX() + (getWidth() / 2)));
		float y = ((collidable.getY() + (collidable.getCircleHitbox().radius / 2))
				- (getY() + (getHeight() / 2)));
		float angle = (float) Math.atan(x / y);
		if (x >= 0) {
			if (y >= 0) {
				direction = angle;
			} else if (y < 0) {
				direction = (float) (Math.PI + angle);
			}
		} else if (x < 0) {
			if (y >= 0) {
				direction = (float) ((2 * Math.PI) + angle);
			} else if (y < 0) {
				direction = (float) (Math.PI + angle);
			}
		}
	}


	public float getDistanceTo(Collidable collidable) {
		float xDist = (collidable.getX() - getX());
		float yDist = (collidable.getY() - getY());
		return (float) Math.sqrt((xDist * xDist) + (yDist * yDist));
	}


	public void updateVelocities() {
		xVelocity = (float) (defaultVelocity * Math.sin(direction));
		yVelocity = (float) (defaultVelocity * Math.cos(direction));
	}


	public float getDirection() {
		return direction;
	}


	public void setDirection(float direction) {
		this.direction = direction;
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
