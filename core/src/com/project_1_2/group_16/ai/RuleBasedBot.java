package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.NumericalSolver;
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

    static final int Population = 20;
    static Random rand = new Random();
    static final float max = 5.0F;          //what is the maximum force that we can apply?
    static float score;
    static float bestScore;
    static boolean scoreInitialise = false;

    static float float_randomX;
    static float float_randomY;
    static int random_int;
    public StateVector svForXandY;
    public StateVector sv;
    public StateVector newsv;
    private Game game;

    public RuleBasedBot(StateVector sv){
        this.sv = sv;
        this.game = new Game();
        this.svForXandY = sv;
        BestShot(sv,game);
    }

    /**
     * Method that finds the best shot.
     * Multiple generations run.
     * The score is being evaluated using the FloodFill matrix.
     * Lowest value means closest 'floodFill' based distance to
     * the target. This will update the best shot.
     */
    public void BestShot(StateVector sv,Game game) {
        this.sv = sv;
        this.game = game;

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

            sv = new StateVector(svForXandY.x, svForXandY.y, float_randomX, float_randomY);

            //game.run(sv, null);
            this.game.setNumericalSolver(NumericalSolver.RK4);
            this.game.runEngine(sv, null, null, null, null);
            //score = FloodFill.getMatrixValue(sv.x, sv.y,);
            score = Score.calculateEucledianDistance(this.sv.x, this.sv.y, Input.VT.x, Input.VT.y);

            if ((!scoreInitialise || bestScore > score) && score!=-1) {
                scoreInitialise = true;
                bestScore = score;
                newsv = new StateVector(sv.x, sv.y, float_randomX, float_randomY);
                System.out.println("the best score at the moment is " + bestScore);
                System.out.println("with a force applied in the x direction of " + sv.vx);
                System.out.println("with a force applied in the y direction of " + sv.vy);
            }
        }

        System.out.println(newsv);
        System.out.println("\n");

    }
    public StateVector Play(){
        sv.vx = newsv.vx;
        sv.vy = newsv.vy;
        return sv;
    }

}
