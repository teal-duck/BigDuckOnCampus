package com.muscovy.game.save.control;


import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.input.ControllerBinding;
import com.muscovy.game.input.ControllerBindingType;


@SuppressWarnings("rawtypes")
public class ControllerBindingSerializer implements Json.Serializer<ControllerBinding> {
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
		JsonValue indexValue = jsonData.get("index");
		JsonValue deadzoneValue = jsonData.get("deadzone");
		JsonValue povDirectionValue = jsonData.get("povDirection");

		ControllerBindingType controllerBindingType = ControllerBindingType
				.valueOf(controllerBindingTypeValue.asString());
		int index = indexValue.asInt();
		float deadzone = deadzoneValue.asFloat();

		PovDirection povDirection;
		String povString = povDirectionValue.asString();
		if (povString != null) {
			povDirection = PovDirection.valueOf(povString);
		} else {
			povDirection = null;
		}

		ControllerBinding controllerBinding = new ControllerBinding(controllerBindingType, index, deadzone,
				povDirection);
		return controllerBinding;
	}

}
