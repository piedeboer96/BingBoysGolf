package com.project_1_2.group_16.bot.simpleBot;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.bot.ai.AIHelper;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.math.Physics;
import com.project_1_2.group_16.math.StateVector;

import java.util.Random;

/**
 * This class contains our most basic bot.
 */
public class RandomBot {

    static final int Population = 20;
    static Random rand = new Random();
    static final float max = 5.0F;
    static float score;
    static float bestScore;
    static boolean scoreInitialise = false;

    public float float_randomX;
    public float float_randomY;
    static int random_int;
    public StateVector svForXandY;
    public StateVector sv;
    public static StateVector newsv;
    private Game game;

    final boolean DEBUG = false;

    public RandomBot(StateVector sv){
        this.sv = sv;
        this.game = new Game();
        this.svForXandY = sv;
        bestShot(sv,game);
    }

    /**
     * Method that finds the best shot.
     * Multiple generations run.
     * The score is being evaluated using the FloodFill matrix.
     * Lowest value means closest 'floodFill' based distance to
     * the target. This will update the best shot.
     */
    public void bestShot(StateVector sv,Game game) {
        this.sv = sv;
        this.game = game;

        for(int i = 0; i < Population; ++i) {

            randomise();
            sv = new StateVector(svForXandY.x, svForXandY.y, float_randomX, float_randomY);
            this.game.setNumericalSolver(NumericalSolver.RK4);
            this.game.runEngine(sv, null);
            score = AIHelper.calculateEucledianDistance(sv.x, sv.y, Input.VT.x, Input.VT.y);
            if ((!scoreInitialise || bestScore > score) && score!=-1) {
                scoreInitialise = true;
                bestScore = score;
                newsv = new StateVector(sv.x, sv.y, float_randomX, float_randomY);
                if(DEBUG)System.out.println("the best score at the moment is " + bestScore);
                if(DEBUG)System.out.println("with a force applied in the x direction of " + sv.vx);
                if(DEBUG)System.out.println("with a force applied in the y direction of " + sv.vy);
            }
        }

        //System.out.println(newsv);
        //System.out.println("\n");
        scoreInitialise = false;
    }

    /**
     * Give a random x and y value, that is positive or negative, and between 0 and 5
     *
     */
    public void randomise(){
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
        getValidVelocity(float_randomX,float_randomY);
    }

    /**
     *
     * @param float_randomX takes the random x value
     * @param float_randomY takes the random y value
     * Randomise again if the magnitude is above the max
     */
    public void getValidVelocity(float float_randomX,float float_randomY){
        if(Physics.magnitude(float_randomX, float_randomY) > max){
            randomise();
        }
    }

    public float[] play() {
        return new float[] {newsv.vx, newsv.vy};
    }
}
