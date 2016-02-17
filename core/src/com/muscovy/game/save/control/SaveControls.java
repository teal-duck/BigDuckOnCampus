package com.muscovy.game.save.control;


import com.muscovy.game.MuscovyGame;
import com.muscovy.game.input.Binding;
import com.muscovy.game.input.ControlMap;
import com.muscovy.game.input.ControllerBinding;
import com.muscovy.game.input.KeyBinding;
import com.muscovy.game.save.Saver;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Saver for a control map.
 */
public class SaveControls extends Saver<ControlMap> {
	public SaveControls(MuscovyGame game) {
		super(game);
	}


	@Override
	protected void initialiseSerializers() {
		dataClass = ControlMap.class;

		json.setSerializer(ControlMap.class, new ControlMapSerializer(game));
		json.setSerializer(Binding.class, new BindingSerializer(game));
		json.setSerializer(KeyBinding.class, new KeyBindingSerializer(game));
		json.setSerializer(ControllerBinding.class, new ControllerBindingSerializer(game));
	}
}