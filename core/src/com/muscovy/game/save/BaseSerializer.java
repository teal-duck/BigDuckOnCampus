package com.muscovy.game.save;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;


public abstract class BaseSerializer<T> implements Json.Serializer<T> {
	protected final MuscovyGame game;


	public BaseSerializer(MuscovyGame game) {
		this.game = game;
	}


	@SuppressWarnings("rawtypes")
	protected <L> L fromJson(Class<L> clazz, Json json, JsonValue data, Class type) {
		return json.getSerializer(clazz).read(json, data, type);
	}


	protected Vector2 loadVector2(JsonValue data) {
		float x = data.getFloat("x");
		float y = data.getFloat("y");
		return new Vector2(x, y);
	}

	// protected <L extends Enum<L>> L loadEnum(Class<L> enumType, JsonValue value) {
	// String string = value.asString();
	// if (string == null) {
	// return null;
	// } else {
	// return Enum.valueOf(enumType, string);
	// }
	// }
}
