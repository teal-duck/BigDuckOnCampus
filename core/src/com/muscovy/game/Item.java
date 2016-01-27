package com.muscovy.game;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by ewh502 on 19/01/2016.
 */
public class Item extends Collidable {
	/**
	 * This will be for items which change your stats like fire rate etc, but not for pickups which increase your
	 * health etc
	 */

	public Item(Sprite sprite, Vector2 position) {
		super(sprite, position);
	}
}
