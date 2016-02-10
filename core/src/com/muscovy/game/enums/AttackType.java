package com.muscovy.game.enums;


/**
 *
 */
public enum AttackType {
	// TODO: Should range include touch as well (i.e. act as both?)
	TOUCH, RANGE, BOTH;

	/**
	 * @return
	 */
	public int getScoreMultiplier() {
		return ordinal();
	}
}
