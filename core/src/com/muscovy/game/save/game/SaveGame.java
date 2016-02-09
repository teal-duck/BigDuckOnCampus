package com.muscovy.game.save.game;


import com.muscovy.game.Levels;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.entity.Item;
import com.muscovy.game.entity.Obstacle;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.level.DungeonRoom;
import com.muscovy.game.level.Level;
import com.muscovy.game.save.Saver;


//public class SaveGame {
//	// https://github.com/libgdx/libgdx/wiki/Reading-&-writing-JSON
//	@SuppressWarnings("unused")
//	private final MuscovyGame game;
//	private final Json json;
//
//
//	public SaveGame(MuscovyGame game) {
//		this.game = game;
//
//		json = new Json();
//		// json.setTypeName(null);
//		json.setUsePrototypes(false);
//		json.setIgnoreUnknownFields(true);
//		json.setOutputType(OutputType.json);
//
//		json.setSerializer(SaveData.class, new SaveDataSerializer(game));
//		json.setSerializer(PlayerCharacter.class, new PlayerCharacterSerializer(game));
//		json.setSerializer(Levels.class, new LevelsSerializer(game));
//		json.setSerializer(Level.class, new LevelSerializer(game));
//		json.setSerializer(DungeonRoom.class, new DungeonRoomSerializer(game));
//		json.setSerializer(Obstacle.class, new ObstacleSerializer(game));
//		json.setSerializer(Enemy.class, new EnemySerializer(game));
//		json.setSerializer(Item.class, new ItemSerializer(game));
//
//	}
//
//
//	public String getPrettySaveString(SaveData saveData) {
//		return json.prettyPrint(saveData);
//	}
//
//
//	public void saveToFile(SaveData saveData, FileHandle fileHandle) {
//		json.toJson(saveData, fileHandle);
//	}
//
//
//	public SaveData loadFromSaveString(String string) {
//		return json.fromJson(SaveData.class, string);
//	}
//
//
//	public SaveData loadFromFile(FileHandle fileHandle) {
//		return json.fromJson(SaveData.class, fileHandle);
//	}
//}

public class SaveGame extends Saver<SaveData> {
	public SaveGame(MuscovyGame game) {
		super(game);
	}


	@Override
	protected void initialiseSerializers() {
		dataClass = SaveData.class;
		json.setSerializer(SaveData.class, new SaveDataSerializer(game));
		json.setSerializer(PlayerCharacter.class, new PlayerCharacterSerializer(game));
		json.setSerializer(Levels.class, new LevelsSerializer(game));
		json.setSerializer(Level.class, new LevelSerializer(game));
		json.setSerializer(DungeonRoom.class, new DungeonRoomSerializer(game));
		json.setSerializer(Obstacle.class, new ObstacleSerializer(game));
		json.setSerializer(Enemy.class, new EnemySerializer(game));
		json.setSerializer(Item.class, new ItemSerializer(game));
	}
}
