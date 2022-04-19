package com.project_1_2.group_16;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.project_1_2.group_16.titlescreen.TitleScreen;

public class DesktopLauncher {

	public static void main(String[] arg) {
		//create titlescreen (comment out if on mac for now) TODO
//		TitleScreen titleScreen = new TitleScreen();
//		titleScreen.setUpGUI();
//		titleScreen.setUpButtonListeners();
//
//		// input screen buffer
//		while (titleScreen.getBuffer()) {
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}//

		// launch the game
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(Lwjgl3ApplicationConfiguration.getDisplayMode().width,
				Lwjgl3ApplicationConfiguration.getDisplayMode().height);
		config.setForegroundFPS(30);
		config.useVsync(false);
		new Lwjgl3Application(new App(), config);

		// reminder for self to build executable jar:
		// - open new terminal
		// - cd to desktop
		// - gradle dist command will create an executable jar in build/libs
		// use launch4j to wrap the jar
	}
}
