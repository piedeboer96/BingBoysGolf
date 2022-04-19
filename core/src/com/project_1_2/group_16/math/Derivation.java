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
    public Derivation(){
        this.dx_dt = 0;
        this.dy_dt = 0;
        this.dvx_dt = 0;
        this.dvy_dt = 0;
    }
    /**
     * Get Derivation
     */
    public static Derivation getDerivation(StateVector sv, float h, Derivation d, float multiplier){
        float multipliedH = h*multiplier;
        StateVector tempSV = new StateVector(sv.pos_x + (multipliedH * d.dx_dt), sv.pos_y, sv.velocity_x + (multipliedH * d.dvx_dt), sv.velocity_y);
        StateVector tempSV2 = new StateVector(sv.pos_x, sv.pos_y + (multipliedH * d.dy_dt), sv.velocity_x, sv.velocity_y + (multipliedH * d.dvy_dt));
        float [] pDerivatives = Terrain.getSlope(new float[]{tempSV.pos_x, tempSV.pos_y}, h);
        float [] pDerivatives2 = Terrain.getSlope(new float[]{tempSV2.pos_x, tempSV2.pos_y}, h);
        float accelerationX = acc.getAccelerationX(pDerivatives[0], pDerivatives[1], tempSV);
        float accelerationY = acc.getAccelerationY(pDerivatives2[0], pDerivatives2[1], tempSV2);
        return new Derivation(tempSV.velocity_x, tempSV2.velocity_y, accelerationX, accelerationY);
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
