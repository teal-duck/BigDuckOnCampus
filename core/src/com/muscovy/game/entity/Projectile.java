package com.muscovy.game.entity;


import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.enums.ProjectileDamager;
import com.muscovy.game.enums.ProjectileType;


/**
 * Created by ewh502 on 11/01/2016.
 */
public class Projectile extends OnscreenDrawable {
	public static final float SPEED = 150f;
	public static final float DAMAGE = 10f;
	public static final float LIFE_TIME = 1.5f; //Time projectile on screen

	private Vector2 velocity;
	private float speed = Projectile.SPEED;
	private float damage = Projectile.DAMAGE;
	private float maxLifeTime = Projectile.LIFE_TIME;
	private float lifeCounter = 0;	
	private ProjectileDamager damagesWho = ProjectileDamager.ENEMY;
	private Circle collisionBox;
	private ProjectileType projectileType = ProjectileType.STANDARD;
	
	//For curved bullets
	private int rotateDirection = 1;	//anti-clockwise
	private float rotateSpeed = 1.5f;	//curvature path of projectile

	/**
	 * 
	 * @param game
	 * @param position
	 * @param direction
	 * @param life
	 * @param speed
	 * @param damagesWho
	 * @param projectileType
	 */
	public Projectile(MuscovyGame game, Vector2 position, Vector2 direction, float life, float speed,
			ProjectileDamager damagesWho, ProjectileType projectileType) {
		
		//Projectile texture, position
		super(game, AssetLocations.BULLET, position);
		
		//Changing texture for Flame Thrower
		if (projectileType == ProjectileType.FLAMES) {
			setTexture(AssetLocations.FLAME);
			damage = 5;
		}

		maxLifeTime = life;
		this.speed = speed;
		this.damagesWho = damagesWho;
		this.projectileType = projectileType;

		velocity = direction.cpy().setLength(speed);
		collisionBox = new Circle((int) getX(), (int) getY(), getTexture().getWidth() / 2f);

		rotateDirection = 1;
	}


	/**
	 * X and Y setters move collision box too
	 */
	@Override
	public void setX(float x) {
		super.setX(x);
		collisionBox.setX(x);
	}


	@Override
	public void setY(float y) {
		super.setY(y);
		collisionBox.setY(y);
	}


	/**
	 *
	 */
	public void updateCollisionBox() {
		collisionBox.setX(getX());
		collisionBox.setY(getY());
	}


	/**
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		movementLogic(deltaTime);
		moveEntity(deltaTime);
		lifeCounter += deltaTime;
	}


	/**
	 * @param deltaTime
	 */
	public void movementLogic(float deltaTime) {
		switch (projectileType) {
		case STANDARD:
			break;
		case HOMING:
			// TODO: Homing projectile logic
			// Use centers of objects
			PlayerCharacter player = getPlayer();
			velocity.set(player.getCenter()).sub(getCenter());
			velocity.setLength(speed);
		case FLAMES:
			break;
		case CURVED:
			velocity.rotate(rotateDirection * rotateSpeed);
			break;
		}

	}


	/**
	 * @return
	 */
	public PlayerCharacter getPlayer() {
		return game.getPlayerCharacter();
	}


	/**
	 * @param deltaTime
	 */
	public void moveEntity(float deltaTime) {
		getPosition().mulAdd(velocity, deltaTime);
		updateCollisionBox();
	}


	/**
	 *
	 */
	public void killSelf() {
		maxLifeTime = 0;
	}


	/**
	 * @return
	 */
	public boolean isLifeOver() {
		return (lifeCounter > maxLifeTime);
	}


	/**
	 * @return
	 */
	public float getDamage() {
		return damage;
	}


	/**
	 * @param damage
	 */
	public void setDamage(float damage) {
		this.damage = damage;
	}


	/**
	 * @return
	 */
	public ProjectileDamager getDamagesWho() {
		return damagesWho;
	}


	/**
	 * @param damagesWho
	 */
	public void setDamagesWho(ProjectileDamager damagesWho) {
		this.damagesWho = damagesWho;
	}


	/**
	 * @return
	 */
	public float getLife() {
		return maxLifeTime;
	}


	/**
	 * @param life
	 */
	public void setLife(float life) {
		maxLifeTime = life;
	}


	/**
	 * @return
	 */
	public float getMaxVelocity() {
		return speed;
	}


	/**
	 * @param maxVelocity
	 */
	public void setMaxVelocity(float maxVelocity) {
		speed = maxVelocity;
	}


	/**
	 * @return
	 */
	public Circle getCollisionBox() {
		return collisionBox;
	}


	/**
	 * Helper function for shooting multiple projectiles with a spread.
	 *
	 * @param game
	 * @param count
	 * @param position
	 * @param direction
	 * @param life
	 * @param maxVelocity
	 * @param damagesWho
	 * @param projectileType
	 * @return
	 */
	public static ArrayList<Projectile> shootProjectiles(MuscovyGame game, int count, Vector2 position,
			Vector2 direction, float life, float maxVelocity, ProjectileDamager damagesWho,
			ProjectileType projectileType) {
		ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
		
		//firing angle of projectiles
		float maxSpread = 0;	
		float spreadDelta = 0; 
		
		switch (count) {
		case 1:
			maxSpread = 0;
			spreadDelta = 0;
			break;
		case 2:
			maxSpread = MathUtils.PI / 24;
			spreadDelta = maxSpread * 2;
			break;
		case 3:
			maxSpread = (2 * MathUtils.PI) / 12;
			spreadDelta = maxSpread;
			break;

		// up, down, left, right
		case 4:
			maxSpread = MathUtils.PI / 2;
			spreadDelta = maxSpread;
			break;

		// 6 in circle
		case 6:
			maxSpread = MathUtils.PI / 3;
			spreadDelta = maxSpread;
			break;
		// 8 in a circle
		case 8:
			maxSpread = MathUtils.PI / 4;
			spreadDelta = maxSpread;
			break;

		default:
			System.out.println("Spread not calculated for " + count + " bullets");
			maxSpread = 0;
			spreadDelta = 0;
			break;
		}

		float spreadAngle = -maxSpread;

		for (int i = 0; i < count; i += 1) {
			projectiles.add(new Projectile(game, position.cpy(), direction.cpy().rotateRad(spreadAngle),
					life, maxVelocity, damagesWho, projectileType));
			spreadAngle += spreadDelta;
		}

		return projectiles;
	}
}