package com.muscovy.game.save;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Provides helper functions for our serializers.
 *
 * @param <T>
 */
public abstract class BaseSerializer<T> implements Json.Serializer<T> {
	protected final MuscovyGame game;


	public BaseSerializer(MuscovyGame game) {
		this.game = game;
	}


	/**
	 * @param clazz
	 * @param json
	 * @param data
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected <L> L fromJson(Class<L> clazz, Json json, JsonValue data, Class type) {
		return json.getSerializer(clazz).read(json, data, type);
	}


	/**
	 * @param data
	 * @return
	 */
	protected Vector2 loadVector2(JsonValue data) {
		float x = data.getFloat("x");
		float y = data.getFloat("y");
		return new Vector2(x, y);
	}
}
