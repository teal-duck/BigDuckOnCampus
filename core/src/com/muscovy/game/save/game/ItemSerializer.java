package com.muscovy.game.save.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.entity.Item;


@SuppressWarnings("rawtypes")
public class ItemSerializer implements Serializer<Item> {
	@Override
	public void write(Json json, Item item, Class knownType) {
		json.writeObjectStart();
		json.writeValue("position", item.getPosition());
		json.writeValue("type", item.getType());
		json.writeObjectEnd();
	}


	@Override
	public Item read(Json json, JsonValue jsonData, Class type) {
		return null;
	}
}
