package com.project_1_2.group_16.misc;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Spline;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.math.*;
import com.project_1_2.group_16.physics.Physics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Tester {

    public static ArrayList<float[][]> tesStepSize(StateVector sv, int nmbr_steps, float stepSize, NumericalSolver[] solvers, int MaxIt){
        float[] initialValues = {sv.x, sv.y, sv.vx, sv.vy};
        ArrayList<float[][]> shrimp = new ArrayList<>();
        int local_nmbr_steps = nmbr_steps;
        float local_stepSize = stepSize;
        shrimp.add(testSolvers(new StateVector(initialValues[0], initialValues[1], initialValues[2], initialValues[3]), local_nmbr_steps, local_stepSize, solvers));
        for(int i = 0; i < MaxIt-1; i++){
            local_nmbr_steps *= 2;
            local_stepSize /= 2;
            System.out.println(local_nmbr_steps + " stepsize: " + local_stepSize);
            shrimp.add(testSolvers(new StateVector(initialValues[0], initialValues[1], initialValues[2], initialValues[3]), local_nmbr_steps, local_stepSize, solvers));

        }
        return shrimp;
    }

    /**
     * Method used to test multiple solvers with the same state vector
      * @param sv state vectory
     * @param nmbr_steps the amount of steps
     * @param stepSize the stepsize
     * @param solvers the solvers used for testing
     * @return an array which contains the end_x position, the end_y position, the end_x velocity and the end_y velocity
     */
    public static float[][] testSolvers(StateVector sv, int nmbr_steps, float stepSize, NumericalSolver[] solvers){
        float[] initialValues = {sv.x, sv.y, sv.vx, sv.vy};
        float[][] endValues = new float[solvers.length][4];
        int index = 0;
        for(NumericalSolver i : solvers){
            endValues[index] = testSolver(new StateVector(initialValues[0], initialValues[1], initialValues[2], initialValues[3]), nmbr_steps, stepSize, i);
            index++;
        }
        return endValues;
    }

    /**
     * Method used to test a single solver
     @param sv state vectory
      * @param nmbr_steps the amount of steps
     * @param stepSize the stepsize
     * @param solver the solver used for testing
     * @return
     */
    public static float[] testSolver(StateVector sv, int nmbr_steps, float stepSize, NumericalSolver solver){
        //float t = 0;
        for(int i = 1; i <= nmbr_steps; i++){
            solver.solve(stepSize, sv);
            //t += stepSize;
        }
        return new float[]{sv.x, sv.y, sv.vx, sv.vy};
    }

    public static String buildString(StateVector sv, NumericalSolver solver){
        return solver.toString() + ": [ x: " + sv.x  + " y: " + sv.y + " vx: " + sv.x + " vy: " + sv.y + " ]";
    }

    public static float[] analyticalAnswer(float velocity, float time){
        float acceleration = -Input.MUK * Physics.GRAV_CONSTANT;
        float distance = (float)(velocity*time + 0.5*acceleration*(time*time));
        float endVelocity = velocity + acceleration*time;
        return new float[]{distance, endVelocity};
    }


    public static void main(String[] args) {
        Terrain.setSpline(Input.H, new float[Spline.SPLINE_SIZE][Spline.SPLINE_SIZE]);
        Terrain.spline.createSpline();
        StateVector sv = new StateVector(0, 0, 5, 0);
        float stepSize = 0.1f;
        int nmbr_steps = (int)(1/stepSize);
        float[] result = analyticalAnswer(sv.vx, stepSize*nmbr_steps);
        System.out.print(Arrays.toString(result) + " ?= ");

        ArrayList<float[][]> data = tesStepSize(sv, nmbr_steps, stepSize, new NumericalSolver[]{new RK4(), new RK2(), new Euler(), new AdamsM4th(), new AdamsB2nd()}, 6);

        for(int i = 0; i < data.size(); i++){
            for(int j = 0; j < data.get(i).length; j++){
                System.out.println(Arrays.toString(data.get(i)[j]));
            }
            System.out.println();
        }

    }
}
