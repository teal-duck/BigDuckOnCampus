package com.muscovy.game.entity;


import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.enums.ItemType;
import com.muscovy.game.enums.PlayerShotType;
import com.muscovy.game.enums.ProjectileType;


/**
 * Created by ewh502 on 19/01/2016.
 * 
 *  For any items that can be picked up
 */
public class Item extends Collidable {
	private ItemType type;
	private boolean lifeOver;

	private static final int HEALTHPACK_REGEN = 10;
	private static final int BOMB_GIVE_COUNT = 1;


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
	 * @return true if item successfully applied.
	 */
	public boolean applyToPlayer(PlayerCharacter playerCharacter) {
		switch (type) {
		case HEALTH:
			boolean appliedHealthPack = playerCharacter.gainHealth(Item.HEALTHPACK_REGEN);
			return appliedHealthPack;
		case TRIPLE_SHOT:
			playerCharacter.setShotType(PlayerShotType.TRIPLE);
			playerCharacter.addItemToObtainedItems(type);
			return true;
		case RAPID_FIRE:
			playerCharacter.setAttackInterval(PlayerCharacter.RAPID_FIRE_ATTACK_INTERVAL);
			playerCharacter.addItemToObtainedItems(type);
			return true;
		case FLAME_THROWER:
			playerCharacter.setAttackInterval(PlayerCharacter.FLAME_THROWER_ATTACK_INTERVAL);
			playerCharacter.setProjectileType(ProjectileType.FLAMES);
			playerCharacter.addItemToObtainedItems(type);
			return true;
		case BOMB:
			playerCharacter.giveBombs(Item.BOMB_GIVE_COUNT);
			return true;
		case FLIGHT:
			playerCharacter.setMaxFlightTime();
			playerCharacter.addItemToObtainedItems(type);
			return true;
		case EXTRA_HEALTH:
			playerCharacter.setMaxHealth(PlayerCharacter.MAX_HEALTH * 2);
			playerCharacter.addItemToObtainedItems(type);
			return true;
		case HEALTH_UNLOCK:
			playerCharacter.setHealth(PlayerCharacter.MAX_HEALTH);
			return true;
		case SUNGLASSES:
			playerCharacter.setTexture(AssetLocations.PLAYER_SUNGLASSES);
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
