package com.muscovy.game.save.game;


import com.muscovy.game.Levels;
import com.muscovy.game.entity.PlayerCharacter;


public class SaveData {
	private final PlayerCharacter player;
	private final Levels levels;

	// private final int currentLevel;
	// private final int currentDungeonRoomX;
	// private final int currentDungeonRoomY;


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
