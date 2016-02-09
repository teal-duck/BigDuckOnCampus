package com.muscovy.game.save.control;


import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.input.ControllerBinding;
import com.muscovy.game.input.ControllerBindingType;
import com.muscovy.game.save.BaseSerializer;


@SuppressWarnings("rawtypes")
public class ControllerBindingSerializer extends BaseSerializer<ControllerBinding> {
	public ControllerBindingSerializer(MuscovyGame game) {
		super(game);
	}


	@Override
	public void write(Json json, ControllerBinding controllerBinding, Class knownType) {
		json.writeObjectStart();
		json.writeValue("controllerBindingType", controllerBinding.getControllerBindingType());
		json.writeValue("index", controllerBinding.getIndex());
		json.writeValue("deadzone", controllerBinding.getDeadzone());
		json.writeValue("povDirection", controllerBinding.getPovDirection());
		json.writeObjectEnd();
	}


	@Override
	public ControllerBinding read(Json json, JsonValue jsonData, Class type) {
		JsonValue controllerBindingTypeValue = jsonData.get("controllerBindingType");
		ControllerBindingType controllerBindingType = ControllerBindingType
				.valueOf(controllerBindingTypeValue.asString());

		JsonValue povDirectionValue = jsonData.get("povDirection");
		PovDirection povDirection;
		String povString = povDirectionValue.asString();
		if (povString != null) {
			povDirection = PovDirection.valueOf(povString);
		} else {
			povDirection = null;
		}

		int index = jsonData.getInt("index");
		float deadzone = jsonData.getFloat("deadzone");

		ControllerBinding controllerBinding = new ControllerBinding(controllerBindingType, index, deadzone,
				povDirection);
		return controllerBinding;
	}
}
