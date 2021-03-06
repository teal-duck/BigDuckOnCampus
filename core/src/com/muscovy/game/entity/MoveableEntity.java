package com.muscovy.game.entity;


import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Various duplicated functionalities from Enemy and PlayerCharacter extracted into common ancestor. Make
 * objects move via the power of physics
 */
public abstract class MoveableEntity extends Collidable {
	public static final float WORLD_FRICTION = 48;
	public static final float WATER_FRICTION = 53;
	public static final float PLAYER_ACCELERATION_SPEED = 5000;
	public static final float ENEMY_ACCELERATION_SPEED = MoveableEntity.PLAYER_ACCELERATION_SPEED * 0.5f;
	public static final float BOSS_ACCELERATION_SPEED = MoveableEntity.PLAYER_ACCELERATION_SPEED * 0.4f;

	private float friction = MoveableEntity.WORLD_FRICTION;
	private float accelerationSpeed = 0;

	private Vector2 acceleration;
	private Vector2 velocity;


	/**
	 *
	 * @param game
	 * @param textureName
	 */
	public MoveableEntity(MuscovyGame game, String textureName) {
		this(game, textureName, new Vector2(0, 0));
	}


	/**
	 *
	 * @param game
	 * @param textureName
	 * @param position
	 */
	public MoveableEntity(MuscovyGame game, String textureName, Vector2 position) {
		this(game, textureName, position, new Vector2(0, 0));
	}


	/**
	 *
	 * @param game
	 * @param textureName
	 * @param position
	 * @param velocity
	 */
	public MoveableEntity(MuscovyGame game, String textureName, Vector2 position, Vector2 velocity) {
		this(game, textureName, position, velocity, OnscreenDrawable.DEFAULT_ENTITY_WIDTH,
				OnscreenDrawable.DEFAULT_ENTITY_HEIGHT);
	}


	/**
	 *
	 * @param game
	 * @param textureName
	 * @param position
	 * @param velocity
	 * @param width
	 * @param height
	 */
	public MoveableEntity(MuscovyGame game, String textureName, Vector2 position, Vector2 velocity, int width,
			int height) {
		super(game, textureName, position, width, height);
		this.velocity = velocity;
		acceleration = new Vector2(0, 0);
	}


	/**
	 * @param deltaTime
	 */
	public final void update(float deltaTime) {
		selfUpdate(deltaTime);
		movementLogic(deltaTime);
		moveEntity(deltaTime);
	}


	/**
	 * Non-movement related updating.
	 *
	 * @param deltaTime
	 */
	public abstract void selfUpdate(float deltaTime);


	/**
	 * Calculate the acceleration for this frame.
	 *
	 * @param deltaTime
	 */
	public abstract void movementLogic(float deltaTime);


	/**
	 * Integrates the acceleration and velocity to move the entity. Also updates the collision box and applies
	 * friction.
	 *
	 * @param deltaTime
	 */
	public void moveEntity(float deltaTime) {
		velocity.mulAdd(acceleration, deltaTime);
		getPosition().mulAdd(velocity, deltaTime);
		updateBoxesPosition();
		velocity.scl(friction * deltaTime);
		acceleration.setZero();
	}


	/**
	 * Flips the direction of the velocity (e.g. when the enemy hits an obstacle).
	 */
	public void flipDirection() {
		getVelocity().scl(-1f);
	}


	/**
	 * @return
	 */
	public Vector2 getAcceleration() {
		return acceleration;
	}


	/**
	 * @param a
	 */
	public void addAcceleration(Vector2 a) {
		acceleration.add(a);
	}


	/**
	 * Scales the direction by accelerationSpeed and adds to acceleration.
	 *
	 * @param direction
	 *                Normalised vector.
	 */
	public void addMovementAcceleration(Vector2 direction) {
		acceleration.mulAdd(direction, accelerationSpeed);
	}


	/**
	 * @return
	 */
	public float getAccelerationSpeed() {
		return accelerationSpeed;
	}


	/**
	 * @param accelerationSpeed
	 */
	public void setAccelerationSpeed(float accelerationSpeed) {
		this.accelerationSpeed = accelerationSpeed;
	}


	/**
	 * @return
	 */
	public float getFriction() {
		return friction;
	}


	/**
	 * @param friction
	 */
	public void setFriction(float friction) {
		this.friction = friction;
	}


	/**
	 * @return
	 */
	public float getVelocityX() {
		return velocity.x;
	}


	/**
	 * @param velocityX
	 */
	public void setVelocityX(float velocityX) {
		velocity.x = velocityX;
	}


	/**
	 * @return
	 */
	public float getVelocityY() {
		return velocity.y;
	}


	/**
	 * @param velocityY
	 */
	public void setVelocityY(float velocityY) {
		velocity.y = velocityY;
	}


	/**
	 * @return
	 */
	public Vector2 getVelocity() {
		return velocity;
	}


	/**
	 *
	 */
	public void setVelocityToZero() {
		velocity.setZero();
	}


	/**
	 * @return 0 = down, 1 = up, 2 = left, 3 = right
	 */
	public int getDirection() {
		int direction = 0; // 0 = down, 1 = up, 2 = left, 3 = right
		float vx = getVelocityX();
		float vy = getVelocityY();

		if (Math.abs(vy) > Math.abs(vx)) {
			if (vy > 0) {
				direction = 1; // Up
			} else {
				direction = 0; // Down
			}
		} else {
			if (vx > 0) {
				direction = 3; // Right
			} else {
				direction = 2; // Left
			}
		}
		return direction;
	}
}
