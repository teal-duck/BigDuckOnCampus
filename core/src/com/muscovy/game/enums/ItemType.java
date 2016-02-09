package com.muscovy.game.enums;


import com.muscovy.game.AssetLocations;


public enum ItemType {
	HEALTH, COIN, TRIPLE_SHOT;

	// public static Texture getItemTexture(MuscovyGame game, ItemType itemType) {
	// switch (itemType) {
	// case HEALTH:
	// return game.getTextureMap().getTextureOrLoadFile(AssetLocations.HEALTHPACK);
	// case TRIPLE_SHOT:
	// return game.getTextureMap().getTextureOrLoadFile(AssetLocations.TRIPLE_SHOT);
	// default:
	// return null;
	// }
	// }
	public static String getItemTextureName(ItemType itemType) {
		switch (itemType) {
		case HEALTH:
			return AssetLocations.HEALTHPACK;
		case TRIPLE_SHOT:
			return AssetLocations.TRIPLE_SHOT;
		default:
			return null;
		}
	}
}
