package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;

/**
 * Classical 4th-order Runge-Kutta method.
 */
public class RK4 implements NumericalSolver {

    private float[] partialDerivatives;

    @Override
    public void solve(float h, StateVector sv) {

        //Get Derivations K1, K2, K3 and K4
        Derivation k1, k2, k3, k4;
        k1 = Derivation.getDerivation(sv, h, new Derivation(), 0);
        k2 = Derivation.getDerivation(sv, h, k1, 0.5f);
        k3 = Derivation.getDerivation(sv, h, k2, 0.5f);
        k4 = Derivation.getDerivation(sv, h, k3, 1f);

        //Update positions using the Derivations
        float pos_x1 = sv.x + h * ((k1.dx_dt/6.0f + k2.dx_dt/3.0f + k3.dx_dt/3.0f + k4.dx_dt/6.0f));
        float pos_y1 = sv.y + h * ((k1.dy_dt/6.0f +  k2.dy_dt/3.0f + k3.dy_dt/3.0f + k4.dy_dt/6.0f));

        //Update velocities using the Derivations
        float vel_x1 = sv.vx + h * ((k1.dvx_dt/6.0f + k2.dvx_dt/3.0f +  k3.dvx_dt/3.0f + k4.dvx_dt/6.0f));
        float vel_y1 = sv.vy + h * ((k1.dvy_dt/6.0f + k2.dvy_dt/3.0f + k3.dvy_dt/3.0f + k4.dvy_dt/6.0f));

        //Update StateVector using the newly found positions and velocities
        sv.x = pos_x1;
        sv.y = pos_y1;
        sv.vx = vel_x1;
        sv.vy = vel_y1;


        this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y});
    }

    @Override
    public float[] getPartialDerivatives() {
        return this.partialDerivatives;
    }
}
