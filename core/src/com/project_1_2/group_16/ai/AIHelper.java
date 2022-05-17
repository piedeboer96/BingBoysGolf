package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.math.Physics;
import com.project_1_2.group_16.math.StateVector;

import java.util.ArrayList;

/**
 * Helper methods used for the AI algorithms
 */
public class AIHelper {

    /**
     * Method which calculates the eucledian distance
     * @param xt  the x position of the hole
     * @param yt the y position of the hole
     * @param endx the x position of the particle
     * @param endy the y position of the particle
     * @return the eucledian distance
     */
    public static float calculateEucledianDistance(float xt, float yt, float endx, float endy){
        //will become floodFill output
        return (float) Math.sqrt(Math.pow(xt-endx, 2) + Math.pow(yt-endy, 2));
    }

    /**
     * Calculates a random valid velocity
     * @param minVelocity the minimum velocity
     * @param maxVelocity the maximum velocity
     * @return a velocity
     */
    public static float[] validVelocity(float minVelocity, float maxVelocity, float startX, float startY){
        float[] vxy = new float[2];
        vxy[0] = (float)(minVelocity + Math.random()*(Math.abs(maxVelocity-minVelocity)));
        vxy[1] = (float)(minVelocity + Math.random()*(Math.abs(maxVelocity-minVelocity)));
        if(Physics.magnitude(vxy[0], vxy[1]) > 5 || !checkIfBetter(vxy[0], vxy[1], startX, startY)){
            return validVelocity(minVelocity, maxVelocity, startX, startY);
        }
        return vxy;
    }

    /**
     * Simulates one step of the Numerical solver using the given velocities and checks if it moves in a direction closer to
     * the hole
     * @param velX given x velocity
     * @param velY given y velocity
     * @return true if moves in a closer direction, else false
     */
    public static boolean checkIfBetter(float velX, float velY, float startX, float startY){
        Game runner = new Game();
        runner.setNumericalSolver(NumericalSolver.RK4);
        StateVector sv = new StateVector(startX, startY, velX, velY);
        runner.run(sv, null);
        if(calculateEucledianDistance(sv.x, sv.y, Input.VT.x, Input.VT.y) < calculateEucledianDistance(startX, startY, Input.VT.x, Input.VT.y)){
            return true;
        }
        return false;
    }

    /**
     * Static method to develop a list of candidates of possible solutions
     * @return the list of possible solutions
     */
    public static ArrayList<float[]> availableVelocities (float startX, float startY) {
        float minVelX = -5.0f;
        float maxVelX = 5.0f;
        float minVelY = -5.0f;
        float maxVelY = 5.0f;
        float xH, yH;
        xH = (Math.abs(maxVelX - minVelX))/6.15f;
        yH = (Math.abs(maxVelY - minVelY))/6.15f;
        ArrayList<float[]>toReturn = new ArrayList<float[]>();

        toReturn.add(new float[] {((Input.VT.x - startX)/(Math.abs(Input.VT.x - startX) + Math.abs(Input.VT.y-startY)))*5.0f, ((Input.VT.y-startY)/(Math.abs(Input.VT.x - startX) + Math.abs(Input.VT.y-startY)))*5.0f});

        for(float velX = minVelX; velX<=maxVelX; velX+=xH){
            for(float velY = minVelY; velY<=maxVelY; velY+=yH){
                if(Physics.magnitude(velX, velY) < 5.0f && checkIfBetter(velX, velY, startX, startY)){
                    toReturn.add(new float [] {velX, velY});
                }
            }
        }

        return toReturn;
    }
}
