package com.muscovy.game.save.game;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Obstacle;
import com.muscovy.game.save.BaseSerializer;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Serialises an Obstacle object.
 */
@SuppressWarnings("rawtypes")
public class ObstacleSerializer extends BaseSerializer<Obstacle> {
	public ObstacleSerializer(MuscovyGame game) {
		super(game);
	}


	@Override
	public void write(Json json, Obstacle obstacle, Class knownType) {
		json.writeObjectStart();
		json.writeValue("position", obstacle.getPosition());
		json.writeValue("textureName", obstacle.getTextureName());
		json.writeValue("damaging", obstacle.isDamaging());
		json.writeValue("touchDamage", obstacle.getTouchDamage());
		json.writeObjectEnd();
	}


	@Override
	public Obstacle read(Json json, JsonValue jsonData, Class type) {
		Vector2 position = loadVector2(jsonData.get("position"));
		String textureName = jsonData.getString("textureName");
		boolean damaging = jsonData.getBoolean("damaging");
		float touchDamage = jsonData.getFloat("touchDamage");

		Obstacle obstacle = new Obstacle(game, textureName, position);
		obstacle.setDamaging(damaging);
		obstacle.setTouchDamage(touchDamage);
		return obstacle;
	}
}
