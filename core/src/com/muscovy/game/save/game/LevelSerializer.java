package com.muscovy.game.save.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.enums.LevelType;
import com.muscovy.game.enums.ObjectiveType;
import com.muscovy.game.level.DungeonRoom;
import com.muscovy.game.level.Level;
import com.muscovy.game.level.LevelParameters;
import com.muscovy.game.save.BaseSerializer;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Serialises a Level object.
 */
@SuppressWarnings("rawtypes")
public class LevelSerializer extends BaseSerializer<Level> {
	public LevelSerializer(MuscovyGame game) {
		super(game);
	}


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
		json.writeValue("groundFriction", level.getGroundFriction());
		json.writeValue("completed", level.isCompleted());

		json.writeArrayStart("levelArray");
		for (int y = 0; y < level.getRoomsHigh(); y += 1) {
			json.writeArrayStart();
			for (int x = 0; x < level.getRoomsWide(); x += 1) {
				DungeonRoom room = level.getRoom(x, y);
				json.writeValue(room);
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
		JsonValue levelTypeValue = jsonData.get("levelType");
		LevelType levelType = LevelType.valueOf(levelTypeValue.asString());

		JsonValue objectiveTypeValue = jsonData.get("objectiveType");
		ObjectiveType objectiveType = ObjectiveType.valueOf(objectiveTypeValue.asString());

		int roomsWide = jsonData.getInt("roomsWide");
		int roomsHigh = jsonData.getInt("roomsHigh");
		int startX = jsonData.getInt("startX");
		int startY = jsonData.getInt("startY");
		int roomCount = jsonData.getInt("roomCount");
		float groundFriction = jsonData.getFloat("groundFriction");
		boolean completed = jsonData.getBoolean("completed");

		LevelParameters levelParameters = new LevelParameters(roomsWide, roomsHigh, roomCount, startX, startY,
				objectiveType, groundFriction);
		levelParameters.setRoomCount(roomCount);

		JsonValue levelArrayParentValue = jsonData.get("levelArray");
		JsonValue levelArrayValue = levelArrayParentValue.child;
		JsonValue dungeonRoomValue = null;

		int y = 0;
		int x = 0;
		DungeonRoom[][] levelArray = new DungeonRoom[roomsHigh][roomsWide];

		while (levelArrayValue != null) {
			x = 0;
			dungeonRoomValue = levelArrayValue.child;

			while (dungeonRoomValue != null) {
				DungeonRoom room = fromJson(DungeonRoom.class, json, dungeonRoomValue, type);
				if (room != null) {
					room.loadBackgroundTexture(levelType);
				}
				levelArray[y][x] = room;
				dungeonRoomValue = dungeonRoomValue.next;
				x += 1;
			}

			levelArrayValue = levelArrayValue.next;
			y += 1;
		}

		Level level = new Level(levelArray, levelType, levelParameters);
		level.setCompleted(completed);

		JsonValue visitedArrayParentValue = jsonData.get("visited");
		JsonValue visitedArrayValue = visitedArrayParentValue.child;
		JsonValue visitedValue = null;

		y = 0;
		x = 0;

		while (visitedArrayValue != null) {
			x = 0;
			visitedValue = visitedArrayValue.child;

			while (visitedValue != null) {
				if (visitedValue.asBoolean()) {
					level.markRoomVisited(x, y);
				}
				visitedValue = visitedValue.next;
				x += 1;
			}

			visitedArrayValue = visitedArrayValue.next;
			y += 1;
		}

		return level;
	}
}
