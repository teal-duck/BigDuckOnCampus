package com.muscovy.game.save.control;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.muscovy.game.input.Binding;
import com.muscovy.game.input.ControlMap;
import com.muscovy.game.input.ControllerBinding;
import com.muscovy.game.input.KeyBinding;


public class SaveControls {
	private final Json json;


	public SaveControls() {

		json = new Json();
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		json.setOutputType(OutputType.json);

		json.setSerializer(ControlMap.class, new ControlMapSerializer());
		json.setSerializer(Binding.class, new BindingSerializer());
		json.setSerializer(KeyBinding.class, new KeyBindingSerializer());
		json.setSerializer(ControllerBinding.class, new ControllerBindingSerializer());
	}


	public String getSaveString(ControlMap controlMap) {
		return json.prettyPrint(controlMap);
	}


	public ControlMap loadFromSaveString(String string) {
		ControlMap controlMap = json.fromJson(ControlMap.class, string);
		return controlMap;
	}
}
