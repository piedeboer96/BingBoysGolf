package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;

public interface NumericalSolver {
    public void Solve(float h, StateVector sv, Terrain terrain);
}
