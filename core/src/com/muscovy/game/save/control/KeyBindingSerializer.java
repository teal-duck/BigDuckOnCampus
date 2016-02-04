package com.muscovy.game.save.control;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.input.KeyBinding;


@SuppressWarnings("rawtypes")
public class KeyBindingSerializer implements Json.Serializer<KeyBinding> {
	@Override
	public void write(Json json, KeyBinding keyBinding, Class knownType) {
		json.writeObjectStart();
		json.writeValue("primary", keyBinding.getPrimary());
		json.writeValue("secondary", keyBinding.getSecondary());
		json.writeObjectEnd();
	}


	@Override
	public KeyBinding read(Json json, JsonValue jsonData, Class type) {
		JsonValue primaryValue = jsonData.get("primary");
		JsonValue secondaryValue = jsonData.get("secondary");

		int primary = primaryValue.asInt();
		int secondary = secondaryValue.asInt();

		KeyBinding keyBinding = new KeyBinding(primary, secondary);
		return keyBinding;
	}
}