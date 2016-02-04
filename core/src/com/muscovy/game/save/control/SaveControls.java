package com.muscovy.game.save.control;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.muscovy.game.input.Binding;
import com.muscovy.game.input.ControlMap;
import com.muscovy.game.input.ControllerBinding;
import com.muscovy.game.input.KeyBinding;


public class SaveControls {
	// @SuppressWarnings("unused")
	// private final MuscovyGame game;
	private final Json json;


	public SaveControls() { // MuscovyGame game) {
		// this.game = game;

		json = new Json();
		// json.setTypeName(null);
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		json.setOutputType(OutputType.json);

		json.setSerializer(ControlMap.class, new ControlMapSerializer());
		json.setSerializer(Binding.class, new BindingSerializer());
		json.setSerializer(KeyBinding.class, new KeyBindingSerializer());
		json.setSerializer(ControllerBinding.class, new ControllerBindingSerializer());

		// json.setSerializer(SaveData.class, new SaveDataSerializer());
		// json.setSerializer(PlayerCharacter.class, new PlayerCharacterSerializer(game));
		// json.setSerializer(Levels.class, new LevelsSerializer());
		// json.setSerializer(Level.class, new LevelSerializer());
		// json.setSerializer(DungeonRoom.class, new DungeonRoomSerializer());
		// json.setSerializer(Obstacle.class, new ObstacleSerializer());
		// json.setSerializer(Enemy.class, new EnemySerializer());
		// json.setSerializer(Item.class, new ItemSerializer());
	}


	public String getSaveString(ControlMap controlMap) {
		return json.prettyPrint(controlMap);
	}


	public ControlMap loadFromSaveString(String string) {
		ControlMap controlMap = json.fromJson(ControlMap.class, string);
		return controlMap;
	}

}
