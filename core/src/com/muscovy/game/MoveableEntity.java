package com.muscovy.game;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;


public abstract class MoveableEntity extends Collidable {
	private Vector2 velocity;
	private float maxSpeed = Enemy.MAX_SPEED;
	private float currentSpeed = 0;


	public MoveableEntity(Sprite sprite, Vector2 position) {
		super(sprite, position);
		velocity = new Vector2(0, 0);
	}


	public final void update(float deltaTime) {
		selfUpdate(deltaTime);
		movementLogic(deltaTime);
		moveEntity(deltaTime);
	}


	public abstract void selfUpdate(float deltaTime);


	public abstract void movementLogic(float deltaTime);


	public void setVelocityLengthToCurrentSpeed() {
		velocity.setLength(currentSpeed);
	}


	public void moveEntity(float deltaTime) {
		setVelocityLengthToCurrentSpeed();
		getPosition().mulAdd(velocity, deltaTime);
		updateBoxesPosition();
	}


	// public void clampVelocity() {
	// velocity.limit(maxSpeed);
	// }

	public void incrementVelocityX(float dx) {
		velocity.x += dx;
		// clampVelocity();
	}


	public void incrementVelocityY(float dy) {
		velocity.y += dy;
		// clampVelocity();
	}


	public void incrementVelocity(float dx, float dy) {
		velocity.add(dx, dy);
	}


	public void incrementVelocity(Vector2 dd) {
		velocity.add(dd);
	}


	public float getVelocityX() {
		return velocity.x;
	}


	public void setVelocityX(float velocityX) {
		velocity.x = velocityX;
		// clampVelocity();
	}


	public float getVelocityY() {
		return velocity.y;
	}


	public void setVelocityY(float velocityY) {
		velocity.y = velocityY;
		// clampVelocity();
	}


	public Vector2 getVelocity() {
		return velocity;
	}


	public void setVelocity(Vector2 velocity) {
		this.velocity.set(velocity);
		// clampVelocity();
	}


	public void setVelocity(float x, float y) {
		velocity.set(x, y);
		// clampVelocity();
	}


	public float getMaxSpeed() {
		return maxSpeed;
	}


	public void setMaxSpeed(float speed) {
		maxSpeed = speed;
		if (currentSpeed > maxSpeed) {
			currentSpeed = maxSpeed;
		}
		// clampVelocity();
	}


	public float getCurrentSpeed() {
		return currentSpeed;
	}


	public void setCurrentSpeed(float speed) {
		currentSpeed = speed;
		if (currentSpeed > maxSpeed) {
			currentSpeed = maxSpeed;
		}
		// clampVelocity();
	}


	public void setSpeedToMax() {
		currentSpeed = maxSpeed;
	}
}
