package com.muscovy.game.desktop;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.muscovy.game.MuscovyGame;


public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.x = -1;
		config.y = -1;
		config.width = 1280;
		config.height = 816; // 960;
		config.resizable = true;
		config.title = "Big Duck On Campus";
		config.useGL30 = false;
		config.allowSoftwareMode = true;
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
	
		new LwjglApplication(new MuscovyGame(), config);
	}
}
