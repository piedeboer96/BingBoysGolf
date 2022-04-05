package com.project_1_2.group_16.physics;

public class Acceleration {

    /**
     * Acceleration in the x direction.
     * It takes the kinetic friction into account.
     * @param dh_dx difference in height over x difference
     * @param sv StateVector to take x,y pos and velocities from
     * @return acceleration in x direction
     */
    float getAccelerationX(float dh_dx, float dh_dy, StateVector sv, Terrain terrain) {
        float kinetic_friction = terrain.getKineticFriction(sv);
        if (Physics.magnitude(sv.velocity_x,sv.velocity_y) < 0.05  && Physics.magnitude(dh_dx,dh_dy) > 0) {
            return (-Physics.GRAV_CONSTANT * kinetic_friction * dh_dx) / Physics.magnitude(dh_dx, dh_dy);
        }
        return ((-Physics.GRAV_CONSTANT * (dh_dx)) - ((kinetic_friction * Physics.GRAV_CONSTANT) * (sv.velocity_x / (Physics.magnitude(sv.velocity_x,sv.velocity_y)))));
    }

    /**
     * Acceleration in the y-direction.
     * It takes the kinetic friction into account.
     * @param dh_dy difference in height over difference in y
     * @param sv StateVector
     * @return acceleration in y direction
     */
    float getAccelerationY(float dh_dx, float dh_dy, StateVector sv, Terrain terrain) {
        float kinetic_friction = terrain.getKineticFriction(sv);
        if (Physics.magnitude(sv.velocity_x,sv.velocity_y) < 0.05 && Physics.magnitude(dh_dx,dh_dy) > 0) {
            return (-Physics.GRAV_CONSTANT * kinetic_friction * dh_dy) / Physics.magnitude(dh_dx, dh_dy);
        }
        return ((-Physics.GRAV_CONSTANT * (dh_dy)) - ((kinetic_friction * Physics.GRAV_CONSTANT) * (sv.velocity_y / (Physics.magnitude(sv.velocity_x, sv.velocity_y)))));
    }
}
