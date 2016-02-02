package com.muscovy.game.save;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.muscovy.game.Levels;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.entity.Item;
import com.muscovy.game.entity.Obstacle;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.level.DungeonRoom;
import com.muscovy.game.level.Level;


public class SaveGame {
	// https://github.com/libgdx/libgdx/wiki/Reading-&-writing-JSON
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
		json.setSerializer(Level.class, new LevelSerializer());
		json.setSerializer(DungeonRoom.class, new DungeonRoomSerializer());
		json.setSerializer(Obstacle.class, new ObstacleSerializer());
		json.setSerializer(Enemy.class, new EnemySerializer());
		json.setSerializer(Item.class, new ItemSerializer());
	}


	public String getSaveString(SaveData saveData) {
		return json.prettyPrint(saveData);
	}


	public SaveData loadFromSaveString(String string) {
		SaveData saveData = json.fromJson(SaveData.class, string);
		return saveData;
	}
}
