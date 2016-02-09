package com.muscovy.game.input;


/**
 * The possible actions that require binding to some input event.
 */
public enum Action {
	WALK_UP, WALK_RIGHT, WALK_DOWN, WALK_LEFT, //
	SHOOT_UP, SHOOT_RIGHT, SHOOT_DOWN, SHOOT_LEFT, //
	DPAD_UP, DPAD_DOWN, // These are used for controller GUI with controller
	PAUSE, ENTER, ESCAPE;
}
