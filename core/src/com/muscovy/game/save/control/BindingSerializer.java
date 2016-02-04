package com.muscovy.game.save.control;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.input.Binding;


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
		return null;
	}

}
