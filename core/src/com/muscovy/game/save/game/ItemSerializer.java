package com.muscovy.game.save.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Item;
import com.muscovy.game.save.BaseSerializer;


@SuppressWarnings("rawtypes")
public class ItemSerializer extends BaseSerializer<Item> {
	public ItemSerializer(MuscovyGame game) {
		super(game);
	}


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
