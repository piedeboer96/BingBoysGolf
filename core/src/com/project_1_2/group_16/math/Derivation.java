package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.physics.Acceleration;

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
     * For RK4 Derivation, calculate the derivation based on the stateVector
     * @param sv StateVector ball state
     * @param h step-size dt
     * @return the first derivation required for RK4
     */
    public static Derivation getK1Derivation(StateVector sv, float h) {
        float [] coords = new float[] {sv.pos_x, sv.pos_y};
        float [] pDerivatives = Terrain.getSlope(coords, h);
        float [] acceleration = new float[] {acc.getAccelerationX(pDerivatives[0], pDerivatives[1], sv), acc.getAccelerationY(pDerivatives[0], pDerivatives[1], sv)};
        return new Derivation(sv.velocity_x, sv.velocity_y, acceleration[0], acceleration[1]);
    }

    /**
     * For RK4 Derivation, calculate the derivation based on the previous derivation provided
     * @param sv State Vector
     * @param h step-size
     * @param d k1/k2 derivation (previous derivation)
     * @return the k2 or the k3 derivation required for RK4
     */
    public static Derivation getK2K3Derivation(StateVector sv, float h, Derivation d) {
        StateVector tempSV = new StateVector(sv.pos_x + h/2 * d.dx_dt, sv.pos_y + h/2, sv.velocity_x + h/2 * d.dvx_dt, sv.velocity_y + h/2);
        StateVector tempSV2 = new StateVector(sv.pos_x + h/2, sv.pos_y + h/2 * d.dy_dt, sv.velocity_x + h/2, sv.velocity_y + h/2 * d.dvy_dt);
        float [] pDerivatives = Terrain.getSlope(new float[]{tempSV.pos_x, tempSV.pos_y}, h);
        float [] pDerivatives2 = Terrain.getSlope(new float[]{tempSV2.pos_x, tempSV2.pos_y}, h);
        float [] acceleration = new float[] {acc.getAccelerationX(pDerivatives[0], pDerivatives[1], tempSV), acc.getAccelerationY(pDerivatives[0], pDerivatives[1], tempSV)};
        float [] acceleration2 = new float[] {acc.getAccelerationX(pDerivatives2[0], pDerivatives2[1], tempSV2), acc.getAccelerationY(pDerivatives2[0], pDerivatives2[1], tempSV2)};
        return new Derivation(tempSV.velocity_x, tempSV2.velocity_y, acceleration[0], acceleration2[1]);
    }

    /**
     * For RK4 Derivation, calculate the derivation based on the K3 derivation
     * @param sv State-Vector
     * @param h step-size
     * @param d k3 derivation
     * @return k4 derivation required for RK4
     */
    public static Derivation getK4Derivation(StateVector sv, float h, Derivation d) {
        StateVector tempSV = new StateVector(sv.pos_x + h * d.dx_dt, sv.pos_y + h, sv.velocity_x + h * d.dvx_dt, sv.velocity_y + h);
        StateVector tempSV2 = new StateVector(sv.pos_x + h, sv.pos_y + h * d.dy_dt, sv.velocity_x + h, sv.velocity_y + h * d.dvy_dt);
        float [] pDerivatives = Terrain.getSlope(new float[]{tempSV.pos_x, tempSV.pos_y}, h);
        float [] pDerivatives2 = Terrain.getSlope(new float[]{tempSV2.pos_x, tempSV2.pos_y}, h);
        float [] acceleration = new float[] {acc.getAccelerationX(pDerivatives[0], pDerivatives[1], tempSV), acc.getAccelerationY(pDerivatives[0], pDerivatives[1], tempSV)};
        float [] acceleration2 = new float[] {acc.getAccelerationX(pDerivatives2[0], pDerivatives2[1], tempSV2), acc.getAccelerationY(pDerivatives2[0], pDerivatives2[1], tempSV2)};
        return new Derivation(tempSV.velocity_x, tempSV2.velocity_y, acceleration[0], acceleration2[1]);
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
