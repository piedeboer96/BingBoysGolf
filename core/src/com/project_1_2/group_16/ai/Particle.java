package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.math.StateVector;

public class Particle {

    private StateVector sv, init_sv;
    private Game game;
    public double fitness = Integer.MAX_VALUE;

    //LocalBest particle
    private Particle localBest;

    /**
     * Constructor for particle that initializes it's statevector with the passed statevector and runs the game.
     * @param sv statevector
     */
    public Particle(StateVector sv){
        this.sv = sv;
        this.init_sv =  new StateVector(sv.x, sv.y, sv.vx, sv.vy);
        this.game = new Game();
        this.game.setNumericalSolver(NumericalSolver.RK4);
        this.game.runEngine(this.sv, this, null, null);
    }

    /**
     * Constructor for particle
     * @param init initial statevector
     * @param sv statevector
     * @param fitness fitness
     */
    public Particle(StateVector init, StateVector sv, double fitness){
        this.fitness = fitness;
        this.init_sv = init;
        this.sv = sv;
    }

    //GETTERS AND SETTERS
    public StateVector getResultSV(){
        return this.sv;
    }
    public StateVector getSv(){
        return this.init_sv;
    }
    public double getFitness(){
        return this.fitness;
    }
    public float getVx(){
        return this.init_sv.vx;
    }
    public float getVy(){
        return this.init_sv.vy;
    }
    public float getX(){
        return  this.sv.x;
    }
    public float getY(){
        return this.sv.y;
    }
    public void setlocalBest(Particle best){
        localBest = Particle.clone(best);
    }
    public Particle getlocalBest(){
        return localBest;
    }

    /**
     * Clone method for particle
     * @param particle
     * @return cloned particle
     */
    public static Particle clone(Particle particle){
        Particle clone = new Particle(particle.getSv(), particle.getResultSV(), particle.getFitness());
        return clone;
    }
}
