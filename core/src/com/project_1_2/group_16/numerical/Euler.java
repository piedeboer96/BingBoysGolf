package com.project_1_2.group_16.numerical;

import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.physics.Acceleration;
import com.project_1_2.group_16.physics.Physics;
import com.project_1_2.group_16.physics.StateVector;

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

        float[] partialDerivatives = terrain.getSlope(new float[] {sv.pos_x, sv.pos_y}, h);

        float acceleration_x = acceleration.getAccelerationX(partialDerivatives[0], partialDerivatives[1], sv, terrain);
        float acceleration_y = acceleration.getAccelerationY(partialDerivatives[0], partialDerivatives[1], sv, terrain);

        vel_x1 = sv.velocity_x + (h * acceleration_x);
        vel_y1 = sv.velocity_y + (h * acceleration_y);

        pos_x1 = sv.pos_x + (h * vel_x1);
        pos_y1 = sv.pos_y + (h * vel_y1);

        sv.pos_x = pos_x1;
        sv.pos_y = pos_y1;

        sv.velocity_x = vel_x1;
        sv.velocity_y = vel_y1;

        System.out.println("sv_END: " + sv);

        if (terrain.ballIsInWater(sv)) {
            System.out.println("water");
            App.ballInWater = true;
            App.hitsCounter++;
        }

        if (terrain.ballHitTree(sv)) {
            System.out.println("tree");
            App.ballInWater = true;
            App.hitsCounter++;
        }

        //check if the ball is rolling
        if (Physics.magnitude(sv.velocity_x,sv.velocity_y) < 0.05) {

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
