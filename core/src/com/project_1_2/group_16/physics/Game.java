package com.project_1_2.group_16.physics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JOptionPane;

import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.math.*;


public class Game {

    public static Euler euler = new Euler();
    public static RK4 rk4 = new RK4();
    static float[] initialPos = new float[2];
    static float[] targetPos = new float[2];
    static float radius;
    static float[] frictionGrass = new float[2];
    static String heightF;
    static float[] sandPosX = new float[2];
    static float[] sandPosY = new float[2];
    static float[] frictionSand = new float[2];
    static float[] initialVelocity = new float[2];
    static HashMap<Integer, float[]> hM = new HashMap<>();
    static int treeCount;

    public static Terrain terrain;
    public static StateVector sv;

    /**
     * A Method to run our Euler based Engine.
     * (for self, the best current value for euler 0.05 or 0.005 )
     */
    public static void runEuler() {
        euler.euler_complete(0.05f, sv, terrain);
    }
    /**
     * Method to run our RK4 based Engine.
     */
    public static void runRK4() {
        rk4.rk4Complete(0.05f, sv, terrain);
    }

    /**
     * Read the input.txt file.
     */
    public static void getInput() {
        File f = new File(App.os_is_windows ? "./input.txt" : "../input.txt");
        FileReader fr = null;
        BufferedReader br = null;
        String line;
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }
        try {
            while((line = br.readLine()) != null){
                String [] split = line.split(" ");
                switch (split[0]) {
                    case "x0":
                        initialPos[0] = Float.parseFloat(split[2]);
                        break;
                    case "y0":
                        initialPos[1] = Float.parseFloat(split[2]);
                        break;
                    case "xt":
                        targetPos[0] = Float.parseFloat(split[2]);
                        break;
                    case "yt":
                        targetPos[1] = Float.parseFloat(split[2]);
                        break;
                    case "r":
                        radius = Float.parseFloat(split[2]);
                        break;
                    case "muk":
                        frictionGrass[0] = Float.parseFloat(split[2]);
                        break;
                    case "mus":
                        frictionGrass[1] = Float.parseFloat(split[2]);
                        break;
                    case "heightProfile":
                        heightF = split[2];
                        break;
                    case "sandPitX":
                        String [] splitAgain = split[2].split("<");
                        sandPosX[0] = Float.parseFloat(splitAgain[0]);
                        sandPosY[1] = Float.parseFloat(splitAgain[2]);
                        break;
                    case "sandPitY":
                        splitAgain = split[2].split("<");
                        sandPosY[0] = Float.parseFloat(splitAgain[0]);
                        sandPosY[1] = Float.parseFloat(splitAgain[2]);
                        break;
                    case "muks":
                        frictionSand[0] = Float.parseFloat(split[2]);
                        break;
                    case "muss":
                        frictionSand[1]= Float.parseFloat(split[2]);
                        break;
                    case "tree:" :
                        treeCount++;
                        float xParsed = Float.parseFloat(split[3]);
                        float yParsed = Float.parseFloat(split[6]);
                        float radParsed = Float.parseFloat(split[9]);
                        hM.put(treeCount, new float[] {xParsed, yParsed, radParsed});
                        break;
                    case "vx":
                        initialVelocity[0] = Float.parseFloat(split[2]);
                        break;
                    case "vy":
                        initialVelocity[1] = Float.parseFloat(split[2]);
                        break;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // check if the max velocity isn't too high
        if (Physics.magnitude(initialVelocity[0], initialVelocity[1]) > 5) {
            JOptionPane.showMessageDialog(null, "maximum velocity reached", "error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        terrain = new Terrain(initialPos, targetPos, radius, frictionGrass, heightF, sandPosX, sandPosY, frictionSand, hM);
        sv = new StateVector(terrain.initialPos[0], terrain.initialPos[1], initialVelocity[0], initialVelocity[1]);
    }
}
