package com.muscovy.game.entity;


import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;


/**
 *
 */
public abstract class MoveableEntity extends Collidable {
	public static final float WORLD_FRICTION = 48;
	public static final float WATER_FRICTION = 58;
	public static final float PLAYER_ACCELERATION_SPEED = 5000;
	public static final float ENEMY_ACCELERATION_SPEED = (MoveableEntity.PLAYER_ACCELERATION_SPEED * 2) / 3;

	private float maxSpeed = Enemy.MAX_SPEED;
	private float friction = MoveableEntity.WORLD_FRICTION;
	private float accelerationSpeed = 0;

	private Vector2 acceleration;
	private Vector2 velocity;


	public MoveableEntity(MuscovyGame game, String textureName) {
		this(game, textureName, new Vector2(0, 0));
	}


	public MoveableEntity(MuscovyGame game, String textureName, Vector2 position) {
		this(game, textureName, position, new Vector2(0, 0));
	}


	public MoveableEntity(MuscovyGame game, String textureName, Vector2 position, Vector2 velocity) {
		super(game, textureName, position);
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
		velocity.limit(maxSpeed);
		getPosition().mulAdd(velocity, deltaTime);
		updateBoxesPosition();
		velocity.scl(friction * deltaTime);
		acceleration.setZero();
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
	 * @return
	 */
	public float getMaxSpeed() {
		return maxSpeed;
	}


	/**
	 * @param speed
	 */
	public void setMaxSpeed(float speed) {
		maxSpeed = speed;
	}
}
