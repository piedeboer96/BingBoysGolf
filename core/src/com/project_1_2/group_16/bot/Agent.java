package com.project_1_2.group_16.bot;

import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;

/**
 * An abstract class used for AI simulations.
 */
public abstract class Agent {

    public float startX, startY, velX, velY;
    private final Game game;
    public float fitness = Integer.MAX_VALUE;

    public Agent(StateVector sv, Game game){
        this.startX = sv.x;
        this.startY = sv.y;
        this.velX = sv.vx;
        this.velY = sv.vy;
        this.game = game;
        calcFitness();
    }

    /**
     * Calculate the fitness of a soldier based on the closest euclidean distance in each Numerical Solver iteration
     */
    public void calcFitness(){
        StateVector sv = new StateVector(startX, startY, velX, velY);
        game.runEngine(sv,this);
    }

    public Game getGame() {
        return game;
    }
}
