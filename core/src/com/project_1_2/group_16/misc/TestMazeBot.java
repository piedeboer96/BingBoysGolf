package com.project_1_2.group_16.misc;
import com.badlogic.gdx.math.Vector2;

import com.project_1_2.group_16.bot.ai.BRO;
import com.project_1_2.group_16.bot.ai.MazeBot;
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

        int stuck_count = 0;


        int prev_fitness = Integer.MAX_VALUE;
        int cur_fitness;
        boolean hasfound = true;

        while(!goal){

            counter++;


            PSO pso = new PSO(20, 10, x, y, game, true);
            BRO bro = new BRO(20, 10, 2,x, y, game, true);
            SA sa = new SA(10, 0.2f, x, y, game, true);



            List<Float> sol = sa.runBot();

            sv.vx = sol.get(0);
            sv.vy = sol.get(1);


            game.runEngineTester(sv);
            cur_fitness = BotHelper.getFloodFillFitness(x, y);

            if(stuck_count >= 5){
                hasfound = false;
                break;
            }

            if(prev_fitness == cur_fitness){
                stuck_count++;
            }
            else if(sv.x == Integer.MAX_VALUE || sv.y == Integer.MAX_VALUE){
            }
            else{
                stuck_count = 0;
            }

            //invalid shot --> try mazebot shot
            if(sv.x == Integer.MAX_VALUE || sv.y == Integer.MAX_VALUE){
                sv.x = x;
                sv.y = y;
                prev_fitness = cur_fitness;
                stuck_count++;
                MazeBot maze_bot = new MazeBot(x, y, game);
                sol = maze_bot.runBot();
                sv.vx = sol.get(0);
                sv.vy = sol.get(1);
                sv.x = x;
                sv.y = y;
                game.runEngineTester(sv);
                if(sv.x == Integer.MAX_VALUE || sv.y == Integer.MAX_VALUE){

                }else{
                    cur_fitness = BotHelper.getFloodFillFitness(sv.x, sv.y);
                }


            }
            else{
                //valid but either to close to the original position --> try mazebot
                if(prev_fitness == cur_fitness || Math.abs(x-sv.x) < 0.001f || Math.abs(y-sv.y) < 0.001f){
                    MazeBot maze_bot = new MazeBot(x, y, game);
                    sol = maze_bot.runBot();
                    sv.vx = sol.get(0);
                    sv.vy = sol.get(1);
                    sv.x = x;
                    sv.y = y;
                    game.runEngineTester(sv);
                    cur_fitness = BotHelper.getFloodFillFitness(sv.x, sv.y);

                }

                prev_fitness = cur_fitness;
                x = sv.x;
                y = sv.y;
            }


            if(BotHelper.calculateEucledianDistance(Input.VT.x, Input.VT.y, sv.x, sv.y) < Input.R){
                goal = true;
            }

            sv.stop = false;

        }
        if(hasfound){
            System.out.println("Number of shots needed is "+ counter);
            System.out.println("The number of simulation is " + Game.simulCounter);
        }else{
            System.out.println("Hasnt found a solution!");
        }

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