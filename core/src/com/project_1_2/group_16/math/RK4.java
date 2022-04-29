package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;

public class RK4 implements NumericalSolver {

    private float[] partialDerivatives;

    /**
     * class needed to run the RK4 derivation algorithm which is a more improved version of Euler's implementation by 3 orders
     * of accuracy. It runs by taking a weighted average of different estimations/derivations k1, k2, k3 and k4.
     * @param h step-size
     * @param sv StateVector
     */
    @Override
    public void solve(float h, StateVector sv) {
        Derivation k1, k2, k3, k4;

        k1 = Derivation.getDerivation(sv, h, new Derivation(), 0);
        k2 = Derivation.getDerivation(sv, h, k1, 0.5f);
        k3 = Derivation.getDerivation(sv, h, k2, 0.5f);
        k4 = Derivation.getDerivation(sv, h, k3, 1f);

        float pos_x1 = sv.x + (h/6f)*((k1.dx_dt + (2 * k2.dx_dt) + (2 * k3.dx_dt) + k4.dx_dt));
        float pos_y1 = sv.y + (h/6f)*((k1.dy_dt + (2 * k2.dy_dt) + (2 * k3.dy_dt) + k4.dy_dt));
        float vel_x1 = sv.vx + (h/6f)*((k1.dvx_dt + (2 * k2.dvx_dt) + (2 * k3.dvx_dt) + k4.dvx_dt));
        float vel_y1 =  sv.vy + (h/6f)*((k1.dvy_dt + (2 * k2.dvy_dt) + (2 * k3.dvy_dt) + k4.dvy_dt));

        sv.x = pos_x1;
        sv.y = pos_y1;

        sv.vx = vel_x1;
        sv.vy = vel_y1;

        this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);

//        System.out.println("sv_END: " + sv);
    }

    @Override
    public float[] getPartialDerivatives() {
        return this.partialDerivatives;
    }
}
