package com.kilobolt.zombiebird.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kilobolt.zombiebird.ZBGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ZombieBird";
		cfg.width = 272;
		cfg.height = 408;
		new LwjglApplication(new ZBGame(), cfg);
	}
}
