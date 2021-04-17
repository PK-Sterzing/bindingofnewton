package com.bindingofnewton.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bindingofnewton.game.BindingOfNewton;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = false;
		config.width = 1920;
		config.height = 1080;
		config.resizable = false;
		new LwjglApplication(new BindingOfNewton(), config);
	}
}
