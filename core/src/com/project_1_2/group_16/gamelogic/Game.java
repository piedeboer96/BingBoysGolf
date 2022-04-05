package com.project_1_2.group_16.gamelogic;

import com.project_1_2.group_16.App;
import com.project_1_2.group_16.math.*;


public class Game {

    public static Euler euler = new Euler();
    public static RK4 rk4 = new RK4();

    public static StateVector sv;

    /**
     * A Method to run our Euler based Engine.
     * (for self, the best current value for euler 0.05 or 0.005 )
     */
    public static void runEuler() {
        euler.solve(0.05f, sv);
    }
    /**
     * Method to run our RK4 based Engine.
     */
    public static void runRK4() {
        rk4.solve(0.05f, sv);
    }
}
