package com.project_1_2.group_16.math;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Collision;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.models.Tree;
import com.project_1_2.group_16.physics.Acceleration;
import com.project_1_2.group_16.physics.Physics;

public class Euler implements NumericalSolver {

    Acceleration acceleration = new Acceleration();

    /**
     * Euler ODE Solver
     * @param h step-size
     * @param sv state vector of the ball
     */
    @Override
    public void solve(float h, StateVector sv) {

        float pos_x1, pos_y1, vel_x1, vel_y1;
        App.pos_x = sv.pos_x;
        App.pos_y = sv.pos_y;

        float[] partialDerivatives = Terrain.getSlope(new float[]{sv.pos_x, sv.pos_y}, h);
        
        pos_x1 = sv.pos_x + (h * sv.velocity_x);
        pos_y1 = sv.pos_y + (h * sv.velocity_y);

        float acceleration_x = acceleration.getAccelerationX(partialDerivatives[0], partialDerivatives[1], sv);
        float acceleration_y = acceleration.getAccelerationY(partialDerivatives[0], partialDerivatives[1], sv);

        vel_x1 = sv.velocity_x + (h * acceleration_x);
        vel_y1 = sv.velocity_y + (h * acceleration_y);

        sv.pos_x = pos_x1;
        sv.pos_y = pos_y1;

        sv.velocity_x = vel_x1;
        sv.velocity_y = vel_y1;

//        System.out.println("sv_END: " + sv);

        if (Collision.ballIsInWater(sv)) {
            System.out.println("water");

            // reset position
            App.pos_x = App.prevPos.x;
            App.pos_y = App.prevPos.y;
            App.staticStop = true;

            // stroke penalty
            App.hitsCounter++;
        }

        Tree hittree = Collision.ballHitTree(sv);
        if (hittree != null) {
            System.out.println("tree");

            Vector2 vT = new Vector2(hittree.pos.x, hittree.pos.z);
            Vector2 vB = new Vector2(sv.pos_x, sv.pos_y);

            Vector2 rc1 = new Vector2(sv.velocity_x, sv.velocity_y).limit2(1f);
            Vector2 rc2 = new Vector2(vT.x - vB.x, vB.y - vT.y).limit2(1f);
        
            float a = vB.dst(vB.x + 1f, (rc1.x / rc1.y) * vB.x + 1f);
            float b = vB.dst(vB.x + 1f, (rc2.x / rc2.y) * vB.x + 1f);
            float c = Vector2.dst(vB.x + 1f, (rc1.x / rc1.y) * vB.x + 1f, vB.x + 1f, (rc2.x / rc2.y) * vB.x + 1f);
            float hitAngle = (float)Math.acos((a*a + b*b - c*c) / (2 * a * b));
            System.out.println(hitAngle);
            
            //TODO
            sv.velocity_x *= -0.8f;
            sv.velocity_y *= -0.8f;
        }

        //check if the ball is rolling
        if (Physics.magnitude(sv.velocity_x, sv.velocity_y) < h/2) {
            if ((Physics.magnitude(partialDerivatives[0], partialDerivatives[1]) < Terrain.getStaticFriction(sv))) {
                if (Collision.ballIsInTargetRadius(sv, App.flagpole)) {
                    Game.endGame();
                }
                App.staticStop = true;
            }
        }
    }
}