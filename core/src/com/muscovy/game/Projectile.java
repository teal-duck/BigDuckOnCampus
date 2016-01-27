package com.muscovy.game;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.enums.ProjectileDamager;


/**
 * Created by ewh502 on 11/01/2016.
 */
public class Projectile extends OnscreenDrawable {
	private float damage = 10;
	private ProjectileDamager damagesWho = ProjectileDamager.ENEMY;
	private Vector2 velocity;
	private float maxVelocity = 150;
	private float lifeCounter = 0;
	private float life = 1.5f;
	private Circle collisionBox;


	public Projectile(TextureMap textureMap, Vector2 position, Vector2 direction, float life, float maxVelocity,
			ProjectileDamager damagesWho) {
		setSprite(new Sprite(textureMap.getTextureOrLoadFile("breadBullet.png")));
		setPosition(position);
		this.life = life;
		this.maxVelocity = maxVelocity;
		velocity = direction.cpy().setLength(maxVelocity);
		this.damagesWho = damagesWho;
	}


	/**
	 * Getters and Setters
	 */
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
		return life;
	}


	public void setLife(float life) {
		this.life = life;
	}


	public float getMaxVelocity() {
		return maxVelocity;
	}


	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}


	public Circle getCollisionBox() {
		return collisionBox;
	}


	public void setCollisionBox(Circle collisionBox) {
		this.collisionBox = collisionBox;
	}


	@Override
	public void setSprite(Sprite sprite) {
		super.setSprite(sprite);
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
	public void update() {
		movement();
		lifeOver();
	}


	public void movement() {
		getPosition().mulAdd(velocity, Gdx.graphics.getDeltaTime());
		updateCollisionBox();
		lifeCounter += Gdx.graphics.getDeltaTime();
	}


	public void kill() {
		life = 0;
	}


	public boolean lifeOver() {
		return (lifeCounter > life);
	}


	public static ArrayList<Projectile> shootProjectiles(TextureMap textureMap, int count, Vector2 position,
			Vector2 direction, float life, float maxVelocity, ProjectileDamager damagesWho) {
		ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

		float maxSpread = 0;
		float spreadDelta = 0;

		switch (count) {
		case 1:
			maxSpread = 0;
			spreadDelta = 0;
		case 2:
			maxSpread = MathUtils.PI / 24;
			spreadDelta = maxSpread * 2;
			break;
		case 3:
			maxSpread = MathUtils.PI / 12;
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
			projectiles.add(new Projectile(textureMap, position.cpy(),
					direction.cpy().rotateRad(spreadAngle), life, maxVelocity, damagesWho));
			spreadAngle += spreadDelta;
		}

		return projectiles;
	}
}
