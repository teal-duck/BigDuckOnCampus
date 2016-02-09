package com.muscovy.game.save.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.entity.Item;
import com.muscovy.game.entity.Obstacle;
import com.muscovy.game.enums.RoomType;
import com.muscovy.game.level.DungeonRoom;
import com.muscovy.game.save.BaseSerializer;


@SuppressWarnings("rawtypes")
public class DungeonRoomSerializer extends BaseSerializer<DungeonRoom> {
	public DungeonRoomSerializer(MuscovyGame game) {
		super(game);
	}


	@Override
	public void write(Json json, DungeonRoom room, Class knownType) {
		json.writeObjectStart();
		json.writeValue("roomType", room.getRoomType());
		json.writeValue("hasUpDoor", room.hasUpDoor());
		json.writeValue("hasRightDoor", room.hasRightDoor());
		json.writeValue("hasDownDoor", room.hasDownDoor());
		json.writeValue("hasLeftDoor", room.hasLeftDoor());
		json.writeValue("allEnemiesDead", room.areAllEnemiesDead());

		json.writeArrayStart("obstacleList");
		for (Obstacle obstacle : room.getObstacleList()) {
			json.writeValue(obstacle);
		}
		json.writeArrayEnd();

		json.writeArrayStart("enemyList");
		for (Enemy enemy : room.getEnemyList()) {
			json.writeValue(enemy);
		}
		json.writeArrayEnd();

		json.writeArrayStart("itemList");
		for (Item item : room.getItemList()) {
			json.writeValue(item);
		}
		json.writeArrayEnd();

		json.writeObjectEnd();
	}


	@SuppressWarnings("unused")
	@Override
	public DungeonRoom read(Json json, JsonValue jsonData, Class type) {
		if (jsonData.isNull()) {
			return null;
		}

		DungeonRoom dungeonRoom = new DungeonRoom(game);

		JsonValue roomTypeValue = jsonData.get("roomType");
		RoomType roomType = RoomType.valueOf(roomTypeValue.asString());
		dungeonRoom.setRoomType(roomType);

		boolean hasUpDoor = jsonData.getBoolean("hasUpDoor");
		boolean hasRightDoor = jsonData.getBoolean("hasRightDoor");
		boolean hasDownDoor = jsonData.getBoolean("hasDownDoor");
		boolean hasLeftDoor = jsonData.getBoolean("hasLeftDoor");

		dungeonRoom.setHasUpDoor(hasUpDoor);
		dungeonRoom.setHasRightDoor(hasRightDoor);
		dungeonRoom.setHasDownDoor(hasDownDoor);
		dungeonRoom.setHasLeftDoor(hasLeftDoor);
		dungeonRoom.initialiseDoors();

		boolean allEnemiesDead = jsonData.getBoolean("allEnemiesDead");
		dungeonRoom.setEnemiesDead(allEnemiesDead);

		// TODO: Load obstacles, enemies and items
		JsonValue obstacleListValue = jsonData.get("obstacleList");
		JsonValue enemyListValue = jsonData.get("enemyList");
		JsonValue itemListValue = jsonData.get("itemList");

		return dungeonRoom;
	}
}