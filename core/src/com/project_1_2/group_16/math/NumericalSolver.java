package com.project_1_2.group_16.math;

public interface NumericalSolver {
    public static final int EULER = 1;

    public static final int RK2 = 2;

    public static final int RK4 = 3;

    public static final int ADAMSB2 = 4;

    public static final int ADAMSM4 = 5;


    public void solve(float h, StateVector sv);

    public float[] getPartialDerivatives();

}
