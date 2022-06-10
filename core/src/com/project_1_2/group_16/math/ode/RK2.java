package com.project_1_2.group_16.math.ode;

import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.math.Derivation;
import com.project_1_2.group_16.math.StateVector;

/**
 * Classical 2nd-order Runge-Kutta method.
 */
public class RK2 implements NumericalSolver{

    private float[] partialDerivatives;

    @Override
    public void solve(float h, StateVector sv) {
        //Get Derivations K1 and K2
        Derivation k1, k2;
        k1 = Derivation.getDerivation(sv, h, new Derivation(), 0.0f);
        k2 = Derivation.getDerivation(sv, h, k1, 1.0f);

        //Update positions using the Derivations
        float pos_x1 = sv.x + h * (k1.dx_dt/2.0f + k2.dx_dt/2.0f);
        float pos_y1 = sv.y + h * (k1.dy_dt/2.0f + k2.dy_dt/2.0f);

        //Update velocities using the Derivations
        float vel_x1 = sv.vx + h * (k1.dvx_dt/2.0f + k2.dvx_dt/2.0f);
        float vel_y1 = sv.vy + h * (k1.dvy_dt/2.0f + k2.dvy_dt/2.0f);

        //Update StateVector using the newly found positions and velocities
        sv.vx = vel_x1;
        sv.vy = vel_y1;
        sv.x = pos_x1;
        sv.y = pos_y1;

        this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y});
    }

    @Override
    public float[] getPartialDerivatives() {
        return this.partialDerivatives;
    }
}
