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


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Saver for a SaveData object.
 */
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
