package com.muscovy.game.save;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.Levels;


@SuppressWarnings("rawtypes")
public class LevelsSerializer implements Json.Serializer<Levels> {
	@Override
	public void write(Json json, Levels levels, Class knownType) {
		json.writeObjectStart();
		json.writeObjectEnd();
	}


	@Override
	public Levels read(Json json, JsonValue jsonData, Class type) {
		Levels levels = new Levels();
		return levels;
	}
}
