package com.muscovy.game.save;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.muscovy.game.Levels;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.PlayerCharacter;


public class SaveGame {
	@SuppressWarnings("unused")
	private final MuscovyGame game;
	private final Json json;


	public SaveGame(MuscovyGame game) {
		this.game = game;

		json = new Json();
		// json.setTypeName(null);
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		json.setOutputType(OutputType.json);

		json.setSerializer(SaveData.class, new SaveDataSerializer());
		json.setSerializer(PlayerCharacter.class, new PlayerCharacterSerializer(game));
		json.setSerializer(Levels.class, new LevelsSerializer());
	}


	// https://github.com/libgdx/libgdx/wiki/Reading-&-writing-JSON
	public String getSaveString(SaveData saveData) {
		// String string = json.prettyPrint(saveData.getPlayer());
		// return string;
		return json.prettyPrint(saveData);
	}


	public SaveData loadFromSaveString(String string) {
		// PlayerCharacter player = json.fromJson(PlayerCharacter.class, string);
		SaveData saveData = json.fromJson(SaveData.class, string);
		return saveData;
	}
}
