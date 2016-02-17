package com.muscovy.game.entity;


import com.muscovy.game.enums.EnemyShotType;
import com.muscovy.game.enums.MovementType;
import com.muscovy.game.enums.ProjectileType;


/**
 * Project URL : http://teal-duck.github.io/teal-duck
 * <br>
 * New class: Grouping of parameters to make unique bosses.
 */
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


	/**
	 * @return Time between projectiles appearing.
	 */
	public float getAttackInterval() {
		return attackInterval;
	}


	/**
	 * @return Speed of projectiles to be fired by boss.
	 */
	public float getProjectileVelocity() {
		return projectileVelocity;
	}


	/**
	 * @return Time projectile takes to disappear.
	 */
	public float getProjectileLife() {
		return projectileLife;
	}


	/**
	 * @return Scalar for additional accelerations.
	 */
	public float getAccelerationSpeed() {
		return accelerationSpeed;
	}


	/**
	 * @return Damage to deal to players upon touch.
	 */
	public float getTouchDamage() {
		return touchDamage;
	}


	/**
	 * @return Max health for the boss.
	 */
	public float getHealth() {
		return health;
	}


	@Override
	public String toString() {
		return "BossParameters(" + projectileType.toString() + ", " + attackInterval + ")";
	}
}
