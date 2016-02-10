package com.muscovy.game.entity;


import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;


/**
 * Created by SeldomBucket on 05-Dec-15. I should hope this is all fairly self-explanatory
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
