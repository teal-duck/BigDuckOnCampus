package com.muscovy.game.input;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.PovDirection;
import com.muscovy.game.input.controller.PS4;
import com.muscovy.game.input.controller.Xbox360;
import com.muscovy.game.input.controller.Xbox360Windows;


/**
 * Helper class for creating new control maps.
 */
public class ControlMapCreator {
	private ControlMapCreator() {
	};


	public static final float DEFAULT_DEADZONE = 0.2f;


	/**
	 * Applies the default keyboard control scheme to a control map.
	 *
	 * @param controlMap
	 * @return the control map after changes
	 */
	public static ControlMap applyDefaultKeyControls(ControlMap controlMap) {
		// controlMap.addKeyForAction(Action.RIGHT, Keys.D, Keys.RIGHT);
		// controlMap.addKeyForAction(Action.LEFT, Keys.A, Keys.LEFT);
		// controlMap.addKeyForAction(Action.UP, Keys.W, Keys.UP);
		// controlMap.addKeyForAction(Action.DOWN, Keys.S, Keys.DOWN);
		// controlMap.addKeyForAction(Action.SPRINT, Keys.SHIFT_LEFT,
		// Keys.SHIFT_RIGHT);
		// controlMap.addKeyForAction(Action.FIRE, Keys.SPACE);
		// controlMap.addKeyForAction(Action.RELOAD, Keys.R);

		controlMap.addKeyForAction(Action.WALK_UP, Keys.W);
		controlMap.addKeyForAction(Action.WALK_RIGHT, Keys.D);
		controlMap.addKeyForAction(Action.WALK_DOWN, Keys.S);
		controlMap.addKeyForAction(Action.WALK_LEFT, Keys.A);

		controlMap.addKeyForAction(Action.SHOOT_UP, Keys.UP);
		controlMap.addKeyForAction(Action.SHOOT_RIGHT, Keys.RIGHT);
		controlMap.addKeyForAction(Action.SHOOT_DOWN, Keys.DOWN);
		controlMap.addKeyForAction(Action.SHOOT_LEFT, Keys.LEFT);

		controlMap.addKeyForAction(Action.ENTER, Keys.ENTER);
		controlMap.addKeyForAction(Action.PAUSE, Keys.P);
		controlMap.addKeyForAction(Action.ESCAPE, Keys.ESCAPE, Keys.BACKSPACE);

		return controlMap;
	}


	/**
	 * Applies the default PS4 control scheme to a control map.
	 *
	 * @param controlMap
	 * @param deadzone
	 * @return the control map after changes
	 */
	public static ControlMap applyDefaultPs4Controls(ControlMap controlMap, float deadzone) {
		Gdx.app.log("Controls", "Applying default PS4 controls");
		controlMap.addControllerForAction(Action.WALK_UP, ControllerBindingType.AXIS_NEGATIVE, PS4.AXIS_LEFT_Y,
				deadzone);
		controlMap.addControllerForAction(Action.WALK_RIGHT, ControllerBindingType.AXIS_POSITIVE,
				PS4.AXIS_LEFT_X, deadzone);
		controlMap.addControllerForAction(Action.WALK_DOWN, ControllerBindingType.AXIS_POSITIVE,
				PS4.AXIS_LEFT_Y, deadzone);
		controlMap.addControllerForAction(Action.WALK_LEFT, ControllerBindingType.AXIS_NEGATIVE,
				PS4.AXIS_LEFT_X, deadzone);

		controlMap.addControllerForAction(Action.SHOOT_UP, ControllerBindingType.AXIS_NEGATIVE,
				PS4.AXIS_RIGHT_Y, deadzone);
		controlMap.addControllerForAction(Action.SHOOT_RIGHT, ControllerBindingType.AXIS_POSITIVE,
				PS4.AXIS_RIGHT_X, deadzone);
		controlMap.addControllerForAction(Action.SHOOT_DOWN, ControllerBindingType.AXIS_POSITIVE,
				PS4.AXIS_RIGHT_Y, deadzone);
		controlMap.addControllerForAction(Action.SHOOT_LEFT, ControllerBindingType.AXIS_NEGATIVE,
				PS4.AXIS_RIGHT_X, deadzone);

		controlMap.addControllerForAction(Action.DPAD_UP, ControllerBindingType.POV, PS4.DPAD,
				PovDirection.north);
		controlMap.addControllerForAction(Action.DPAD_DOWN, ControllerBindingType.POV, PS4.DPAD,
				PovDirection.south);

		controlMap.addControllerForAction(Action.ENTER, ControllerBindingType.BUTTON, PS4.BUTTON_CROSS);
		controlMap.addControllerForAction(Action.PAUSE, ControllerBindingType.BUTTON, PS4.BUTTON_OPTIONS);
		controlMap.addControllerForAction(Action.ESCAPE, ControllerBindingType.BUTTON, PS4.BUTTON_CIRCLE);

		return controlMap;
	}


