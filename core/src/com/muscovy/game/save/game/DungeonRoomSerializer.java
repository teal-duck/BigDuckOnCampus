package com.muscovy.game.save.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.entity.Item;
import com.muscovy.game.entity.Obstacle;
import com.muscovy.game.level.DungeonRoom;


@SuppressWarnings("rawtypes")
public class DungeonRoomSerializer implements Serializer<DungeonRoom> {
	@Override
	public void write(Json json, DungeonRoom room, Class knownType) {
		json.writeObjectStart();
		json.writeValue("roomType", room.getRoomType());
		json.writeValue("hasUpDoor", room.hasUpDoor());
		json.writeValue("hasRightDoor", room.hasRightDoor());
		json.writeValue("hasDownDoor", room.hasDownDoor());
		json.writeValue("hasLeftDoor", room.hasLeftDoor());
		json.writeValue("allEnemiesDead", room.areAllEnemiesDead());

		json.writeArrayStart("obstacles");
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


	@Override
	public DungeonRoom read(Json json, JsonValue jsonData, Class type) {
		return null;
	}
}
