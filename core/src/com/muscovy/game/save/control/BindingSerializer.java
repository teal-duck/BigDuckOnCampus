package com.muscovy.game.save.control;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.input.Binding;
import com.muscovy.game.input.ControllerBinding;
import com.muscovy.game.input.KeyBinding;


@SuppressWarnings("rawtypes")
public class BindingSerializer implements Json.Serializer<Binding> {
	@Override
	public void write(Json json, Binding binding, Class knownType) {
		json.writeObjectStart();
		json.writeValue("keyBinding", binding.getKeyBinding());
		json.writeValue("controllerBinding", binding.getControllerBinding());
		json.writeObjectEnd();
	}


	@Override
	public Binding read(Json json, JsonValue jsonData, Class type) {
		JsonValue keyBindingValue = jsonData.get("keyBinding");
		JsonValue controllerBindingValue = jsonData.get("controllerBinding");

		KeyBinding keyBinding = json.getSerializer(KeyBinding.class).read(json, keyBindingValue, type);
		ControllerBinding controllerBinding = json.getSerializer(ControllerBinding.class).read(json,
				controllerBindingValue, type);

		Binding binding = new Binding(keyBinding, controllerBinding);
		return binding;
	}

}
