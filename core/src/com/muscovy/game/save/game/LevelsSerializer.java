package com.muscovy.game.save.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.Levels;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.level.Level;
import com.muscovy.game.save.BaseSerializer;


@SuppressWarnings("rawtypes")
public class LevelsSerializer extends BaseSerializer<Levels> {
	public LevelsSerializer(MuscovyGame game) {
		super(game);
	}


	@Override
	public void write(Json json, Levels levels, Class knownType) {
		json.writeArrayStart();
		for (Level level : levels.getLevels()) {
			json.writeValue(level);
		}
		json.writeArrayEnd();
	}


	@Override
	public Levels read(Json json, JsonValue jsonData, Class type) {
		int levelCount = jsonData.size;
		Level[] levels = new Level[levelCount];

		JsonValue levelValue = jsonData.child;
		int i = 0;
		while (levelValue != null) {
			levels[i] = fromJson(Level.class, json, levelValue, type);
			i += 1;
			levelValue = levelValue.next;
		}

		Levels levelsObject = new Levels(levels);
		return new Levels(levels);
	}
}
