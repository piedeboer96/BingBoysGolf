package com.project_1_2.group_16.bot;

import com.project_1_2.group_16.bot.ai.FloodFill;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.io.Input;
import com.project_1_2.group_16.math.Physics;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.math.ode.NumericalSolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods used for the AI algorithms
 */
public class BotHelper {
    private static FloodFill floodFill;
    private static int[][] scoreMatrix;

    /**
     * Method which calculates the eucledian distance
     * @param xt  the x position of the hole
     * @param yt the y position of the hole
     * @param endx the x position of the particle
     * @param endy the y position of the particle
     * @return the eucledian distance
     */
    public static float calculateEucledianDistance(float xt, float yt, float endx, float endy){
        return (float) Math.sqrt(Math.pow(xt-endx, 2) + Math.pow(yt-endy, 2));
    }

    public static int getFloodFillFitness(float x, float y){

        int fitness;
        try{
            if(Math.abs(x) > Terrain.MAP_EDGE || Math.abs(y) > Terrain.MAP_EDGE){
                return Integer.MAX_VALUE;
            }
            fitness = scoreMatrix[floodFill.coordinateToIndex(x)][floodFill.coordinateToIndex(y)];
        }catch (Exception e){
            fitness = Integer.MAX_VALUE;
        }
        return fitness;
    }

    public static void setFloodFillTable(){
        floodFill = new FloodFill(.025f);
        scoreMatrix = floodFill.runFloodFill(Input.VT.x, Input.VT.y);
//        floodFill.prettyPrint(scoreMatrix);
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
     * Static method to develop a list of candidates of possible solutions (velocityX, velocityY pair) for all the bots except the maze
     * @return the list of possible solutions
     */
    public static List<float[]> availableVelocities (float startX, float startY) {
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
                if(!Game.useFloodFill) {
                    if (Physics.magnitude(velX, velY) < 5.0f && checkIfBetter(velX, velY, startX, startY)) {
                        toReturn.add(new float[]{velX, velY});
                    }
                }else {
                    if (Physics.magnitude(velX, velY) < 5.0f) {
                        toReturn.add(new float[]{velX, velY});
                    }
                }
            }
        }

        return toReturn;
    }
    /**
     * Static method to help develop a list of candidates of possible solutions (velocityX, velocityY pair) for the bots
     * by generating a random point in a circle with a radius of 5
     * CODE TAKEN FROM : https://stackoverflow.com/questions/5837572/generate-a-random-point-within-a-circle-uniformly
     * @return the list of possible solutions
     */
    public static float[] randomVelocity(){
        float[] toReturn = new float[2];
        double r = 5 * Math.sqrt(Math.random());
        double theta = Math.random() * 2 * Math.PI;
        float x = (float)(r * Math.cos(theta));
        float y = (float)(r * Math.sin(theta));
        toReturn = new float[] {x, y};
        return toReturn;
    }

    public static List <float[]> mazeBotCandidates(){
        ArrayList<float[]>toReturn = new ArrayList<float[]>();
        for(int i=0; i<100; i++){
            double r = 5 * Math.sqrt(Math.random());
            double theta = Math.random() * 2 * Math.PI;
            float x = (float)(r * Math.cos(theta));
            float y = (float)(r * Math.sin(theta));
            float [] temp = new float[] {x, y};
            toReturn.add(temp);
        }
        return toReturn;
    }
}
