package com.project_1_2.group_16.math.ode;

import com.project_1_2.group_16.math.StateVector;

/**
 * An interface for numerical solvers that can solve ODE's for our physics engine.
 */
public interface NumericalSolver {

    /**
     * Reference for the Euler-numerical solver.
     */
    public static final int EULER = 1;

    /**
     * Reference for the RK2-numerical solver.
     */
    public static final int RK2 = 2;

    /**
     * Reference for the RK4-numerical solver.
     */
    public static final int RK4 = 3;

    /**
     * Calculate the next step for the state vector.
     * @param h step size
     * @param sv state vector
     */
    public void solve(float h, StateVector sv);

    /**
     * Get the partial derivatives from the numerical solver.
     * These are needed for various game-logic.
     * @return an array containing the partial derivatives
     */
    public float[] getPartialDerivatives();
}
