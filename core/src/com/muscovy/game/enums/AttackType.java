package com.muscovy.game.enums;


public enum AttackType {
	// 0 = touch damage, 1 = ranged attack, 2 = both
	TOUCH, RANGE, BOTH;

	public int getScoreMultiplier() {
		return ordinal();
	}
}
