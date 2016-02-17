package com.muscovy.game.save.game;


import com.muscovy.game.Levels;
import com.muscovy.game.entity.PlayerCharacter;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: Groups together the data that needs to be saved.
 */
public class SaveData {
	private final PlayerCharacter player;
	private final Levels levels;


	public SaveData(PlayerCharacter player, Levels levels) {
		this.player = player;
		this.levels = levels;
	}


	/**
	 * @return
	 */
	public PlayerCharacter getPlayer() {
		return player;
	}


	/**
	 * @return
	 */
	public Levels getLevels() {
		return levels;
	}


	@Override
	public String toString() {
		return "SaveData(" + player.toString() + ", " + levels.toString() + ")";
	}
}
