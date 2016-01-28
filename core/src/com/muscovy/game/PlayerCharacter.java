package com.muscovy.game;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.enums.PlayerShotType;
import com.muscovy.game.enums.ProjectileDamager;


/**
 * Created by ewh502 on 04/12/2015. Good luck
 */
public class PlayerCharacter extends Collidable {
	// TODO: Possibly make a MoveableEntity class with these velocity parameters?
	// Player and enemy extend MoveableEntity
	public static final float MAX_SPEED = 350f;
	public static final float ACCELERATION = PlayerCharacter.MAX_SPEED * 6;
	public static final float DECELERATION = PlayerCharacter.MAX_SPEED * 5.5f;
	public static final float ATTACK_INTERVAL = 0.25f;
	public static final float PROJECTILE_SPEED = 450;
	public static final float PROJECTILE_RANGE = 600;
	public static final float MAX_HEALTH = 100;
	public static final float INVINCIBILITY_DURATION = 2;

	private Vector2 velocity;
	private float maxSpeed = PlayerCharacter.MAX_SPEED;
	private float currentSpeed = maxSpeed;

	private float acceleration = PlayerCharacter.ACCELERATION;
	private float deceleration = PlayerCharacter.DECELERATION;

	private Vector2 shotDirection;
	private PlayerShotType shotType = PlayerShotType.SINGLE;

	private float attackInterval = PlayerCharacter.ATTACK_INTERVAL;
	private float timeSinceLastAttack = attackInterval;

	private float projectileSpeed = PlayerCharacter.PROJECTILE_SPEED;
	private float projectileRange = PlayerCharacter.PROJECTILE_RANGE;
	private float projectileLife = projectileRange / projectileSpeed;

	private float maxHealth = PlayerCharacter.MAX_HEALTH;
	private float currentHealth = maxHealth;
	private boolean invincible = false;
	private float invincibilityCounter = 0;
	private float invincibilityDuration = PlayerCharacter.INVINCIBILITY_DURATION;

	private int score = 0;

	private TextureMap textureMap;

	private int animationCycle;
	private int animationCounter;
	// private ArrayList<Texture> downWalkCycle;
	// private ArrayList<Texture> leftWalkCycle;
	// private ArrayList<Texture> rightWalkCycle;
	// private ArrayList<Texture> upWalkCycle;


	public PlayerCharacter(Sprite playerSprite, Vector2 position, TextureMap textureMap) {
		super(playerSprite, position);
		this.textureMap = textureMap;

		animationCycle = 0;
		velocity = new Vector2(0, 0);
		shotDirection = new Vector2(0, 1);

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

		// playerSprite = new Sprite();
		// playerSprite.setRegion(textureMap.getTextureOrLoadFile("duck.png"));
		// setSprite(playerSprite);
		setHitboxYOffset(-6); // Just to get the hitbox in line with that fat fuck of a duck's body
		setHitboxRadius((74 / 2) - 2);
	}


	public void update(float deltaTime) {
		// movementLogic(deltaTime);
		movementLogic(deltaTime);
		moveEntity(deltaTime);

		if (invincible) {
			invincibilityUpdate(deltaTime);
		}
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}


	public void movementLogic(float deltaTime) {
	}


	/**
	 * Changes X and Y according to velocity and time elapsed between frames
	 *
	 */
	public void moveEntity(float deltaTime) {
		getPosition().mulAdd(velocity, deltaTime);
		updateBoxesPosition();
	}


	/**
	 * Movement methods. Called when the gamestate is 2 and the listener hears W A S or D If opposite directions are
	 * pressed at the same time, velocity decelerated to 0 Calculates velocity based on delta time and acceleration
	 */
	// TODO: Why do only right and up apply deceleration?
	public void goRight(float deltaTime) {
		if (animationCycle > 10) {
			animationCycle = 0;
		}

		if (Gdx.input.isKeyPressed(MuscovyGame.KEY_RIGHT) && Gdx.input.isKeyPressed(MuscovyGame.KEY_LEFT)) {
			decelXToStop(deltaTime);
		} else {
			changeXVelocity(acceleration * deltaTime);
		}
	}


	public void goLeft(float deltaTime) {
		if (animationCycle > 10) {
			animationCycle = 0;
		}

		changeXVelocity((-acceleration) * deltaTime);
	}


	public void goUp(float deltaTime) {
		if (animationCycle > 6) {
			animationCycle = 0;
		}

		if (Gdx.input.isKeyPressed(MuscovyGame.KEY_UP) && Gdx.input.isKeyPressed(MuscovyGame.KEY_DOWN)) {
			decelYToStop(deltaTime);
		} else {
			changeYVelocity(acceleration * deltaTime);
		}
	}


