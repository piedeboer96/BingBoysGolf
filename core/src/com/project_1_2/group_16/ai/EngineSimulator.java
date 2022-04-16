package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.math.RK4;
import com.project_1_2.group_16.math.Euler;
import com.project_1_2.group_16.math.StateVector;

/**
 * Class for bots to simulate the engines
 */
public class EngineSimulator {
    float startingPos_x = 0;
    float startingPos_y = 0;
    float startingVelocity_x = 0;
    float startingVelocity_y = 0;
    StateVector sv;
    RK4 rk4 = new RK4();

    public EngineSimulator(float startingPos_x, float startingPos_y, float startingVelocity_x, float startingVelocity_y){
        sv = new StateVector(startingPos_x, startingPos_y, startingVelocity_x, startingVelocity_y);
    }

    public static void main(String[] args) {
        System.out.println("here");
        EngineSimulator es = new EngineSimulator(0,0,1, 0);
        System.out.println("hey");
        es.runRK4ai();
    }

    //Runs until ball stops
    public void runRK4ai(){
        System.out.println("hey");
        int i = 0;
        while(i<100){
            System.out.println(sv);
            sv = rk4.solveRK4oneStep(sv, rk4.stepSize);
            i++;
        }
        System.out.println("im here");
    }
}
