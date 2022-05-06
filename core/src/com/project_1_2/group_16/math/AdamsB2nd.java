package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.physics.Physics;

public class AdamsB2nd implements NumericalSolver{
    RK4 rk4Bootstrap = new RK4();
    private float[] partialDerivatives;
    public int count = 0;
    StateVector sv1;
    StateVector sv2;

    /**
     * Uses the Adams Bashforth 2nd order method
     */
    public void solve(float h, StateVector sv){
        //rk4 bootstrapping for first 2 iterations
        if(count<2){
            if(count==0) {
                rk4Bootstrap.solve(h, sv);
                sv1 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
                this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
                count++;
            }else if (count==1){
                rk4Bootstrap.solve(h, sv);
                sv2 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
                this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
                count++;
            }
        }else {
            //derivations to update the state
            Derivation deriv1 = Derivation.getDerivation(sv1, h, new Derivation(), 0);
            Derivation deriv2 = Derivation.getDerivation(sv2, h, new Derivation(), 0);

            //update the ball's positions abd velocities
            float pos_x1, pos_y1, vel_x1, vel_y1;
            pos_x1 = sv.x + (h / 2f) * (3 * deriv2.dx_dt - deriv1.dx_dt);
            pos_y1 = sv.y + (h / 2f) * (3 * deriv2.dy_dt - deriv1.dy_dt);
            vel_x1 = sv.vx + (h / 2f) * (3 * deriv2.dvx_dt - deriv1.dvx_dt);
            vel_y1 = sv.vy + (h / 2f) * (3 * deriv2.dvy_dt - deriv1.dvy_dt);

            sv.x = pos_x1;
            sv.y = pos_y1;
            sv.vx = vel_x1;
            sv.vy = vel_y1;

            sv2 = new StateVector(sv1.x, sv1.y, sv1.vx, sv1.vy);
            sv1 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);

            this.partialDerivatives = Terrain.getSlope(new float[]{sv.x, sv.y}, h);
        }
        if (Physics.magnitude(sv.vx, sv.vy) < h) {
            float[] partialDerivatives = this.getPartialDerivatives();
            sv.stop = Physics.magnitude(partialDerivatives[0], partialDerivatives[1]) < Terrain.getStaticFriction(sv);
        }
        if(sv.stop){
            this.count = 0;
        }
    }


    public float[] getPartialDerivatives() {
        return this.partialDerivatives;
    }
}