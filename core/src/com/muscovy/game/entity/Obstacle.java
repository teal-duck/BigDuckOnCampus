package com.muscovy.game.entity;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;


/**
 * Created by SeldomBucket on 05-Dec-15. I should hope this is all fairly self-explanatory
 */
public class Obstacle extends Collidable {
	private boolean damaging;
	private float touchDamage;


	public Obstacle(MuscovyGame game, Sprite sprite, Vector2 position) {
		super(game, sprite, position);
	}


	public boolean isDamaging() {
		return damaging;
	}


	public void setDamaging(boolean damaging) {
		this.damaging = damaging;
	}


	public float getTouchDamage() {
		return touchDamage;
	}


	public void setTouchDamage(float touchDamage) {
		this.touchDamage = touchDamage;
	}


	@Override
	public boolean collides(Collidable collidable) {
		return Intersector.overlaps(collidable.getCircleHitbox(), getRectangleHitbox());
	}
}
