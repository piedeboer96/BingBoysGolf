package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.io.Input;

/**
 * Class containing calculations related to physics.
 */
public class Physics {

    /**
     * Find the magnitude based on two coordinates,
     * for example in case of a vector it represents the length of the vector.
     * It is equal to vector magnitude or Pythagoras
     * @param x1 coordinate 1
     * @param x2 coordinate 2
     * @return length
     */
    public static float magnitude(float x1, float x2) {
        return (float)Math.sqrt(x1 * x1 + x2 * x2);
    }

    /**
     * Acceleration in the x direction.
     * It takes the kinetic friction into account.
     * @param dh_dx difference in height over x difference
     * @param sv StateVector to take x,y pos and velocities from
     * @return acceleration in x direction
     */
    public static float getAccelerationX(float dh_dx, float dh_dy, StateVector sv) {
        float kinetic_friction = Terrain.getKineticFriction(sv);
        if (magnitude(sv.vx,sv.vy) < 0.05  && magnitude(dh_dx,dh_dy) > 0) {
            return (-Input.GRAVITY * kinetic_friction * dh_dx) / magnitude(dh_dx, dh_dy);
        }
        return ((-Input.GRAVITY * (dh_dx)) - ((kinetic_friction * Input.GRAVITY) * (sv.vx / (magnitude(sv.vx,sv.vy)))));
    }

    /**
     * Acceleration in the y-direction.
     * It takes the kinetic friction into account.
     * @param dh_dy difference in height over difference in y
     * @param sv StateVector
     * @return acceleration in y direction
     */
    public static float getAccelerationY(float dh_dx, float dh_dy, StateVector sv) {
        float kinetic_friction = Terrain.getKineticFriction(sv);
        if (magnitude(sv.vx,sv.vy) < 0.05 && magnitude(dh_dx,dh_dy) > 0) {
            return (-Input.GRAVITY * kinetic_friction * dh_dy) / magnitude(dh_dx, dh_dy);
        }
        return ((-Input.GRAVITY * (dh_dy)) - ((kinetic_friction * Input.GRAVITY) * (sv.vy / (magnitude(sv.vx, sv.vy)))));
    }
}
