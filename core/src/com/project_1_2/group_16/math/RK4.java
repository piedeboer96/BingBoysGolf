package com.project_1_2.group_16.math;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Collision;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.models.Tree;
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

        float vel_x0 = sv.velocity_x;
        float vel_y0 = sv.velocity_y;

        float vel_x1, vel_y1, pos_y1, pos_x1;

        Derivation k1, k2, k3, k4;

        k1 = Derivation.getK1Derivation(sv, h);
        k2 = Derivation.getK2K3Derivation(sv, h, k1);
        k3 = Derivation.getK2K3Derivation(sv, h, k2);
        k4 = Derivation.getK4Derivation(sv, h, k3);

        pos_x1 = sv.pos_x + (h/6f)*((k1.dx_dt + (2 * k2.dx_dt) + (2 * k3.dx_dt) + k4.dx_dt));
        pos_y1 = sv.pos_y + (h/6f)*((k1.dy_dt + (2 * k2.dy_dt) + (2 * k3.dy_dt) + k4.dy_dt));
        vel_x1 = sv.velocity_x + (h/6f)*((k1.dvx_dt + (2 * k2.dvx_dt) + (2 * k3.dvx_dt) + k4.dvx_dt));
        vel_y1 =  sv.velocity_y + (h/6f)*((k1.dvy_dt + (2 * k2.dvy_dt) + (2 * k3.dvy_dt) + k4.dvy_dt));


        sv.pos_x = pos_x1;
        sv.pos_y = pos_y1;

        sv.velocity_x = vel_x1;
        sv.velocity_y = vel_y1;

//        System.out.println("sv_END: " + sv);

        if(Collision.ballIsInWater(sv)) {
            System.out.println("water");
            
            // reset position
            App.pos_x = App.prevPos.x;
            App.pos_y = App.prevPos.y;
            App.staticStop = true;

            // stroke penalty
            App.hitsCounter++;
        }

        Tree hittree = Collision.ballHitTree(sv);
        if(hittree != null) {
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

        // check if the ball is still rolling
        if (Physics.magnitude(sv.velocity_x,sv.velocity_y) < h/2) {
            float[] partialDerivatives = Terrain.getSlope(new float[] {sv.pos_x, sv.pos_y}, h);
            if ((Physics.magnitude(partialDerivatives[0],partialDerivatives[1]) < Terrain.getStaticFriction(sv))) {
                if (Collision.ballIsInTargetRadius(sv, App.flagpole)) {
                    Game.endGame();
                }
                App.staticStop = true;
            }
        }
    }
}
