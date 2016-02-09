package com.muscovy.game.save.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.Levels;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.save.BaseSerializer;


@SuppressWarnings("rawtypes")
public class SaveDataSerializer extends BaseSerializer<SaveData> {
	public SaveDataSerializer(MuscovyGame game) {
		super(game);
	}


	@Override
	public void write(Json json, SaveData saveData, Class knownType) {
		json.writeObjectStart();
		json.writeValue("player", saveData.getPlayer());
		json.writeValue("levels", saveData.getLevels());
		json.writeObjectEnd();
	}


	@Override
	public SaveData read(Json json, JsonValue jsonData, Class type) {
		JsonValue playerValue = jsonData.get("player");
		JsonValue levelsValue = jsonData.get("levels");

		PlayerCharacter player = fromJson(PlayerCharacter.class, json, playerValue, type);
		Levels levels = fromJson(Levels.class, json, levelsValue, type);

		SaveData saveData = new SaveData(player, levels);
		return saveData;
	}
}
