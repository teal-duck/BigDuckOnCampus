package com.muscovy.game.entity;


import java.util.ArrayList;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.enums.PlayerShotType;
import com.muscovy.game.enums.ProjectileDamager;
import com.muscovy.game.enums.ProjectileType;
import com.muscovy.game.input.Action;
import com.muscovy.game.input.ControlMap;


/**
 * Created by ewh502 on 04/12/2015. Good luck
 */
public class PlayerCharacter extends MoveableEntity {
	
	// TODO: Possibly make a MoveableEntity class with these velocity parameters?
	// Player and enemy extend MoveableEntity
	public static final float MAX_SPEED = 350f;
	public static final float ACCELERATION = PlayerCharacter.MAX_SPEED * 6;
	public static final float DECELERATION = PlayerCharacter.MAX_SPEED * 5.5f;
	public static final float FLIGHT_SPEED = 3f;
	public static final float BASE_ATTACK_INTERVAL = 0.25f;
	public static final float PROJECTILE_SPEED = 450;
	public static final float PROJECTILE_RANGE = 600;
	public static final int MAX_HEALTH = 100;
	public static final float INVINCIBILITY_DURATION = 2;

	public static final float HITBOX_Y_OFFSET = -6;
	public static final float HITBOX_RADIUS = (74 / 2) - 8; // (74 / 2) - 2;
	public static final float MAX_FLIGHT_TIME = 0.25f;

	private Vector2 shotDirection;
	private PlayerShotType shotType = PlayerShotType.SINGLE;

	private float attackInterval = PlayerCharacter.BASE_ATTACK_INTERVAL;

	private float timeSinceLastAttack = attackInterval;

	private float projectileSpeed = PlayerCharacter.PROJECTILE_SPEED;
	private float projectileRange = PlayerCharacter.PROJECTILE_RANGE;
	private float projectileLife = projectileRange / projectileSpeed;

	private int maxHealth = PlayerCharacter.MAX_HEALTH;
	private int currentHealth = maxHealth;
	private boolean invincible = false;
	private float invincibilityCounter = 0;
	private float invincibilityDuration = PlayerCharacter.INVINCIBILITY_DURATION;

	private int score = 0;

	private int animationCycle;
	private int animationCounter;
	// private ArrayList<Texture> downWalkCycle;
	// private ArrayList<Texture> leftWalkCycle;
	// private ArrayList<Texture> rightWalkCycle;
	// private ArrayList<Texture> upWalkCycle;

	private ControlMap controlMap;
	private Controller controller;

	private boolean firing = false;
	private float flightTime = 0;


	public PlayerCharacter(MuscovyGame game, String textureName, Vector2 position, ControlMap controlMap,
			Controller controller) {
		super(game, textureName, position);
		this.controlMap = controlMap;
		this.controller = controller;

		setMaxSpeed(PlayerCharacter.MAX_SPEED);
		setAccelerationSpeed(MoveableEntity.PLAYER_ACCELERATION_SPEED);

		animationCycle = 0;
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

		setHitboxYOffset(PlayerCharacter.HITBOX_Y_OFFSET); // Just to get the hitbox in line with that fat fuck
									// of a duck's body
		setHitboxRadius(PlayerCharacter.HITBOX_RADIUS);
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


	@Override
	public void movementLogic(float deltaTime) {
		
		// start a counter when the player presses 'fly'
		// TODO: keep track of some sort of stamina bar to prevent permanent 'flight'?
		if (flightTime <= 0 && new Float(controlMap.getStateForAction(Action.FLY, controller)).equals(1f)) {
			flightTime = MAX_FLIGHT_TIME;
		}
		
		float rightState = controlMap.getStateForAction(Action.WALK_RIGHT, controller);
		float leftState = controlMap.getStateForAction(Action.WALK_LEFT, controller);
		float upState = controlMap.getStateForAction(Action.WALK_UP, controller);
		float downState = controlMap.getStateForAction(Action.WALK_DOWN, controller);

		float dx = rightState - leftState;
		float dy = upState - downState;

		Vector2 acceleration = new Vector2(dx, dy);
		acceleration.limit(1);
		
		// increase speed during flight
		if (flightTime > 0) {
			acceleration.scl(FLIGHT_SPEED);
			setMaxSpeed(MAX_SPEED * FLIGHT_SPEED);
			flightTime -= deltaTime;
		}
		addMovementAcceleration(acceleration);

		float shootRightState = controlMap.getStateForAction(Action.SHOOT_RIGHT, controller);
		float shootLeftState = controlMap.getStateForAction(Action.SHOOT_LEFT, controller);
		float shootUpState = controlMap.getStateForAction(Action.SHOOT_UP, controller);
		float shootDownState = controlMap.getStateForAction(Action.SHOOT_DOWN, controller);

		float shootDX = shootRightState - shootLeftState;
		float shootDY = shootUpState - shootDownState;

		int sx = 0;
		int sy = 0;
		
		if (Math.abs(shootDX) > Math.abs(shootDY)) {
			sx = (int) Math.signum(shootDX);
			sy = 0;
		} else {
			sx = 0;
			sy = (int) Math.signum(shootDY);
		}

		if ((sx != 0) || (sy != 0)) {
			setFiring(true);
			setShotDirection(sx, sy);
		} else {
			setFiring(false);
		}
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
		float x = (getX() + (getWidth() / 2)) - 8;
		// TODO: This should get player's height, not tile size
		float y = getY() + (getHeight() / 2);

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
				ProjectileDamager.ENEMY, ProjectileType.STANDARD);
	}


	public boolean isFiring() {
		return firing;
	}


	public void setFiring(boolean firing) {
		this.firing = firing;
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


	public boolean gainHealth(int health) {
		if (currentHealth == maxHealth) {
			return false;
		} else {
			currentHealth = Math.min(currentHealth + health, maxHealth);
			return true;
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


	public int getHealth() {
		return currentHealth;
	}


	public void setHealth(int newHealth) {
		currentHealth = newHealth;
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}


	public float getMaxHealth() {
		return maxHealth;
	}


	public void setMaxHealth(int maxHealth) {
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


	public void setShotType(PlayerShotType type) {
		shotType = type;
	}


	public float getAttackInterval() {
		return attackInterval;
	}


	public void setAttackInterval(float attackInterval) {
		this.attackInterval = attackInterval;
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
