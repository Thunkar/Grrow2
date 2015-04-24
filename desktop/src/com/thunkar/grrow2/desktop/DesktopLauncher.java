package com.thunkar.grrow2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.salt.grrow.view.GrrowMain;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Grrow";
		config.width = 1366;
		config.height = 768;
		new LwjglApplication(new GrrowMain(), config);
	}
}
