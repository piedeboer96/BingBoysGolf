package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;

/**
 * Euler’s method for solving ODE's.
 */
public class Euler implements NumericalSolver {

    private float[] partialDerivatives;
    
    @Override
    public void solve(float h, StateVector sv) {
        this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y});

        //Compute future positions
        float pos_x1 = sv.x + (h * sv.vx);
        float pos_y1 = sv.y + (h * sv.vy);

        //Compute the accelerations relative to the 2 axis using the StateVector
        float acceleration_x = Physics.getAccelerationX(this.partialDerivatives[0], this.partialDerivatives[1], sv);
        float acceleration_y = Physics.getAccelerationY(this.partialDerivatives[0], this.partialDerivatives[1], sv);

        //Use the accelerations to compute the next velocities
        float vel_x1 = sv.vx + (h * acceleration_x);
        float vel_y1 = sv.vy + (h * acceleration_y);

        //Update StateVector using newly found positions and velocities
        sv.x = pos_x1;
        sv.y = pos_y1;
        sv.vx = vel_x1;
        sv.vy = vel_y1;
    }

    @Override
    public float[] getPartialDerivatives() {
        return this.partialDerivatives;
    }
}