	/**
	 * Applies the default Xbox 360 control scheme to a control map.
	 *
	 * @param controlMap
	 * @param deadzone
	 * @return the control map after changes
	 */
	public static ControlMap applyDefaultXbox360Controls(ControlMap controlMap, float deadzone) {
		Gdx.app.log("Controls", "Applying default Xbox 360 controls");
		controlMap.addControllerForAction(Action.WALK_UP, ControllerBindingType.AXIS_NEGATIVE,
				Xbox360.AXIS_LEFT_Y, deadzone);
		controlMap.addControllerForAction(Action.WALK_RIGHT, ControllerBindingType.AXIS_POSITIVE,
				Xbox360.AXIS_LEFT_X, deadzone);
		controlMap.addControllerForAction(Action.WALK_DOWN, ControllerBindingType.AXIS_POSITIVE,
				Xbox360.AXIS_LEFT_Y, deadzone);
		controlMap.addControllerForAction(Action.WALK_LEFT, ControllerBindingType.AXIS_NEGATIVE,
				Xbox360.AXIS_LEFT_X, deadzone);

		controlMap.addControllerForAction(Action.SHOOT_UP, ControllerBindingType.AXIS_NEGATIVE,
				Xbox360.AXIS_RIGHT_Y, deadzone);
		controlMap.addControllerForAction(Action.SHOOT_RIGHT, ControllerBindingType.AXIS_POSITIVE,
				Xbox360.AXIS_RIGHT_X, deadzone);
		controlMap.addControllerForAction(Action.SHOOT_DOWN, ControllerBindingType.AXIS_POSITIVE,
				Xbox360.AXIS_RIGHT_Y, deadzone);
		controlMap.addControllerForAction(Action.SHOOT_LEFT, ControllerBindingType.AXIS_NEGATIVE,
				Xbox360.AXIS_RIGHT_X, deadzone);

		controlMap.addControllerForAction(Action.DPAD_UP, ControllerBindingType.POV, Xbox360.DPAD,
				PovDirection.north);
		controlMap.addControllerForAction(Action.DPAD_DOWN, ControllerBindingType.POV, Xbox360.DPAD,
				PovDirection.south);

		controlMap.addControllerForAction(Action.ENTER, ControllerBindingType.BUTTON, Xbox360.BUTTON_A);
		controlMap.addControllerForAction(Action.PAUSE, ControllerBindingType.BUTTON, Xbox360.BUTTON_START);
		controlMap.addControllerForAction(Action.ESCAPE, ControllerBindingType.BUTTON, Xbox360.BUTTON_B);

		return controlMap;
	}


