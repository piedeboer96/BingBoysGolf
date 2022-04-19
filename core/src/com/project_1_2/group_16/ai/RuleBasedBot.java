//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.project_1_2.group_16.ai;

import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.camera.BallCamera;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.models.Golfball;

import java.util.Random;

public class RuleBasedBot {
    static EngineSimulator simulator;

    static final int Population = 200;
    static Random rand = new Random();
    static final float max = 5.0F; //what is the maximum force that we can apply?
    static float score;
    static float bestScore;
    static boolean scoreInitialise = false;
    static float float_randomX;
    static float float_randomY;
    static int random_int;
    public static StateVector svForXandY;
    public static StateVector sv;
    public static StateVector newsv;
    public static boolean useAnimation = true;
    public static boolean firstGen = true;

    //public final Vector3 v = App.v;

    public RuleBasedBot() {
    }

    public static void BestShot() {

        useAnimation = false;
        for(int i = 0; i < Population; ++i) {
            random_int = rand.nextInt(2);
            if(random_int == 0){
                float_randomX = rand.nextFloat() * max;
            }
            else{
                float_randomX = -(rand.nextFloat() * max);
            }
            random_int = rand.nextInt(2);
            if(random_int == 0){
                float_randomY = rand.nextFloat() * max;
            }
            else{
                float_randomY = -(rand.nextFloat() * max);
            }
            if(firstGen){
                sv = new StateVector(0, 0, float_randomX, float_randomY);
            }else{
                sv = new StateVector(svForXandY.pos_x, svForXandY.pos_y, float_randomX, float_randomY);
            }

            score = Game.runWithAI(sv);
            //simulator = new EngineSimulator(Game.sv.pos_x,Game.sv.pos_y,sv.velocity_x,sv.velocity_y);
            //score = PSO.calculateEucledianDistance(simulator.endPos_X,simulator.endPos_Y, Input.VT.x,Input.VT.y);
            if ((!scoreInitialise || bestScore > score) && score!=-1) {
                scoreInitialise = true;
                bestScore = score;
                newsv = new StateVector(sv.pos_x, sv.pos_y, float_randomX, float_randomY);
                System.out.println("the best score at the moment is " + bestScore);
                System.out.println("with a force applied in the x direction of " + sv.velocity_x);
                System.out.println("with a force applied in the y direction of " + sv.velocity_y);
            }
        }
        useAnimation = true;
        Game.sv = newsv;
        System.out.println(Game.sv);
        Game.run();
        System.out.println("\n");
        scoreInitialise = false;
        firstGen = false;
        svForXandY = Game.sv;

        //shoot(Game.sv.velocity_x,Game.sv.velocity_y);
        //return newsv;
    }
    public static void ShotBestShot(){
        useAnimation = true;
        Game.sv = newsv;
        System.out.println(Game.sv);
        Game.run();
        System.out.println("\n");
        scoreInitialise = false;
        firstGen = false;
        svForXandY = Game.sv;
    }

}
