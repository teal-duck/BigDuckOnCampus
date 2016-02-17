package com.muscovy.game.enums;


import com.muscovy.game.AssetLocations;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Enumerates all possible item types.
 */
public enum ItemType {
	HEALTH, TRIPLE_SHOT, RAPID_FIRE, FLAME_THROWER, BOMB, FLIGHT, EXTRA_HEALTH, SUNGLASSES, HEALTH_UNLOCK;

	/**
	 * @param itemType
	 * @return
	 */
	public static String getItemTextureName(ItemType itemType) {
		switch (itemType) {
		case HEALTH:
			return AssetLocations.HEALTHPACK;
		case TRIPLE_SHOT:
			return AssetLocations.TRIPLE_SHOT;
		case RAPID_FIRE:
			return AssetLocations.RAPID_FIRE;
		case FLAME_THROWER:
			return AssetLocations.FLAME_THROWER;
		case BOMB:
			return AssetLocations.BOMB_ITEM;
		case FLIGHT:
			return AssetLocations.FLIGHT_ITEM;
		case EXTRA_HEALTH:
			return AssetLocations.EXTRA_HEALTH;
		case SUNGLASSES:
			return AssetLocations.SUNGLASSES;
		case HEALTH_UNLOCK:
			return AssetLocations.HEALTHPACK;
		default:
			return null;
		}
	}
}
