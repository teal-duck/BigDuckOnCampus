package com.muscovy.game.save.control;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.input.Action;
import com.muscovy.game.input.Binding;
import com.muscovy.game.input.ControlMap;
import com.muscovy.game.save.BaseSerializer;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Serialises a ControlMap object.
 */
@SuppressWarnings("rawtypes")
public class ControlMapSerializer extends BaseSerializer<ControlMap> {
	public ControlMapSerializer(MuscovyGame game) {
		super(game);
	}


	@Override
	public void write(Json json, ControlMap controlMap, Class knownType) {
		json.writeObjectStart();

		for (Action action : Action.values()) {
			json.writeValue(action.toString(), controlMap.getBindingForAction(action));
		}

		json.writeObjectEnd();
	}


	@Override
	public ControlMap read(Json json, JsonValue jsonData, Class type) {
		ControlMap controlMap = new ControlMap();

		JsonValue actionValue = jsonData.child;
		while (actionValue != null) {
			Action action = Action.valueOf(actionValue.name);
			Binding binding = fromJson(Binding.class, json, actionValue, type);
			controlMap.addBindingForAction(action, binding);
			actionValue = actionValue.next;
		}

		return controlMap;
	}
}
