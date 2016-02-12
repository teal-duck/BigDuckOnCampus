package com.muscovy.game.entity;


import com.muscovy.game.enums.EnemyShotType;
import com.muscovy.game.enums.MovementType;
import com.muscovy.game.enums.ProjectileType;


public class BossParameters {
	private final ProjectileType projectileType;
	private final EnemyShotType enemyShotType;
	private final MovementType movementType;
	private final float attackInterval;
	private final float projectileVelocity;
	private final float projectileLife;
	private final float accelerationSpeed;
	private final float touchDamage;
	private final float health;


	public BossParameters(ProjectileType projectileType, EnemyShotType enemyShotType, MovementType movementType,
			float attackInterval, float projectileVelocity, float projectileLife, float accelerationSpeed,
			float touchDamage, float health) {
		this.projectileType = projectileType;
		this.enemyShotType = enemyShotType;
		this.movementType = movementType;
		this.attackInterval = attackInterval;
		this.projectileVelocity = projectileVelocity;
		this.projectileLife = projectileLife;
		this.accelerationSpeed = accelerationSpeed;
		this.touchDamage = touchDamage;
		this.health = health;
	}


	public ProjectileType getProjectileType() {
		return projectileType;
	}


	public EnemyShotType getEnemyShotType() {
		return enemyShotType;
	}


	public MovementType getMovementType() {
		return movementType;
	}


	public float getAttackInterval() {
		return attackInterval;
	}


	public float getProjectileVelocity() {
		return projectileVelocity;
	}


	public float getProjectileLife() {
		return projectileLife;
	}


	public float getAccelerationSpeed() {
		return accelerationSpeed;
	}


	public float getTouchDamage() {
		return touchDamage;
	}


	public float getHealth() {
		return health;
	}


	@Override
	public String toString() {
		return "BossParameters(" + projectileType.toString() + ", " + attackInterval + ")";
	}
}
