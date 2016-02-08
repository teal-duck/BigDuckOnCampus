package com.muscovy.game.save.control;


import com.muscovy.game.MuscovyGame;
import com.muscovy.game.input.Binding;
import com.muscovy.game.input.ControlMap;
import com.muscovy.game.input.ControllerBinding;
import com.muscovy.game.input.KeyBinding;
import com.muscovy.game.save.Saver;


//public class SaveControls {
//	private final MuscovyGame game;
//	private final Json json;
//
//
//	public SaveControls(MuscovyGame game) {
//		this.game = game;
//		json = new Json();
//		json.setUsePrototypes(false);
//		json.setIgnoreUnknownFields(true);
//		json.setOutputType(OutputType.json);
//
//		json.setSerializer(ControlMap.class, new ControlMapSerializer());
//		json.setSerializer(Binding.class, new BindingSerializer());
//		json.setSerializer(KeyBinding.class, new KeyBindingSerializer());
//		json.setSerializer(ControllerBinding.class, new ControllerBindingSerializer());
//	}
//
//
//	public String getSaveString(ControlMap controlMap) {
//		return json.prettyPrint(controlMap);
//	}
//
//
//	public ControlMap loadFromSaveString(String string) {
//		ControlMap controlMap = json.fromJson(ControlMap.class, string);
//		return controlMap;
//	}
//}

public class SaveControls extends Saver<ControlMap> {
	public SaveControls(MuscovyGame game) {
		super(game);
	}


	@Override
	protected void initialiseSerializers() {
		dataClass = ControlMap.class;
		json.setSerializer(ControlMap.class, new ControlMapSerializer());
		json.setSerializer(Binding.class, new BindingSerializer());
		json.setSerializer(KeyBinding.class, new KeyBindingSerializer());
		json.setSerializer(ControllerBinding.class, new ControllerBindingSerializer());
	}
}