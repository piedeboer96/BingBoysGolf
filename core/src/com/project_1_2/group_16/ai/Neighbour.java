package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.math.RK4;
import com.project_1_2.group_16.math.StateVector;

import javax.swing.plaf.nimbus.State;

public class Neighbour {
    public static final float STEP_SIZE = SA.SOLVER_STEP_SIZE;
    private StateVector sv;
    private EngineSimulator es;
    private static float hole_x, hole_y;
    private double fitness;

    public Neighbour(StateVector sv){
        this.sv = sv;
        this.es = new EngineSimulator(Input.V0.x, Input.V0.y, sv.velocity_x, sv.velocity_y);
        this.hole_x = Input.VT.x;
        this.hole_y = Input.VT.y;
        es.runRK4ai();
        fitness = Score.calculateEucledianDistance(es.endPos_X, es.endPos_Y, hole_x, hole_y);
        this.sv = new StateVector(es.endPos_X , es.endPos_Y, sv.velocity_x, sv.velocity_y);

    }

    public StateVector getSv(){
        return this.sv;
    }

    public double getFitness(){
        return this.fitness;
    }

    public float getVx(){
        return this.sv.velocity_x;
    }

    public float getVy(){
        return this.sv.velocity_y;
    }

    public float getX(){
        return  this.sv.pos_x;
    }

    public float getY(){
        return this.sv.pos_y;
    }

    public void print(){
        System.out.println("Vx: " + getVx() + " Vy: " + getVy() + " Fitness: " + getFitness());
    }

    public static Neighbour clone(Neighbour n){
        return new Neighbour(n.getSv());
    }







}
