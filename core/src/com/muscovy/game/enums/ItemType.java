package com.muscovy.game.enums;

import com.badlogic.gdx.graphics.Texture;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;

public enum ItemType {
	HEALTH, COIN, TRIPLE_SHOT;
	
	public static Texture getItemSprite(MuscovyGame game, ItemType itemType) {
		switch (itemType) {
		case HEALTH:
			return game.getTextureMap().getTextureOrLoadFile(AssetLocations.HEALTHPACK);
		case TRIPLE_SHOT:
			return game.getTextureMap().getTextureOrLoadFile(AssetLocations.TRIPLE_SHOT);
		default:
			return null;
		}
	}
}
