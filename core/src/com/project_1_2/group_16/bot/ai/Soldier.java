package com.project_1_2.group_16.bot.ai;

import com.project_1_2.group_16.bot.Agent;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;

/**
 * Individual class for soldier (agent) for usage of the BRO algorithm.
 */
public class Soldier extends Agent {
    public int damageCounter;

    /**
     * Constructor for soldier.
     */
    public Soldier(StateVector sv, Game game){
        super(sv, game);
    }

    /**
     * Soldier cloning factory
     * THIS IS REALLY INEFFICIENT, PLEASE FIND BETTER SOLUTION LATER LMAO
     */
    public Soldier createClone (){
        return new Soldier(new StateVector(this.startX, this.startY, this.velX, this.velY), this.getGame());
    }

    @Override
    public String toString(){
        return "velX " + velX + " velY " + velY + " fitness " + fitness;
    }
}
