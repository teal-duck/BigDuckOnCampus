package com.muscovy.game.entity;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.enums.ItemType;


/**
 * Created by ewh502 on 19/01/2016.
 */
public class Item extends Collidable {
	/**
	 * This will be for items which change your stats like fire rate etc, but not for pickups which increase your
	 * health etc
	 */

	private ItemType type;
	private boolean lifeOver;

	private static final int HEALTHPACK_REGEN = 10;


	public Item(MuscovyGame game, Sprite sprite, Vector2 position, ItemType type) {
		super(game, sprite, position);
		this.type = type;
		lifeOver = false;
	}


	public ItemType getType() {
		return type;
	}


	public boolean applyToPlayer(PlayerCharacter playerCharacter) {
		switch (type) {
		case HEALTH:
			return playerCharacter.gainHealth(Item.HEALTHPACK_REGEN);
		default:
			return false;
		}
	}


	public void setLifeOver() {
		lifeOver = true;
	}


	public boolean isLifeOver() {
		return lifeOver;
	}

}