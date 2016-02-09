package com.muscovy.game.entity;


import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;


public abstract class MoveableEntity extends Collidable {
	public static final float WORLD_FRICTION = 0.8f;
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


	public final void update(float deltaTime) {
		selfUpdate(deltaTime);
		movementLogic(deltaTime);
		moveEntity(deltaTime);
	}


	public abstract void selfUpdate(float deltaTime);


	public abstract void movementLogic(float deltaTime);


	public void moveEntity(float deltaTime) {
		velocity.mulAdd(acceleration, deltaTime);
		velocity.limit(maxSpeed);
		getPosition().mulAdd(velocity, deltaTime);
		updateBoxesPosition();
		velocity.scl(friction);
		acceleration.setZero();
	}


	public Vector2 getAcceleration() {
		return acceleration;
	}


	public void addAcceleration(Vector2 a) {
		acceleration.add(a);
	}


	public void addMovementAcceleration(Vector2 direction) {
		acceleration.mulAdd(direction, accelerationSpeed);
	}


	public float getAccelerationSpeed() {
		return accelerationSpeed;
	}


	public void setAccelerationSpeed(float accelerationSpeed) {
		this.accelerationSpeed = accelerationSpeed;
	}


	public float getFriction() {
		return friction;
	}


	public void setFriction(float friction) {
		this.friction = friction;
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


	public Vector2 getVelocity() {
		return velocity;
	}


	public void setVelocityToZero() {
		velocity.setZero();
	}


	public float getMaxSpeed() {
		return maxSpeed;
	}


	public void setMaxSpeed(float speed) {
		maxSpeed = speed;
	}
}
