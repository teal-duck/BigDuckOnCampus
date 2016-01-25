package com.muscovy.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by ewh502 on 04/12/2015.
 */
public abstract class OnscreenDrawable {
	/**
	 * Basically just a wrapper class for a sprite, but in such a way so I can extend from it and know what I'm
	 * getting myself into, innit
	 */
	private Sprite sprite;
	private Vector2 position;


	public OnscreenDrawable() {
		position = new Vector2(0, 0);
	}


	public float getHeight() {
		return sprite.getTexture().getHeight();
	}


	public float getWidth() {
		return sprite.getTexture().getWidth();
	}


	public Sprite getSprite() {
		return sprite;
	}


	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}


	public void setTexture(Texture texture) {
		sprite.setTexture(texture);
	}


	public float getX() {
		return position.x;
	}


	public void setX(float x) {
		position.x = x;
	}


	public float getY() {
		return position.y;
	}


	public void setY(float y) {
		position.y = y;
	}


	public Vector2 getPosition() {
		return position;
	}


	public void setPosition(Vector2 position) {
		this.position.set(position);
	}
}
