package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
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
    float endPos_X = 0;
    float endPos_Y = 0;
    StateVector sv;
    RK4 rk4 = new RK4();
    Euler euler = new Euler();
    Particle particle;
    boolean particleSimulation = false;

    public EngineSimulator(float startingPos_x, float startingPos_y, float startingVelocity_x, float startingVelocity_y, Particle particle){
        sv = new StateVector(startingPos_x, startingPos_y, startingVelocity_x, startingVelocity_y);
        this.particle = particle;
        particleSimulation = true;
    }
    public EngineSimulator(float startingPos_x, float startingPos_y, float startingVelocity_x, float startingVelocity_y){
        sv = new StateVector(startingPos_x, startingPos_y, startingVelocity_x, startingVelocity_y);
    }


    //Runs until ball stops
    public void runRK4ai(){
        while(!rk4.stop){
            sv = rk4.solveRK4oneStep(sv, rk4.stepSize);
            if(particleSimulation){
                if(sv.pos_x!=-1.0f && sv.pos_y!=-1 && Score.calculateEucledianDistance(Input.VT.x, Input.VT.y, sv.pos_x, sv.pos_y) < particle.getScore()){
                    particle.setScore(Score.calculateEucledianDistance(Input.VT.x, Input.VT.y, sv.pos_x, sv.pos_y));
                }
            }
        }
        endPos_X = sv.pos_x;
        endPos_Y = sv.pos_y;
    }
    public void runEulerai(){
        while(!euler.stop){
            sv = euler.solveEulerai(sv, euler.numericalStepSize);

        }
        endPos_X = sv.pos_x;
        endPos_Y = sv.pos_y;
    }
}
