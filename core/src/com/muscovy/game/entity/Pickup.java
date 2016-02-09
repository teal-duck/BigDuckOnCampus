package com.muscovy.game.entity;


import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;


/**
 * Created by ewh502 on 19/01/2016.
 */
public class Pickup extends Collidable {
	/**
	 * This will be for stuff like health pickups, not items which change your stats
	 */

	// public Pickup(MuscovyGame game, Sprite sprite, Vector2 position) {
	// super(game, sprite, position);
	// }

	public Pickup(MuscovyGame game, String textureName, Vector2 position) {
		super(game, textureName, position);
	}
}
