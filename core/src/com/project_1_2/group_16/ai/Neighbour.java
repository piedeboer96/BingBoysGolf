package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.math.StateVector;

public class Neighbour {
    private StateVector sv, init_sv;
    private float vx, vy;
    private Game game;
    public double fitness = Integer.MAX_VALUE;

    public Neighbour(StateVector sv){
        //Changing stateVector
        this.sv = sv;
        //Starting StateVector
        this.init_sv = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
        this.game = new Game();
        this.game.setNumericalSolver(NumericalSolver.RK4);
        this.game.runEngine(this.sv, null, this, null);
    }
    public Neighbour (Neighbour n){
        this.vx = n.vx;
        this.vy = n.vy;
        this.init_sv = n.init_sv;
        this.sv = n.sv;
        this.fitness = n.fitness;
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

    public float calculateManhattanX (){
        return sv.x - Input.VT.x;
    }

    public float calculateManhattanY (){
        return sv.y - Input.VT.y;
    }

    public void print(){
        System.out.println("Vx: " + getVx() + " Vy: " + getVy() + " Fitness: " + getFitness());
    }

    public static Neighbour clone(Neighbour n){
        return new Neighbour(n.getSv());
    }

}
