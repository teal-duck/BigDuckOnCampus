package com.muscovy.game.enums;


/**
 *
 */
public enum MovementType {
	STATIC, RANDOM, FOLLOW;

	public int getScoreMultiplier() {
		return ordinal();
	}
}
