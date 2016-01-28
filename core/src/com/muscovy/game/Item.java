package com.muscovy.game;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
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


	public Item(Sprite sprite, Vector2 position, ItemType type) {
		super(sprite, position);
		this.type = type;
	}

	public ItemType getType() {
		return this.type;
	}

	public boolean applyToPlayer(PlayerCharacter playerCharacter) {
		switch (type) {
		case HEALTH:
//			if (!playerCharacter.)
		default:
			return false;
		}
	}
}
