package com.muscovy.game.save.game;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.PlayerCharacter;
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
		json.writeValue("score", playerCharacter.getScore());
		json.writeValue("health", playerCharacter.getHealth());
		json.writeObjectEnd();
	}


	@Override
	public PlayerCharacter read(Json json, JsonValue jsonData, Class type) {
		JsonValue positionData = jsonData.get("position");
		float x = positionData.getFloat("x");
		float y = positionData.getFloat("y");
		Vector2 position = new Vector2(x, y);

		int score = jsonData.getInt("score");
		int health = jsonData.getInt("health");

		Sprite playerSprite = new Sprite();
		playerSprite.setRegion(game.getTextureMap().getTextureOrLoadFile(AssetLocations.PLAYER));
		Controller controller = game.getController();
		ControlMap controlMap = game.getControlMap();

		PlayerCharacter playerCharacter = new PlayerCharacter(game, playerSprite, position, controlMap,
				controller);
		playerCharacter.setScore(score);
		playerCharacter.setHealth(health);

		return playerCharacter;
	}
}
