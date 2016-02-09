package com.muscovy.game.entity;


import java.util.ArrayList;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.enums.ProjectileDamager;
import com.muscovy.game.enums.ProjectileType;
import com.muscovy.game.screen.GameScreen;


/**
 * Created by ewh502 on 11/01/2016.
 */
public class Projectile extends OnscreenDrawable {
	public static final float SPEED = 150f;
	public static final float DAMAGE = 10f;
	public static final float LIFE_TIME = 1.5f;

	private Vector2 velocity;
	private float speed = Projectile.SPEED;
	private float damage = Projectile.DAMAGE;
	private float maxLifeTime = Projectile.LIFE_TIME;
	private float lifeCounter = 0;
	private ProjectileDamager damagesWho = ProjectileDamager.ENEMY;
	private Circle collisionBox;
	private ProjectileType projectileType = ProjectileType.STANDARD;


	public Projectile(MuscovyGame game, Vector2 position, Vector2 direction, float life, float speed,
			ProjectileDamager damagesWho, ProjectileType projectileType) {
		// super(game, new Sprite(game.getTextureMap().getTextureOrLoadFile(AssetLocations.BULLET)), position);
		super(game, AssetLocations.BULLET, position);

		maxLifeTime = life;
		this.speed = speed;
		this.damagesWho = damagesWho;
		this.projectileType = projectileType;

		velocity = direction.cpy().setLength(speed);
		collisionBox = new Circle((int) getX(), (int) getY(), getSprite().getRegionWidth() / 2);
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


	public void updateCollisionBox() {
		collisionBox.setX(getX());
		collisionBox.setY(getY());
	}


	/**
	 * Other shit, self explanatory
	 */
	public void update(float deltaTime) {
		movementLogic(deltaTime);
		moveEntity(deltaTime);
		lifeCounter += deltaTime;
	}


	public void movementLogic(float deltaTime) {
		switch (projectileType) {
		case STANDARD:
			break;
		case HOMING:
			// TODO: Homing projectile logic
			PlayerCharacter player = getPlayer();
			velocity.set(player.getPosition()).sub(getPosition());
			velocity.setLength(speed);
		}
	}


	public PlayerCharacter getPlayer() {
		// TODO: Make Enemy.getPlayer() nicer
		Screen screen = game.getScreen();

		if (screen instanceof GameScreen) {
			return ((GameScreen) screen).getPlayer();
		} else {
			return null;
		}
	}


	public void moveEntity(float deltaTime) {
		getPosition().mulAdd(velocity, deltaTime);
		updateCollisionBox();
	}


	public void killSelf() {
		maxLifeTime = 0;
	}


	public boolean isLifeOver() {
		return (lifeCounter > maxLifeTime);
	}


	public static ArrayList<Projectile> shootProjectiles(MuscovyGame game, int count, Vector2 position,
			Vector2 direction, float life, float maxVelocity, ProjectileDamager damagesWho,
			ProjectileType projectileType) {
		ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

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


	public float getDamage() {
		return damage;
	}


	public void setDamage(float damage) {
		this.damage = damage;
	}


	public ProjectileDamager getDamagesWho() {
		return damagesWho;
	}


	public void setDamagesWho(ProjectileDamager damagesWho) {
		this.damagesWho = damagesWho;
	}


	public float getLife() {
		return maxLifeTime;
	}


	public void setLife(float life) {
		maxLifeTime = life;
	}


	public float getMaxVelocity() {
		return speed;
	}


	public void setMaxVelocity(float maxVelocity) {
		speed = maxVelocity;
	}


	public Circle getCollisionBox() {
		return collisionBox;
	}
}
