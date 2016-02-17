package com.muscovy.game.entity;


import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;


/**
 * Project URL : http://teal-duck.github.io/teal-duck
 * <br>
 * Inherited class: Minor changes only.
 */
public class Obstacle extends Collidable {
	private boolean damaging;
	private float touchDamage;


	public Obstacle(MuscovyGame game, String textureName, Vector2 position) {
		super(game, textureName, position);
	}


	/**
	 * @return
	 */
	public boolean isDamaging() {
		return damaging;
	}


	/**
	 * @param damaging
	 */
	public void setDamaging(boolean damaging) {
		this.damaging = damaging;
	}


	/**
	 * @return
	 */
	public float getTouchDamage() {
		return touchDamage;
	}


	/**
	 * @param touchDamage
	 */
	public void setTouchDamage(float touchDamage) {
		this.touchDamage = touchDamage;
	}


	@Override
	public boolean collides(Collidable collidable) {
		return Intersector.overlaps(collidable.getCircleHitbox(), getRectangleHitbox());
	}
}
