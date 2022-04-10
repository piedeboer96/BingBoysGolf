package com.project_1_2.group_16;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.ai.NavigationGraph;

import java.util.Arrays;

import static com.project_1_2.group_16.ai.NavigationGraph.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		// configure os
		if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
			App.os_is_windows = true;
		}

		// get inputs
		// (call to input screen)
		//App.gV = new Vector2(xPositionOfGolfball, yPositionOfGolfball);
		//App.tV = new Vector2(xPositionOfTarget, yPositionOfTarget);
		//App.tR = targetRadius;
		//App.NUMBER_OF_SANDPITS = ...;
		//App.NUMBER_OF_TREES = ...;

		// launch game
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(Lwjgl3ApplicationConfiguration.getDisplayMode().width, 
							   Lwjgl3ApplicationConfiguration.getDisplayMode().height);
		config.setForegroundFPS(30);
		config.useVsync(false);


		//Uncomment for floadFIll
//		long start = System.currentTimeMillis();
//		NavigationGraph.fillGraphTable();
//		NavigationGraph.fload_fill((int)NavigationGraph.flood_i,(int)NavigationGraph.flood_j);
//		long end = System.currentTimeMillis();
//		System.out.println("runtime: " + (end-start));
		//System.out.println(Arrays.deepToString(matrixParcour));

		new Lwjgl3Application(new App(), config);
	}
}
