package com.project_1_2.group_16.misc;
import com.badlogic.gdx.math.Vector2;

import com.project_1_2.group_16.bot.ai.BRO;
import com.project_1_2.group_16.bot.ai.PSO;
import com.project_1_2.group_16.bot.ai.SA;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.bot.BotHelper;
import com.project_1_2.group_16.gamelogic.*;
import com.project_1_2.group_16.math.ode.NumericalSolver;
import com.project_1_2.group_16.models.Wall;
import com.project_1_2.group_16.io.Input;
import com.project_1_2.group_16.models.Tree;

import java.util.ArrayList;
import java.util.List;

public class TestMazeBot {


    static StateVector sv;
    static Game game;

    public TestMazeBot(){
        run();
    }

    public void createTerrain(String H, float[][] spline) {
        Terrain.setSpline(H, new float[Spline.SPLINE_SIZE][Spline.SPLINE_SIZE]);
        Terrain.spline.createSpline();
    }

    public static void runBot(){

        boolean goal = false;
        int counter = 0;

        float x = sv.x;
        float y = sv.y;

        int prev_fitness = Integer.MAX_VALUE;

        MazeGenerator.display();

        while(!goal){

            counter++;


            PSO pso = new PSO(20, 10, x, y, game, true);
            BRO bro = new BRO(20, 10, 2,x, y, game, true);



            List<Float> sol = bro.runBot();

            sv.vx = sol.get(0);
            sv.vy = sol.get(1);


            game.runEngineTester(sv);


            System.out.println("sv.x" + sv.x + "  sv.y " + sv.y + " immut x: " + x + " y: " + y + " FITNESS: " + BotHelper.getFloodFillFitness(x, y));


            if(prev_fitness == BotHelper.getFloodFillFitness(x, y)){

            }
            if(Math.abs(x-sv.x) < 0.001f || Math.abs(y-sv.y) < 0.001f){
                System.out.println("no sol found");
                goal = true;
                System.out.println("sv.x" + sv.x + "  sv.y " + sv.y + " immut x: " + x + " y: " + y);
            }

            x = sv.x;
            y = sv.y;


            if(BotHelper.calculateEucledianDistance(Input.VT.x, Input.VT.y, sv.x, sv.y) < Input.R){
                goal = true;
            }

            sv.stop = false;

        }
        System.out.println("Number of shots needed is "+ counter);
        System.out.println("The number of simulation is " + Game.simulCounter);
    }
    public void run(){
        // Initiate all the inputs needed to run the tester class without the GUI
        Input.H = "0.1";
        Input.GRAVITY = 9.81f;
        Input.MUK = 0.3f;
        Input.MUS = 0.2f;
        createTerrain(Input.H, Input.BICUBIC_INPUT);
        Input.V0 = new Vector2(-3.0f,0f);
        Input.VT = new Vector2(4.0f,1f);
        Input.R = 0.15f;
        Input.VH = 2f;
        Input.NUMBER_OF_SANDPITS = 0;
        Input.NUMBER_OF_TREES = 0;
        Input.SAND = new ArrayList<Sandpit>();
        Input.TREES = new ArrayList<Tree>();
        Input.WALLS = new ArrayList<Wall>();
        Input.BICUBIC_INPUT = new float[Spline.SPLINE_SIZE][Spline.SPLINE_SIZE];
        for (int i = 0; i < Input.BICUBIC_INPUT.length; i++) {
            for (int j = 0; j < Input.BICUBIC_INPUT.length; j++) {

                Input.BICUBIC_INPUT[i][j] = 0.0f;

            }
        }

        game = new Game();
        game.setNumericalSolver(NumericalSolver.RK4);
        Input.VT = new Vector2(4.0f,1f);
        MazeGenerator.generateMaze(7);
        BotHelper.setFloodFillTable();
        sv = new StateVector(Input.V0.x, Input.V0.y, 0f, 0f);
        System.out.println("The Hole position is x = " + Input.VT.x + " and the y = " + Input.VT.y);
        runBot();

    }

    public static void main(String[] args) {


        for(int i=0; i<10; i++){
            TestMazeBot mb = new TestMazeBot();
        }
    }
}