	public void goDown(float deltaTime) {
		if (animationCycle > 6) {
			animationCycle = 0;
		}

		changeYVelocity((-acceleration) * deltaTime);
	}


	public void decelXToStop(float deltaTime) {
		if (getXVelocity() > 0) {
			if ((getXVelocity() - (deceleration * deltaTime)) < 0) {
				setXVelocity(0);
			} else {
				changeXVelocity(-deceleration * deltaTime);
			}
		}

		if (getXVelocity() < 0) {
			if ((getXVelocity() + (deceleration * deltaTime)) > 0) {
				setXVelocity(0);
			} else {
				changeXVelocity(deceleration * deltaTime);
			}
		}

		idleAnimation();
	}


	public void decelYToStop(float deltaTime) {
		if (getYVelocity() > 0) {
			if ((getYVelocity() - (deceleration * deltaTime)) < 0) {
				setYVelocity(0);
			} else {
				changeYVelocity(-deceleration * deltaTime);
			}
		}

		if (getYVelocity() < 0) {
			if ((getYVelocity() + (deceleration * deltaTime)) > 0) {
				setYVelocity(0);
			} else {
				changeYVelocity(deceleration * deltaTime);
			}
		}

		idleAnimation();
	}


	public void changeXVelocity(float x) {
		velocity.x += x;
		clampVelocity();
	}


	public void changeYVelocity(float y) {
		velocity.y += y;
		clampVelocity();
	}


	private void clampVelocity() {
		velocity.limit(maxSpeed);
	}


	public void resetMaxVelocity() {
		maxSpeed = currentSpeed;
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


	public void incrementTimeSinceLastAttack(float deltaTime) {
		timeSinceLastAttack += deltaTime;
	}


	public void resetAttackTimer() {
		timeSinceLastAttack = attackInterval;
	}


	public ArrayList<Projectile> rangedAttack() {
		/**
		 * Returns a different projectile array list depending on the shot type, so that needs to be given
		 * directly to the entity manager
		 */
		float x = ((getX() + (getWidth() / 2)) - 8);
		// TODO: This should get player's height, not tile size
		float y = ((getY() + getHeight()) - (MuscovyGame.TILE_SIZE / 2));

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
		count = 3;

		return Projectile.shootProjectiles(count, position, direction, projectileLife, projectileSpeed,
				ProjectileDamager.ENEMY, textureMap);
	}


	public void increaseScore(int score) {
		this.score += score;
	}


	public void takeDamage(float damage) {
		if (!invincible) {
			currentHealth -= damage;
			invincible = true;
		}
	}


	public void gainHealth(float health) {
		currentHealth += health;
		if (currentHealth < maxHealth) {
			currentHealth = maxHealth;
		}
	}


	private void invincibilityUpdate(float deltaTime) {
		invincibilityCounter += deltaTime;
		if (invincibilityCounter > invincibilityDuration) {
			invincible = false;
			invincibilityCounter = 0;
		}
	}


	public boolean isInvincible() {
		return invincible;
	}


	public float getHealth() {
		return currentHealth;
	}


	public void setHealth(float newHealth) {
		currentHealth = newHealth;
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}


	public float getMaxHealth() {
		return maxHealth;
	}


	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}


	public float getInvincibilityCounter() {
		return invincibilityCounter;
	}


	public int getAnimationCounter() {
		return animationCounter;
	}


	public int getAnimationCycle() {
		return animationCycle;
	}


	public void setScore(int score) {
		this.score = score;
	}


	public int getScore() {
		return score;
	}


	public float getXVelocity() {
		return velocity.x;
	}


	public void setXVelocity(float x) {
		velocity.x = x;
		clampVelocity();
	}


	public float getYVelocity() {
		return velocity.y;
	}


	public void setYVelocity(float y) {
		velocity.y = y;
		clampVelocity();
	}


	public void setMaxVelocity(float maxVelocity) {
		maxSpeed = maxVelocity;
	}


	public float getMaxVelocity() {
		return maxSpeed;
	}


	public Vector2 getShotDirection() {
		return shotDirection;
	}


	public void setShotDirection(Vector2 shotDirection) {
		this.shotDirection.set(shotDirection).nor();
	}


	public void setShotDirection(float x, float y) {
		shotDirection.set(x, y).nor();
	}


	public float getTimeSinceLastAttack() {
		return timeSinceLastAttack;
	}


	public void setTimeSinceLastAttack(float timeSinceLastAttack) {
		this.timeSinceLastAttack = timeSinceLastAttack;
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


	// TODO: Animations

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
