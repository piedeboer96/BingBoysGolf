package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.physics.Acceleration;

public class Derivation {

    float dx_dt;
    float dy_dt;
    float dvx_dt;
    float dvy_dt;

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
     * For RK4 Derivation, calculate the derivation based on the stateVector
     * @param sv StateVector ball state
     * @param h step-size dt
     * @param terrain terrain
     * @return the first derivation required for RK4
     */
    public static Derivation getK1Derivation(StateVector sv, float h) {
        Acceleration acc = new Acceleration();
        float [] coords = new float[] {sv.getPos_x(), sv.getPos_y()};
        float [] pDerivatives = Terrain.getSlope(coords, h);
        float [] acceleration = new float[] {acc.getAccelerationX(pDerivatives[0], pDerivatives[1], sv), acc.getAccelerationY(pDerivatives[0], pDerivatives[1], sv)};
        return new Derivation(sv.getVelocity_x() * h, sv.getVelocity_y() * h, acceleration[0] * h, acceleration[1] * h);
    }

    /**
     * For RK4 Derivation, calculate the derivation based on the previous derivation provided
     * @param sv State Vector
     * @param h step-size
     * @param terrain terrain
     * @param d k1/k2 derivation (previous derivation)
     * @return the k2 or the k3 derivation required for RK4
     */
    public static Derivation getK2K3Derivation(StateVector sv, float h, Derivation d) {
        Acceleration acc = new Acceleration();
        StateVector tempSV = new StateVector(sv.getPos_x() + d.dx_dt/2 , sv.getPos_y() + d.dy_dt/2, sv.getVelocity_x() + d.dvx_dt/2, sv.getVelocity_y() + d.dvy_dt/2);
        float [] pDerivatives = Terrain.getSlope(new float[]{tempSV.getPos_x(), tempSV.getPos_y()}, h);
        float [] acceleration = new float[] {acc.getAccelerationX(pDerivatives[0], pDerivatives[1], tempSV), acc.getAccelerationY(pDerivatives[0], pDerivatives[1], tempSV)};
        return new Derivation(tempSV.getVelocity_x() * h, tempSV.getVelocity_y() * h, acceleration[0] * h, acceleration[1] * h);
    }

    /**
     * For RK4 Derivation, calculate the derivation based on the K3 derivation
     * @param sv State-Vector
     * @param h step-size
     * @param terrain terrain
     * @param d k3 derivation
     * @return k4 derivation required for RK4
     */
    public static Derivation getK4Derivation(StateVector sv, float h, Derivation d) {
        Acceleration acc = new Acceleration();
        StateVector tempSV = new StateVector(sv.getPos_x() + d.dx_dt , sv.getPos_y() + d.dy_dt, sv.getVelocity_x() + d.dvx_dt, sv.getVelocity_y() + d.dvy_dt);
        float [] pDerivatives = Terrain.getSlope(new float[]{tempSV.getPos_x(), tempSV.getPos_y()}, h);
        float [] acceleration = new float[] {acc.getAccelerationX(pDerivatives[0], pDerivatives[1], tempSV), acc.getAccelerationY(pDerivatives[0], pDerivatives[1], tempSV)};
        return new Derivation(tempSV.getVelocity_x() * h, tempSV.getVelocity_y() * h, acceleration[0] * h, acceleration[1] * h);
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
