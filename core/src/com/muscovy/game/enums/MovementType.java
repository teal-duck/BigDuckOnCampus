package com.muscovy.game.enums;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Enumerates all possible enemy movement logics.
 */
public enum MovementType {
	STATIC, RANDOM, FOLLOW;

	public int getScoreMultiplier() {
		return ordinal();
	}
}
