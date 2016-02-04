package com.muscovy.game.save.control;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.input.Action;
import com.muscovy.game.input.Binding;
import com.muscovy.game.input.ControlMap;


@SuppressWarnings("rawtypes")
public class ControlMapSerializer implements Json.Serializer<ControlMap> {
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
		if (actionValue != null) {
			do {
				Action action = Action.valueOf(actionValue.name);
				Binding binding = json.getSerializer(Binding.class).read(json, actionValue, type);
				controlMap.addBindingForAction(action, binding);
			} while ((actionValue = actionValue.next) != null);
		}

		return controlMap;
	}

}
