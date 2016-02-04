package com.muscovy.game.save.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.Levels;
import com.muscovy.game.level.Level;


@SuppressWarnings("rawtypes")
public class LevelsSerializer implements Json.Serializer<Levels> {
	@Override
	public void write(Json json, Levels levels, Class knownType) {
		json.writeArrayStart();
		int i = 0;
		for (Level level : levels.getLevels()) {
			json.writeValue(level);
			i += 1;
			if (i > 2) {
				// For testing as Eclipse console won't show the data for all 8 levels
				// break;
			}
		}
		json.writeArrayEnd();
	}


	@Override
	public Levels read(Json json, JsonValue jsonData, Class type) {
		int levelCount = jsonData.size;
		Level[] levels = new Level[levelCount];

		JsonValue levelValue = jsonData.child;
		if (levelValue != null) {
			int i = 0;
			do {
				// System.out.println(i + ": " + levelValue.toString());
				levels[i] = json.getSerializer(Level.class).read(json, levelValue, type);
				i += 1;
			} while ((levelValue = levelValue.next) != null);
		} else {
			System.out.println("Level value when loading levels is null");
		}

		Levels levelsObject = new Levels();
		levelsObject.setLevels(levels);
		return levelsObject;
	}
}
