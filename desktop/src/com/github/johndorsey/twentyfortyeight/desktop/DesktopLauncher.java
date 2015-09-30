package com.github.johndorsey.twentyfortyeight.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.johndorsey.twentyfortyeight.TwentyFortyEight;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "2048";
		config.width = 1080;
		config.height = 720;
		new LwjglApplication(new TwentyFortyEight(), config);
	}
}
