package com.muscovy.game.enums;


/**
 *
 */
public enum AttackType {
	// TODO: Should range include touch as well (i.e. act as both?)
	TOUCH, RANGE;

	/**
	 * @return
	 */
	public int getScoreMultiplier() {
		return ordinal();
	}
}
