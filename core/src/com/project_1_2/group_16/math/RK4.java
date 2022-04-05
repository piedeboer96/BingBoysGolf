package com.project_1_2.group_16.math;

import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Collision;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.physics.Physics;

public class RK4 implements NumericalSolver{

    /**
     * class needed to run the RK4 derivation algorithm which is a more improved version of Euler's implementation by 3 orders
     * of accuracy. It runs by taking a weighted average of different estimations/derivations k1, k2, k3 and k4.
     * @param h step-size
     * @param sv StateVector
     */
    @Override
    public void solve(float h, StateVector sv) {

        App.pos_x = sv.pos_x;
        App.pos_y = sv.pos_y;

        float vel_x1, vel_y1, pos_y1, pos_x1;

        Derivation k1, k2, k3, k4;
        k1 = Derivation.getK1Derivation(sv, h);
        k2 = Derivation.getK2K3Derivation(sv, h, k1);
        k3 = Derivation.getK2K3Derivation(sv, h, k2);
        k4 = Derivation.getK4Derivation(sv, h, k3);

        pos_x1 = sv.pos_x + ((k1.getDx_dt() + (2 * k2.getDx_dt()) + (2 * k3.getDx_dt()) + k4.getDx_dt())/6f);
        pos_y1 = sv.pos_y + ((k1.getDy_dt() + (2 * k2.getDy_dt()) + (2 * k3.getDy_dt()) + k4.getDy_dt())/6f);
        vel_x1 = sv.velocity_x + ((k1.getDvx_dt() + (2 * k2.getDvx_dt()) + (2 * k3.getDvx_dt()) + k4.getDvx_dt())/6f);
        vel_y1 = sv.velocity_y + ((k1.getDvy_dt() + (2 * k2.getDvy_dt()) + (2 * k3.getDvy_dt()) + k4.getDvy_dt())/6f);

        sv.pos_x = pos_x1;
        sv.pos_y = pos_y1;
        sv.velocity_x = vel_x1;
        sv.velocity_y = vel_y1;

        if(Collision.ballIsInWater(sv)) {
            System.out.println("water");
            App.ballInWater = true;
            App.hitsCounter++;
        }

        if(Collision.ballHitTree(sv)) {
            System.out.println("tree");
            App.ballInWater = true;
            App.hitsCounter++;
        }

        if (Physics.magnitude(sv.velocity_x,sv.velocity_y) < 0.05) {

            float[] partialDerivatives = Terrain.getSlope(new float[] {sv.pos_x, sv.pos_y}, h);

            if ((Physics.magnitude(partialDerivatives[0],partialDerivatives[1]) < Terrain.getStaticFriction(sv))) {

                if (Collision.ballIsInTargetRadius(sv, App.flagpole)) {
                    System.out.println("LOW VELOCITY, HIGH STATIC FRICTION ---> TARGET HIT");
                    
                    App.staticStop = true;
                    App.targetHit = true;
                }

                System.out.println("LOW VELOCITY, HIGH STATIC FRICTION");
                App.staticStop = true;
            }
        }
    }
}
