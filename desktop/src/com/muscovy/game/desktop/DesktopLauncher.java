package com.muscovy.game.desktop;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.muscovy.game.MuscovyGame;


public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 816; // 960;
		config.resizable = true;
		config.title = "Big Duck On Campus";
		new LwjglApplication(new MuscovyGame(), config);
	}
}
