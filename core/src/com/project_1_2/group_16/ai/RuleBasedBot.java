package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;
import java.util.Random;

/**
 * This class contains our most basic bot.
 * It is partly based on a genetic approach.
 *
 * NOTE: I safe-deleted a lot of unused code,
 * if you need it PLEASE check an earlier revision in git.
 */

public class RuleBasedBot {

    static final int Population = 200;
    static Random rand = new Random();
    static final float max = 5.0F;          //what is the maximum force that we can apply?
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

    /**
     * Method that finds the best shot.
     * Multiple generations run.
     * The score is being evaluated using the FloodFill matrix.
     * Lowest value means closest 'floodFill' based distance to
     * the target. This will update the best shot.
     */
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
                sv = new StateVector(Input.V0.x, Input.V0.y, float_randomX, float_randomY);
            }else{
                sv = new StateVector(svForXandY.pos_x, svForXandY.pos_y, float_randomX, float_randomY);
            }

            score = Game.runWithAI(sv);

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

    }

}
