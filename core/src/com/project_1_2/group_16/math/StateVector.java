package com.project_1_2.group_16.math;

import com.badlogic.gdx.math.Vector2;

public class StateVector extends Vector2 {

    /**
     * x-velocity
     */
    public float vx;

    /**
     * y-velocity
     */
    public float vy;

    /**
     * The previous position
     */
    public Vector2 prev;

    /**
     * Stopping condition
     */
    public boolean stop;

    /**
     * StateVector that gives information about the pos (x,y) and velocity (x,y) of the ball.
     * @param pos_x_init initial pos_x
     * @param pos_y_init initial pos_y
     * @param velocity_x_init initial vel_x
     * @param velocity_y_init initial vel_y
     */
    public StateVector(float x, float y, float vx, float vy) {
        super(x, y);
        this.vx = vx;
        this.vy = vy;
    }
}
