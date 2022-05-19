package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;

/**
 * Here the derivatives are calculated using the physics class.
 */
public class Derivation {

    public float dx_dt;
    public float dy_dt;
    public float dvx_dt;
    public float dvy_dt;

    /**
     * Initialize derivatives.
     * @param dx_dt derivative of position in x direction w.r.t. time
     * @param dy_dt derivative of position in y direction w.r.t. time
     * @param dvx_dt derivative of velocity in x direction w.r.t. time
     * @param dvy_dt derivative of velocity in y direction w.r.t. time
     */
    public Derivation(float dx_dt, float dy_dt, float dvx_dt, float dvy_dt) {
        this.dx_dt = dx_dt;
        this.dy_dt = dy_dt;
        this.dvx_dt = dvx_dt;
        this.dvy_dt = dvy_dt;
    }

    /**
     * Empty constructor for convenience
     */
    public Derivation() {
        this.dx_dt = 0;
        this.dy_dt = 0;
        this.dvx_dt = 0;
        this.dvy_dt = 0;
    }

    /**
     * Get the derivation of a certain state vector, for Runge Kutta purposes
     * @param sv The specific state vector
     * @param h The step size
     * @param d Previous derivation current is based on
     * @param multiplier For Runge Kutta, in some instances you would need to multiply the "h" with a certain "multiplier"
     * @return new Derivation object containing all the Derivations of the state vector
     */
    public static Derivation getDerivation(StateVector sv, float h, Derivation d, float multiplier) {
        // Change the StateVector to be used to derive the velocities and accelerations
        float multipliedH = h*multiplier;
        StateVector tempSV = new StateVector(sv.x + (multipliedH * d.dx_dt), sv.y+ (multipliedH * d.dy_dt), sv.vx + (multipliedH * d.dvx_dt), sv.vy + (multipliedH * d.dvy_dt));
        // Use the newly found StateVector to compute the accelerations
        float [] pDerivatives = Terrain.getSlope(new float[]{tempSV.x, tempSV.y});
        float accelerationX = Physics.getAccelerationX(pDerivatives[0], pDerivatives[1], tempSV);
        float accelerationY = Physics.getAccelerationY(pDerivatives[0], pDerivatives[1], tempSV);
        // Return the found Derivations (velocities wrt X and Y, accelerations wrt X and Y)
        return new Derivation(tempSV.vx, tempSV.vy, accelerationX, accelerationY);
    }
}
