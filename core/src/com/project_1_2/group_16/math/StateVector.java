package com.project_1_2.group_16.math;

public class StateVector {

    public float pos_x;
    public float pos_y;
    public float velocity_x;
    public float velocity_y;

    /**
     * StateVector that gives information about the pos (x,y) and velocity (x,y) of the ball.
     * @param pos_x_init initial pos_x
     * @param pos_y_init initial pos_y
     * @param velocity_x_init initial vel_x
     * @param velocity_y_init initial vel_y
     */
    public StateVector(float pos_x_init, float pos_y_init, float velocity_x_init, float velocity_y_init) {
        this.pos_x = pos_x_init;
        this.pos_y = pos_y_init;
        this.velocity_x = velocity_x_init;
        this.velocity_y = velocity_y_init;
    }

    /**
     * Getter position x.
     * @return pos_x
     */
    public float getPos_x() {
        return pos_x;
    }

    /**
     * Get position y
     * @return pos_y
     */
    public float getPos_y() {
        return pos_y;
    }

    /**
     * Get velocity x
     * @return vel_x
     */
    public float getVelocity_x() {
        return velocity_x;
    }

    /**
     * Get velocity y
     * @return vel_y
     */
    public float getVelocity_y() {
        return velocity_y;
    }

    @Override
    public String toString() {
        return "[ x " + pos_x + " y " + pos_y + " vel_x " + velocity_x + " vel_y " + velocity_y + " ]";
    }
}
