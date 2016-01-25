package com.muscovy.game.enums;


public enum MovementType {
	// 0 = static, 1 = random movement, 2 = following
	STATIC, RANDOM, FOLLOW;

	public int getScoreMultiplier() {
		return ordinal();
	}
}
