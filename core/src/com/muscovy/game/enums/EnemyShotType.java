package com.muscovy.game.enums;


public enum EnemyShotType {
	// 0 = single shot in direction of movement, 1 = shoot towards player if in range, 2 =
	// double shot towards player if in range, 3 = triple shot towards player if in
	// range, 4 = random shot direction
	SINGLE_MOVEMENT, SINGLE_TOWARDS_PLAYER, DOUBLE_TOWARDS_PLAYER, TRIPLE_TOWARDS_PLAYER, RANDOM_DIRECTION;
}
