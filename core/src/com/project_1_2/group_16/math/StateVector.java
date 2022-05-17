package com.project_1_2.group_16.math;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents a vector that contains position and velocity.
 * Captures the 'state' of an object.
 */
public class StateVector extends Vector2 {

    /**
     * x-velocity.
     */
    public float vx;

    /**
     * y-velocity.
     */
    public float vy;

    /**
     * The previous position.
     * Used for resetting the ball if it hits an obstacle.
     */
    public Vector2 prev;

    /**
     * Stopping condition.
     */
    public boolean stop = false;

    /**
     * StateVector that gives information about the pos (x,y) and velocity (x,y) of the ball.
     * @param x initial pos_x
     * @param y initial pos_y
     * @param vx initial vel_x
     * @param vy initial vel_y
     */
    public StateVector(float x, float y, float vx, float vy) {
        super(x, y);
        this.vx = vx;
        this.vy = vy;
    }

    @Override
    public String toString() {
        return ("x: "+x).concat(" y: "+y).concat(" vx: "+vx).concat(" vy: "+vy).concat(" prev: "+prev);
    }
}
