package com.muscovy.game;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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
	private Vector2 velocity;
	private float maxVelocity = 350;
	private float defaultVelocity = 350;

	private float accel = maxVelocity * 6;
	private float decel = maxVelocity * 5.5f;

	private Vector2 shotDirection;

	private ArrayList<Texture> downWalkCycle;
	private ArrayList<Texture> leftWalkCycle;
	private ArrayList<Texture> rightWalkCycle;
	private ArrayList<Texture> upWalkCycle;

	private int animationCycle;
	private int animationCounter;

	private PlayerShotType shotType = PlayerShotType.SINGLE;

	// MuscovyGame.java checks these and does an attack if attack timer is greater than attack interval.
	private float attackInterval = 0.25f;
	private float timeSinceLastAttack = attackInterval;

	private float projectileVelocity = 450;
	private float projectileRange = 600;
	private float projectileLife = projectileRange / projectileVelocity;

	private float currentHealth = 100;
	private float maxHealth = 100;
	private boolean invincible = false;
	private float invincibilityCounter = 0;

	private int score = 0;

	private TextureMap textureMap;


	public PlayerCharacter(TextureMap textureMap) {
		this.textureMap = textureMap;

		animationCycle = 0;
		velocity = new Vector2(0, 0);
		shotDirection = new Vector2(0, 1);

		Sprite playerSprite;
		downWalkCycle = new ArrayList<Texture>();
		upWalkCycle = new ArrayList<Texture>();
		rightWalkCycle = new ArrayList<Texture>();
		leftWalkCycle = new ArrayList<Texture>();
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

		playerSprite = new Sprite();
		playerSprite.setRegion(textureMap.getTextureOrLoadFile("duck.png"));
		setSprite(playerSprite);
		initialiseX(0);
		initialiseY(0);
		setUpBoxes();
		setHitboxYOffset(-6); // Just to get the hitbox in line with that fat fuck of a duck's body
		setHitboxRadius((74 / 2) - 2);
	}


	/**
	 * Getters and Setters
	 */
	public float getHealth() {
		return currentHealth;
	}


	public void setHealth(float currentHealth) {
		if (currentHealth <= maxHealth) {
			this.currentHealth = currentHealth;
		} else {
			this.currentHealth = maxHealth;
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
		// Clamps velocity to max velocity
		velocity.x = x;
		clampVelocity();
	}


	public float getYVelocity() {
		return velocity.y;
	}


	public void setYVelocity(float y) {
		// Clamps velocity to max velocity
		velocity.y = y;
		clampVelocity();
	}


	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}


	public float getMaxVelocity() {
		return maxVelocity;
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


	/**
	 * Projectile life, range and velocity work so that the range, life and shot speed are consistent with each
	 * other.
	 */
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


	public void update() {
		movement();
		if (invincible) {
			invincibilityUpdate();
		}
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}


	/**
	 * Score Methods
	 */
	public void increaseScore(int score) {
		this.score += score;
	}


	/**
	 * Health methods
	 */
	public void damage(float damage) {
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


	private void invincibilityUpdate() {
		invincibilityCounter += Gdx.graphics.getDeltaTime();
		if (invincibilityCounter > 2) {
			invincible = false;
			invincibilityCounter = 0;
		}
	}


	public boolean isInvincible() {
		return invincible;
	}


	/**
	 * Animation methods currently commented out, as we only have one sprite atm, but they should be easy to work
	 * out. Might be worth revamping later though
	 */
	public void walkCycleNext() {
		float direction = velocity.angleRad();
		int switcher = (int) (direction / (Math.PI / 2));

		// int switcher = 0;
		// if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
		// if (velocity.x > 0) {
		// switcher = 1;
		// } else {
		// switcher = 3;
		// }
		// } else {
		// if (velocity.y > 0) {
		// switcher = 0;
		// } else {
		// switcher = 2;
		// }
		// }

		switch (switcher) {
		case 0:
			setTexture(upWalkCycle.get(animationCycle));
			if (animationCycle == 6) {
				animationCycle = 0;
			} else {
				animationCycle++;
			}
			break;
		case 1:
			setTexture(rightWalkCycle.get(animationCycle));
			if (animationCycle == 10) {
				animationCycle = 2;
			} else {
				animationCycle++;
			}
			break;
		case 2:
			setTexture(downWalkCycle.get(animationCycle));
			if (animationCycle == 6) {
				animationCycle = 0;
			} else {
				animationCycle++;
			}
			break;
		case 3:
			setTexture(leftWalkCycle.get(animationCycle));
			if (animationCycle == 10) {
				animationCycle = 2;
			} else {
				animationCycle++;
			}
			break;
		}
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
		// this.setTexture(downWalkCycle.get(0));
		// animationCycle = 0;
		// }
	}


	/**
	 * Movement methods. Called when the gamestate is 2 and the listener hears W A S or D If opposite directions are
	 * pressed at the same time, velocity decelerated to 0 Calculates velocity based on delta time and acceleration
	 */
	// TODO: Why do only right and up apply deceleration?
	public void goRight() {
		if (animationCycle > 10) {
			animationCycle = 0;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.A)) {
			decelXToStop();
		} else {
			changeXVelocity(accel * Gdx.graphics.getDeltaTime());
		}
	}


	public void goLeft() {
		if (animationCycle > 10) {
			animationCycle = 0;
		}

		changeXVelocity((-accel) * Gdx.graphics.getDeltaTime());
	}


	public void goUp() {
		if (animationCycle > 6) {
			animationCycle = 0;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.S)) {
			decelYToStop();
		} else {
			changeYVelocity(accel * Gdx.graphics.getDeltaTime());
		}
	}


	public void goDown() {
		if (animationCycle > 6) {
			animationCycle = 0;
		}

		changeYVelocity((-accel) * Gdx.graphics.getDeltaTime());
	}


	public void decelXToStop() {
		if (getXVelocity() > 0) {
			if ((getXVelocity() - (decel * Gdx.graphics.getDeltaTime())) < 0) {
				setXVelocity(0);
			} else {
				changeXVelocity(-decel * Gdx.graphics.getDeltaTime());
			}
		}

		if (getXVelocity() < 0) {
			if ((getXVelocity() + (decel * Gdx.graphics.getDeltaTime())) > 0) {
				setXVelocity(0);
			} else {
				changeXVelocity(decel * Gdx.graphics.getDeltaTime());
			}
		}

		idleAnimation();
	}


	public void decelYToStop() {
		if (getYVelocity() > 0) {
			if ((getYVelocity() - (decel * Gdx.graphics.getDeltaTime())) < 0) {
				setYVelocity(0);
			} else {
				changeYVelocity(-decel * Gdx.graphics.getDeltaTime());
			}
		}

		if (getYVelocity() < 0) {
			if ((getYVelocity() + (decel * Gdx.graphics.getDeltaTime())) > 0) {
				setYVelocity(0);
			} else {
				changeYVelocity(decel * Gdx.graphics.getDeltaTime());
			}
		}

		idleAnimation();
	}


	public void movement() {
		/**
		 * Changes X and Y according to velocity and time elapsed between frames
		 */
		getPosition().mulAdd(velocity, Gdx.graphics.getDeltaTime());
		updateBoxesPosition();
	}


	public void changeXVelocity(float x) {
		velocity.x += x;
		clampVelocity();
	}


	public void changeYVelocity(float y) {
		velocity.y += y;
		clampVelocity();
	}


	// TODO: Use velocity.limit(maxVelocity) instead of setting components
	// This is mathematically correct, but changes game play
	private void clampVelocity() {
		if (velocity.x > maxVelocity) {
			velocity.x = maxVelocity;
		}

		if (velocity.x < -maxVelocity) {
			velocity.x = -maxVelocity;
		}

		if (velocity.y > maxVelocity) {
			velocity.y = maxVelocity;
		}

		if (velocity.y < -maxVelocity) {
			velocity.y = -maxVelocity;
		}
	}


	public void resetMaxVelocity() {
		maxVelocity = defaultVelocity;
	}


	/**
	 * Attack methods (only shots currently)
	 */
	public boolean checkRangedAttack() {
		if (timeSinceLastAttack > attackInterval) {
			timeSinceLastAttack = 0;
			return true;
		} else {
			incrementTimeSinceLastAttack();
			return false;
		}
	}


	public void incrementTimeSinceLastAttack() {
		timeSinceLastAttack += Gdx.graphics.getDeltaTime();
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
		float y = ((getY() + getHeight()) - 32);

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

		return Projectile.shootProjectiles(textureMap, count, position, direction, projectileLife,
				projectileVelocity, ProjectileDamager.ENEMY);
	}
}
