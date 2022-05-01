package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.physics.Physics;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Calculate score based on euclidean distance.
 */

public class Score {

    /**
     * Method which calculates the eucledian distance
     * @param xt  the x position of the hole
     * @param yt the y position of the hole
     * @param endx the x position of the particle
     * @param endy the y position of the particle
     * @return the eucledian distance
     */
    public static float calculateEucledianDistance(float xt, float yt, float endx, float endy){
        //will become floodfill output
        return (float) Math.sqrt(Math.pow(xt-endx, 2) + Math.pow(yt-endy, 2));
    }

    /**
     * Calculates a random valid velocity
     * @param minVelocity the minimum velocity
     * @param maxVelocity the maximum velocity
     * @return a velocity
     */
    public static float[] validVelocity(float minVelocity, float maxVelocity){
        float[] vxy = new float[2];
        vxy[0] = (float)(minVelocity + Math.random()*(maxVelocity-minVelocity));
        vxy[1] = (float)(minVelocity + Math.random()*(maxVelocity-minVelocity));
        if(Physics.magnitude(vxy[0], vxy[1]) > 5 || !checkIfBetter(vxy[0], vxy[1])){
            return validVelocity(minVelocity, maxVelocity);
        }
        return vxy;
    }
//    public static float [] calcHillDeriv (float velX, float velY, float stepSize){
//        float[] toReturn = new float[2];
//        float denom = 2 * stepSize;
//        Game g0 = new Game();
//        Game g1 = new Game();
//        Game g2 = new Game();
//        Game g3 = new Game();
//        g0.setNumericalSolver(NumericalSolver.RK4);
//        g0.runEngine(new StateVector(Input.V0.x, Input.V0.y, velX));
//
//        float dF_dVelX =
//    }
    public static boolean checkIfBetter(float velX, float velY){
        Game runner = new Game();
        runner.setNumericalSolver(NumericalSolver.RK4);
        StateVector sv = new StateVector(Input.V0.x, Input.V0.y, velX, velY);
        runner.run(sv, null);
        if(calculateEucledianDistance(sv.x, sv.y, Input.VT.x, Input.VT.y) < calculateEucledianDistance(Input.V0.x, Input.V0.y, Input.VT.x, Input.VT.y)){
            return true;
        }
        return false;
    }
    public static ArrayList<float[]> availableVelocities () {
        float [] minMaxValues = determineMinMax();
        System.out.println("array here : " + Arrays.toString(minMaxValues));
        float minVelX = -5.0f;
        float maxVelX = 5.0f;
        float minVelY = -5.0f;
        float maxVelY = 5.0f;
        float xH, yH;
        xH = (Math.abs(maxVelX - minVelX))/9.15f;
        yH = (Math.abs(maxVelY - minVelY))/9.15f;
        ArrayList<float[]>toReturn = new ArrayList<float[]>();
        for(float velX = minVelX; velX<=maxVelX; velX+=xH){
            for(float velY = minVelY; velY<=maxVelY; velY+=yH){
                if(Physics.magnitude(velX, velY) < 5.0f && checkIfBetter(velX, velY)){
                    toReturn.add(new float [] {velX, velY});
                }
            }
        }
        System.out.println(toReturn.size());
        return toReturn;
    }
    // idx 0 : minX, idx 1 : maxX, idx 2 : minY, idx 3 : maxY
    public static float[] determineMinMax (){
        float[] toReturn = new float[4];
        float xDifference = Input.VT.x - Input.V0.x;
        float yDifference = Input.VT.y - Input.V0.y;
        if(xDifference<0){
            toReturn[0] = -5;
            toReturn[1] = 0;
        }else {
            toReturn[0] = 0;
            toReturn[1] = 5;
        }
        if(yDifference<0){
            toReturn[2] = -5;
            toReturn[3] = 0;
        }else {
            toReturn[2] = 0;
            toReturn[3] = 5;
        }
        return toReturn;
    }

    public static void main(String[] args) {
        ArrayList<float[]> toPrint = availableVelocities();
        for(float[] f : toPrint){
            System.out.println(Arrays.toString(f));
        }
        System.out.println(toPrint.size());
    }
}
