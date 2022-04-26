package com.project_1_2.group_16.math;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.ai.FloodFill;
import com.project_1_2.group_16.gamelogic.Collision;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.models.Tree;
import com.project_1_2.group_16.physics.Physics;

public class RK4 implements NumericalSolver{

    public boolean stop;
    public float stepSize = 0.05f;
    //public int bestDistance;
    public RK4(){
        //bestDistance = Integer.MAX_VALUE;
        stop = false;
    }

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

        k1 = Derivation.getDerivation(sv, h, new Derivation(), 0);
        k2 = Derivation.getDerivation(sv, h, k1, 0.5f);
        k3 = Derivation.getDerivation(sv, h, k2, 0.5f);
        k4 = Derivation.getDerivation(sv, h, k3, 1f);

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

            Vector2 vT = new Vector2(hittree.getPosition().x, hittree.getPosition().z);
            Vector2 vB = new Vector2(sv.pos_x, sv.pos_y);

            if (vB.x > vT.x + hittree.getRadius() * 0.5) {
                sv.velocity_x *= -.75;
            }
            else if (vB.x < vT.x - hittree.getRadius() * 0.5) {
                sv.velocity_x *= -.75;
            }
            else if (vB.y > vT.y + hittree.getRadius() * 0.5) {
                sv.velocity_y *= -.75;
            }
            else if (vB.y < vT.y - hittree.getRadius() * 0.5) {
                sv.velocity_y *= -.75;
            }
        }

        // check if the ball is still rolling
        if (Physics.magnitude(sv.velocity_x,sv.velocity_y) < h) {
            float[] partialDerivatives = Terrain.getSlope(new float[] {sv.pos_x, sv.pos_y}, h);
            if ((Physics.magnitude(partialDerivatives[0],partialDerivatives[1]) < Terrain.getStaticFriction(sv))) {
                if (Collision.ballIsInTargetRadius(sv)) {
                    Game.endGame();
                }
                App.staticStop = true;
            }
        }
    }

    /**
     * Does one iteration of RK4 () for engine simulations
     */
    public StateVector solveRK4oneStep(StateVector sv, float h){
        float vel_x1, vel_y1, pos_y1, pos_x1;

        Derivation k1, k2, k3, k4;

        k1 = Derivation.getDerivation(sv, h, new Derivation(), 0);
        k2 = Derivation.getDerivation(sv, h, k1, 0.5f);
        k3 = Derivation.getDerivation(sv, h, k2, 0.5f);
        k4 = Derivation.getDerivation(sv, h, k3, 1f);

        pos_x1 = sv.pos_x + (h/6f)*((k1.dx_dt + (2 * k2.dx_dt) + (2 * k3.dx_dt) + k4.dx_dt));
        pos_y1 = sv.pos_y + (h/6f)*((k1.dy_dt + (2 * k2.dy_dt) + (2 * k3.dy_dt) + k4.dy_dt));
        vel_x1 = sv.velocity_x + (h/6f)*((k1.dvx_dt + (2 * k2.dvx_dt) + (2 * k3.dvx_dt) + k4.dvx_dt));
        vel_y1 =  sv.velocity_y + (h/6f)*((k1.dvy_dt + (2 * k2.dvy_dt) + (2 * k3.dvy_dt) + k4.dvy_dt));

        sv.pos_x = pos_x1;
        sv.pos_y = pos_y1;
        sv.velocity_x = vel_x1;
        sv.velocity_y = vel_y1;

        if(Collision.ballIsInWater(sv)){
            stop = true;
            //Impossible stateVector, to indicate that ball hit water

            return new StateVector(sv.pos_x,sv.pos_y,Integer.MAX_VALUE,Integer.MAX_VALUE);
        }
        Tree hittree = Collision.ballHitTree(sv);
        if(hittree != null) {
//            System.out.println("tree");
            Vector2 vT = new Vector2(hittree.getPosition().x, hittree.getPosition().z);
            Vector2 vB = new Vector2(sv.pos_x, sv.pos_y);

            if (vB.x > vT.x + hittree.getRadius() * 0.5) {
                sv.velocity_x *= -.75;
            }
            else if (vB.x < vT.x - hittree.getRadius() * 0.5) {
                sv.velocity_x *= -.75;
            }
            else if (vB.y > vT.y + hittree.getRadius() * 0.5) {
                sv.velocity_y *= -.75;
            }
            else if (vB.y < vT.y - hittree.getRadius() * 0.5) {
                sv.velocity_y *= -.75;
            }
        }
        if (Collision.ballIsInTargetRadius(sv)) {
            stop = true;
            return sv;
        }
        if(Physics.magnitude(sv.velocity_x,sv.velocity_y) < h){
            float[] partialDerivatives = Terrain.getSlope(new float[] {sv.pos_x, sv.pos_y}, h);
            if ((Physics.magnitude(partialDerivatives[0],partialDerivatives[1]) < 0.2f)) {
                stop = true;
                return sv;
            }
        }
        return sv;
    }
}
