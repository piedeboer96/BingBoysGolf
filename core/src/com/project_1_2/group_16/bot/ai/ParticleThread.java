package com.project_1_2.group_16.bot.ai;

import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;


/**
 * Class used for multithreading in PSO
 */
public class ParticleThread extends Thread{

    StateVector sv;
    Game game;
    Particle particle;

    /**
     * constructor for the particle thread
     * @param sv statevector
     * @param game game object
     */
    public ParticleThread(StateVector sv, Game game){
        this.sv = sv;
        this.game = game;
    }


    /**
     * Code which gets excecuted when the thread is started
     */
    @Override
    public void run() {
        this.particle = new Particle(this.sv, this.game);
    }

    /**
     * Method which returns the particle formed in the run method
     * @return particle
     */
    public Particle getParticle(){
        return this.particle;
    }

}
