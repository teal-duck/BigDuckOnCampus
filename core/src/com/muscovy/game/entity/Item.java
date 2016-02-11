package com.muscovy.game.entity;


import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.enums.ItemType;
import com.muscovy.game.enums.PlayerShotType;


/**
 * Created by ewh502 on 19/01/2016.
 */
public class Item extends Collidable {
	/**
	 * For any items that can be picked up
	 * 
	 */

	private ItemType type;
	private boolean lifeOver;

	private static final int HEALTHPACK_REGEN = 10;


	public Item(MuscovyGame game, String textureName, Vector2 position, ItemType type) {
		super(game, textureName, position);
		this.type = type;
		lifeOver = false;
	}


	/**
	 * @return
	 */
	public ItemType getType() {
		return type;
	}


	/**
	 * Attempts to apply its own effect to the player. Returns true if it was successful, else false.
	 *
	 * @param playerCharacter
	 * @return
	 */
	public boolean applyToPlayer(PlayerCharacter playerCharacter) {
		switch (type) {
		case HEALTH:
			return playerCharacter.gainHealth(Item.HEALTHPACK_REGEN);
		case TRIPLE_SHOT:
			playerCharacter.setShotType(PlayerShotType.TRIPLE);
			playerCharacter.addItemToObtainedItems(type);
			return true;
		case RAPID_FIRE:
			playerCharacter.setAttackInterval(0.5f * playerCharacter.getAttackInterval());
			playerCharacter.addItemToObtainedItems(type);
			return true;
		default:
			return false;
		}
	}


	/**
	 *
	 */
	public void setLifeOver() {
		lifeOver = true;
	}


	/**
	 * @return
	 */
	public boolean isLifeOver() {
		return lifeOver;
	}
}
