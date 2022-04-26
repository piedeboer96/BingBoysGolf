package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.math.StateVector;

public class Neighbour {
    public static final float STEP_SIZE = SA.SOLVER_STEP_SIZE;
    private StateVector sv;
    private Game game;
    private double fitness;

    public Neighbour(StateVector sv){
        this.sv = sv;
        this.game = new Game();
        this.game.setNumericalSolver(NumericalSolver.RK4);
        //this.game = new EngineSimulator(Input.V0.x, Input.V0.y, sv.vx, sv.vy);
        this.game.run(sv, null);
        fitness = Score.calculateEucledianDistance(sv.x, sv.y, Input.VT.x, Input.VT.y);
    }

    public StateVector getSv(){
        return this.sv;
    }

    public double getFitness(){
        return this.fitness;
    }

    public float getVx(){
        return this.sv.vx;
    }

    public float getVy(){
        return this.sv.vy;
    }

    public float getX(){
        return  this.sv.x;
    }

    public float getY(){
        return this.sv.y;
    }

    public void print(){
        System.out.println("Vx: " + getVx() + " Vy: " + getVy() + " Fitness: " + getFitness());
    }

    public static Neighbour clone(Neighbour n){
        return new Neighbour(n.getSv());
    }







}
