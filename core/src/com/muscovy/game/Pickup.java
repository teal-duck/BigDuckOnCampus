package com.muscovy.game;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by ewh502 on 19/01/2016.
 */
public class Pickup extends Collidable {
	/**
	 * This will be for stuff like health pickups, not items which change your stats
	 */

	public Pickup(Sprite sprite, Vector2 position) {
		super(sprite, position);
	}
}
