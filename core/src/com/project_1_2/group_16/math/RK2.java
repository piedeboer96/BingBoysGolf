package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;

/**
 * Classical 2nd-order Runge-Kutta method.
 */
public class RK2 implements NumericalSolver{

    private float[] partialDerivatives;

    @Override
    public void solve(float h, StateVector sv) {
        Derivation k1, k2;
        k1 = Derivation.getDerivation(sv, h, new Derivation(), 0.0f);
        k2 = Derivation.getDerivation(sv, h, k1, 1.0f);

        float vel_x1 = ((h/2f) * (k1.dvx_dt + k2.dvx_dt)) + sv.vx;
        float vel_y1 = ((h/2f) * (k1.dvy_dt + k2.dvy_dt)) + sv.vy;

        float pos_x1 = ((h/2f) * (k1.dx_dt + k2.dx_dt)) + sv.x;
        float pos_y1 = ((h/2f) * (k1.dy_dt + k2.dy_dt)) + sv.y;

        sv.vx = vel_x1;
        sv.vy = vel_y1;

        sv.x = pos_x1;
        sv.y = pos_y1;

        this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
    }

    @Override
    public float[] getPartialDerivatives() {
        return this.partialDerivatives;
    }
}
