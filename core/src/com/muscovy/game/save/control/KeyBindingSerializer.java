package com.muscovy.game.save.control;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.input.KeyBinding;
import com.muscovy.game.save.BaseSerializer;


@SuppressWarnings("rawtypes")
public class KeyBindingSerializer extends BaseSerializer<KeyBinding> {
	public KeyBindingSerializer(MuscovyGame game) {
		super(game);
	}


	@Override
	public void write(Json json, KeyBinding keyBinding, Class knownType) {
		json.writeObjectStart();
		json.writeValue("primary", keyBinding.getPrimary());
		json.writeValue("secondary", keyBinding.getSecondary());
		json.writeObjectEnd();
	}


	@Override
	public KeyBinding read(Json json, JsonValue jsonData, Class type) {
		int primary = jsonData.getInt("primary");
		int secondary = jsonData.getInt("secondary");

		KeyBinding keyBinding = new KeyBinding(primary, secondary);
		return keyBinding;
	}
}