package com.project_1_2.group_16;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.project_1_2.group_16.titlescreen.TitleScreen;

public class DesktopLauncher extends Thread {

	public final static DesktopLauncher launch = new DesktopLauncher();

	public static void main (String[] arg) {
		// configure os
		if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
			App.os_is_windows = true;
		}

		// set it false if you don't want to see the title and the input screen
		boolean runTitleScreen = false;

		/*Uncomment for floadFIll
		long start = System.currentTimeMillis();
		NavigationGraph.fillGraphTable();
		NavigationGraph.fload_fill((int)NavigationGraph.flood_i,(int)NavigationGraph.flood_j);
		long end = System.currentTimeMillis();
		System.out.println("runtime: " + (end-start));
		//System.out.println(Arrays.deepToString(matrixParcour));//*/

		if (runTitleScreen) {
			TitleScreen titleScreen = new TitleScreen();
			titleScreen.setUpGUI();
			titleScreen.setUpButtonListeners();

			while (titleScreen.inputScreen == null || !titleScreen.inputScreen.status) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		// launch game
		launch.start();
	}

	/**
	 * Runs the application
	 */
	@Override
	public void run() {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(Lwjgl3ApplicationConfiguration.getDisplayMode().width,
				Lwjgl3ApplicationConfiguration.getDisplayMode().height);
		config.setForegroundFPS(30);
		config.useVsync(false);
		new Lwjgl3Application(new App(), config);
	}
}
