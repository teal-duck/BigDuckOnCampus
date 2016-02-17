package com.muscovy.game.enums;


/**
 * Project URL : http://teal-duck.github.io/teal-duck
 * <br>
 * New class: enumerates all possible attack types for enemies. 
 */
public enum AttackType {
	TOUCH, RANGE;

	/**
	 * @return
	 */
	public int getScoreMultiplier() {
		return ordinal();
	}
}
