package com.muscovy.game.entity;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;


/**
 * Base class for all drawable entities. Provides methods around the entity's sprite and position on the map.
 */
public abstract class OnscreenDrawable {
	/**
	 * Instance of the game in which the entity exists.
	 */
	public final MuscovyGame game;
	
	public static final int DEFAULT_ENTITY_WIDTH = 64;
	public static final int DEFAULT_ENTITY_HEIGHT = 64;

	private String textureName;
	private Sprite sprite;
	private Vector2 position;
	private int width;
	private int height;


	/**
	 * Places the entity at (0, 0).
	 *
	 * @param game
	 *                Instance of the game in which the entity exists.
	 * @param textureName
	 *                Filename storing the texture.
	 */
	public OnscreenDrawable(MuscovyGame game, String textureName) {
		this(game, textureName, new Vector2(0, 0));
	}
	
	
	/**
	 * Places the entity at (0, 0).
	 *
	 * @param game
	 *                Instance of the game in which the entity exists.
	 * @param textureName
	 *                Filename storing the texture.
	 */
	public OnscreenDrawable(MuscovyGame game, String textureName, int width, int height) {
		this(game, textureName, new Vector2(0, 0), width, height);
	}


	/**
	 *
	 * @param game
	 *                Instance of the game in which the entity exists.
	 * @param textureName
	 *                Filename storing the texture.
	 * @param position
	 *                {@link Vector2} describing the bottom-left of the entity's sprite in pixels.
	 */
	public OnscreenDrawable(MuscovyGame game, String textureName, Vector2 position) {
		this(game, textureName, position, DEFAULT_ENTITY_WIDTH, DEFAULT_ENTITY_HEIGHT);
	}
	
	/**
	 *
	 * @param game
	 *                Instance of the game in which the entity exists.
	 * @param textureName
	 *                Filename storing the texture.
	 * @param position
	 *                {@link Vector2} describing the bottom-left of the entity's sprite in pixels.
	 * @param width
	 * 				  Width of the entity's drawable area.
	 * @param height
	 * 				  Height of the entity's drawable area.
	 */
	public OnscreenDrawable(MuscovyGame game, String textureName, Vector2 position, int width, int height) {
		this.game = game;
		this.position = position;
		
		this.width = width;
		this.height = height;

		sprite = new Sprite();
		setTexture(textureName);
	}


	/**
	 * @return Filename storing the texture.
	 */
	public String getTextureName() {
		return textureName;
	}


	/**
	 * @return Height of sprite in pixels.
	 */
	public float getHeight() {
		return height;
	}


	/**
	 * @return Width of sprite in pixels.
	 */
	public float getWidth() {
		return width;
	}


	/**
	 * @return Instance of the entity's sprite.
	 */
	public Sprite getSprite() {
		return sprite;
	}


	/**
	 * Sets the texture name. Also loads the texture from the texture map and updates the sprite to use this
	 * texture.
	 *
	 * @param textureName
	 *                Filename storing the texture.
	 */
	public void setTexture(String textureName) {
		this.textureName = textureName;
		Texture texture = game.getTextureMap().getTextureOrLoadFile(textureName);
		sprite.setTexture(texture);
	}


	/**
	 * @return Instance of the texture used on the entity's sprite.
	 */
	public Texture getTexture() {
		return sprite.getTexture();
	}


	/**
	 * @return x co-ordinate at bottom left of entity in pixels.
	 */
	public float getX() {
		return position.x;
	}


	/**
	 * @param x
	 *                Co-ordinate at bottom left of entity in pixels.
	 */
	public void setX(float x) {
		position.x = x;
	}


	/**
	 * @return y co-ordinate at bottom left of entity in pixels.
	 */
	public float getY() {
		return position.y;
	}


	/**
	 * @param y
	 *                Co-ordinate at bottom left of entity in pixels.
	 */
	public void setY(float y) {
		position.y = y;
	}


	/**
	 * @return Co-ordinates at bottom left of entity in pixels.
	 */
	public Vector2 getPosition() {
		return position;
	}


	/**
	 * @param position
	 *                Co-ordinates at bottom left of entity in pixels.
	 */
	public void setPosition(Vector2 position) {
		this.position.set(position);
	}


	/**
	 * @return Co-ordinate at centre of entity in pixels.
	 */
	public float getCenterX() {
		return position.x + (getWidth() / 2);
	}


	/**
	 * @return Co-ordinate at centre of entity in pixels.
	 */
	public float getCenterY() {
		return position.y + (getHeight() / 2);
	}


	/**
	 * @return Co-ordinates at centre of entity in pixels.
	 */
	public Vector2 getCenter() {
		return new Vector2(getCenterX(), getCenterY());
	}
}
