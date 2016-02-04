package com.muscovy.game.save.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.level.DungeonRoom;
import com.muscovy.game.level.Level;


@SuppressWarnings("rawtypes")
public class LevelSerializer implements Serializer<Level> {
	@Override
	public void write(Json json, Level level, Class knownType) {
		json.writeObjectStart();
		json.writeValue("levelType", level.getLevelType());
		json.writeValue("objectiveType", level.getObjectiveType());
		json.writeValue("roomsWide", level.getRoomsWide());
		json.writeValue("roomsHigh", level.getRoomsHigh());
		json.writeValue("startX", level.getStartX());
		json.writeValue("startY", level.getStartY());
		json.writeValue("roomCount", level.getRoomCount());
		json.writeValue("completed", level.isCompleted());

		json.writeArrayStart("levelArray");
		for (int y = 0; y < level.getRoomsHigh(); y += 1) {
			json.writeArrayStart();
			for (int x = 0; x < level.getRoomsWide(); x += 1) {
				DungeonRoom room = level.getRoom(x, y);
				if (room != null) {
					json.writeValue(room);
				} else {
					json.writeObjectStart();
					json.writeObjectEnd();
				}
			}
			json.writeArrayEnd();
		}
		json.writeArrayEnd();

		json.writeArrayStart("visited");
		for (int y = 0; y < level.getRoomsHigh(); y += 1) {
			json.writeArrayStart();
			for (int x = 0; x < level.getRoomsWide(); x += 1) {
				json.writeValue(level.isRoomVisited(x, y));
			}
			json.writeArrayEnd();
		}
		json.writeArrayEnd();

		json.writeObjectEnd();
	}


	@Override
	public Level read(Json json, JsonValue jsonData, Class type) {
		return null;
	}
}
