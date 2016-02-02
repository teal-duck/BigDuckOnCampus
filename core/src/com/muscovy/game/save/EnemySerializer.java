package com.muscovy.game.save;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.entity.Enemy;


@SuppressWarnings("rawtypes")
public class EnemySerializer implements Serializer<Enemy> {
	@Override
	public void write(Json json, Enemy enemy, Class knownType) {
		json.writeObjectStart();
		json.writeValue("position", enemy.getPosition());
		json.writeValue("movementType", enemy.getMovementType());
		json.writeValue("attackType", enemy.getAttackType());
		json.writeValue("shotType", enemy.getShotType());
		json.writeValue("touchDamage", enemy.getTouchDamage());
		json.writeValue("health", enemy.getCurrentHealth());
		json.writeObjectEnd();
	}


	@Override
	public Enemy read(Json json, JsonValue jsonData, Class type) {
		return null;
	}
}
