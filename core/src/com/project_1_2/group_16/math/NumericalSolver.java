package com.project_1_2.group_16.math;

/**
 * Interface to define numerical solvers.
 */
public interface NumericalSolver {
    public static final int EULER = 1;

    public static final int RK2 = 2;

    public static final int RK4 = 3;

    public static final int ADAMSB2 = 4;

    /**
     * Every numerical solver should be able to run a 'solve' process.
     * @param h stepsize
     * @param sv statevector
     */
    public void solve(float h, StateVector sv);

    public float[] getPartialDerivatives();

}
