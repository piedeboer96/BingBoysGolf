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
    Euler euler = new Euler();

    public EngineSimulator(float startingPos_x, float startingPos_y, float startingVelocity_x, float startingVelocity_y){
        sv = new StateVector(startingPos_x, startingPos_y, startingVelocity_x, startingVelocity_y);
    }

    public static void main(String[] args) {
    }

    //Runs until ball stops
    public void runRK4ai(){
        while(!rk4.stop){
            sv = rk4.solveRK4oneStep(sv, rk4.stepSize);
        }
    }
    public void runEulerai(){
        while(!euler.stop){
            sv = euler.solveEulerai(sv, euler.numericalStepSize);
        }
        System.out.println(sv);
    }
}
