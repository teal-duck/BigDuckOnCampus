package com.muscovy.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;


/**
 * Created by SeldomBucket on 05-Dec-15.
 */
public class Enemy extends Collidable {
	private Random random;

	private boolean collidingWithSomething;

	private int attackType; // 0 = touch damage, 1 = ranged attack, 2 = both
	private float touchDamage;
	private float currentHealth = 40;
	private boolean dead = false;
	private int scoreOnDeath = 0;

	private ArrayList<Projectile> rangedAttack;
	private int shotType = 1; // 0 = single shot in direction of movement, 1 = shoot towards player if in range, 2 =
					// double shot towards player if in range, 3 = triple shot towards player if in
					// range, 4 = random shot direction
	private float attackTimer, attackInterval = 1.5f; // MuscovyGame.java checks these and does an attack if attack
								// timer is greater than attack interval.
	private float projectileRange = 400, projectileVelocity = 150,
			projectileLife = projectileRange / projectileVelocity;
	private float attackRange = 480;

	private int movementType; // 0 = static, 1 = random movement, 2 = following
	private float movementRange = 480;
	private float xVelocity = 0, yVelocity = 0, defaultVelocity = 200; //, maxVelocity = 200;
	private float direction; // 0 to 2*pi, 0 being vertically upwards
	float directionCounter = 0;

	private float upperXBounds = 1280 - 32, upperYBounds = 720 - 128, /*lowerYBounds = 32, lowerXBounds = 32,*/
			spriteWidth, spriteHeight;


	public Enemy(Sprite sprite) {
		random = new Random();
		spriteWidth = sprite.getRegionWidth();
		spriteHeight = sprite.getRegionHeight();
		rangedAttack = new ArrayList<Projectile>();
		setSprite(sprite);
		touchDamage = 10.0f;
		movementType = 0;
		attackType = 0;
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


	public int getAttackType() {
		return attackType;
	}


	public void setAttackType(int attackType) {
		this.attackType = attackType;
		scoreOnDeath = (attackType * 10) + (movementType * 10);
	}


	public ArrayList<Projectile> rangedAttack(PlayerCharacter playerCharacter) {
		float x = (getX() + (getWidth() / 2));
		float y = (getY() + (getHeight() / 2));
		float dist = getDistanceTo(playerCharacter);
		switch (shotType) {
		case 0:
			rangedAttack.add(new Projectile(x, y, direction, projectileRange, projectileVelocity, xVelocity,
					yVelocity, 0));
			break;
		case 1:
			if (dist < attackRange) {
				rangedAttack.add(new Projectile(x, y, getAngleTo(playerCharacter), projectileRange,
						projectileVelocity, xVelocity, yVelocity, 0));
			}
			break;
		case 2:
			if (dist < attackRange) {
				rangedAttack.add(new Projectile(x, y,
						(float) (getAngleTo(playerCharacter) - (Math.PI / 24)), projectileRange,
						projectileVelocity, xVelocity, yVelocity, 0));
				rangedAttack.add(new Projectile(x, y,
						(float) (getAngleTo(playerCharacter) + (Math.PI / 24)), projectileRange,
						projectileVelocity, xVelocity, yVelocity, 0));
			}
			break;
		case 3:
			if (dist < attackRange) {
				rangedAttack.add(new Projectile(x, y,
						(float) (getAngleTo(playerCharacter) - (Math.PI / 12)), projectileRange,
						projectileVelocity, xVelocity, yVelocity, 0));
				rangedAttack.add(new Projectile(x, y,
						(float) (getAngleTo(playerCharacter) + (Math.PI / 12)), projectileRange,
						projectileVelocity, xVelocity, yVelocity, 0));
				rangedAttack.add(new Projectile(x, y, getAngleTo(playerCharacter), projectileRange,
						projectileVelocity, xVelocity, yVelocity, 0));
			}
			break;
		case 4:
			rangedAttack.add(new Projectile(x, y, (float) (random.nextFloat() * Math.PI * 2),
					projectileRange, projectileVelocity, xVelocity, yVelocity, 0));
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


	public void setShotType(int shotType) {
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
		scoreOnDeath = (attackType * 10) + (movementType * 10);
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


	public void setMovementType(int movementType) {
		this.movementType = movementType;
	}


	public int getMovementType() {
		return movementType;
	}


//	public void setMaxVelocity(float maxVelocity) {
//		this.maxVelocity = maxVelocity;
//	}
//
//
//	public void resetMaxVelocity() {
//		maxVelocity = defaultVelocity;
//	}


	public void movement(PlayerCharacter player) {
		switch (movementType) {
		case 0:
			setXVelocity(0);
			setYVelocity(0);
			break;
		case 1:
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
		case 2:

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
		float xdist = (collidable.getX() - getX());
		float ydist = (collidable.getY() - getY());
		return (float) Math.sqrt((xdist * xdist) + (ydist * ydist));
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
