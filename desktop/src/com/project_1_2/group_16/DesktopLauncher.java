package com.project_1_2.group_16;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;

public class DesktopLauncher {

	public static void main (String[] arg) {

		// set it false if you don't want to see the title and the input screen
		boolean runTitleScreen = true;

		// configure os
		if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
			App.os_is_windows = true;
		}

		//Uncomment for floadFIll
//		long start = System.currentTimeMillis();
//		NavigationGraph.fillGraphTable();
//		NavigationGraph.fload_fill((int)NavigationGraph.flood_i,(int)NavigationGraph.flood_j);
//		long end = System.currentTimeMillis();
//		System.out.println("runtime: " + (end-start));
		//System.out.println(Arrays.deepToString(matrixParcour));

		if (runTitleScreen) {
			TitleScreen titleScreen = new TitleScreen();
			titleScreen.setUpGUI();
			titleScreen.setUpButtonListeners();
		}
		else {
			// launch game
			Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
			config.setWindowedMode(Lwjgl3ApplicationConfiguration.getDisplayMode().width,
					Lwjgl3ApplicationConfiguration.getDisplayMode().height);
			config.setForegroundFPS(30);
			config.useVsync(false);
			new Lwjgl3Application(new App(), config);
		}
	}

	/**
	 * this method will run the game, it is useful for running the game from other classes
	 */
	public static void run (){


		// get inputs from the Input screen and set them for the App class
		// (call to input screen)
		 App.gV = new Vector2(InputScreen.x0, InputScreen.y0);
		 App.tV = new Vector2(InputScreen.xt, InputScreen.yt);
		 App.tR = InputScreen.r;
		 App.NUMBER_OF_SANDPITS = InputScreen.sandPitsNum;
		 App.NUMBER_OF_TREES = InputScreen.treesNum;
		 App.userOrBot = InputScreen.userOrBot;
		 System.out.println(App.userOrBot);

		// launch game
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(Lwjgl3ApplicationConfiguration.getDisplayMode().width,
				Lwjgl3ApplicationConfiguration.getDisplayMode().height);
		config.setForegroundFPS(30);
		config.useVsync(false);
		new Lwjgl3Application(new App(), config);
	}
}