	/**
	 * Applies the default Xbox 360 Windows control scheme to a control map.
	 *
	 * @param controlMap
	 * @param deadzone
	 * @return the control map after changes
	 */
	public static ControlMap applyDefaultXbox360WindowsControls(ControlMap controlMap, float deadzone) {
		Gdx.app.log("Controls", "Applying default Xbox 360 Windows controls");
		controlMap.addControllerForAction(Action.WALK_UP, ControllerBindingType.AXIS_NEGATIVE,
				Xbox360Windows.AXIS_LEFT_Y, deadzone);
		controlMap.addControllerForAction(Action.WALK_RIGHT, ControllerBindingType.AXIS_POSITIVE,
				Xbox360Windows.AXIS_LEFT_X, deadzone);
		controlMap.addControllerForAction(Action.WALK_DOWN, ControllerBindingType.AXIS_POSITIVE,
				Xbox360Windows.AXIS_LEFT_Y, deadzone);
		controlMap.addControllerForAction(Action.WALK_LEFT, ControllerBindingType.AXIS_NEGATIVE,
				Xbox360Windows.AXIS_LEFT_X, deadzone);

		controlMap.addControllerForAction(Action.SHOOT_UP, ControllerBindingType.AXIS_NEGATIVE,
				Xbox360Windows.AXIS_RIGHT_Y, deadzone);
		controlMap.addControllerForAction(Action.SHOOT_RIGHT, ControllerBindingType.AXIS_POSITIVE,
				Xbox360Windows.AXIS_RIGHT_X, deadzone);
		controlMap.addControllerForAction(Action.SHOOT_DOWN, ControllerBindingType.AXIS_POSITIVE,
				Xbox360Windows.AXIS_RIGHT_Y, deadzone);
		controlMap.addControllerForAction(Action.SHOOT_LEFT, ControllerBindingType.AXIS_NEGATIVE,
				Xbox360Windows.AXIS_RIGHT_X, deadzone);

		controlMap.addControllerForAction(Action.DPAD_UP, ControllerBindingType.POV, Xbox360Windows.DPAD,
				PovDirection.north);
		controlMap.addControllerForAction(Action.DPAD_DOWN, ControllerBindingType.POV, Xbox360Windows.DPAD,
				PovDirection.south);

		controlMap.addControllerForAction(Action.ENTER, ControllerBindingType.BUTTON, Xbox360Windows.BUTTON_A);
		controlMap.addControllerForAction(Action.PAUSE, ControllerBindingType.BUTTON,
				Xbox360Windows.BUTTON_START);
		controlMap.addControllerForAction(Action.ESCAPE, ControllerBindingType.BUTTON, Xbox360Windows.BUTTON_B);

		return controlMap;
	}


	/**
	 * @return
	 */
	public static ControlMap newDefaultControlMap() {
		return ControlMapCreator.newDefaultControlMap(null, ControlMapCreator.DEFAULT_DEADZONE);
	}


	/**
	 * @param deadzone
	 * @return
	 */
	public static ControlMap newDefaultControlMap(float deadzone) {
		return ControlMapCreator.newDefaultControlMap(null, deadzone);
	}


	/**
	 * @param controllerName
	 * @return
	 */
	public static ControlMap newDefaultControlMap(String controllerName) {
		return ControlMapCreator.newDefaultControlMap(controllerName, ControlMapCreator.DEFAULT_DEADZONE);
	}


	/**
	 * Creates a new control map with default keyboard and controller controls.
	 *
	 * @param controllerName
	 *                null if no controller plugged in
	 * @param deadzone
	 * @return
	 */
	public static ControlMap newDefaultControlMap(String controllerName, float deadzone) {
		Gdx.app.log("Controls", "Creating default controls. Controller name: \""
				+ ((controllerName != null) ? controllerName : "null") + "\"");
		ControlMap controlMap = new ControlMap();

		ControlMapCreator.applyDefaultKeyControls(controlMap);
		ControlMapCreator.applyDefaultControllerControls(controlMap, controllerName, deadzone);

		return controlMap;
	}


	/**
	 * Applies default controller controls to a control map if the controller is known. If the controller is not
	 * known, no changes are made.
	 * <p>
	 * Known controllers: PS4, Xbox 360
	 *
	 * @param controlMap
	 * @param controllerName
	 * @param deadzone
	 * @return
	 */
	public static ControlMap applyDefaultControllerControls(ControlMap controlMap, String controllerName,
			float deadzone) {
		if (controllerName != null) {
			if (controllerName.equals(PS4.NAME)) {
				ControlMapCreator.applyDefaultPs4Controls(controlMap, deadzone);
			} else if (controllerName.equals(Xbox360.NAME)) {
				ControlMapCreator.applyDefaultXbox360Controls(controlMap, deadzone);
			} else if (controllerName.equals(Xbox360Windows.NAME)) {
				ControlMapCreator.applyDefaultXbox360WindowsControls(controlMap, deadzone);
			} else {
				Gdx.app.log("Controls", "Unknown controller \"" + controllerName + "\"");
			}
		}
		return controlMap;
	}


	/**
	 * @param controllerName
	 * @return
	 */
	public static boolean isControllerKnown(String controllerName) {
		if (controllerName == null) {
			return false;
		}
		if (controllerName.equals(PS4.NAME)) {
			return true;
		} else if (controllerName.equals(Xbox360.NAME)) {
			return true;
		} else if (controllerName.equals(Xbox360Windows.NAME)) {
			return true;
		}
		return false;
	}
}
