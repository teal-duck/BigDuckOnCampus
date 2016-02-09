package com.muscovy.game.save.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.save.BaseSerializer;


@SuppressWarnings("rawtypes")
public class EnemySerializer extends BaseSerializer<Enemy> {
	public EnemySerializer(MuscovyGame game) {
		super(game);
	}


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
