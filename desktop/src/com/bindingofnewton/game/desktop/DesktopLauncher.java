package com.bindingofnewton.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bindingofnewton.game.BindingOfNewton;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// This is fucking broken (resolution has to be fixed, switch to borderless window)
		config.fullscreen = false;
		new LwjglApplication(new BindingOfNewton(), config);
	}
}
