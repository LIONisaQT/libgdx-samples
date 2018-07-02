package com.missionbit.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.missionbit.LibGDXSamples;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = LibGDXSamples.WIDTH;
		config.height = LibGDXSamples.HEIGHT;
		config.title = LibGDXSamples.TITLE;
		new LwjglApplication(new LibGDXSamples(), config);
	}
}
