package com.muscovy.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


/**
 * Created by ewh502 on 04/12/2015.
 */
public abstract class OnscreenDrawable {
	/**
	 * Basically just a wrapper class for a sprite, but in such a way so I can extend from it and know what I'm
	 * getting myself into, innit
	 */
	private Sprite sprite;
	private float x, y;


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
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}

}
