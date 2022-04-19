package com.project_1_2.group_16.math;

public interface NumericalSolver {
    public void solve(float h, StateVector sv);

    public float[] getPartialDerivatives();
}
