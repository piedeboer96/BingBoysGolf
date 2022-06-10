package com.project_1_2.group_16.bot.ai;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.bot.AdvancedBot;
import com.project_1_2.group_16.bot.BotHelper;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;

import java.util.ArrayList;
import java.util.List;

//MazeBot, basically brute force with better Heuristics/Fitness function
public class MazeBot extends AdvancedBot {
    public MazeBot(float startX, float startY, Game game){
        //Always set useFloodFill to true as maze bot relies on that specific Heuristic
        super(startX, startY, game, true);
    }

    @Override
    public List<Float> runBot() {
        ArrayList<float[]> candidates = (ArrayList<float[]>) BotHelper.availableVelocities(getStartX(), getStartY());
//        ArrayList<float[]> candidates = (ArrayList<float[]>) BotHelper.availableVelocities(getStartX(), getStartY());
        float bestFitness = Integer.MAX_VALUE;
        float [] bestCandidate = new float[2];
        if(BotHelper.getFloodFillFitness(getStartX(), getStartY()) == 0){
            float diffX = Input.VT.x - getStartX();
            float diffY = Input.VT.y - getStartY();
            Vector2 v2 = new Vector2(diffX, diffY);
            v2.nor();
            List<Float> toReturn = new ArrayList<Float>();
            toReturn.add(v2.x);
            toReturn.add(v2.y);
            return toReturn;
        }
        for (float[] candidate : candidates){
            StateVector sv = new StateVector(getStartX(), getStartY(), candidate[0], candidate[1]);
            getGame().runEngine(sv, null);
            float [] endPosition = new float[] {sv.x, sv.y};
            float curFitness =  BotHelper.getFloodFillFitness(endPosition[0], endPosition[1]);
            System.out.println("fitness = " + curFitness);
            if(curFitness < bestFitness){
                bestFitness = curFitness;
                bestCandidate = candidate;
            }
        }
        System.out.println("best Fitness = " + bestFitness);
        List<Float> toReturn = new ArrayList<Float>();
        toReturn.add(bestCandidate[0]); toReturn.add(bestCandidate[1]);
        return toReturn;
    }
}
