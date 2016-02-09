package com.muscovy.game.save.control;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.input.Binding;
import com.muscovy.game.input.ControllerBinding;
import com.muscovy.game.input.KeyBinding;
import com.muscovy.game.save.BaseSerializer;


@SuppressWarnings("rawtypes")
public class BindingSerializer extends BaseSerializer<Binding> {
	public BindingSerializer(MuscovyGame game) {
		super(game);
	}


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

		KeyBinding keyBinding = fromJson(KeyBinding.class, json, keyBindingValue, type);
		ControllerBinding controllerBinding = fromJson(ControllerBinding.class, json, controllerBindingValue,
				type);

		Binding binding = new Binding(keyBinding, controllerBinding);
		return binding;
	}
}
