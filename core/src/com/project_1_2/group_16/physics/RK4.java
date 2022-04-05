package com.project_1_2.group_16.physics;

import com.project_1_2.group_16.App;

public class RK4 {

    /**
     * class needed to run the RK4 derivation algorithm which is a more improved version of Euler's implementation by 3 orders
     * of accuracy. It runs by taking a weighted average of different estimations/derivations k1, k2, k3 and k4.
     * @param h step-size
     * @param sv StateVector
     * @param terrain terrain
     */
    public void rk4Complete(float h, StateVector sv, Terrain terrain) {

        App.pos_x = sv.pos_x;
        App.pos_y = sv.pos_y;

        float vel_x1, vel_y1, pos_y1, pos_x1;

        Derivation k1, k2, k3, k4;
        k1 = Derivation.getK1Derivation(sv, h, terrain);
        k2 = Derivation.getK2K3Derivation(sv, h, terrain, k1);
        k3 = Derivation.getK2K3Derivation(sv, h, terrain, k2);
        k4 = Derivation.getK4Derivation(sv, h, terrain, k3);

        pos_x1 = sv.pos_x + ((k1.dx_dt + (2 * k2.dx_dt) + (2 * k3.dx_dt) + k4.dx_dt)/6f);
        pos_y1 = sv.pos_y + ((k1.dy_dt + (2 * k2.dy_dt) + (2 * k3.dy_dt) + k4.dy_dt)/6f);
        vel_x1 = sv.velocity_x + ((k1.dvx_dt + (2 * k2.dvx_dt) + (2 * k3.dvx_dt) + k4.dvx_dt)/6f);
        vel_y1 = sv.velocity_y + ((k1.dvy_dt + (2 * k2.dvy_dt) + (2 * k3.dvy_dt) + k4.dvy_dt)/6f);

        sv.pos_x = pos_x1;
        sv.pos_y = pos_y1;
        sv.velocity_x = vel_x1;
        sv.velocity_y = vel_y1;

        if(terrain.ballIsInWater(sv)) {
            System.out.println("water");
            App.ballInWater = true;
            App.hitsCounter++;
        }

        if(terrain.ballHitTree(sv)) {
            System.out.println("tree");
            App.ballInWater = true;
            App.hitsCounter++;
        }

        if (Physics.magnitude(sv.velocity_x,sv.velocity_y) < 0.05) {

            float[] partialDerivatives = terrain.getSlope(new float[] {sv.pos_x, sv.pos_y}, h);

            if ((Physics.magnitude(partialDerivatives[0],partialDerivatives[1]) < terrain.getStaticFriction(sv))) {

                if (terrain.ballIsInTargetRadius(sv)) {
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
