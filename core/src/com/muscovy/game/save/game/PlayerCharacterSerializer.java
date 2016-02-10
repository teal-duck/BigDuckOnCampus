package com.muscovy.game.save.game;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Item;
import com.muscovy.game.entity.PlayerCharacter;
import com.muscovy.game.enums.ItemType;
import com.muscovy.game.input.ControlMap;
import com.muscovy.game.save.BaseSerializer;


@SuppressWarnings("rawtypes")
public class PlayerCharacterSerializer extends BaseSerializer<PlayerCharacter> {
	public PlayerCharacterSerializer(MuscovyGame game) {
		super(game);
	}


	@Override
	public void write(Json json, PlayerCharacter playerCharacter, Class knownType) {
		json.writeObjectStart();
		json.writeValue("position", playerCharacter.getPosition());
		json.writeValue("friction", playerCharacter.getFriction());
		json.writeValue("accelerationSpeed", playerCharacter.getAccelerationSpeed());
		json.writeValue("score", playerCharacter.getScore());
		json.writeValue("health", playerCharacter.getHealth());
		json.writeValue("maxFlightTime", playerCharacter.getMaxFlightTime());
		json.writeValue("flightSpeedScale", playerCharacter.getFlightSpeedScale());

		json.writeArrayStart("obtainedItems");
		for (ItemType item : playerCharacter.getObtainedItems()) {
			json.writeValue(item);
		}
		json.writeArrayEnd();

		json.writeObjectEnd();
	}


	@Override
	public PlayerCharacter read(Json json, JsonValue jsonData, Class type) {
		Vector2 position = loadVector2(jsonData.get("position"));
		float friction = jsonData.getFloat("friction");
		float accelerationSpeed = jsonData.getFloat("accelerationSpeed");
		int score = jsonData.getInt("score");
		int health = jsonData.getInt("health");
		float maxFlightTime = jsonData.getFloat("maxFlightTime");
		float flightSpeedScale = jsonData.getFloat("flightSpeedScale");

		Controller controller = game.getController();
		ControlMap controlMap = game.getControlMap();

		PlayerCharacter playerCharacter = new PlayerCharacter(game, AssetLocations.PLAYER, position, controlMap,
				controller);
		playerCharacter.getSprite().setRegion(game.getTextureMap().getTextureOrLoadFile(AssetLocations.PLAYER));
		playerCharacter.setFriction(friction);
		playerCharacter.setAccelerationSpeed(accelerationSpeed);
		playerCharacter.setScore(score);
		playerCharacter.setHealth(health);
		playerCharacter.setMaxFlightTime(maxFlightTime);
		playerCharacter.setFlightSpeedScale(flightSpeedScale);

		JsonValue obtainedItemsArrayValue = jsonData.get("obtainedItems");
		JsonValue obtainedItemValue = obtainedItemsArrayValue.child;
		while (obtainedItemValue != null) {
			ItemType itemType = ItemType.valueOf(obtainedItemValue.asString());
			Item item = new Item(game, ItemType.getItemTextureName(itemType), new Vector2(), itemType);
			item.applyToPlayer(playerCharacter);
			obtainedItemValue = obtainedItemValue.next;
		}

		return playerCharacter;
	}
}
