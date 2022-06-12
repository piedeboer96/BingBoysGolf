package com.project_1_2.group_16.bot.ai;

import com.project_1_2.group_16.bot.Agent;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;

/**
 * This is our neighbour class used in Simulated annealing.
 */
public class Neighbour extends Agent {
    /**
     * Constructor of the neighbour class
     * @param sv statevector
     */
    public Neighbour(StateVector sv, Game game){
        super(sv, game);
    }

    /**
     * Constructor which uses a neighbour
     */
    public Neighbour createClone (){
        return new Neighbour(new StateVector(this.startX, this.startY, this.velX, this.velY), getGame());
    }


    public float getFitness(){
        return this.fitness;
    }

    public float getVx(){
        return this.velX;
    }

    public float getVy(){
        return this.velY;
    }
}
