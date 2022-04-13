package com.project_1_2.group_16;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.project_1_2.group_16.ai.FloodFill;
import com.project_1_2.group_16.titlescreen.TitleScreen;

public class DesktopLauncher {

	public static void main (String[] arg) {
		// configure os
		if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
			App.os_is_windows = true;
		}
		//laurent
		//Uncomment for floadFIll
		/*long start = System.currentTimeMillis();
		FloodFill.fillGraphTable();
		FloodFill.floodFill((int)FloodFill.flood_i,(int)FloodFill.flood_j);
		long end = System.currentTimeMillis();
		System.out.println("runtime: " + (end-start));
		//System.out.println(Arrays.deepToString(matrixParcour));*/

		// create titlescreen
		TitleScreen titleScreen = new TitleScreen();
		titleScreen.setUpGUI();
		titleScreen.setUpButtonListeners();

		// input screen buffer
		while (titleScreen.getBuffer()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {e.printStackTrace();}
		}

		// launch the game
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(Lwjgl3ApplicationConfiguration.getDisplayMode().width,
				Lwjgl3ApplicationConfiguration.getDisplayMode().height);
		config.setForegroundFPS(30);
		config.useVsync(false);
		new Lwjgl3Application(new App(), config);
	}
}
