package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.math.StateVector;

//Individual class for BRO algorithm
public class Soldier {
    public float velX;
    public float velY;
    Game game = new Game();
    public float fitness;
    public int damageCounter;
    public Soldier(float velX, float velY){
        this.velX = velX;
        this.velY = velY;
        game.setNumericalSolver(NumericalSolver.RK4);
        calcFitness();
    }
    public void calcFitness(){
        StateVector sv = new StateVector(Input.V0.x, Input.V0.y, velX, velY);
        game.runEngine(sv,null);
        fitness = Score.calculateEucledianDistance(Input.VT.x, Input.VT.y, sv.x, sv.y);
    }
    public String toString(){
        return "velX " + velX + " velY " + velY + " fitness " + fitness;
    }
}
