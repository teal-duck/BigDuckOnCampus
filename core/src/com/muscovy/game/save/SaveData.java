package com.muscovy.game.save;


import com.muscovy.game.Levels;
import com.muscovy.game.entity.PlayerCharacter;


public class SaveData {
	private final PlayerCharacter player;
	private final Levels levels;


	public SaveData(PlayerCharacter player, Levels levels) {
		this.player = player;
		this.levels = levels;
	}


	public PlayerCharacter getPlayer() {
		return player;
	}


	public Levels getLevels() {
		return levels;
	}
}
