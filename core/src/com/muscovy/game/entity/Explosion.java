package com.muscovy.game.entity;


import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;


/**
 *
 * Projectile explosion i.e. the bomb
 *
 */
public class Explosion extends OnscreenDrawable {
	public static final float VIEW_TIME = 0.5f;
	private float radius;
	private float viewTime;


	public Explosion(MuscovyGame game, String textureName, Vector2 position, float radius) {
		super(game, textureName, position);

		this.radius = radius;
		viewTime = Explosion.VIEW_TIME;
	}


	public void update(float deltaTime) {
		viewTime -= deltaTime;
	}


	public float getViewTime() {
		return viewTime;
	}


	public float getRadius() {
		return radius;
	}
}
