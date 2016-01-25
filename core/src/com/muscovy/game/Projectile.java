package com.muscovy.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.muscovy.game.enums.ProjectileDamager;


/**
 * Created by ewh502 on 11/01/2016.
 */
public class Projectile extends OnscreenDrawable {
	private float damage = 10;
	// Old documentation, both is now 2
	// 0 = damages player, 1 = damages enemy, 3 = damages both
	private ProjectileDamager damagesWho = ProjectileDamager.ENEMY;
	private float xVelocity = 0;
	private float yVelocity = 0;
	private float maxVelocity = 150;
	private float direction = 0;
	private float lifeCounter = 0;
	private float life = 1.5f;
	private Circle collisionBox;


	public Projectile(float x, float y, float direction, float life, float maxVelocity, float xVelocity,
			float yVelocity, ProjectileDamager damagesWho) {
		setSprite(new Sprite(new Texture(Gdx.files.internal("breadBullet.png"))));
		setX(x);
		setY(y);
		this.direction = direction;
		this.life = life;
		this.maxVelocity = maxVelocity;
		updateVelocities();
		this.xVelocity += xVelocity;
		this.yVelocity += yVelocity;
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


	public float getxVelocity() {
		return xVelocity;
	}


	public void setxVelocity(float xVelocity) {
		this.xVelocity = xVelocity;
	}


	public float getyVelocity() {
		return yVelocity;
	}


	public void setyVelocity(float yVelocity) {
		this.yVelocity = yVelocity;
	}


	public float getMaxVelocity() {
		return maxVelocity;
	}


	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}


	public float getDirection() {
		return direction;
	}


	public void setDirection(float direction) {
		this.direction = direction;
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


	/**
	 * Other shit, self explanatory
	 */
	public void update() {
		movement();
		lifeOver();
	}


	public void movement() {
		setX(getX() + (xVelocity * Gdx.graphics.getDeltaTime()));
		setY(getY() + (yVelocity * Gdx.graphics.getDeltaTime()));
		lifeCounter += Gdx.graphics.getDeltaTime();
	}


	public void updateVelocities() {
		xVelocity = (float) (maxVelocity * Math.sin(direction));
		yVelocity = (float) (maxVelocity * Math.cos(direction));
	}


	public void kill() {
		life = 0;
	}


	public boolean lifeOver() {
		return (lifeCounter > life);
	}
}
