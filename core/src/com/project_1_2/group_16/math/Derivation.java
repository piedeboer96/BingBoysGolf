package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.physics.Acceleration;

/**
 * Here the derivatives are calculated using the physics 'acceleration' class.
 */
public class Derivation {
    float dx_dt;
    float dy_dt;
    float dvx_dt;
    float dvy_dt;
    public static Acceleration acc = new Acceleration();

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
    public Derivation(){
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
    public static Derivation getDerivation(StateVector sv, float h, Derivation d, float multiplier){
        float multipliedH = h*multiplier;
        StateVector tempSV = new StateVector(sv.x + (multipliedH * d.dx_dt), sv.y+ (multipliedH * d.dy_dt), sv.vx + (multipliedH * d.dvx_dt), sv.vy + (multipliedH * d.dvy_dt));
        float [] pDerivatives = Terrain.getSlope(new float[]{sv.x, sv.y}, h);
        float accelerationX = acc.getAccelerationX(pDerivatives[0], pDerivatives[1], sv);
        float accelerationY = acc.getAccelerationY(pDerivatives[0], pDerivatives[1], sv);
        return new Derivation(tempSV.vx, tempSV.vy, accelerationX, accelerationY);
    }

    /**
     * Getter for dx_dt
     * @return dx_dt
     */
    public float getDx_dt() {
        return dx_dt;
    }

    /**
     * Getter for dy_dt
     * @return dy_dt
     */
    public float getDy_dt() {
        return dy_dt;
    }

    /**
     * Getter for dvx_dt
     * @return dvx_dt
     */
    public float getDvx_dt() {
        return dvx_dt;
    }

    /**
     * Getter for dvy_dt
     * @return dvy_dt
     */
    public float getDvy_dt() {
        return dvy_dt;
    }
}
