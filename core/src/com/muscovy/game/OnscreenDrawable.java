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


	public OnscreenDrawable(Sprite sprite) {
		this(sprite, new Vector2(0, 0));
	}


	public OnscreenDrawable(Sprite sprite, Vector2 position) {
		this.sprite = sprite;
		this.position = position;
	}


	public float getHeight() {
		return getTexture().getHeight();
	}


	public float getWidth() {
		return getTexture().getWidth();
	}


	public Sprite getSprite() {
		return sprite;
	}


	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}


	public Texture getTexture() {
		return sprite.getTexture();
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


	public float getCenterX() {
		return position.x + (getWidth() / 2);
	}


	public float getCenterY() {
		return position.y + (getHeight() / 2);
	}


	public Vector2 getCenter() {
		return new Vector2(getCenterX(), getCenterY());
	}
}
