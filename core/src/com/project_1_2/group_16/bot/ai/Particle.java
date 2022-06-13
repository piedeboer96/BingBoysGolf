package com.project_1_2.group_16.bot.ai;

import com.project_1_2.group_16.bot.Agent;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;

/**
 * This class contains the particle which is used in the Particle swarm optimazation.
 */
public class Particle extends Agent {

    //LocalBest particle
    private Particle localBest;

    /**
     * Constructor for particle that initializes it's statevector with the passed statevector and runs the game.
     * @param sv statevector
     */
    public Particle(StateVector sv, Game game){
        super(sv, game);
    }

    /**
     * Constructor for particle
     * @return particle
     */
    public Particle createClone (){
        return new Particle(new StateVector(startX, startY, velX, velY), getGame());
    }
    public void setlocalBest(Particle best){
        localBest = best.createClone();
    }
    public Particle getlocalBest(){
        return localBest;
    }
}
