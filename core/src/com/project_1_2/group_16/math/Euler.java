package com.project_1_2.group_16.math;

import com.project_1_2.group_16.*;
import com.project_1_2.group_16.gamelogic.Collision;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.physics.Acceleration;
import com.project_1_2.group_16.physics.Physics;

public class Euler {

    Acceleration acceleration = new Acceleration();

    /**
     * Euler's implementation for our physics engine.
     * We start at t0 and use step-size h.
     * It takes the stateVector and terrain as input.
     * @param h step size
     * @param sv StateVector
     * @param terrain terrain
     */
    public void euler_complete(float h, StateVector sv, Terrain terrain) {
        float pos_x1, pos_y1, vel_x1, vel_y1;

        App.pos_x = sv.pos_x;
        App.pos_y = sv.pos_y;

        float[] partialDerivatives = Terrain.getSlope(new float[] {sv.pos_x, sv.pos_y}, h);

        pos_x1 = sv.pos_x + (h * sv.velocity_x);
        pos_y1 = sv.pos_y + (h * sv.velocity_y);

        float acceleration_x = acceleration.getAccelerationX(partialDerivatives[0], partialDerivatives[1], sv, terrain);
        float acceleration_y = acceleration.getAccelerationY(partialDerivatives[0], partialDerivatives[1], sv, terrain);

        vel_x1 = sv.velocity_x + (h * acceleration_x);
        vel_y1 = sv.velocity_y + (h * acceleration_y);


        sv.pos_x = pos_x1;
        sv.pos_y = pos_y1;

        sv.velocity_x = vel_x1;
        sv.velocity_y = vel_y1;

        System.out.println("sv_END: " + sv);

        if (Collision.ballIsInWater(sv)) {
            System.out.println("water");
            App.ballInWater = true;
            App.hitsCounter++;
        }

        if (Collision.ballHitTree(sv)) {
            System.out.println("tree");
            App.ballInWater = true;
            App.hitsCounter++;
        }

        //check if the ball is rolling
        if (Physics.magnitude(sv.velocity_x,sv.velocity_y) < 0.05) {

            if ((Physics.magnitude(partialDerivatives[0],partialDerivatives[1]) < Terrain.getStaticFriction(sv))) {

                if (Collision.ballIsInTargetRadius(sv)) {
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
