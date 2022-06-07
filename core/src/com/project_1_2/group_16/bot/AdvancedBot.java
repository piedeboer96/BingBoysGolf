package com.project_1_2.group_16.bot;

import com.project_1_2.group_16.gamelogic.Game;

import java.util.List;

public abstract class AdvancedBot {
    private float startX, startY;
    private Game game;
    private final boolean RANDOM;
    private final float randomBound = 0.5f;

    public AdvancedBot(float startX, float startY, Game game, boolean RANDOM){
        this.startX = startX;
        this.startY = startY;
        if(RANDOM){
            float [] fxy = this.randomize(startX, startY);
            this.startX = fxy[0];
            this.startY = fxy[1];
        }
        this.game = game;
        this.RANDOM = RANDOM;
    }
    public abstract List<Float> runBot();
    public float getStartX(){
        return startX;
    }
    public float getStartY(){
        return startY;
    }
    public Game getGame(){
        return game;
    }
    public boolean getRandom(){
       return this.RANDOM;
    }

    protected float[] randomize(float val1, float val2){
        double plusOrMinus = Math.random();
        double boundTwo = Math.random() * randomBound;
        float[] toReturn = new float[2];
        toReturn[0] = val1;
        toReturn[1] = val2;
        //positive or negative
        if(plusOrMinus<=0.5){
            toReturn[0] += (float) boundTwo;
        } else {
            toReturn[0] -= (float) boundTwo;
        }
        plusOrMinus = Math.random();
        if(plusOrMinus<=0.5){
            toReturn[1] += (float) boundTwo;
        } else {
            toReturn[1] -= (float) boundTwo;
        }
        return toReturn;
    }

}
