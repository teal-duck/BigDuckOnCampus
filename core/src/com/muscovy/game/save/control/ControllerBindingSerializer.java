package com.muscovy.game.save.control;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.input.ControllerBinding;


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
		return null;
	}

}
