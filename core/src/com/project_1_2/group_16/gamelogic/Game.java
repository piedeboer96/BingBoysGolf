package com.project_1_2.group_16.gamelogic;

import com.project_1_2.group_16.App;
import com.project_1_2.group_16.math.*;

public class Game {

    /**
     * Solver
     */
    public static NumericalSolver solver;

    /**
     * State Vector
     */
    public static StateVector sv = new StateVector(0, 0, 0, 0);

    /**
     * A Method to run our Euler based Engine.
     * (for self, the best current value for euler 0.05 or 0.005 )
     */
    public static void runEuler() {
        Game.solver = new Euler();
    }

    /**
     * Method to run our RK4 based Engine.
     */
    public static void runRK4() {
        Game.solver = new RK4();
    }

    /**
     * Run the physics engine
     */
    public static void run() {
        Game.solver.solve(0.1f, Game.sv);
    }

    /**
     * Display a message that the user has completed the course
     */
    public static void endGame() {
        String message = "Congratulations! ";
        switch (App.hitsCounter) {
            case 1: message += " You got a hole-in-one! Unbelievable!"; break;
            default: message += "You finished the hole in "+App.hitsCounter+" shots!";
        }
        System.out.println(message);
    }
}
