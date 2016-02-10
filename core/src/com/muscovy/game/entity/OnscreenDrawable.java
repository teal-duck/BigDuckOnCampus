package com.muscovy.game.entity;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;


/**
 * Created by ewh502 on 04/12/2015.
 */
public abstract class OnscreenDrawable {
	/**
	 * Basically just a wrapper class for a sprite, but in such a way so I can extend from it and know what I'm
	 * getting myself into, innit
	 */
	public final MuscovyGame game;
	private String textureName;
	private Sprite sprite;
	private Vector2 position;


	public OnscreenDrawable(MuscovyGame game, String textureName) {
		this(game, textureName, new Vector2(0, 0));
	}


	public OnscreenDrawable(MuscovyGame game, String textureName, Vector2 position) {
		this.game = game;
		this.position = position;

		sprite = new Sprite();
		setTexture(textureName);
	}


	/**
	 * @return
	 */
	public String getTextureName() {
		return textureName;
	}


	/**
	 * @return
	 */
	public float getHeight() {
		return getTexture().getHeight();
	}


	/**
	 * @return
	 */
	public float getWidth() {
		return getTexture().getWidth();
	}


	/**
	 * @return
	 */
	public Sprite getSprite() {
		return sprite;
	}


	/**
	 * Sets the texture name. Also loads the texture from the texture map and updates the sprite to use this
	 * texture.
	 *
	 * @param textureName
	 */
	public void setTexture(String textureName) {
		this.textureName = textureName;
		Texture texture = game.getTextureMap().getTextureOrLoadFile(textureName);
		sprite.setTexture(texture);
	}


	/**
	 * @return
	 */
	public Texture getTexture() {
		return sprite.getTexture();
	}


	/**
	 * @return
	 */
	public float getX() {
		return position.x;
	}


	/**
	 * @param x
	 */
	public void setX(float x) {
		position.x = x;
	}


	/**
	 * @return
	 */
	public float getY() {
		return position.y;
	}


	/**
	 * @param y
	 */
	public void setY(float y) {
		position.y = y;
	}


	/**
	 * @return
	 */
	public Vector2 getPosition() {
		return position;
	}


	/**
	 * @param position
	 */
	public void setPosition(Vector2 position) {
		this.position.set(position);
	}


	/**
	 * @return
	 */
	public float getCenterX() {
		return position.x + (getWidth() / 2);
	}


	/**
	 * @return
	 */
	public float getCenterY() {
		return position.y + (getHeight() / 2);
	}


	/**
	 * @return
	 */
	public Vector2 getCenter() {
		return new Vector2(getCenterX(), getCenterY());
	}
}
