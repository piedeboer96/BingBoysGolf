package com.project_1_2.group_16.bot.simpleBot;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.io.Input;
import com.project_1_2.group_16.math.StateVector;

/**
 * The class of the DumbBot
 * this is the most basic bot
 * Shoots the golfball with full velocity in the direction of the target
 * c1 is the scalar of the velocity in the x direction
 * c2 is the scalar of the velocity in the y direction
 * target the target coordinates
 */
public class RuleBasedBot {

    private StateVector sv;
    private final Vector2 target = Input.VT;
    private final float maxV = 5f;
    private float c1;
    private float c2;
    final boolean DEBUG = false;

    //Initialisation of the StateVector
    public RuleBasedBot(StateVector sv){
        this.sv = sv;
        //adjust the scalar of c1 and c2
        scaleC();
    }

    public void scaleC() {
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
        //System.out.println("total C " + c1 +" " + c2);

        if(DEBUG) System.out.println("this is c1 " + c1 + "\n" + "this is c2 " + c2);
    }

    public float[] play() {
        return new float[] {c1, c2};
    }
}
