package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.physics.Physics;

/**
 * 2-stage Adams-Bashforth method using RK2 bootstrap.
 */
public class AdamsB2nd implements NumericalSolver{

    RK2 rk2Bootstrap = new RK2();
    private float[] partialDerivatives;
    public int count = 0;
    StateVector sv1;
    StateVector sv2;

    /**
     * Here the actually 2-stage Adams-Bashforth method is run.
     * Bootsrap is done using RK2.
     * It updates the statevector.
     * @param h step-size
     * @param sv statevector
     */
    public void solve(float h, StateVector sv){
        if(count<2){
            if(count==0) {
                rk2Bootstrap.solve(h, sv);
                sv1 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
                this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
                count++;
                return;
            }else if (count==1){
                rk2Bootstrap.solve(h, sv);
                sv2 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
                this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
                count++;
                return;
            }
        }
        Derivation deriv1 = Derivation.getDerivation(sv1, h, new Derivation(), 0);
        Derivation deriv2 = Derivation.getDerivation(sv2, h, new Derivation(), 0);

        float pos_x1, pos_y1, vel_x1, vel_y1;
        pos_x1 = sv.x + (h/2f) * (3 * deriv2.dx_dt - deriv1.dx_dt);
        pos_y1 = sv.y + (h/2f) * (3 * deriv2.dy_dt - deriv1.dy_dt);
        vel_x1 = sv.vx + (h/2f) * (3 * deriv2.dvx_dt - deriv1.dvx_dt);
        vel_y1 = sv.vy + (h/2f) * (3 * deriv2.dvy_dt - deriv1.dvy_dt);

        sv.x = pos_x1; sv.y = pos_y1; sv.vx = vel_x1; sv.vy = vel_y1;

        sv2 = sv1;
        sv1 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);

        this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);

        if (Physics.magnitude(sv.vx, sv.vy) < h) {
            float[] partialDerivatives = this.getPartialDerivatives();
            sv.stop = Physics.magnitude(partialDerivatives[0], partialDerivatives[1]) < Terrain.getStaticFriction(sv);
        }
        if(sv.stop){
            this.count = 0;
        }
    }

    /**
     * Getter for the partial derivatives
     * @return array of partial derivatives
     */
    public float[] getPartialDerivatives() {
        return this.partialDerivatives;
    }
}