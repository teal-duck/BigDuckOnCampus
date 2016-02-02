package com.muscovy.game.save;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.Levels;
import com.muscovy.game.entity.PlayerCharacter;


@SuppressWarnings("rawtypes")
public class SaveDataSerializer implements Json.Serializer<SaveData> {
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
		// System.out.println("--");
		// System.out.println(playerValue.toString());
		// System.out.println("--");

		PlayerCharacter player = json.getSerializer(PlayerCharacter.class).read(json, playerValue, type);
		Levels levels = json.getSerializer(Levels.class).read(json, levelsValue, type);

		SaveData saveData = new SaveData(player, levels);
		return saveData;
	}

}
