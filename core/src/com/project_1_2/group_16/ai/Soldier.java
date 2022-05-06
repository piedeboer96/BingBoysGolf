package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.math.StateVector;

//Individual class for BRO algorithm
public class Soldier {
    public float velX;
    public float velY;
    public float endPosX;
    public float endPosY;
    Game game = new Game();
    public float fitness = Integer.MAX_VALUE;
    public int damageCounter;

    public Soldier(float velX, float velY){
        this.velX = velX;
        this.velY = velY;
        game.setNumericalSolver(NumericalSolver.RK4);
        calcFitness();
    }

    public Soldier(Soldier s){
        this.velX = s.velX;
        this.velY = s.velY;
        this.fitness = s.fitness;
        this.damageCounter = s.damageCounter;
        this.endPosX = s.endPosX;
        this.endPosY = s.endPosY;
    }
    public void calcFitness(){
        game.setNumericalSolver(NumericalSolver.RK4);
        StateVector sv = new StateVector(Input.V0.x, Input.V0.y, velX, velY);
        game.runEngine(sv,null, null, null, this);
        this.endPosX = sv.x;
        this.endPosY = sv.y;
//        fitness = Score.calculateEucledianDistance(Input.VT.x, Input.VT.y, sv.x, sv.y);
    }
    public String toString(){
        return "velX " + velX + " velY " + velY + " fitness " + fitness + " endPosX : " + endPosX + " endPosY : " + endPosY;
    }
}
