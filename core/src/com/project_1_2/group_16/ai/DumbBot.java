package com.project_1_2.group_16.ai;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;

/**
 * The class of the DumbBot
 * this is the most basic bot
 * Shoots the golfball with full velocity in the direction of the target
 */

/**
 * @param c1 is the scalar of the velocity in the x direction
 * @param c2 is the scalar of the velocity in the y direction
 * @param target the target coordinates
 */
public class DumbBot {
    private StateVector sv, init_sv;
    private Game game;
    private final Vector2 target = Input.VT;
    private final float maxV = 5f;
    private float c1;
    private float c2;
    final boolean DEBUG = false;

    //Initialisation of the StateVector
    public DumbBot(StateVector sv){
        this.sv = sv;
        this.init_sv = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
        this.game = new Game();
        //adjust the scalar of c1 and c2
        ScaleC();
    }

    public void ScaleC(){
        //Calculates the difference in the points
        c1 = target.x - sv.x;
        c2 = target.y - sv.y;
        if(DEBUG) System.out.println("this is c1 " + c1 + "\n" + "this is c2 " + c2);
        //Scale the c1 and c2 down
        float total = Math.abs(c1)+Math.abs(c2);
        c1 = c1 / total;
        c2 = c2 / total;
        if(DEBUG) System.out.println("this is c1 " + c1 + "\n" + "this is c2 " + c2);
        //multiply every Scaled velocity by the maximum velocity
        c1 = c1*maxV;
        c2 = c2*maxV;

        if(DEBUG) System.out.println("this is c1 " + c1 + "\n" + "this is c2 " + c2);
    }

    public StateVector Play(){
        sv.vx = c1;
        sv.vy = c2;
        return sv;
    }

}
