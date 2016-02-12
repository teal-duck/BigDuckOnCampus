package com.muscovy.game.entity;


import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.EntityManager;
import com.muscovy.game.MuscovyGame;


public class Bomb extends OnscreenDrawable {
	public static final float BLAST_RADIUS = 64 * 3;
	public static final float BLAST_TIME = 3f;
	public static final float BLAST_DAMAGE = 50;
	public static final float MIN_DAMAGE = 5;
	public static final float BLAST_FORCE = 100000;
	public static final float MIN_FORCE = 10000;

	private float blastRadius;
	private float blastTime;
	private float blastDamage;
	private float minDamage;
	private float blastForce;
	private float minForce;


	public Bomb(MuscovyGame game, String textureName, Vector2 position) {
		super(game, textureName, position);

		blastRadius = Bomb.BLAST_RADIUS;
		blastTime = Bomb.BLAST_TIME;
		blastDamage = Bomb.BLAST_DAMAGE;
		minDamage = Bomb.MIN_DAMAGE;
		blastForce = Bomb.BLAST_FORCE;
		minForce = Bomb.MIN_FORCE;
	}


	public float getBlastTime() {
		return blastTime;
	}


	public void update(float deltaTime) {
		blastTime -= deltaTime;
	}


	public void goKaboom(EntityManager entityManager) {
		for (Enemy enemy : entityManager.getEnemies()) {
			attemptToBlowUp(enemy);
		}

		attemptToBlowUp(entityManager.getPlayer());
	}


	private void attemptToBlowUp(MoveableEntity entity) {
		Vector2 bombPosition = getCenter();
		Vector2 entityPosition = entity.getCenter();

		Vector2 vectorToEntity = entityPosition.cpy().sub(bombPosition);
		float distance = vectorToEntity.len();

		if (distance < blastRadius) {
			// 1 = on the bomb, 0 = on the edge
			float inverseDistance = 1 - (distance / blastRadius);
			float damage = ((blastDamage - minDamage) * inverseDistance) + minDamage;

			Vector2 directionToPush = vectorToEntity.cpy().nor();
			float forceToPush = ((blastForce - minForce) * inverseDistance) + minForce;

			if (entity instanceof Enemy) {
				Enemy enemy = (Enemy) entity;
				enemy.takeDamage(damage);
			} else if (entity instanceof PlayerCharacter) {
				PlayerCharacter player = (PlayerCharacter) entity;
				player.takeDamage(damage);
			}

			entity.addAcceleration(directionToPush.scl(forceToPush));
		}
	}


	public float getBlastRadius() {
		return blastRadius;
	}


	public Vector2 getBottomLeft() {
		return getCenter().cpy().sub(blastRadius, blastRadius);
	}
}
