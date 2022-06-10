package com.project_1_2.group_16.bot.ai;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.bot.AdvancedBot;
import com.project_1_2.group_16.bot.BotHelper;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//MazeBot, basically brute force with better Heuristics/Fitness function
public class MazeBot extends AdvancedBot {
    float[] bestCandidate = new float[4];
    public MazeBot(float startX, float startY, Game game){
        //Always set useFloodFill to true as maze bot relies on that specific Heuristic
        super(startX, startY, game, true);
    }

    /**
     * Runs the MazeBot
     * @return the velocity pair
     */
    @Override
    public List<Float> runBot() {
        if(BotHelper.getFloodFillFitness(getStartX(), getStartY()) <= 1){
            return makeSimpleShot();
        }
        return makeHeuristic_BasedShot();
    }

    /**
     * If the Heuristic Score is too low, no need to make a Heuristic-Based shot so just shoot into the direction
     * of the Target
     * @return list containing velocity w.r.t x and y direction
     */
    private List<Float> makeSimpleShot(){
        float diffX = Input.VT.x - getStartX();
        float diffY = Input.VT.y - getStartY();
        Vector2 v2 = new Vector2(diffX, diffY);
        v2.nor();
        List<Float> toReturn = new ArrayList<Float>();
        toReturn.add(v2.x);
        toReturn.add(v2.y);
        return toReturn;
    }

    /**
     * Method to determine best shot based on the Heuristic-Score in a brute-force like manner
     * we check two iterations into the future to optimize it
     * @return list containing velocity w.r.t x and y direction
     */
    private List<Float> makeHeuristic_BasedShot(){
        ArrayList<float[]> candidates = (ArrayList<float[]>) BotHelper.availableVelocities2();
        ArrayList<Shot> shots = new ArrayList<Shot>();
        float bestFitness = Integer.MAX_VALUE;
        bestCandidate = new float[2];
        for(float[] candidate_a : candidates){
            StateVector sv = new StateVector(getStartX(), getStartY(), candidate_a[0], candidate_a[1]);
            getGame().runEngine(sv, null);
            float tempFitness = BotHelper.getFloodFillFitness(sv.x, sv.y);
            shots.add(new Shot(sv.x, sv.y, candidate_a[0], candidate_a[1], tempFitness));
        }
        Shot bestShot = new Shot(0,0,0,0,0);
        Collections.sort(shots);
        for(int i=0; i<5; i++){
            Shot firstShot = shots.get(i);
            float tempBestFitness = Integer.MAX_VALUE;
            float newPosX = firstShot.newX; float newPosY = firstShot.newY;
            for(float[] candidate_b : candidates){
                StateVector sv = new StateVector(newPosX, newPosY, candidate_b[0], candidate_b[1]);
                getGame().runEngine(sv, null);
                float tempFitness = BotHelper.getFloodFillFitness(sv.x, sv.y);
                if(tempFitness < tempBestFitness){
                    tempBestFitness = tempFitness;
                }
            }
            if(tempBestFitness < bestFitness){
                bestFitness = tempBestFitness;
                bestShot = firstShot;
            }
        }
        List<Float> toReturn = new ArrayList<Float>();
        toReturn.add(bestShot.velX); toReturn.add(bestShot.velY);
        return toReturn;
    }

    class Shot implements Comparable<Shot> {
        float velX;
        float velY;
        float newX;
        float newY;
        float fitness;
        public Shot(float newX, float newY, float velX, float velY, float fitness){
            this.newX = newX;
            this.newY = newY;
            this.velX = velX;
            this.velY = velY;
            this.fitness = fitness;
        }
        //Sorts in Ascending Order based on Fitness (FloodFill distance)
        @Override
        public int compareTo(Shot other) {
            return (int) (this.fitness - other.fitness);
        }
    }
}
