package com.muscovy.game.save.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Obstacle;
import com.muscovy.game.save.BaseSerializer;


@SuppressWarnings("rawtypes")
public class ObstacleSerializer extends BaseSerializer<Obstacle> {
	public ObstacleSerializer(MuscovyGame game) {
		super(game);
	}


	@Override
	public void write(Json json, Obstacle obstacle, Class knownType) {
		json.writeObjectStart();
		json.writeValue("position", obstacle.getPosition());
		json.writeValue("damaging", obstacle.isDamaging());
		json.writeValue("touchDamage", obstacle.getTouchDamage());
		// json.writeValue("width", obstacle.getWidth());
		// json.writeValue("height", obstacle.getHeight());
		json.writeObjectEnd();
	}


	@Override
	public Obstacle read(Json json, JsonValue jsonData, Class type) {
		return null;
	}
}
