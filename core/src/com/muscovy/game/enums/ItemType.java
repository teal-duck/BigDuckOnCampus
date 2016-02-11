package com.muscovy.game.enums;


import com.muscovy.game.AssetLocations;


/**
 *
 */
public enum ItemType {
	HEALTH, COIN, TRIPLE_SHOT, RAPID_FIRE, FLAME_THROWER;

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
		default:
			return null;
		}
	}
}
