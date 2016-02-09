package com.muscovy.game.save.game;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.entity.Enemy;
import com.muscovy.game.enums.AttackType;
import com.muscovy.game.enums.EnemyShotType;
import com.muscovy.game.enums.MovementType;
import com.muscovy.game.enums.ProjectileType;
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
		json.writeValue("friction", enemy.getFriction());
		json.writeValue("accelerationSpeed", enemy.getAccelerationSpeed());
		json.writeValue("textureName", enemy.getTextureName());
		json.writeValue("movementType", enemy.getMovementType());
		json.writeValue("attackType", enemy.getAttackType());
		json.writeValue("shotType", enemy.getShotType());
		json.writeValue("projectileType", enemy.getProjectileType());
		json.writeValue("touchDamage", enemy.getTouchDamage());
		json.writeValue("health", enemy.getCurrentHealth());
		json.writeValue("maxAttackInterval", enemy.getMaxAttackInterval());
		json.writeValue("attackRandomness", enemy.getAttackRandomness());
		json.writeObjectEnd();
	}


	@Override
	public Enemy read(Json json, JsonValue jsonData, Class type) {
		Vector2 position = loadVector2(jsonData.get("position"));
		String textureName = jsonData.getString("textureName");

		JsonValue movementTypeValue = jsonData.get("movementType");
		MovementType movementType = MovementType.valueOf(movementTypeValue.asString());

		JsonValue attackTypeValue = jsonData.get("attackType");
		AttackType attackType = AttackType.valueOf(attackTypeValue.asString());

		JsonValue shotTypeValue = jsonData.get("shotType");
		EnemyShotType shotType = EnemyShotType.valueOf(shotTypeValue.asString());

		JsonValue projectileTypeValue = jsonData.get("projectileType");
		ProjectileType projectileType = ProjectileType.valueOf(projectileTypeValue.asString());

		float friction = jsonData.getFloat("friction");
		float accelerationSpeed = jsonData.getFloat("accelerationSpeed");
		float touchDamage = jsonData.getFloat("touchDamage");
		float health = jsonData.getFloat("health");
		float maxAttackInterval = jsonData.getFloat("maxAttackInterval");
		float attackRandomness = jsonData.getFloat("attackRandomness");

		Enemy enemy = new Enemy(game, textureName, position);
		enemy.setMovementType(movementType);
		enemy.setAttackType(attackType);
		enemy.setShotType(shotType);
		enemy.setProjectileType(projectileType);
		enemy.setFriction(friction);
		enemy.setAccelerationSpeed(accelerationSpeed);
		enemy.setTouchDamage(touchDamage);
		enemy.setCurrentHealth(health);
		enemy.setMaxAttackInterval(maxAttackInterval);
		enemy.setAttackRandomness(attackRandomness);

		return enemy;
	}
}
